package eit.application;

import java.io.File;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import eit.application.exception.FileLengthException;
import eit.application.exception.FileNameException;
import eit.linecode.Codes6T;
import eit.linecode.DataDecoder8B6T;
import eit.linecode.DataEncoder8B6T;
import eit.medium.Cable;

/**
 * This class can get any file with the command <b>"transmitFile"</b>. The file
 * will be splitted if its bigger than 2048 bits.
 * <p>
 * The class creates exactly one control frame and at least one data frame based
 * on the file to be transferred.
 * <p>
 * The file will be 8B6T encoded and inserted into the queue for transmission.
 * <p>
 * <b>"receiveFile"</b> is the command to retrieve any existing data from the
 * queue,
 * <p>
 * decode it and return it as a file again.
 * 
 * @author ceylo
 *
 */

public class EasyFileTransferProtocol implements Codes6T {


	
	DataEncoder8B6T enc = new DataEncoder8B6T();
	DataDecoder8B6T dec = new DataDecoder8B6T();
	Dataframe df = new Dataframe();
	Controlframe cf = new Controlframe();
	Cable<String[]> transmitter = new Cable<String[]>();
	

	/**
	 * Constructor of the class.
	 * 
	 * @param enc   DataEncoder8B6T class.
	 * @param dec   DataDecoder8B6T class.
	 * @param cable Cable class.
	 */

	public EasyFileTransferProtocol(DataEncoder8B6T enc, DataDecoder8B6T dec, Cable<String[]> cable) {

	}

	/**
	 * This function encodes the file and send it to the cable.
	 * 
	 * @param file the file which should be transmit.
	 * @throws Exception exception
	 */

	public void transmitFile(File file) throws Exception {
		int maxBytes = (int) (Math.pow(2, MAX_FILE_BYTE) - 1);
		String fileName = file.getName();
		int fileNameLen = fileName.length();
		
		
		if (fileNameLen <= FOURTH) {
			throw new FileNameException("File name is too short!!!");
		}
		if (fileNameLen >= MAX_BYTE) {
			throw new FileNameException("File name is too long!!!");
		}

		if (file.length() > maxBytes) {
			throw new FileLengthException("File is too big!!!");
		}

		cf.controlFrame(file);
		try {
			df.dataFrame(file);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * This method receives the file from the cable, decodes it and joins it
	 * together.
	 * 
	 * @return file the decoded and joined file will be returned.
	 */

	public File receiveFile() {

		Cable<String[]> receiver = new Cable<String[]>();

		String[] decodedString = new String[THIRD];
		byte[] decodedBytes , bb = new byte[0];

		String byteToString = new String();
		decodedString = receiver.receive();
		decodedBytes = dec.decode(decodedString);

		byteToString = new String(decodedBytes, StandardCharsets.UTF_8);
		int fileNameLen = Integer.parseInt(byteToString);

		decodedString = receiver.receive();
		decodedBytes = dec.decode(decodedString);
		byteToString = new String(decodedBytes, StandardCharsets.UTF_8);
		int byteLength = Integer.parseInt(byteToString);

		decodedString = receiver.receive();
		decodedBytes = dec.decode(decodedString);
		byteToString = new String(decodedBytes, StandardCharsets.UTF_8);

		File file = new File("rcvd-" + byteToString);

		int seq = (byteLength / MAX_BYTE);
		if (byteLength % MAX_BYTE != 0) {
			seq = seq + 1;
		}
		int counter = 0;
		while(counter < seq) {
			decodedString = receiver.receive();
			decodedBytes = dec.decode(decodedString);
			bb = ByteBuffer.allocate(bb.length + decodedBytes.length).put(bb).put(decodedBytes).array();
			counter = counter +1;
		}

		try {
			OutputStream os = new FileOutputStream(file);
			os.write(bb);
			os.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return file;
	}
	
	
	
	

}
