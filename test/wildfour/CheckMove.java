package wildfour;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import maps.MapB;
import wildfour.MoveFinder.BestMove;

public class CheckMove {
	
	private static final String MOVE = "000000?>000H00";
	
	private static final MapMoveFinder FINDER = new MapMoveFinder(MapB.MAP);
	private static final MaxMinMoveFinder ANALYZER = new MaxMinMoveFinder(18);
	
	private static final Set<String> LOSSES = new HashSet<>();
	
	private static Move loadMove () {
		List<Move> moves = MovesLoader.loadMoves(MapB.MAP);
		String norm = MapMoveFinder.normalize(MOVE);
		for (Move m: moves) {
			if (m.getEncoded().equals(norm)) {
				return m;
			}
		}
		throw new IllegalStateException("Move not found");
	}
	
	public static void main(String[] args) throws IOException {
		LOSSES.addAll(Files.readAllLines(new File("known_losses.txt").toPath(), Charset.defaultCharset()));
		PlayField field = MapMoveFinder.decodeField(MOVE);
		field.print();
		String norm = MapMoveFinder.normalize(MOVE);
		if (!norm.equals(MOVE)) {
			System.out.println("Normalized move: " + norm);
		}
		if (LOSSES.contains(norm)) {
			System.out.println("Move is a known loss");
		}
		Optional<Integer> found = FINDER.findMove(field);
		if (found.isPresent()) {
			System.out.println("Known move: " + found.get());
		}
		BestMove best = ANALYZER.findBestMove(field);
		System.out.println("Analysis of position: best=" + best.move + " (score " + best.score + ")" );
		BestMove mirrorBest = ANALYZER.findBestMove(MapMoveFinder.decodeField(MapMoveFinder.mirror(MOVE)));
		System.out.println("Analysis of mirrored position: best=" + mirrorBest.move + " (score " + mirrorBest.score + ")" );
		Move loaded = loadMove();
		System.out.println("Move has next moves: " + loaded.hasNext());
		System.out.println("Previous moves (" + loaded.getPrevious().size() + "): " + 
				loaded.getPrevious().stream().map(Move::getEncoded).collect(toList()));
	}

}
