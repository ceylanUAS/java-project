package eit.medium;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import eit.linecode.Codes6T;

/**
 * This class puts the input in the queue with the command <b>"transmit"</b>.
 * <p>
 * With the command <b>"receive"</b> the data is outputted of the queue. The
 * cable works in the FIFO principe.
 * 
 * @author Ceylan Yildirim
 *
 * @param <E> input of the user.
 * 
 */

public class Cable<E> implements Line<E>, Codes6T {

	static ArrayList<String[]> list = new ArrayList<String[]>();

	/**
	 * This method puts the encoded string into the queque.
	 * 
	 */

	@Override
	public boolean transmit(E element) {

		String[] dataStreams = new String[THIRD];
		dataStreams = (String[]) element;
		list.add((String[]) element);

		return true;
	}

	/**
	 * This function gets the value out of the queue.
	 * 
	 * @throws exception if the queue is empty.
	 * @return E the string array will be returned.
	 */

	@SuppressWarnings("unchecked")
	@Override

	public E receive() throws NoSuchElementException {
		if (hasData() == false) {
			throw new NoSuchElementException();
		}

		Iterator<String[]> it = list.iterator();

		String[] dataStreams = new String[THIRD];
		
		
		dataStreams = it.next();

		if (dataStreams == null) {
			throw new NoSuchElementException();
		}
		it.remove();
		return (E) dataStreams;
	}

	/**
	 * This function checks if the queue is empty or not.
	 * 
	 * @return boolean.
	 */
	@Override
	public boolean hasData() {

		boolean hasData = true;

		if (list.isEmpty()) {

			hasData = false;

		}
		return hasData;

	}

}