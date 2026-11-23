package edu.monmouth.hw3;

public class HW3Constants {

	private HW3Constants() {
	}

	// System.exit() values
	public static final int REQUIREDARGS = 2;
	public static final int BADARGS = 1;
	public static final int BADPROPERTIESFILE = 2;
	public static final int BADREDIRECT = 3;
	public static final int MISSINGPROPERTY = 4;
	public static final int BADDATAFILE = 5;

	// property names in the properties file
	public static final String LOGFILENAME = "LogFileName";
	public static final String KEYVALUEDELIMITER = "KeyValueDelimiter";
	public static final String FIELDDELIMITER = "DelimiterBetweenOverAndVehicle";

	// data file layout
	public static final int KEYVALUEFIELDS = 2;
	public static final int OWNERFIELDS = 2;
	public static final int VEHICLEFIELDS = 3;

	// vehicle type codes
	public static final String CAR = "c";
	public static final String BOAT = "b";
	public static final String PLANE = "p";
}
