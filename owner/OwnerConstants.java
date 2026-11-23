package edu.monmouth.owner;

public class OwnerConstants {

	private OwnerConstants() {
	}

	public static final int MINYEAROFBIRTH = 1000;
	public static final int HASHPRIME = 31;

	public static final String NULLNAME = "Name must not be null";
	public static final String EMPTYNAME = "Name must contain characters";
	public static final String YEAROFBIRTHERROR = "Year of birth must not be less than " + MINYEAROFBIRTH + ": ";
}
