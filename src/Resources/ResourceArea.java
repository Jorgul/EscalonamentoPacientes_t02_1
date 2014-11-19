package Resources;

public class ResourceArea {
	private String expertise;
	private int numMinutesExam;
	//private double healthStateVariance;
	
	ResourceArea(String expertise, int numMinutesExam)
	{
		this.expertise = expertise;
		this.numMinutesExam = numMinutesExam;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	public int getNumMinutesExam() {
		return numMinutesExam;
	}

	public void setNumMinutesExam(int numMinutesExam) {
		this.numMinutesExam = numMinutesExam;
	}
	
	
}
