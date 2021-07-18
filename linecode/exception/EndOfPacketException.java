package eit.linecode.exception;


/**
 * This class is for EndOfPacketException.
 * 
 * @author Ceylan Yildirim
 *
 */ 
@SuppressWarnings("serial")
public class EndOfPacketException extends RuntimeException{
	
	
	/**
	 * This exception occurs if one of the data streams has faulty end delimiters. 
	 * Accordingly, this is made known to the user with the messages displayed.
	 * Depending on the occurrence of the error during decoding, the message End of Stream X is incorrect!!! 
	 * is generated, where X is the number of the stream containing the error.
	 * 
	 * @param msg the exception message.
	 */
	
	public EndOfPacketException(String msg) {
		super(msg);

	}

}
