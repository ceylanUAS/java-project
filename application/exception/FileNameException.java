package eit.application.exception;

/**
 * This class is for a FileNameException.
 * 
 *
 */
@SuppressWarnings("serial")
public class FileNameException extends RuntimeException{
	
	/**
	 * This method throws an exception, if the filename ist too long or too short.
	 * 
	 * @param msg the exception message.
	 */
	public FileNameException(String msg) {
		super(msg);

	}

}