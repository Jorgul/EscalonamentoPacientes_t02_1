package Patients;

import java.util.ArrayList;

public class Patient {
	
	private String pathology;
	private int patientID;
	private float bid;
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

	public float getBid() {
		return bid;
	}

	public void setBid(float bid) {
		this.bid = bid;
	}
	
}
