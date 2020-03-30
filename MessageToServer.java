import java.io.Serializable;

/**
 * Message class to send code to server to indicate 
 * what action client wishes to request
 * @author Andrew
 *
 */
public class MessageToServer implements Serializable{
	
	/**
	 * 	Code to indicated request.
	 * 
	 * 	Code:
	 * 	1 - Increase stake
	 * 	2 - Decrease stake
	 * 	3 - Draw card
	 *  4 - Stand (end turn)
	 *  5 - Join table
	 *  6 - Leave table
	 */
	private Integer code;
	
	/**
	 * ID of this client
	 */
	private Integer ID;
	
	/**
	 * Constructor
	 * @param code request code
	 * @param ID ID of this client
	 */
	public MessageToServer(Integer code, Integer ID) {
		this.code = code;
		this.ID = ID;
	}
	
	/**
	 * Getter for code
	 * @return code
	 */
	public Integer getCode() {
		return code;
	}
	
	/**
	 * Getter for ID
	 * @return ID
	 */
	public Integer getID() {
		return ID;
	}
	
	
}
