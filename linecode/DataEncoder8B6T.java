package eit.linecode;



/**
 * This class implements the encoder functionality of the 8B/6T encoder. This
 * method converts octet (bytes) into the transmission structure according to
 * the 8B/6T encoder. As a result, an array consisting of strings is returned.
 * 
 * @author Ceylan Yildirim, 1211500
 */

public class DataEncoder8B6T implements Codes6T{
	
	/**
	 * This method converts octets (bytes) into the transmission structure according
	 * to the 8B/6T encoder.
	 * <p>
	 * As a result, an array consisting of strings is returned.
	 * 
	 * @param data unsers input
	 * @return DataEncoder8B6T Class returns a String Array where the three encoded
	 *         Datastreams are stored
	 */

	public String[] encode(byte[] data) {

	
		String[] userInput = convertToHex(data);

		String[] dataStream = new String[THIRD];
		dataStream[0] = "";
		dataStream[1] = "";
		dataStream[2] = "";

		int array_index = 0;
		// the letters of the input will be segmented in 3 and stored in 3 data streams
		for (int i = 0; i < userInput.length; i++) {
			if (data[i] >= 0 && data[i] < CODE1_BEGIN) {
				userInput[i] = "0" + userInput[i];
			}
			dataStream[array_index] += userInput[i];
			array_index++;
			if (array_index == THIRD) {
				array_index = 0;
			}
		}

		dataStream[0] = buildDataStream(TABLE, dataStream[0], EOP1, EOP4);
		dataStream[1] = buildDataStream(TABLE, dataStream[1], EOP2, EOP5);
		dataStream[2] = buildDataStream(TABLE, dataStream[2], EOP3, "");

		String dataSet_0 = (P4 + SOSA + SOSB + dataStream[0]);
		String dataSet_1 = (SOSA + SOSA + SOSB + dataStream[1]);
		String dataSet_2 = (P3 + SOSA + SOSA + SOSB + dataStream[2]);

		String[] identifier = new String[THIRD];
		identifier[0] = dataSet_0;
		identifier[1] = dataSet_1;
		identifier[2] = dataSet_2;

		// the three data streams will be returned to main
		return identifier;

	}

	/**
	 * This method converts the input (bytes) into hexadecimal. Each letter of the
	 * input will be converted to hexadecimal.
	 * 
	 * @param dataSet byte array of hexcadeimal values of the input.
	 * @return <b>charToHex</b> string array of hexadecimal values.
	 */
	
	public String[] convertToHex(byte[] dataSet) {
		String[] charToHex = new String[dataSet.length];

		for (int i = 0; i < dataSet.length; i++) {
			charToHex[i] = Integer.toHexString(dataSet[i]);
			if (dataSet[i] < 0) {
				charToHex[i] = charToHex[i].substring(CODE_LENGTH, EIGHT);
			}
		}
		return charToHex;

	}

	/**
	 * This method converts the String into six ternary symbols the symbols are
	 * found in the list by the hex code.
	 * 
	 * @param table      table of ternary symbol of each 6T Code group.
	 * @param dataStream one letter of the input.
	 * @return encodedString String of 6 ternary symbols.
	 */
	
	public static String encodeForwards(String table, String dataStream) {

		String encodedString = new String();
		for (int i = 0; i < table.length(); i++) {
			if (table.charAt(i) == dataStream.charAt(0) && table.charAt(i + 1) == dataStream.charAt(1)) {
				encodedString = table.substring(i + THIRD, i + NINE);

			}
		}
		return encodedString;
	}


	/**
	 * This method takes the 6T for each letter of the datastream and counts the
	 * DC balance DC balance is only 0 or 1. 
	 * <p>If the DC balance is 1 and a codeword
	 * with the balance 1 is to be transmitted,<p> the inverted DC balance gets the
	 * value (-1).
	 * <p>
	 * If the sum of the DC balance for each Datastream is 0 (cumulative sum), the
	 * eop (end of package) will be inverted.
	 * 
	 * @param table      the 8B6T code table.
	 * @param dataStream string with hex values of each data stream.
	 * @param eopFirst   first 'end of package' string.
	 * @param eopLast    last 'end of package' string.
	 * @return newDatastream datastream of 6T Code groups.
	 */
	
	public String buildDataStream(String table, String dataStream, String eopFirst, String eopLast) {
		int first6T, next6T = 0, inverted_sum = 0, cumulative_sum = 0;
		String newDatastream = new String();
		
		int length = dataStreamLength(dataStream);

		String[] dataStreamArray = subString(dataStream, length);

		for (int i = 0; i < length; i++) {

			dataStream = (encodeForwards(table, dataStreamArray[i]));
			first6T = Util.dcBalance(dataStream);

			if (first6T == 1 && next6T == 1) {

				dataStream = Util.invertCode(dataStream);

				first6T = 0;
				inverted_sum = first6T - 1;

			} else if (first6T == 0 && next6T == 1) {

				inverted_sum = first6T + 0;
				first6T = 1;
			} else {
				inverted_sum = first6T + 0;

			}
			cumulative_sum = cumulative_sum + inverted_sum;
			next6T = first6T;

			newDatastream = newDatastream + dataStream;
		}
		if (cumulative_sum == 0)

		{
			eopLast = Util.invertCode(eopLast);
			eopFirst = Util.invertCode(eopFirst);
		}

		return (newDatastream + eopFirst + eopLast);

	}
	
	/**
	 * This method counts the data stream length.
	 * 
	 * @param dataStream string of hexadecimal values of a data stream.
	 * @return integer the length of a data stream.
	 */

	public int dataStreamLength(String dataStream) {
		
		int streamLength;
		if (dataStream.length() % 2 == 0) {
			streamLength = dataStream.length() / 2;

		} else {
			streamLength = dataStream.length() / 2;
			streamLength++;

		}
		return streamLength;

	}
	
	/**
	 * This method distributes the hexacimal values of a data stream in many strings.
	 * 
	 * @param dataStream string of hexadecimal values of a data stream.
	 * @param length of the data stream.
	 * @return	dataStreamArray
	 * distributed hexadecimal values of the data stream in seperate strings.
	 */ 
	
	public String[] subString(String dataStream, int length) {
		int arrayCounter = 0;
		
		String[] dataStreamArray = new String[length];
		
		for (int i = 0; i < dataStream.length(); i += 2) {
			dataStreamArray[arrayCounter] = dataStream.substring(i, i + 2);
			
			arrayCounter++;
		}
		return dataStreamArray;

	}
}