package eit.cli;


import java.io.File;
import java.nio.charset.StandardCharsets;

import edu.fra.uas.oop.*;
import eit.application.EasyFileTransferProtocol;
import eit.linecode.Codes6T;
import eit.linecode.DataDecoder8B6T;
import eit.linecode.DataEncoder8B6T;
import eit.linecode.exception.DecodeException;
import eit.linecode.exception.EndOfPacketException;
import eit.linecode.exception.StartOfStreamException;
import eit.medium.Cable;

/**
 * This class is used to read commands from the console and to make output on
 * the console.
 * 
 * <p>
 * 'encode' is the command to encode data with the 8B/6T code.
 * <p>
 * 'quit' terminates the program. There is no more output in the console.
 * 
 * 'decode' is the command to decode 8B6T strings to words.
 * 
 * @author Ceylan Yildirim.
 * 
 */

public class Main implements Codes6T {

	/**
	 * 
	 * The following is the list of commands that your input/output serves.
	 *
	 * <p>
	 * <b>encode</b> is the command to encode data with the 8B/6T code.
	 * <p>
	 * <b>quit</b> terminates the program. There is no more output in the console.
	 * <p>
	 * <b>decode</b> decodes 3 datastreams to the word. It shows if the datastream
	 * is incorrect and where the failure is.
	 * 
	 *
	 * @param args input of user in the console
	 * @throws Exception 
	 *
	 */

	public static void main(String[] args) throws Exception {

		DataEncoder8B6T encoder = new DataEncoder8B6T();
		DataDecoder8B6T decoder = new DataDecoder8B6T();
		EasyFileTransferProtocol files = new EasyFileTransferProtocol(encoder, decoder, null);

		while (true) {

			String text = Terminal.readLine();
			byte byteArray[];

			if (text.startsWith("encode")) {

				try {
					text = text.substring(CUT_WORD_ENCODE);
					byteArray = text.getBytes();
					String[] data = encoder.encode(byteArray);

					int dataStreamZaehler = 1;
					for (String stream : data) {
						Terminal.printLine("DataStream " + dataStreamZaehler + ": " + stream);
						dataStreamZaehler++;
					}
				} catch (StringIndexOutOfBoundsException siobe) {
					Terminal.printError("unknown Command \n");
				}

			} else if (text.startsWith("decode")) {
				try {

					String[] datastreams = new String[THIRD];
					Terminal.printLine("Please enter first encoded data stream:");
					datastreams[0] = Terminal.readLine();

					Terminal.printLine("Please enter second encoded data stream:");
					datastreams[1] = Terminal.readLine();

					Terminal.printLine("Please enter third encoded data stream:");
					datastreams[2] = Terminal.readLine();
					byte[] decoded = decoder.decode(datastreams);

					if (decoded == null) {
						break;
					}
					String[] output = new String[decoded.length];

					StringBuffer sb = new StringBuffer();
					int i = 0;
					while (i < decoded.length) {

						if (decoded[i] < 0) {
							output[i] = convertToString(decoded[i]);

						} else {
							output[i] = Character.toString(decoded[i]);
						}
						sb.append(output[i]);
						i++;
					}
					String final_output = sb.toString();
					Terminal.printLine(final_output);
				} catch (StartOfStreamException | DecodeException | EndOfPacketException e) {
					e.printStackTrace();
				}

			} else if (text.startsWith("transmitFile")) {

				text = text.substring(CUT_TEXT);
				File myObj = new File(text);

				try {
					files.transmitFile(myObj);
					Terminal.printLine("send file: " + text);
				
				}catch(Exception e) {
					e.printStackTrace();
				}
				

			} else if (text.startsWith("receiveFile")) {

				try {
					File myObj = files.receiveFile();
					String fileName = myObj.getName();
					Terminal.printLine("received file: " + fileName + " with size: " + myObj.length());
					

				} catch ( NullPointerException e) {
					Terminal.printLine("No data on line!!!");

				}

			} else if (text.startsWith("transmit ")) {
				

				String[] input = new String[THIRD];
				Cable<String[]> transmitter = new Cable<String[]>();

				byteArray = text.substring(NINE).getBytes();

				try {
					byteArray = text.substring(NINE).getBytes();

					input = encoder.encode(byteArray);
					transmitter.transmit(input);

				} catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
					Terminal.printError("unknown Command");
				}

			} else if (text.startsWith("receive")) {
				Cable<String[]> receiver = new Cable<String[]>();

				String[] encodedDataStreams = new String[THIRD];
				byte[] bytes;
				try {
					encodedDataStreams = receiver.receive();
					bytes = decoder.decode(encodedDataStreams);

					String decodedString = new String(bytes, StandardCharsets.UTF_8);
					Terminal.printLine(decodedString);

				
				} catch (Exception e) {
					Terminal.printLine("No data on line!!!");
				}
			} else if (text.equalsIgnoreCase("quit")) {
				break;
			} else {
				Terminal.printError("unknown Command");

			}

		}
	}

	/**
	 * This method converts byte to String.
	 * 
	 * @param byteValue the byte value of the code letters.
	 * @return string value which was converted
	 */

	private static String convertToString(byte byteValue) {
		String toString = Integer.toHexString(byteValue);
		toString = toString.substring(CODE_LENGTH);
		int decByte = Integer.parseInt(toString, CODE1_BEGIN);
		toString = Character.toString(decByte);
		return toString;
	}

}