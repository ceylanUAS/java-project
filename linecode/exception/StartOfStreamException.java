package eit.linecode.exception;

/**
 * This class is for a StartOfStreamException.
 * 
 * @author ceylo
 *
 */

@SuppressWarnings("serial")
public class StartOfStreamException extends RuntimeException {

	/**
	 * This exception occurs if one of the data streams has faulty start delimiters.
	 * Depending on the occurrence of the error during decoding, the message Start
	 * of Stream X is incorrect!!! is generated, where X is the number of the stream
	 * containing the error.
	 * 
	 * @param msg the exception message.
	 */

	public StartOfStreamException(String msg) {
		super(msg);

	}

}
