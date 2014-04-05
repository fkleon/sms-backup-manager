package de.leonhardt.sbm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;

import de.leonhardt.sbm.smsbr.xml.model.Sms;
import de.leonhardt.sbm.smsbr.xml.model.Smses;

/**
 * A bunch of useful utilities to for testing all the stuff.
 * @author Frederik Leonhardt
 *
 */
public class TestUtils {

	/**
	 * Compares SMS based their dates.
	 * @author Frederik Leonhardt
	 *
	 */
	public class SmsMessageDateComparator implements Comparator<Sms> {
		@Override
		public int compare(Sms o1, Sms o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	}
	
	/**
	 * Returns the given file's contents.
	 * @param inputFile	 * @return
	 * @throws IOException
	 */
	public String readFileContent(File inputFile) throws IOException {
		return readFileContent(new FileInputStream(inputFile));
	}
	
	/**
	 * Returns the given file's contents.
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String readFileContent(String filePath) throws IOException {
		return readFileContent(new File(filePath));
	}
	
	/**
	 * returns the given stream's contents.
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public String readFileContent(InputStream inputStream) throws IOException {
		BufferedReader myInput = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String thisLine;
		while ((thisLine = myInput.readLine()) != null) {  
			sb.append(thisLine);
		}
		myInput.close();
		return sb.toString();
	}
	
	/**
	 * Returns the number of lines in the specified file.
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	public int readLineCount(File file) throws IOException {
		return readLineCount(new FileInputStream(file));
	}
	
	/**
	 * Returns the number of lines in the specified file.
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public int readLineCount(String filePath) throws IOException {
		return readLineCount(new File(filePath));
	}
	
	/**
	 * Returns the number of lines in the specified IS.
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public int readLineCount(InputStream inputStream) throws IOException {
		BufferedReader myInput = new BufferedReader(new InputStreamReader(inputStream));
		int lines = 0;
		while (myInput.readLine() != null) {  
			lines++;
		}
		myInput.close();
		return lines;
	}
	
	/**
	 * Compares two files based on their content.
	 * @param filePath1
	 * @param filePath2
	 * @return
	 * @throws IOException
	 */
	public boolean fileCompare(String filePath1, String filePath2) throws IOException {
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		
		String fileContent1 = readFileContent(file1);
		String fileContent2 = readFileContent(file2);
		
		return fileContent1.equals(fileContent2);
	}
	
	/**
	 * Sorts a given Smses object by the dates of their sms messages.
	 * @param smses
	 */
	public void sortSmses(Smses smses) {
		Collections.sort(smses.getSms(), new SmsMessageDateComparator());
	}
	
	/**
	 * Returns the input string to a given resource.
	 * @param resourcePath
	 * @return null, if no such file exists.
	 */
	public static InputStream getInputStreamFromResource(String resourcePath) {
		return TestUtils.class.getClassLoader().getResourceAsStream(resourcePath);
	}
}
