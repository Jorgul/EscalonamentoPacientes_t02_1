package Resources;

import java.util.ArrayList;

import Patients.PatientBid;

public class Resource {

	private ArrayList<ResourceArea> capabilities = new ArrayList<ResourceArea>();
	private ArrayList<Integer> registedPatientIDs = new ArrayList<Integer>();
	private ArrayList<PatientBid> patientBids = new ArrayList<PatientBid>();
	//private ArrayList<Timeblock> timeblocks = new ArrayList<Timeblock>();
	/* classe Timeblock
	 private int patientID
	 private float examMinutes
	 private string expertise
	 private float startTime, endTime;
	 * */

	public int pickWinner(ArrayList<PatientBid> patientBids) // returns the ID of the patient with the biggest bid
	{
		return 0;
	}
	

	public ArrayList<ResourceArea> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(ArrayList<ResourceArea> capabilities) {
		this.capabilities = capabilities;
	}

}
