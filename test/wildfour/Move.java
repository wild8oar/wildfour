package wildfour;

import java.util.ArrayList;
import java.util.List;

public class Move implements Comparable<Move> {
	
	private final String encoded;
	private final PlayField field;
	private final int round;
	private final int move;
	private final boolean flipped;
	private List<Move> previous = new ArrayList<>();
	private boolean hasNext;
	
	public Move (String enc, int move) {
		this.encoded = enc;
		field = MapMoveFinder.decodeField(enc);
		this.round = field.rounds()+1;
		if (!MapMoveFinder.encodeField(field).equals(enc)) {
			this.move = 6 - move;
			flipped = true;
		} else {
			this.move = move;
			flipped = false;
		}
		
	}

	public PlayField getField() {
		return field;
	}

	public int getRound() {
		return round;
	}

	public int getMove() {
		return move;
	}

	public List<Move> getPrevious() {
		return previous;
	}

	public void addPrevious(Move previous) {
		this.previous.add(previous);
	}

	public boolean hasNext() {
		return hasNext;
	}

	public void setHasNext() {
		this.hasNext = true;
	}

	@Override
	public int compareTo(Move o) {
		int roundCompare = Integer.compare(round, o.round);
		if (roundCompare != 0) {
			return roundCompare;
		}
		return getEncoded().compareTo(o.getEncoded());
	}

	public String getEncoded() {
		return encoded;
	}

	public boolean isFlipped() {
		return flipped;
	}

}
