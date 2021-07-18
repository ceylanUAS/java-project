package eit.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import eit.linecode.Codes6T;
import eit.linecode.DataEncoder8B6T;
import eit.medium.Cable;

/**
 * This class builds the dataframe of the file.
 * 
 * @author Ceylan Yildirim
 *
 */

public class Dataframe implements Codes6T{
	
	
	static DataEncoder8B6T enc = new DataEncoder8B6T();
	Cable<String[]> transmitter = new Cable<String[]>();
	
	/**
	 * This method creates the dataframe for the file.
	 * 
	 * @param file file to transmit.
	 * @throws IOException exception.
	 */
	
	
	public void dataFrame(File file) throws IOException {

		OutputStream os;
		byte[] fileInByte = Files.readAllBytes(file.toPath());

		int seq = (fileInByte.length / MAX_BYTE);

		if (fileInByte.length % MAX_BYTE != 0) {
			seq = seq + 1;
		}
		int count = 0;
		int i = 0;

		for (i = 0; i < seq - 1; i++) {
			byte[] byte2DArray = new byte[MAX_BYTE];
			String[] datastream = new String[THIRD];
			for (int j = 0; j < MAX_BYTE; j++) {
				byte2DArray[j] = fileInByte[count];
				count++;
			}

			datastream = enc.encode(byte2DArray);

			transmitter.transmit(datastream);
			os = new FileOutputStream(+i + file.getName());
			os.write(byte2DArray);
			os.close();
		}
		int lastValues = fileInByte.length - ((seq - 1) * MAX_BYTE);

		byte[] byte2DArray = new byte[lastValues];
		if (count != 0) {
			for (int j = 0; j < lastValues; j++) {

				byte2DArray[j] = fileInByte[count];
				count++;
			}
		}
		String[] datastream = new String[THIRD];
		datastream = enc.encode(byte2DArray);

		transmitter.transmit(datastream);

		os = new FileOutputStream(+i + file.getName());
		os.write(byte2DArray);
		os.close();
	}

}
