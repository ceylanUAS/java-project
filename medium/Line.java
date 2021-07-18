package eit.medium;

import java.util.NoSuchElementException;

/**
 * This interface defines the methods transmit(), receive(), and hasData().
 * 
 * @author Ceylan Yildirim
 *
 * @param <E> input of the user.
 */

public interface Line<E> {
	
	/**
	 * This function transmits the inputs into a queue.
	 * 
	 * @param element input of the user that should be transmitted.
	 * @return boolean 
	 */
	
	
	 boolean transmit(E element);
	
	/**
	 * With this function the data can then be retrieved from the queue.
	 * 
	 * @throws exception if the queue is empty.
	 * @return E the string array will be returned.
	 */

	 E receive() throws NoSuchElementException;
	
	/**
	 * This method returns true if in the queue isn't empty.
	 * 
	 * @return boolean
	 */

	 boolean hasData();
	
	
		
	
}
