package Patients;

public class PatientBid {
	private int patientID;
	private double bid;
	
	public PatientBid(int patientID, double bid)
	{
		this.patientID = patientID;
		this.bid = bid;
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
	
	

}
