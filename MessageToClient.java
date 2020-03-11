import java.io.Serializable;

public class MessageToClient implements Serializable{
	
	/*
	 * Message codes:
	 * 	1 - Players array (game state for client)
	 * 	2 - 
	 */
	private Integer code;
	private Integer clientID;
	private String gameMessage;
	
	/*
	 * Array of players to store game state for client
	 * 
	 * This is only passed if updating client
	 * Set to null otherwise
	 */
	private Player[] players;
	
	public MessageToClient(Integer code, Integer clientID,  Player[] players, String gameMessage) {
		this.code = code;
		this.clientID = clientID;
		this.players = players;
		this.gameMessage = gameMessage;
	}

	public Integer getCode() {
		return code;
	}

	public Integer getClientID() {
		return clientID;
	}

	public Player[] getPlayers() {
		return players;
	}

	public String getGameMessage() {
		return gameMessage;
	}
	
	
	
	
}
