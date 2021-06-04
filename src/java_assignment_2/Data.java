package java_assignment_2;

import java.util.Date;

public class Data {
	
	// Variables
	private String date;
	private float eurchf;
	private float gbpchf;
	private float usdchf;
	private float jpychf;
	private float audchf;
	private float xdrchf;
	
	protected Data() {
		date = "";
		eurchf = 0;
		gbpchf = 0;
		usdchf = 0;
		jpychf = 0;
		audchf = 0;
		xdrchf = 0;
	}
	
	// Date
	public String getDate() {
	    return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	// EUR / CHF
	public float getEURCHF() {
	    return eurchf;
	}
	
	public void setEURCHF(float eurchf) {
		this.eurchf = eurchf;
	}
	
	// GBP / CHF
	public float getGBPCHF() {
	    return gbpchf;
	}
	
	public void setGBPCHF(float gbpchf) {
		this.gbpchf = gbpchf;
	}
	
	// USD / CHF
	public float getUSDCHF() {
	    return usdchf;
	}
	
	public void setUSDCHF(float usdchf) {
		this.usdchf = usdchf;
	}
	
	// JPY / CHF
	public float getJPYCHF() {
	    return jpychf;
	}
	
	public void setJPYCHF(float jpychf) {
		this.jpychf = jpychf;
	}
	
	// AUD / CHF
	public float getAUDCHF() {
	    return audchf;
	}
	
	public void setAUDCHF(float audchf) {
		this.audchf = audchf;
	}
	
	// XDR / CHF
	public float getXDRCHF() {
	    return xdrchf;
	}
	
	public void setXDRCHF(float xdrchf) {
		this.xdrchf = xdrchf;
	}
}
