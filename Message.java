import java.io.Serializable;

public class Message implements Serializable{
	
	/*
	 * Message codes:
	 * 	1 - Players array (game state for client)
	 * 	2 - 
	 */
	private Integer code;
	private String gameMessage;
	
	/*
	 * Array of players to store game state for client
	 * 
	 * This is only passed if updating client
	 * Set to null otherwise
	 */
	private Player[] players;
	
	public Message(Integer code, Player[] players, String gameMessage) {
		this.code = code;
		this.players = players;
		this.gameMessage = gameMessage;
	}

	public Integer getCode() {
		return code;
	}

	public Player[] getPlayers() {
		return players;
	}

	public String getGameMessage() {
		return gameMessage;
	}
	
	
	
	
}
