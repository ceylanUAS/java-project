package eit.linecode;


import eit.linecode.exception.DecodeException;
import eit.linecode.exception.EndOfPacketException;
import eit.linecode.exception.StartOfStreamException;

/**
 * This Class converts the transmission structure back into octets according to
 * the 8B/6T decoder.
 * <p>
 * As a result, an array consisting of bytes is returned.
 * <p>
 * If there are failures in the input the first failure will throw an exception
 * with a message but the program doesn't terminates.
 * 
 * @author Ceylan Yildirim, 1211500
 *
 */

public class DataDecoder8B6T implements Codes6T {

	/**
	 * This method seperated the three datastreams in byte arrays und checks if they
	 * are correct. It also gets the decoded string of the input ans returns to
	 * main.
	 * 
	 * @param data datastream.
	 * @return byte array of the 6 ternary bytes converted to byte.
	 */

	public byte[] decode(String[] data) {
		byte[] stream1, stream2, stream3, stream;
		boolean checker = false;
		stream1 = checkFirstStream(data[0]);
		stream2 = checkSecondStream(data[1]);
		stream3 = checkThirdStream(data[2]);

		checker = checkLength(stream1.length, stream2.length, stream3.length);
		if (checker == true) {
			int dataStreamLength = stream1.length + stream2.length + stream3.length;
			stream = createDatastream(stream1, stream2, stream3, dataStreamLength);
			return stream;
		}
		return null;
	}

	/**
	 * This method seperated the first stream in three parts. The begin, code and
	 * end. Then it checks if the datastream is correct. First it checks the begin
	 * of the stream and then the end.
	 * <p>
	 * After that the code will be checked and the DC balance will be count.
	 * <p>
	 * In consideration of the DC balance the end will be checked again.
	 * 
	 * @param stream first datastream.
	 * @return byte array of the 6 ternary bytes of the first datastream converted
	 *         to byte.
	 */
	public byte[] checkFirstStream(String stream) {
		String startOfstream = stream.substring(0, CODE1_BEGIN);
		String endofStream = stream.substring(stream.length() - LENGTH_OF_END, stream.length());
		String code = stream.substring(CODE1_BEGIN, stream.length() - LENGTH_OF_END);
		startOfStream(startOfstream, 0);
		checkEndOfStream(endofStream, 0);
		int dcBalance = Util.dcBalance(code);
		byte[] streamCode = checkDataLength(code, 0);
		checkEndOfStream2(endofStream, dcBalance, 0);
		return streamCode;

	}

	/**
	 * This method seperated the second stream in three parts. The begin, code and
	 * end. Then it checks if the datastream is correct. First it checks the begin
	 * of the stream and then the end.
	 * <p>
	 * After that the code will be checked and the DC balance will be count.
	 * <p>
	 * In consideration of the DC balance the end will be checked again.
	 * 
	 * @param stream second datastream.
	 * @return byte array of the 6 ternary bytes of the second datastream converted
	 *         to byte.
	 */

	public byte[] checkSecondStream(String stream) {
		String startOfstream = stream.substring(0, CODE2_BEGIN);
		String endofStream = stream.substring(stream.length() - LENGTH_OF_END, stream.length());
		String code = stream.substring(CODE2_BEGIN, stream.length() - LENGTH_OF_END);
		startOfStream(startOfstream, 1);
		checkEndOfStream(endofStream, 1);
		int dcBalance = Util.dcBalance(code);
		byte[] streamCode = checkDataLength(code, 1);
		checkEndOfStream2(endofStream, dcBalance, 1);
		return streamCode;
	}

	/**
	 * This method seperated the third stream in three parts. The begin, code and
	 * end. Then it checks if the datastream is correct. First it checks the begin
	 * of the stream and then the end.
	 * <p>
	 * After that the code will be checked and the DC balance will be count.
	 * <p>
	 * In consideration of the DC balance the end will be checked again.
	 * 
	 * @param stream first datastream.
	 * @return byte array of the 6 ternary bytes of the third datastream converted
	 *         to byte.
	 */

	public byte[] checkThirdStream(String stream) {
		String startOfstream = stream.substring(0, CODE3_BEGIN);
		String endofStream = stream.substring(stream.length() - CODE_LENGTH, stream.length());
		String code = stream.substring(CODE3_BEGIN, stream.length() - CODE_LENGTH);
		startOfStream(startOfstream, 2);
		checkEndOfStream(endofStream, 2);
		int dcBalance = Util.dcBalance(code);
		byte[] streamCode = checkDataLength(code, 2);
		checkEndOfStream2(endofStream, dcBalance, 2);
		return streamCode;
	}

	/**
	 * This method checks the start of the stream of correctness. If the start is
	 * wrong it throws an exception.
	 * 
	 * @param stream       the datastream.
	 * @param streamNumber number of which datastream will be checked now.
	 */

	public void startOfStream(String stream, int streamNumber) {

		if (streamNumber == 0 && !DATA_1_START.equals(stream)) {
			throw new StartOfStreamException("Start of Stream 1 is incorrect!!!");
		}
		if (streamNumber == 1 && !DATA_2_START.equals(stream)) {

			throw new StartOfStreamException("Start of Stream 2 is incorrect!!!");
		}
		if (streamNumber == 2 && !DATA3START.equals(stream)) {
			throw new StartOfStreamException("Start of Stream 3 is incorrect!!!");

		}
	}

	/**
	 * This method checks the distance between the numbers of the letters. The first
	 * datastream can only have a maximal distance of 6T (1 letter more) to the
	 * other datastreams.
	 * <p>
	 * Its also possible that the first and second datastream have 6T (1 letter)
	 * more than the third datastream. But in other case the program thows an
	 * exception with the message <b> "Wrong data size!!!" </b>.
	 * 
	 * @param firstStream  first datastream.
	 * @param secondStream second datastream.
	 * @param thirdStream  third datastream.
	 * @return true if it don't thows an exception.
	 */

	public boolean checkLength(int firstStream, int secondStream, int thirdStream) {
		if (firstStream < secondStream || firstStream < thirdStream || secondStream < thirdStream) {
			throw new DecodeException("Wrong data size!!!");
		}
		if ((firstStream - secondStream > 1) || (firstStream - thirdStream > 1) || (secondStream - thirdStream > 1)) {
			throw new DecodeException("Wrong data size!!!");
		}
		return true;
	}

	/**
	 * This function checks the end of the stream. If the end is equal to the
	 * declerated end of stream package or the inverted end of stream package. If
	 * it's not equal the program thows an exception with the message <b> "End of
	 * Stream i is incorrect!!!" </b>.
	 * 
	 * @param data         datastream.
	 * @param streamNumber number of which datastream will be checked now.
	 */

	public void checkEndOfStream(String data, int streamNumber) {
		switch (streamNumber) {
		case 0:
			if (!END_OF_PACKAGE1.equals(data) && !END_OF_PACKAGE1_INV.equals(data)) {
				throw new EndOfPacketException("End of Stream 1 is incorrect!!!");
			}
			break;
		case 1:
			if (!END_OF_PACKAGE2.equals(data) && !END_OF_PACKAGE2_INV.equals(data)) {
				throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
			}
			break;
		case 2:
			if (!END_OF_PACKAGE3.equals(data) && !END_OF_PACKAGE3_INV.equals(data)) {
				throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
			}
		default:
			break;
		}
	}

	/**
	 * This function checks the end of the stream again. But this time the DC
	 * balance is given. If the end of stream is wrong the program throws an
	 * exception with the message <b> "End of Stream i is incorrect!!!" </b>.
	 * 
	 * @param data         datastream.
	 * @param dcBalance    the dc balance of 6 ternary bytes as integer.
	 * @param streamNumber number of which datastream will be checked now.
	 *
	 */
	public void checkEndOfStream2(String data, int dcBalance, int streamNumber) {

		switch (streamNumber) {
		case 0:
			if (dcBalance == 0 && !END_OF_PACKAGE1_INV.equals(data)) {
				throw new EndOfPacketException("End of Stream 1 is incorrect!!!");

			}

			if (dcBalance == 1 && !END_OF_PACKAGE1.equals(data)) {
				throw new EndOfPacketException("End of Stream 1 is incorrect!!!");
			}
			break;

		case 1:
			if (dcBalance == 0 && !END_OF_PACKAGE2_INV.equals(data)) {
				throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
			}
			if (dcBalance == 1 && !END_OF_PACKAGE2.equals(data)) {
				throw new EndOfPacketException("End of Stream 2 is incorrect!!!");
			}
			break;
		case 2:

			if (dcBalance == 0 && !END_OF_PACKAGE3_INV.equals(data)) {
				throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
			}
			if (dcBalance == 1 && !END_OF_PACKAGE3.equals(data)) {
				throw new EndOfPacketException("End of Stream 3 is incorrect!!!");
			}
		default:
			break;
		}
	}

	/**
	 * This method merges the bytes (just of the 6T code) of the three datastreams
	 * to one datastream.
	 * 
	 * @param stream1 first datastream.
	 * @param stream2 second datastream.
	 * @param stream3 third datastream.
	 * @param length  the length of the three datastream codes together.
	 * @return byte array of the decoded datastream.
	 */

	public byte[] createDatastream(byte[] stream1, byte[] stream2, byte[] stream3, int length) {
		int index = 0, next = 0, i = 0;
		byte[] dataStream = new byte[length];
		while (i < length) {
			dataStream[index] = stream1[next];
			index = index + 1;
			if (next < stream2.length) {
				dataStream[index] = stream2[next];
				index = index + 1;
			}
			if (next < stream3.length) {
				dataStream[index] = stream3[next];
				index = index + 1;
			}
			next++;
			i = i + THIRD;
		}

		return dataStream;
	}

	/**
	 * This method checks the middle of the datastream where the 6T strings are to
	 * be decoded. The size of the datastream should be modulo six.
	 * <p>
	 * If its wrong than the program thows an exception with the message <b> "Wrong
	 * data size!!!" </b>. If its true the method returns a byte aray with the decoded
	 * datastream.
	 * 
	 * @param data         datastream.
	 * @param streamNumber streamNumber number of which datastream will be checked
	 *                     now.
	 * @return byte array  with the decoded datastream.
	 */

	public byte[] checkDataLength(String data, int streamNumber) {
		int lengthOfStream = data.length();
		String decodedWord = new String();

		int byteCodeInt = 0, index = 0;
		byte[] array = new byte[data.length() / CODE_LENGTH];
		if (data.length() % CODE_LENGTH == 0) {
			int i = 0;
			while (i < lengthOfStream) {
				decodedWord = Util.decodeForwards(data.substring(i, i + CODE_LENGTH));
				try {
					byteCodeInt = Integer.parseInt(decodedWord, CODE1_BEGIN);
				} catch (NumberFormatException ex) {
					throw new DecodeException("Wrong data size!!!");
				}
				array[index] = (byte) byteCodeInt;
				index++;
				i = i + CODE_LENGTH;

			}
		} else {
			throw new DecodeException("Wrong data size!!!");
		}

		return array;
	}

}