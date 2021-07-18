package eit.application;

import java.io.File;


import eit.linecode.Codes6T;
import eit.linecode.DataEncoder8B6T;
import eit.medium.Cable;

/**
 * This class builds the controlframe of the file with the information:<p>
 * file name, file size (bytes), file name length.
 * 
 * @author ceylo
 *
 */

public class Controlframe implements Codes6T{
	

	static DataEncoder8B6T enc = new DataEncoder8B6T();
	Cable<String[]> transmitter = new Cable<String[]>();
	
	
	/**
	 * This method creates the controlframe for the file.
	 * 
	 * @param file controlframe of the file to be transmit and receive
	 */

	public void controlFrame(File file) {
		String name = file.getName();
		String[] cfInfo = new String[THIRD];

		cfInfo[0] = Integer.toString(name.length());
		cfInfo[1] = Long.toString(file.length());
		cfInfo[2] = file.getName();

		byte[] fineNameLen = new byte[0];
		byte[] fileLength = new byte[THIRD];
		byte[] fileName = new byte[MAX_FNL];

		fineNameLen = cfInfo[0].getBytes();
		fileLength = cfInfo[FIRST].getBytes();
		fileName = cfInfo[SECOND].getBytes();


		transmitter.transmit(enc.encode(fineNameLen));
		transmitter.transmit(enc.encode(fileLength));
		transmitter.transmit(enc.encode(fileName));

	}


}
