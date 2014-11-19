package Patients;

import java.util.ArrayList;

public class Patient {
	
	private String pathology;
	private int patientID = 0;
	private double bid;
	//private double decreaseRate;
	//private double healthState;
	//private ArrayList<Timeblock> timeblocks = new ArrayList<Timeblock>();
	
	public Patient()
	{
		
	}
	
	public Patient(String pathology){
		this.pathology = pathology;
	}

	public String getPathology() {
		return pathology;
	}

	public void setPathology(String pathology) {
		this.pathology = pathology;
	}

	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double calculateBid() {
		
		double bid = 0.6;
		//TODO aplicar formula
		return bid;
	}
	
}
