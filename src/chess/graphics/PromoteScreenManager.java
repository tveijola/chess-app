package chess.graphics;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import chess.ChessApplication;
import chess.chesspiece.Pawn;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PromoteScreenManager {
	
	private static List<Button> promoteButtonList;

	public static void createPromoteScreen (ChessApplication app, Pawn p) {
		
		// Create option buttons
		promoteButtonList = createPromoteButtons();

		Stage promoteScreen = new Stage();
		// Set modality
		promoteScreen.initModality(Modality.APPLICATION_MODAL);

		// Create vertical box to store promote options
		VBox promoteVBox = new VBox(20);
		promoteVBox.setPadding(new Insets(20, 20, 20, 20));
		
		// Add promote choices to VBox
		promoteVBox.getChildren().addAll(promoteButtonList);

		// Show the options window
		Scene optionScene = new Scene(promoteVBox);
		promoteScreen.setScene(optionScene);
		
		setButtonActions (promoteScreen, app, p);
		
		promoteScreen.showAndWait();

	}
	
	private static List<Button> createPromoteButtons () {
		
		List<Button> promoteButtonList =  new ArrayList<Button>(4);
		String [] choices = {"Queen", "Rook", "Bishop", "Knight"};
		
		for (String s : choices) {
			Button btn = new Button(s);
			btn.setId(s.toLowerCase());
			promoteButtonList.add(btn);
		}
		
		return promoteButtonList;
	}
	
	
	public static void setButtonActions (Stage promoteScreen, ChessApplication app, Pawn p) {
		
		for (int i = 0; i < promoteButtonList.size(); i++) {
			Button btn = promoteButtonList.get(i);
			btn.setOnAction(click -> {
				try {
					Pawn.promote(app, p, btn.getId());
					promoteScreen.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			});
		}

	}

}
