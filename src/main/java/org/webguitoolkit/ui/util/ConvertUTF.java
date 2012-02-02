package org.webguitoolkit.ui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Used to convert unicode characters to html encoded entities.
 * 
 * This class can be used for the calendar language files.
 * 
 * @author i102415
 */
public class ConvertUTF {

	public static final String ROOT_DIR = "D:\\deploy\\calendar\\";

	public static void main(String[] args) {
		System.out.println(">>>start");
		File dir = new File(ROOT_DIR);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				convertFile(files[i]);
			}
		}
		System.out.println("<<<end");
	}

	private static void convertFile(File file) {
		System.out.println(file.getAbsolutePath());

		if (file.getAbsolutePath().endsWith(".converted"))
			return;

		BufferedReader in = null;
		FileWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			File convertedFolder = new File(ROOT_DIR + "converted\\");
			if (!convertedFolder.exists())
				convertedFolder.mkdir();

			String outFile = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1);
			System.out.println(outFile);
			out = new FileWriter(new File(convertedFolder, outFile));
			String line = null;
			while ((line = in.readLine()) != null) {
				out.write(convert(line, false) + "\r\n");
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.flush();
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Converts unicodes to encoded &#92;uxxxx
	 * and writes out any of the characters in specialSaveChars
	 * with a preceding slash
	 */
	public static String convert(String theString, boolean escapeSpace) {
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len * 2);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			if ((aChar < 0x0020) || (aChar > 0x007e)) {
				outBuffer.append('\\');
				outBuffer.append('u');
				outBuffer.append(toHex((aChar >> 12) & 0xF));
				outBuffer.append(toHex((aChar >> 8) & 0xF));
				outBuffer.append(toHex((aChar >> 4) & 0xF));
				outBuffer.append(toHex(aChar & 0xF));
			}
			else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Convert a nibble to a hex character
	 * 
	 * @param nibble the nibble to convert.
	 */
	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String specialSaveChars = "=: \t\r\n\f#!";

}
