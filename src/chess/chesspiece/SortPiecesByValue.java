package chess.chesspiece;

import java.util.Comparator;

import chess.chesspiece.Piece;

//CLASS
//SortPiecesByValue class that implements the Comparator class
public class SortPiecesByValue implements Comparator<Piece> {
	
	// Used for sorting collections in ascending order according to piece value.
	public int compare(Piece a, Piece b) {
		return a.getValue() - b.getValue();
	}
}