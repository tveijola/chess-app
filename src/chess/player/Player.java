package chess.player;

public class Player {
	
	private String playerColor;
	private String playerName;
	
	public Player (String col, String name) {
		this.playerColor = col;
		this.playerName = name;
	}

	public String getPlayerColor() {
		return this.playerColor;
	}

	public void setPlayerColor(String col) {
		this.playerColor = col;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}
	
	
	
}
