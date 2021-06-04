package java_assignment_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Test {

	public static void main(String[] args) throws IOException {
		
		String row = "";
		ArrayList<Data> data = new ArrayList<Data>();
		
		// Use BufferedReader to read CSV
		boolean isHeaderLine = true;
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data.csv"));
		try {
			while ((row = reader.readLine()) != null) {
				
				// Check if line is headerline (first)
				if(!isHeaderLine) {
					
					// Split line and set attributes
					String[] line = row.split(";");
					Data dataEntry = new Data();
					dataEntry.setDate(line[0]);
				    dataEntry.setEURCHF(Float.parseFloat(line[1]));
				    dataEntry.setGBPCHF(Float.parseFloat(line[2]));
				    dataEntry.setUSDCHF(Float.parseFloat(line[3]));
				    dataEntry.setJPYCHF(Float.parseFloat(line[4]));
				    dataEntry.setAUDCHF(Float.parseFloat(line[5]));
				    dataEntry.setXDRCHF(Float.parseFloat(line[6]));
				    
				    // Add created object to ArrayList
				    data.add(dataEntry);
				} else {
					isHeaderLine = false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		
		// Test data
		for (Data dataEntry : data) {
			System.out.println("Forex entry:");
		    System.out.println("Date: " + dataEntry.getDate());
		    System.out.println("EUR / CHF: " + dataEntry.getEURCHF());
		    System.out.println("GBP / CHF: " + dataEntry.getGBPCHF());
		    System.out.println("USD / CHF: " + dataEntry.getUSDCHF());
		    System.out.println("JPY / CHF: " + dataEntry.getJPYCHF());
		    System.out.println("AUD / CHF: " + dataEntry.getAUDCHF());
		    System.out.println("XDR / CHF: " + dataEntry.getXDRCHF());
		    System.out.println("");
		}
	    
	}

}
