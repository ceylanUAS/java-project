package eit.linecode.exception;


/**
 * This class is for DecodeException.
 * 
 * @author Ceylan Yildirim
 *
 */
@SuppressWarnings("serial")
public class DecodeException extends RuntimeException{
	
	/**
	 * This exception occurs when one of the data streams is faulty. 
	 * This means that, for example, a data stream has one ternary symbol too few or too many,
	 * but this does not count as a delimiter. Here it is not decisive in which data stream the error occurred.
	 * 
	 * @param msg the exception message.
	 */

	public DecodeException(String msg) {
		super(msg);

	}
}
