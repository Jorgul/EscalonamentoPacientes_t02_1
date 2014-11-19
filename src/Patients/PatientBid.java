package Patients;

public class PatientBid {
	private int patientID;
	private float bid;
	
	public PatientBid(int patientID, float bid)
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

	public float getBid() {
		return bid;
	}

	public void setBid(float bid) {
		this.bid = bid;
	}
	
	

}
