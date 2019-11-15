package chess;

import java.util.List;

import chess.coordinates.GridCoord;
import chess.graphics.NodeManipulator;
import chess.chessboard.BoardSquare;
import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.chesspiece.Piece;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class GameCoordinator {

	/* ----------------------------------------------------------------------------------------
	 * METHODS TO ASSIGN MOUSE EVENTS TO NODES
	 * ---------------------------------------------------------------------------------------- */

	// ASSIGN MOUSE EVENTS TO BOARD SQUARES
	public static void assignActionToSquare (BoardSquare sq, ChessApplication app) {
		
		// When dragged piece enters a new square, update the visuals (and nothing else)
		sq.setOnMouseDragEntered(dragEntered -> {
			Piece draggedPiece = (Piece) dragEntered.getGestureSource();
			visualPieceMove(app, sq, draggedPiece);
		});
		
		// When dragged piece is released on a square, attempt to move it there
		sq.setOnMouseDragReleased(dragRelease -> {
			Piece draggedPiece = (Piece) dragRelease.getGestureSource();
			if (dragRelease.getButton() == MouseButton.PRIMARY){
				attemptPieceMove(app, sq, draggedPiece);
			}
		});
		
		// When dragged piece exits a square, set the piece on the square visible again
		sq.setOnMouseDragExited(dragExited -> {
			Node dragExitNode = (Node) dragExited.getSource();
			NodeManipulator.showPieceAtIndex(GridPane.getColumnIndex(dragExitNode), GridPane.getRowIndex(dragExitNode), app.getChessboard());
		});
	}

	// ASSIGN MOUSE EVENTS TO PIECES
	public static void assignActionToPieces (Piece p, ChessApplication app) {
		
		p.setOnMousePressed(press -> {
			if (press.getButton() == MouseButton.PRIMARY && !press.isSecondaryButtonDown()) {
				if (p.getColor().equals(app.getGameState().getColorToMove())) {
					p.setMouseTransparent(true);
					press.setDragDetect(true);
					if (app.getOptions("optionHighlightMoves")) {
						NodeManipulator.highlightMoves(app, p);
					}
					
				}
			}
		});

		p.setOnMouseDragged(mouseDragged -> {
			mouseDragged.setDragDetect(false);
		});

		p.setOnDragDetected(dragDetected -> {
			p.startFullDrag();
		});
	}

	
	
	/* ----------------------------------------------------------------------------------------
	 * METHODS TO UPDATE PIECE POSITIONS AND GAME STATE
	 * ---------------------------------------------------------------------------------------- */

	
	private static void visualPieceMove (ChessApplication app, BoardSquare sq, Piece draggedPiece) {
		if (!(GridPane.getColumnIndex(sq) == GridPane.getColumnIndex(draggedPiece) && GridPane.getRowIndex(sq) == GridPane.getRowIndex(draggedPiece))) {
			Node occupyingPiece = NodeManipulator.getPieceAtIndex(GridPane.getColumnIndex(sq), GridPane.getRowIndex(sq), app.getChessboard());
			if(occupyingPiece != null) {
				occupyingPiece.setVisible(false);
			}
		}
		NodeManipulator.updatePieceCoordinates(draggedPiece, sq, app.getChessboard());
	}
	
	private static void attemptPieceMove (ChessApplication app, BoardSquare sq, Piece draggedPiece) {

		TextArea moveLogger = app.getMainScreenManager().getInfoScreen().getMoveLogger();
		ChessMove move = ChessMove.parseMove(draggedPiece, sq, app);
		
		if (pieceHasMove(move, app)) {
			
			// Parse the move into algebraic notation format and print it to movelogger
			String movetext = ChessMove.determineMoveNotation(move, app);
			NodeManipulator.addTxtToTextArea(moveLogger, movetext);
			
			// Update piece position and game state
			updatePiecePosition(move, app);
			updateGameState(app);
		} else {
			NodeManipulator.setPieceNodeCoordinates(draggedPiece, move.getMoveSrc());
		}
		draggedPiece.setMouseTransparent(false);
		NodeManipulator.resetSquareFills(app);
	}
	
	private static void updatePiecePosition (ChessMove move, ChessApplication app) {

		Chessboard board = app.getChessgame().getGameBoard();

		Piece.makeMove(move, app);

		if(move.isCastling()) {

			GridCoord dest = move.getMoveDest();
			int col = dest.getGridCoord()[0];
			int row = dest.getGridCoord()[1];

			if (col == 6) {
				GridCoord rookSquare = new GridCoord(new int[] {7, row});
				GridCoord rookDest   = new GridCoord(new int[] {5, row});

				ChessMove rookMove = new ChessMove(rookSquare, rookDest, board.getSquare(rookSquare).getOccupyingPiece(), false);
				Piece.makeMove(rookMove, app);

			} else {
				GridCoord rookSquare = new GridCoord(new int[] {0, row});
				GridCoord rookDest   = new GridCoord(new int[] {3, row});

				ChessMove rookMove = new ChessMove(rookSquare, rookDest, board.getSquare(rookSquare).getOccupyingPiece(), false);
				Piece.makeMove(rookMove, app);
			}

		}
	}

	private static void updateGameState (ChessApplication app) {

		ChessGame      chessgame  = app.getChessgame();
		ChessGameState gamestate  = app.getGameState();
		Chessboard     board      = app.getChessboard();
		TextArea       infoLogger = app.getMainScreenManager().getInfoScreen().getTextLogger();
		TextArea       moveLogger = app.getMainScreenManager().getInfoScreen().getMoveLogger();
		
		
		// SET TURN NUMBER
		int turnNum = gamestate.getTurnNumber() + 1;
		gamestate.setTurnNumber(turnNum);

		// SET PLAYER
		String colToMove = gamestate.getTurnTracker()[turnNum % 2];
		gamestate.setColorToMove(colToMove);

		
		// FLIP THE BOARD
		if (app.getOptions("optionFlipAfterMove")) {
			NodeManipulator.flipBoard(board);
			NodeManipulator.flipBoard(app.getMainScreenManager().getBoardScreen().getBackground());
		}
		
		// Clear the available move info box
		infoLogger.clear();
		NodeManipulator.addTxtToTextArea(infoLogger, colToMove.toUpperCase() + " TO MOVE.\n");

		// PRINT COLOR TO MOVE
		if (turnNum % 2 == 0) {
			int turnNumToPrint = app.getTurnNumToPrint();
			turnNumToPrint += 1;
			NodeManipulator.addTxtToTextArea(moveLogger, "> " + turnNumToPrint + ". ");
			app.setTurnNumToPrint(turnNumToPrint);
		}

		String opponentColor = (colToMove.equals("white")) ? "black" : "white";
		
		// SET THE COLOR WHO JUST MOVED AS NOT IN CHECK
		// ASSUMES THAT THE PLAYER CAN'T BE IN CHECK AS THEY MADE A LEGAL MOVE
		chessgame.resetCheckToFalse(opponentColor);
		
		// RESET EN PASSANT CONDITION FOR COLOR TO MOVE
		chessgame.resetEnPassantCondition(colToMove);
		
		// COMPUTE AVAILABLE MOVES
		chessgame.computeAvailableMoves(opponentColor);
		
		// IS PLAYER TO MOVE IN CHECK
		boolean colorInCheck = chessgame.isColorInCheck(colToMove);
		if (colorInCheck) {
			NodeManipulator.addTxtToTextArea(infoLogger, colToMove.toUpperCase() + " IS IN CHECK\n");
		}
		
		chessgame.computeAvailableMoves(colToMove);
		chessgame.checkIllegalMoves(app);
		
		if (!colorInCheck) {
			chessgame.checkCastling(colToMove, app);
		}
		

		List<ChessMove[]> avMoves = chessgame.getMoveListMap().get(colToMove);
		List<Piece> pieces = chessgame.getPieceListMap().get(colToMove);

		for (int i = 0; i < avMoves.size(); i++) {
			Piece piece = pieces.get(i);
			NodeManipulator.addTxtToTextArea(infoLogger, "> " + piece.getName() + " at " + piece.getCoord().getAlgNotation() + " moves: ");
			for (int j = 0; j < avMoves.get(i).length; j++) {
				if (avMoves.get(i)[j].isLegal()) {
					String algNot = avMoves.get(i)[j].getMoveDest().getAlgNotation();
					if(avMoves.get(i)[j].isCapture()) {algNot = algNot + "x";}
					NodeManipulator.addTxtToTextArea(infoLogger, algNot + ", ");
				}
			}
			NodeManipulator.addTxtToTextArea(infoLogger, "\n");
		}
	}

	private static boolean pieceHasMove(ChessMove move, ChessApplication app) {

		Piece movingPiece = move.getMovingPiece();
		String color = movingPiece.getColor();
		int index = app.getChessgame().getPieceListMap().get(color).indexOf(movingPiece);
		ChessMove[] pieceMoves = app.getChessgame().getMoveListMap().get(color).get(index);

		for (int i = 0; i < pieceMoves.length; i++) {
			if (pieceMoves[i].isLegal() && pieceMoves[i].getMoveDest().equals(move.getMoveDest())) {
				return true;
			}
		}
		return false;
	}
}
