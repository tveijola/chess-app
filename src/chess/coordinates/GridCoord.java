package chess.coordinates;

import java.util.Arrays;

public class GridCoord {
	
	private static String colnames = "abcdefgh";
	
	private int[] gridCoord;
	private String algNotation;
	
	public GridCoord (int[] coord) {
		this.gridCoord   = new int[] {coord[0], coord[1]};
		this.algNotation = toAlgebraicNotation(coord);
		
	}
	public GridCoord (GridCoord coord) {
		int[] tmpCoord = coord.getGridCoord();
		this.gridCoord = new int[] {tmpCoord[0], tmpCoord[1]};
		this.algNotation = toAlgebraicNotation(tmpCoord);
	}

	public int[] getGridCoord() {
		return this.gridCoord;
	}
	public void setGridCoord(int[] coord) {
		this.gridCoord = new int[] {coord[0], coord[1]};
	}
	
	public String getAlgNotation () {
		return this.algNotation;
	}
	
	public boolean equals(GridCoord c) {
		if (Arrays.equals(this.getGridCoord(), c.getGridCoord()) && this.getAlgNotation().equals(c.getAlgNotation())) {
			return true;
		}
		return false;
	}
	
	private static String toAlgebraicNotation(int[] coord) {
		String algNot = "";
		String col = Character.toString(colnames.charAt(coord[0]));
		String row = Integer.toString(8 - coord[1]);
		algNot = col + row;
		return algNot;
	}
}
