package eit.application.exception;

/**
 * This class is for a FileLengthException.
 * 
 * @author Ceylan Yildirim
 *
 */

@SuppressWarnings("serial")


public class FileLengthException extends RuntimeException{
	
	/**
	 * This exception will be thrown, if the file is bigger than (2^24)-1 bytes.
	 * 
	 * @param msg the exception message.
	 */
	
	public FileLengthException(String msg) {
		super(msg);

	}

}
