package eit.linecode;

/**
 * This class contains common methods of the endoder and decoder class.
 * 
 * @author Ceylan Yildirim, 1211500
 *
 */

public class Util implements Codes6T {

	/**
	 * This method counts the minus and plus of the 6 ternary symbols. The sum will
	 * be returned.
	 * 
	 * @param dataStream string of 6T code groups that have to be balanced.
	 * @return balance integer sum of the plus and minus.
	 */

	public static int dcBalance(String dataStream) {

		Integer count_plus = 0, count_minus = 0, balance = 0;
		for (int i = 0; i < dataStream.length(); i++) {

			if (dataStream.charAt(i) == '+') {
				count_plus++;
			} else if (dataStream.charAt(i) == '-') {
				count_minus++;
			}
		}
		if (count_plus > count_minus) {
			balance = 1;
		} else {
			balance = 0;
		}

		return balance;
	}

	/**
	 * This method inverts the 6 ternary symbols.
	 * <p>
	 * all '+' will be '-' and vice versa.
	 *
	 * @param toInvertedCode string of 6T code group of one letter.
	 * @return invertedString string of 6T code goup that is inverted.
	 */

	public static String invertCode(String toInvertedCode) {
		String invertedString = new String();

		for (int i = 0; i < toInvertedCode.length(); i++) {
			if (toInvertedCode.charAt(i) == '+') {
				invertedString += '-';
			} else if (toInvertedCode.charAt(i) == '-') {
				invertedString += '+';
			} else {
				invertedString += '=';
			}

		}
		return invertedString;
	}


	/**
	 * This method inverts the code with couldn't found in the table.
	 * 
	 * @param dataStream datastream.
	 * @return decodedString inverted code as string.
	 */

	public static String decodeForwards(String dataStream) {
		String decodedString = "false";
		decodedString = search(dataStream);
		if (decodedString.equals("false")) {
			dataStream = Util.invertCode(dataStream);
			decodedString = search(dataStream);
		}

		return decodedString;

	}

	/**
	 * This method searches the 6T in the table and takes the hexadecimal value.
	 * <p>
	 * If it can't be found in the table, it will be inverted and searched again.
	 * 
	 * 
	 * @param code 6 ternary symbols.
	 * @return string of the hexadecimal values of the datastream.
	 */

	public static String search(String code) {

		String decodedString = "false";
		char[] dataArray = code.toCharArray();
		for (int i = 0; i < TABLE.length() - CODE_LENGTH; i++) {
			if (dataArray[0] == TABLE.charAt(i) && dataArray[1] == TABLE.charAt(i + 1)
					&& dataArray[SECOND] == TABLE.charAt(i + SECOND) && dataArray[THIRD] == TABLE.charAt(i + THIRD)
					&& dataArray[FOURTH] == TABLE.charAt(i + FOURTH) && dataArray[FIFTH] == TABLE.charAt(i + FIFTH)) {
				decodedString = TABLE.substring(i - THIRD, i - FIRST);
			}
		}

		return decodedString;
	}
	
	/**
	 * This method converts byte to String.
	 * 
	 * @param byteValue the byte value of the code letters.
	 * @return string value which was converted
	 */

	public static String convertToString(byte byteValue) {
		String toString = Integer.toHexString(byteValue);
		toString = toString.substring(CODE_LENGTH);
		int decByte = Integer.parseInt(toString, CODE1_BEGIN);
		toString = Character.toString(decByte);
		return toString;
	}
	

}
