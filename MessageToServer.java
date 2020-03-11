import java.io.Serializable;

public class MessageToServer implements Serializable{
	
	/**
	 * 	1 - Increase stake
	 * 	2 - Decrease stake
	 * 	3 - 
	 */
	private Integer code;
	private Integer ID;
	
	public MessageToServer(Integer code, Integer ID) {
		this.code = code;
		this.ID = ID;
	}

	public Integer getCode() {
		return code;
	}

	public Integer getID() {
		return ID;
	}
	
	
}
