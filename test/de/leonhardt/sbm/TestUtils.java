package de.leonhardt.sbm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;


import de.leonhardt.sbm.model.Message;
import de.leonhardt.sbm.util.Utils;
import de.leonhardt.sbm.util.comparator.MessageDateComparator;
import de.leonhardt.sbm.xml.model.Sms;
import de.leonhardt.sbm.xml.model.Smses;

public class TestUtils {

	public class SmsMessageDateComparator implements Comparator<Sms> {
		@Override
		public int compare(Sms o1, Sms o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	}
	
	public String readFileContent(String filePath) throws IOException {
		return readFileContent(new File(filePath));
	}
	
	public String readFileContent(File inputFile) throws IOException {
		FileInputStream fin =  new FileInputStream(inputFile);
		BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
		StringBuilder sb = new StringBuilder();
		String thisLine;
		while ((thisLine = myInput.readLine()) != null) {  
			sb.append(thisLine);
		}
		myInput.close();
		return sb.toString();
	}
	
	public int readLineCount(String filePath) throws IOException {
		return readLineCount(new File(filePath));
	}
	
	public int readLineCount(File inputFile) throws IOException {
		FileInputStream fin =  new FileInputStream(inputFile);
		BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
		int lines = 0;
		while (myInput.readLine() != null) {  
			lines++;
		}
		myInput.close();
		return lines;
	}
	
	public boolean fileCompare(String filePath1, String filePath2) throws IOException {
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		
		String fileContent1 = readFileContent(file1);
		String fileContent2 = readFileContent(file2);
		
		return fileContent1.equals(fileContent2);
	}
	
	public void sortSmses(Smses smses) {
		Collections.sort(smses.getSms(), new SmsMessageDateComparator());
	}
	
}
