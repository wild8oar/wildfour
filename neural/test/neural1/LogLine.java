package neural1;

import bot.Field;

public class LogLine {
	
	private final Field field;
	private final int movesToEnd;
	private final int winner;


	public LogLine(Field field, int movesToEnd, int winner) {
		this.field = field;
		this.movesToEnd = movesToEnd;
		this.winner = winner;
	}
	
	public static LogLine parseLine(String line) {
		String[] parts = line.split("\t");
		if (parts.length != 3) {
			throw new IllegalStateException("Invalid line: " + line);
		}
		Field field = new Field(7, 6);
		field.parseFromString(parts[0]);
		return new LogLine(field, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}

	public Field getField() {
		return field;
	}

	public int getMovesToEnd() {
		return movesToEnd;
	}

	public int getWinner() {
		return winner;
	}
}
