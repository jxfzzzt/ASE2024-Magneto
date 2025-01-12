package net.sourceforge.ondex.event.type;

/**
 * EventType for deserialisation errors.
 * 
 * @author taubertj
 * 
 */
public class DeserialisationFailedEvent extends EventType {

	/**
	 * Constructor for a customized message with extension.
	 * 
	 * @param message
	 *            String
	 * @param extension
	 *            String
	 */
	public DeserialisationFailedEvent(String message, String extension) {
		super(message, extension);
		super.desc = "An error during ONDEX object deserialisation occurred.";
		this.setLog4jLevel(Level.ERROR);
	}

}
