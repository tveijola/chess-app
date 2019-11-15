package chess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.chesspiece.*;
import chess.coordinates.GridCoord;
import javafx.scene.image.Image;

public class ChessGame {

	// INSTANCE VARIABLES
	private King kingWhite;
	private King kingBlack;
	private Chessboard gameBoard;
	private Map<String, List<Piece>> pieceListMap;
	private Map<String, List<Piece>> capturedPieceListMap;
	private Map<String, List<ChessMove[]>> moveListMap;
	// private List<ChessMove[]> moveList;


	// CONSTRUCTORS
	public ChessGame(Chessboard board, String imageDirectory) throws FileNotFoundException {
		
		FileInputStream instream_wKing = new FileInputStream(imageDirectory + "white_king.png");
		FileInputStream instream_bKing = new FileInputStream(imageDirectory + "black_king.png");
		kingWhite         = new King(new Image(instream_wKing), new GridCoord(new int[] {4, 7}), "white");
		kingBlack         = new King(new Image(instream_bKing), new GridCoord(new int[] {4, 0}), "black");
		
		this.gameBoard    = board;
		this.pieceListMap = this.createInitialPieceListMap(imageDirectory);
		this.capturedPieceListMap = new HashMap<String, List<Piece>>();
		capturedPieceListMap.put("white", new ArrayList<Piece>());
		capturedPieceListMap.put("black", new ArrayList<Piece>());
		
		// this.moveListMap  = this.createInitialMoveListMap();
	}


	// GETTERS AND SETTERS
	public Chessboard getGameBoard() {
		return this.gameBoard;
	}
	
	public void setPieceListMap(Map<String, List<Piece>> pListMap) {
		this.pieceListMap = pListMap;
	}
	public Map<String, List<Piece>> getPieceListMap() {
		return this.pieceListMap;
	}
	
	public Map<String, List<Piece>> getCapturedPieceListMap() {
		return this.capturedPieceListMap;
	}
	
	public void setMoveListMap(Map<String, List<ChessMove[]>> mListMap) {
		this.moveListMap = mListMap;
	}
	public Map<String, List<ChessMove[]>> getMoveListMap() {
		return this.moveListMap;
	}

	// NORMAL METHODS

	public void computeAvailableMoves (String col) {
		
		List<ChessMove[]>  moveList  = this.moveListMap.get(col);
		List<Piece> pieceList = this.pieceListMap.get(col);
		moveList.clear();
		for(int i = 0; i < pieceList.size(); i++) {
			moveList.add(pieceList.get(i).computeAvailableMoves(this.getGameBoard()));
		}
	}
	public void checkCastling(String col, ChessApplication app) {
		if (col.equals("white")) {
			kingWhite.checkCastling(this, app);
		} else {
			kingBlack.checkCastling(this, app);
		}
	}
	
	public void resetCheckToFalse(String color) {
		King king = (color.equals("white")) ? this.kingWhite : this.kingBlack;
		king.setIsInCheck(false);
	}
	
	public boolean isColorInCheck (String color) {
		
		King king = (color.equals("white")) ? this.kingWhite : this.kingBlack;
		String oppCol = (color.equals("white")) ? "black" : "white";
		List<ChessMove[]> oppMoves = this.moveListMap.get(oppCol);
		for (int i = 0; i < oppMoves.size(); i++) {
			for (int j = 0; j < oppMoves.get(i).length; j++) {
				if (oppMoves.get(i)[j].getMoveDest().equals(king.getCoord())) {
					king.setIsInCheck(true);
					return true;
				}
			}
		}
		return false;
	}
	
	public void resetEnPassantCondition (String color) {
		
		List<Piece> pieceList = this.getPieceListMap().get(color);
		for (int i = 0; i < pieceList.size(); i++) {
			if (pieceList.get(i).getName().equals("pawn")) {
				pieceList.get(i).setIsCapturableEnPassant(false);
			}
		}
	}


	// STATIC METHODS
	
	private Map<String, List<Piece>> createInitialPieceListMap (String imageDirectory) throws FileNotFoundException {
		
		Map<String, List<Piece>> pieceListMap = new HashMap<String, List<Piece>>(2);
		pieceListMap.put("white", new ArrayList<Piece>(16));
		pieceListMap.put("black", new ArrayList<Piece>(16));
		
		String[] piecenames = {"rook", "knight", "bishop", "queen", "king", "bishop", "knight", "rook"};
		
		Piece piece_wPawn;
		Piece piece_bPawn;
		Piece piece_wPiece;
		Piece piece_bPiece;
		
		for (int i = 0; i < 8; i++) {
			
			FileInputStream instream_wPawn  = new FileInputStream(imageDirectory + "white_pawn.png");
			FileInputStream instream_bPawn  = new FileInputStream(imageDirectory + "black_pawn.png");
			FileInputStream instream_wPiece = new FileInputStream(imageDirectory + "white" + "_" + piecenames[i] + ".png");
			FileInputStream instream_bPiece = new FileInputStream(imageDirectory + "black" + "_" + piecenames[i] + ".png");

			piece_wPawn  = new Pawn(new Image(instream_wPawn), new GridCoord(new int[] {i, 6}), "white");
			piece_bPawn  = new Pawn(new Image(instream_bPawn), new GridCoord(new int[] {i, 1}), "black");
			
			switch(i){
				case 0: case 7:
					piece_wPiece = new Rook(new Image(instream_wPiece), new GridCoord(new int[] {i, 7}), "white");
					piece_bPiece = new Rook(new Image(instream_bPiece), new GridCoord(new int[] {i, 0}), "black");
					break;
				case 1: case 6:
					piece_wPiece = new Knight(new Image(instream_wPiece), new GridCoord(new int[] {i, 7}), "white");
					piece_bPiece = new Knight(new Image(instream_bPiece), new GridCoord(new int[] {i, 0}), "black");
					break;
				case 2: case 5:
					piece_wPiece = new Bishop(new Image(instream_wPiece), new GridCoord(new int[] {i, 7}), "white");
					piece_bPiece = new Bishop(new Image(instream_bPiece), new GridCoord(new int[] {i, 0}), "black");
					break;
				case 3:
					piece_wPiece = new Queen(new Image(instream_wPiece), new GridCoord(new int[] {i, 7}), "white");
					piece_bPiece = new Queen(new Image(instream_bPiece), new GridCoord(new int[] {i, 0}), "black");
					break;
				case 4:
					piece_wPiece = this.kingWhite;
					piece_bPiece = this.kingBlack;
					break;
				default:
					piece_wPiece = new King(new Image(instream_wPiece), new GridCoord(new int[] {i, 7}), "white");
					piece_bPiece = new King(new Image(instream_bPiece), new GridCoord(new int[] {i, 0}), "black");
					break;
			}
			
			pieceListMap.get("white").add(piece_wPawn );
			pieceListMap.get("black").add(piece_bPawn );
			pieceListMap.get("white").add(piece_wPiece);
			pieceListMap.get("black").add(piece_bPiece);
		}

		Collections.sort(pieceListMap.get("white"), new SortPiecesByValue());
		Collections.sort(pieceListMap.get("black"), new SortPiecesByValue());
		
		return pieceListMap;
	}
	
	public Map<String, List<ChessMove[]>> createInitialMoveListMap () {
		
		String[] colors = {"white", "black"};
		Map<String, List<ChessMove[]>> moveListMap = new HashMap<String, List<ChessMove[]>>(2);
		moveListMap.put(colors[0], new ArrayList<ChessMove[]>(16));
		moveListMap.put(colors[1], new ArrayList<ChessMove[]>(16));
		
		for (String s : colors) {
			List<Piece> colorPieceList = this.pieceListMap.get(s);
			for (int i = 0; i < colorPieceList.size(); i++) {
				moveListMap.get(s).add(colorPieceList.get(i).computeAvailableMoves(this.getGameBoard()));
			}
		}
		return moveListMap;
	}
	
	public void checkIllegalMoves(ChessApplication app) {
		for (String s : new String[] {"white", "black"}) {
			List<ChessMove[]>  moveList = this.moveListMap.get(s);
			for(int i = 0; i < moveList.size(); i++) {
				for(int j = 0; j < moveList.get(i).length; j++) {
					determineMoveLegality(app, moveList.get(i)[j]);
				}
			}
		}
	}
/*
	public void checkIllegalMoves(String col, ChessApplication app) {
		List<ChessMove[]>  moveList  = this.moveListMap.get(col);
		for(int i = 0; i < moveList.size(); i++) {
			for(int j = 0; j < moveList.get(i).length; j++) {
				determineMoveLegality(app, moveList.get(i)[j]);
			}
		}
	}
*/
	private void determineMoveLegality (ChessApplication app, ChessMove move) {
		
		Chessboard tmpBoard = this.getGameBoard().copyChessboard(app);
		
		String playerColor = move.getMovingPiece().getColor();
		String opponentColor = (playerColor.equals("white")) ? "black" : "white";
		
		Map<String, List<Piece>> copyMap = new HashMap<String, List<Piece>>();
		List<Piece> playerPieceList = copyPieceList(this.pieceListMap.get(playerColor));
		List<Piece> opponentPieceList = copyPieceList(this.pieceListMap.get(opponentColor));
		copyMap.put(playerColor, playerPieceList);
		copyMap.put(opponentColor, opponentPieceList);
		
		tmpBoard.fillChessboard(copyMap, app);
		
		Piece movingPiece = tmpBoard.getPiece(move.getMoveSrc());
		
		GridCoord kingSquare = (playerColor.equals("white")) ? new GridCoord(kingWhite.getCoord()) : new GridCoord(kingBlack.getCoord());
		
		GridCoord moveSource = move.getMoveSrc();
		GridCoord moveDest   = move.getMoveDest();

		// If destination is occupied, clear the square
		if(tmpBoard.getSquare(moveDest).getIsOccupied()) {
			//opponentPieceList.remove(findPieceByPiece(opponentPieceList, tmpBoard.getSquare(moveDest).getOccupyingPiece()));
			opponentPieceList.remove(tmpBoard.getSquare(moveDest).getOccupyingPiece());
			tmpBoard.getSquare(moveDest).clearSquare();
		}

		// Modify the coordinates of the piece and place the piece on the new square while clearing the source square
		movingPiece.setCoord(moveDest);
		tmpBoard.getSquare(moveDest).setOccupyingPiece(movingPiece);
		tmpBoard.getSquare(moveSource).clearSquare();

		// If the moving piece is the king, update the king square variable accordingly
		if (movingPiece.getName().equals("king")) {
			kingSquare = new GridCoord(movingPiece.getCoord());
		}

		// Compute the next available moves and review if the king is in check
		for(int i = 0; i < opponentPieceList.size(); i++) {
			ChessMove[] moveArray = opponentPieceList.get(i).computeAvailableMoves(tmpBoard);
			for (int j = 0; j < moveArray.length; j++) {
				if (moveArray[j].getMoveDest().equals(kingSquare)) {
					move.setIsLegal(false);
				}
			}
		}
	}

	public static List<Piece> copyPieceList(List<Piece> origList) {
		List<Piece> copyList = new ArrayList<Piece>();
		for (int i = 0; i < origList.size(); i++) {
			Piece copyPiece = origList.get(i).copyPiece();
			copyList.add(copyPiece);
		}
		return copyList;
	}
	
	public static Piece findPieceByPiece(List<Piece> list, Piece p) {
		
		for(int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(p)) {
				return list.get(i);
			}
		}
		return null;
	}
	
	public int findKingIndex(String col) {
		for (int i = 0; i < this.pieceListMap.get(col).size(); i++) {
			if (this.pieceListMap.get(col).get(i).getName().equals("king")) {
				return i;
			}
		}
		return 0;
	}

}




