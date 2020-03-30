import java.io.Serializable;

/**
 * Message class to send game information to clients
 * @author Andrew
 *
 */
public class MessageToClient implements Serializable{
	
	/*
	 * Message codes:
	 * 	1 - Players array (game state for client)
	 * 	2 - 
	 */
	private Integer code;
	
	/**
	 * ID of client associated with this 
	 */
	private Integer clientID;
	
	/**
	 * Game message to be displayed
	 */
	private String gameMessage;
	
	/**
	 * Player associated with this client
	 */
	private Player player;
	
	/*
	 * Array of players to store game state for client
	 */
	private Player[] players;
	
	/**
	 * Constructor
	 * @param code 
	 * @param clientID client ID associated with this client
	 * @param players players array at table
	 * @param player player associated with this client
	 * @param gameMessage game message
	 */
	public MessageToClient(Integer code, Integer clientID,  Player[] players, Player player, String gameMessage) {
		this.code = code;
		this.clientID = clientID;
		this.players = players;
		this.gameMessage = gameMessage;
		this.player = player;
	}
	
	/**
	 * Getter for code
	 * @return code
	 */
	public Integer getCode() {
		return code;
	}
	
	/**
	 * Getter for client ID
	 * @return clientID
	 */
	public Integer getClientID() {
		return clientID;
	}
	
	/**
	 * Getter for players array
	 * @return players
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Getter for player associated with this client
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Getter for game message
	 * @return gameMessage
	 */
	public String getGameMessage() {
		return gameMessage;
	}
	
}
