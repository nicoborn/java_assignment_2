package java_assignment_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Test {
	
	// Java Assignment 2
	// Authors: Nico Born, Marco Marchese

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
					dataEntry.setDate(line[0]); 						// Date
				    dataEntry.setEURCHF(Float.parseFloat(line[1]));		// EURCHF
				    dataEntry.setGBPCHF(Float.parseFloat(line[2]));		// GBPCHF
				    dataEntry.setUSDCHF(Float.parseFloat(line[3]));		// USDCHF
				    dataEntry.setJPYCHF(Float.parseFloat(line[4]));		// JPYCHF
				    dataEntry.setAUDCHF(Float.parseFloat(line[5]));		// AUDCHF
				    dataEntry.setXDRCHF(Float.parseFloat(line[6]));		// XDRCHF
				    
				    // Add created object to ArrayList
				    data.add(dataEntry);
				} else {
					isHeaderLine = false;
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
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
		System.out.println("");
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
			// Read data from the database
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
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		
	}

}
