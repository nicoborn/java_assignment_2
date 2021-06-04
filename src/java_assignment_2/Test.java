package java_assignment_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Test {
	
	// Java Assignment 2
	// Authors: Nico Born, Marco Marchese

	public static void main(String[] args) throws IOException {
		
		String row = "";
		ArrayList<Data> data = new ArrayList<Data>();
		
		// Use BufferedReader to read CSV
		boolean isHeaderLine = true;
		int testNumberOfLines = 0;
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data.csv"));
		try {
			while ((row = reader.readLine()) != null) {
				
				// Check if line is headerline (first)
				if(!isHeaderLine) {
					
					// Split line and set attributes
					String[] line = row.split(";");
					Data dataEntry = new Data();
					dataEntry.setDate(line[0]); 						// Date
				    dataEntry.setEURCHF(Float.parseFloat(line[1]));		// EURCHF
				    dataEntry.setGBPCHF(Float.parseFloat(line[2]));		// GBPCHF
				    dataEntry.setUSDCHF(Float.parseFloat(line[3]));		// USDCHF
				    dataEntry.setJPYCHF(Float.parseFloat(line[4]));		// JPYCHF
				    dataEntry.setAUDCHF(Float.parseFloat(line[5]));		// AUDCHF
				    dataEntry.setXDRCHF(Float.parseFloat(line[6]));		// XDRCHF
				    
				    // Add created object to ArrayList
				    data.add(dataEntry);
				    
				    // Number of lines read
				    testNumberOfLines++;
				} else {
					isHeaderLine = false;
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		reader.close();
		
		// Test data and compare number of objects and number of entries in the csv
		System.out.println("");
		System.out.println("Testing number of entries and objects:");
		System.out.println("--------------------------------------");
		if(data.size() == testNumberOfLines) {
			System.out.println("OK! Correct number of entries created");
		} else {
			System.out.println("NOK! Something went wrong. Number of objects are not the same as the number of entries in the csv.");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		// Create SQLite DB and add entries, then show report
		Connection connection = null;
		try {
			// Create connection
			connection = DriverManager.getConnection("jdbc:sqlite:data.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			statement.executeUpdate("drop table if exists forexdata");
			statement.executeUpdate("create table forexdata (id integer, dataDate string, eurchf float, gbpchf float, usdchf float, jpychf float, audchf float, xdrchf float)");
			
			// Add data from ArrayList to database
			int i = 0; // ID
			for (Data dataEntry : data) {
				statement.executeUpdate("insert into forexdata values(" + i + ", '" + dataEntry.getDate() + "', " + dataEntry.getEURCHF() + ", " + dataEntry.getGBPCHF() + ", " + dataEntry.getUSDCHF() + ", " + dataEntry.getJPYCHF() + ", " + dataEntry.getAUDCHF() + ", " + dataEntry.getXDRCHF() +")");                              
				i++;
			}
			
			// Print report header
			System.out.println("HISTORICAL DATA: 1.- SWISS FRANC COMPARED TO OTHER CURRENCIES");
			System.out.println("DATA SOURCE: Bundesamt für Statisik / Schweizer Nationalbank");
			System.out.println("URL: https://data.snb.ch/");
			System.out.println("");
			System.out.println("-------------------------------------------------------------");
			System.out.println("COLUMNS COUNT: 7");
			System.out.println("COLUMNS: DATE, EURCHF, GBPCHF, USDCHF, JPYCHF, AUDCHF, XDRCHF");
			System.out.println("ENTRIES COUNT: " + data.size());
			System.out.println("");
			System.out.println("-------------------------------------------------------------");
			
			// Read data from the database and create report
			float avgEURCHF = 0, avgGBPCHF = 0, avgUSDCHF = 0, avgJPYCHF = 0, avgAUDCHF = 0, avgXDRCHF = 0;
			ResultSet rs = statement.executeQuery("select min(dataDate), max(dataDate), min(eurchf), max(eurchf), avg(eurchf), min(gbpchf), max(gbpchf), avg(gbpchf), min(usdchf), max(usdchf), avg(usdchf), min(jpychf), max(jpychf), avg(jpychf), min(audchf), max(audchf), avg(audchf), min(xdrchf),max(xdrchf), avg(xdrchf) from forexdata");
			while(rs.next()) {
				// Read results in DB
				System.out.println("FOREX Pair Breakdown:");
				System.out.println("Time period: From " + rs.getString("min(dataDate)") + " to " + rs.getString("max(dataDate)"));
				System.out.println("");
				System.out.println("EUR / CHF: " + "Average (" + rs.getFloat("avg(eurchf)") + ")" + " | Min (" + rs.getFloat("min(eurchf)") + ")" + " | Max (" + rs.getFloat("max(eurchf)") + ")");
				System.out.println("GBP / CHF: " + "Average (" + rs.getFloat("avg(gbpchf)") + ")" + " | Min (" + rs.getFloat("min(gbpchf)") + ")" + " | Max (" + rs.getFloat("max(gbpchf)") + ")");
				System.out.println("USD / CHF: " + "Average (" + rs.getFloat("avg(usdchf)") + ")" + " | Min (" + rs.getFloat("min(usdchf)") + ")" + " | Max (" + rs.getFloat("max(usdchf)") + ")");
				System.out.println("JPY / CHF: " + "Average (" + rs.getFloat("avg(jpychf)") + ")" + " | Min (" + rs.getFloat("min(jpychf)") + ")" + " | Max (" + rs.getFloat("max(jpychf)") + ")");
				System.out.println("AUD / CHF: " + "Average (" + rs.getFloat("avg(audchf)") + ")" + " | Min (" + rs.getFloat("min(audchf)") + ")" + " | Max (" + rs.getFloat("max(audchf)") + ")");
				System.out.println("XDR / CHF: " + "Average (" + rs.getFloat("avg(xdrchf)") + ")" + " | Min (" + rs.getFloat("min(xdrchf)") + ")" + " | Max (" + rs.getFloat("max(xdrchf)") + ")");
				avgEURCHF = rs.getFloat("avg(eurchf)");
				avgGBPCHF = rs.getFloat("avg(gbpchf)");
				avgUSDCHF = rs.getFloat("avg(usdchf)");
				avgJPYCHF = rs.getFloat("avg(jpychf)");
				avgAUDCHF = rs.getFloat("avg(audchf)");
				avgXDRCHF = rs.getFloat("avg(xdrchf)");
			}
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			
			// Testing DB entries
			ResultSet rsTesting = statement.executeQuery("select count(*) from forexdata");
			int testNumberOfDatabaseEntries = 0;
			if(rs.next()) {
				testNumberOfDatabaseEntries = rsTesting.getInt("count(*)");
			}
			System.out.println("");
			System.out.println("Testing number of entries and database entries:");
			System.out.println("--------------------------------------");
			if(data.size() == testNumberOfDatabaseEntries) {
				System.out.println("OK! Correct number of entries in the database");
			} else {
				System.out.println("NOK! Something went wrong. Number of database entries are not the same as the number of entries in the file.");
			}
			System.out.println("");
			System.out.println("Testing the data:");
			System.out.println("--------------------------------------");
			float sumEURCHF = 0, sumGBPCHF = 0, sumUSDCHF = 0, sumJPYCHF = 0, sumAUDCHF = 0, sumXDRCHF = 0;
			for (Data dataEntry : data) {
				sumEURCHF += dataEntry.getEURCHF();
				sumGBPCHF += dataEntry.getGBPCHF();
				sumUSDCHF += dataEntry.getUSDCHF();
				sumJPYCHF += dataEntry.getJPYCHF();
				sumAUDCHF += dataEntry.getAUDCHF();
				sumXDRCHF += dataEntry.getXDRCHF();
			}
			
			// Compare SQL and Java calculated values
			float avgCalculatedEURCHF = sumEURCHF / testNumberOfLines;
			float avgCalculatedGBPCHF = sumGBPCHF / testNumberOfLines;
			float avgCalculatedUSDCHF = sumUSDCHF / testNumberOfLines;
			float avgCalculatedJPYCHF = sumJPYCHF / testNumberOfLines;
			float avgCalculatedAUDCHF = sumAUDCHF / testNumberOfLines;
			float avgCalculatedXDRCHF = sumXDRCHF / testNumberOfLines;
			
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			
			if(df.format(avgCalculatedEURCHF).equals(df.format(avgEURCHF))) {
				System.out.println("EURCHF correct!" + " DB entry: " + df.format(avgEURCHF) + " | Java calculation: " + df.format(avgCalculatedEURCHF));
			} else {
				System.out.println("EURCHF incorrect!" + " DB entry: " + df.format(avgEURCHF) + " | Java calculation: " + df.format(avgCalculatedEURCHF));
			}
			
			if(df.format(avgCalculatedGBPCHF).equals(df.format(avgGBPCHF))) {
				System.out.println("GBPCHF correct!" + " DB entry: " + df.format(avgGBPCHF) + " | Java calculation: " + df.format(avgCalculatedGBPCHF));
			} else {
				System.out.println("GBPCHF incorrect!" + " DB entry: " + df.format(avgGBPCHF) + " | Java calculation: " + df.format(avgCalculatedGBPCHF));
			}
			
			if(df.format(avgCalculatedUSDCHF).equals(df.format(avgUSDCHF))) {
				System.out.println("USDCHF correct!" + " DB entry: " + df.format(avgUSDCHF) + " | Java calculation: " + df.format(avgCalculatedUSDCHF));
			} else {
				System.out.println("USDCHF incorrect!" + " DB entry: " + df.format(avgUSDCHF) + " | Java calculation: " + df.format(avgCalculatedUSDCHF));
			}
			
			if(df.format(avgCalculatedJPYCHF).equals(df.format(avgJPYCHF))) {
				System.out.println("JPYCHF correct!" + " DB entry: " + df.format(avgJPYCHF) + " | Java calculation: " + df.format(avgCalculatedJPYCHF));
			} else {
				System.out.println("JPYCHF incorrect!" + " DB entry: " + df.format(avgJPYCHF) + " | Java calculation: " + df.format(avgCalculatedJPYCHF));
			}
			
			if(df.format(avgCalculatedAUDCHF).equals(df.format(avgAUDCHF))) {
				System.out.println("AUDCHF correct!" + " DB entry: " + df.format(avgAUDCHF) + " | Java calculation: " + df.format(avgCalculatedAUDCHF));
			} else {
				System.out.println("AUDCHF incorrect!" + " DB entry: " + df.format(avgAUDCHF) + " | Java calculation: " + df.format(avgCalculatedAUDCHF));
			}
			
			if(df.format(avgCalculatedXDRCHF).equals(df.format(avgXDRCHF))) {
				System.out.println("XDRCHF correct!" + " DB entry: " + df.format(avgXDRCHF) + " | Java calculation: " + df.format(avgCalculatedXDRCHF));
			} else {
				System.out.println("XDRCHF incorrect!" + " DB entry: " + df.format(avgXDRCHF) + " | Java calculation: " + df.format(avgCalculatedXDRCHF));
			}
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		
	}

}
