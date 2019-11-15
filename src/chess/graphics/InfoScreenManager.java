package chess.graphics;

import chess.chessboard.Chessboard;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoScreenManager {

	private VBox infoScreen;
	private HBox buttonBox;
	private TextArea textLogger;
	private TextArea moveLogger;
	private Stage optScreen;
	private VBox optionsVBox;
	
	public InfoScreenManager (double INFOSCREEN_WIDTH, double INFOSCREEN_HEIGHT) {
		this.infoScreen = new VBox(20);
		this.infoScreen.setMaxWidth(INFOSCREEN_WIDTH);
		this.infoScreen.setMaxHeight(INFOSCREEN_HEIGHT);
		
		this.buttonBox  = new HBox(25);
		this.textLogger = new TextArea();
		this.moveLogger = new TextArea();
		
		this.textLogger.setEditable(false);
		this.textLogger.setMaxSize(360, 400);
		this.textLogger.setMinSize(360, 400);

		this.moveLogger.setEditable(false);
		this.moveLogger.setMaxSize(360, 400);
		this.moveLogger.setMinSize(360, 400);
		
		this.optScreen = new Stage();
		
		this.createOptionScreen();
		
		this.buttonBox.setPadding(new Insets(20, 25, 20, 25));
		this.addButtonsToHBox();
		this.addNodesToVBox();
	}

	public VBox getInfoScreen() {
		return this.infoScreen;
	}

	public HBox getButtonBox() {
		return this.buttonBox;
	}

	public TextArea getTextLogger() {
		return this.textLogger;
	}

	public TextArea getMoveLogger() {
		return this.moveLogger;
	}
	
	public Stage getOptScreen () {
		return this.optScreen;
	}
	
	public VBox getOptionsVBox () {
		return this.optionsVBox;
	}
	
	// ADD BUTTON NODES TO HORIZONTAL BOX
	private void addButtonsToHBox () {

		Button flipBtn = new Button();
		flipBtn.setId("flipBtn");
		flipBtn.setPrefSize(100, 20);
		flipBtn.setText("FLIP BOARD");

		Button exitBtn = new Button();
		exitBtn.setPrefSize(100, 20);
		exitBtn.setId("exitBtn");
		exitBtn.setText("EXIT");
		
		Button optBtn = new Button();
		optBtn.setPrefSize(100, 20);
		optBtn.setId("optBtn");
		optBtn.setText("OPTIONS");
		
		this.buttonBox.getChildren().addAll(flipBtn, exitBtn, optBtn);
	}

	// ADD HORIZONTAL BOX AND TEXTAREAS TO VERTICAL BOX
	private void addNodesToVBox () {
		this.infoScreen.getChildren().addAll(this.buttonBox, this.moveLogger, this.textLogger);
		VBox.setMargin(this.moveLogger, new Insets(20, 20, 20, 20));
		VBox.setMargin(this.textLogger, new Insets(20, 20, 20, 20));
	}
	
	
	private void createOptionScreen () {
		
		// Set modality
		this.optScreen.initModality(Modality.APPLICATION_MODAL);
		
		// Create vertical box to store option checkboxes
        this.optionsVBox = new VBox(20);
        optionsVBox.setPadding(new Insets(20, 20, 20, 20));
        
        // Create option checkboxes
        CheckBox optionFlipAfterMove = new CheckBox("Flip board after each move.");
        optionFlipAfterMove.setId("optionFlipAfterMove");
        optionFlipAfterMove.setIndeterminate(false);
        optionFlipAfterMove.setSelected(true);
        
        // Create option checkboxes
        CheckBox optionHighlightMoves = new CheckBox("Highlight available moves.");
        optionHighlightMoves.setId("optionHighlightMoves");
        optionHighlightMoves.setIndeterminate(false);
        optionHighlightMoves.setSelected(true);
        
        // Add checkboxes to VBox
        optionsVBox.getChildren().addAll(optionFlipAfterMove, optionHighlightMoves);
        
        // Show the options window
        Scene optionScene = new Scene(optionsVBox);
        optScreen.setScene(optionScene);
	}
	
	
	public void setButtonActions (Chessboard board, GridPane background) {
		
		Button flipBtn;
		Button exitBtn;
		Button optBtn;
		
		ObservableList<Node> children = this.buttonBox.getChildren();
		for (Node node : children) {		
			switch (node.getId()) {
			case "flipBtn":
				flipBtn = (Button) node;
				flipBtn.setOnAction(click -> {
					NodeManipulator.flipBoard(board);
					NodeManipulator.flipBoard(background);
				});
				break;
			case "exitBtn":
				exitBtn = (Button) node;
				exitBtn.setOnAction(click -> {
					NodeManipulator.addTxtToTextArea(this.textLogger, "Quitting...");
					System.out.println("Quitting...");
					Platform.exit();
				});
				break;
			case "optBtn":
				optBtn = (Button) node;
				optBtn.setOnAction(click -> {
		            this.optScreen.show();
				});
				break;
			default:
				break;
			}
		}
	}
	
	public CheckBox getOptCheckBox (String id) {
		CheckBox result = null;
		ObservableList<Node> children = this.optionsVBox.getChildren();
		for (Node node : children) {
			if (node.getId().equals(id)) {
				result = (CheckBox) node;
				break;
			}
		}
		return result;
	}
}
