package edu.monmouth.hw3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import edu.monmouth.owner.*;
import edu.monmouth.vehicle.*;

public class HW3 {
	public static void main(String[] args) {
		Properties properties = initialization(args);

		String keyValueDelimiter = getRequiredProperty(properties, HW3Constants.KEYVALUEDELIMITER);
		String fieldDelimiter = getRequiredProperty(properties, HW3Constants.FIELDDELIMITER);
		
		// verify equals() and hashCode() are consistent
		System.out.println();
		System.out.println("equals() / hashCode() consistency");
		try {
			verifyEqualsHashCode(new Car(PowerTypes.GASOLINE, 4), new Car(PowerTypes.GASOLINE, 4));
			verifyEqualsHashCode(new Car(PowerTypes.GASOLINE, 4), new Car(PowerTypes.ELECTRICITY, 4));
			verifyEqualsHashCode(new Car(PowerTypes.GASOLINE, 4), new Car(PowerTypes.GASOLINE, 6));

			verifyEqualsHashCode(new Boat(PowerTypes.FUELOIL, 100), new Boat(PowerTypes.FUELOIL, 100));
			verifyEqualsHashCode(new Boat(PowerTypes.FUELOIL, 100), new Boat(PowerTypes.DEISEL, 100));
			verifyEqualsHashCode(new Boat(PowerTypes.FUELOIL, 100), new Boat(PowerTypes.FUELOIL, 101));

			verifyEqualsHashCode(new Plane(PowerTypes.FUELOIL, 2), new Plane(PowerTypes.FUELOIL, 2));
			verifyEqualsHashCode(new Plane(PowerTypes.FUELOIL, 2), new Plane(PowerTypes.ELECTRICITY, 2));
			verifyEqualsHashCode(new Plane(PowerTypes.FUELOIL, 2), new Plane(PowerTypes.FUELOIL, 200));

			verifyEqualsHashCode(new Owner("Alan Turing", 1912), new Owner("Alan Turing", 1912));
			verifyEqualsHashCode(new Owner("Alan Turing", 1912), new Owner("Alan Turing", 1913));
			verifyEqualsHashCode(new Owner("Alan Turing", 1912), new Owner("Tommy Tran", 1912));
		} catch (CarException | BoatException | PlaneException | OwnerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("Reading " + args[1]);
		HashMap<Owner, Vehicle> ownerVehicles = new HashMap<>();

		File dataFile = new File(args[1]);
		int lineNumber = 0;

		try (Scanner scanner = new Scanner(dataFile)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNumber++;

				try {
					String[] keyValue = line.split(keyValueDelimiter);
					if (keyValue.length != HW3Constants.KEYVALUEFIELDS) {
						System.err.println("Line " + lineNumber + " discarded - invalid owner/vehicle string: \"" + line + "\"");
						continue;
					}

					String[] ownerFields = keyValue[0].split(fieldDelimiter);
					String[] vehicleFields = keyValue[1].split(fieldDelimiter);

					if (ownerFields.length != HW3Constants.OWNERFIELDS) {
						System.err.println("Line " + lineNumber + " discarded - invalid owner string: \"" + line + "\"");
						continue;
					}
					if (vehicleFields.length != HW3Constants.VEHICLEFIELDS) {
						System.err.println("Line " + lineNumber + " discarded - invalid vehicle string: \"" + line + "\"");
						continue;
					}

					String name = ownerFields[0];
					int yearOfBirth = Integer.parseInt(ownerFields[1]);
					Owner owner = new Owner(name, yearOfBirth);

					String type = vehicleFields[0];
					PowerTypes powerSource = PowerTypes.valueOf(vehicleFields[1]);
					int amount = Integer.parseInt(vehicleFields[2]);

					Vehicle vehicle = null;
					switch (type) {
						case HW3Constants.CAR:
							vehicle = new Car(powerSource, amount);
							break;

						case HW3Constants.BOAT:
							vehicle = new Boat(powerSource, amount);
							break;

						case HW3Constants.PLANE:
							vehicle = new Plane(powerSource, amount);
							break;

						default:
							System.err.println("Line " + lineNumber + " discarded - invalid type of vehicle: \"" + line + "\"");
							continue;
					}

					Vehicle previous = ownerVehicles.put(owner, vehicle);
					if (previous != null) {
						System.out.println("Line " + lineNumber + " - " + owner + " already mapped, replaced " + previous);
					}

				} catch (NumberFormatException e) {
					System.err.println("Line " + lineNumber + " discarded - not a whole number: \"" + line + "\"");
				} catch (IllegalArgumentException e) {
					System.err.println("Line " + lineNumber + " discarded - " + e.getMessage() + ": \"" + line + "\"");
				} catch (OwnerException | CarException | BoatException | PlaneException e) {
					System.err.println("Line " + lineNumber + " discarded - " + e.getMessage() + ": \"" + line + "\"");
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open data file " + args[1] + " " + e.getMessage());
			System.exit(HW3Constants.BADDATAFILE);
		}

		System.out.println();
		System.out.println("Number of key-value mappings: " + ownerVehicles.size());

		System.out.println();
		printOwnerVehicleMap(ownerVehicles);

		try {
			Owner inMap = new Owner("Alan Turing", 1912);
			System.out.println();
			System.out.println("containsKey(" + inMap + "): " + ownerVehicles.containsKey(inMap));
			System.out.println("remove(" + inMap + "): " + ownerVehicles.remove(inMap));
			System.out.println("Number of key-value mappings: " + ownerVehicles.size());
			printOwnerVehicleMap(ownerVehicles);

			Owner notInMap = new Owner("Linus Torvalds", 1969);
			System.out.println();
			System.out.println("containsKey(" + notInMap + "): " + ownerVehicles.containsKey(notInMap));
			System.out.println("remove(" + notInMap + "): " + ownerVehicles.remove(notInMap));
			System.out.println("Number of key-value mappings: " + ownerVehicles.size());
		} catch (OwnerException oe) {
			System.out.println(oe.getMessage());
			oe.printStackTrace();
		}
	}

	private static Properties initialization(String[] args) {
		// check for 2 command line arguments
		if (args.length != HW3Constants.REQUIREDARGS) {
			System.err.println("Usage: java edu.monmouth.hw3.HW3 <properties file> <data file>");
			System.exit(HW3Constants.BADARGS);
		}

		Properties properties = new Properties();
		try (FileInputStream propertiesStream = new FileInputStream(args[0])) {
			properties.load(propertiesStream);
		} catch (FileNotFoundException fnfe) {
			System.err.println("Cannot find properties file " + args[0] + " " + fnfe.getMessage());
			System.exit(HW3Constants.BADPROPERTIESFILE);
		} catch (IOException ioe) {
			System.err.println("Cannot read properties file " + args[0] + " " + ioe.getMessage());
			ioe.printStackTrace();
			System.exit(HW3Constants.BADPROPERTIESFILE);
		}
		properties.list(System.out);

		// redirect STDOUT & STDERR to the log file named in the properties file
		String logFileName = getRequiredProperty(properties, HW3Constants.LOGFILENAME);
		try {
			PrintStream redirectStream = new PrintStream(logFileName);
			System.setOut(redirectStream);
			System.setErr(redirectStream);
		} catch (FileNotFoundException fnfe) {
			System.err.println("Cannot redirect stderr and stdout " + fnfe.getMessage());
			fnfe.printStackTrace();
			System.exit(HW3Constants.BADREDIRECT);
		}

		// list again so the property list is also recorded in the log file
		properties.list(System.out);
		return properties;
	}

	private static String getRequiredProperty(Properties properties, String key) {
		String value = properties.getProperty(key);
		if (value == null || value.isEmpty()) {
			System.err.println("Property \"" + key + "\" is missing or empty");
			System.exit(HW3Constants.MISSINGPROPERTY);
		}
		return value;
	}

	private static void verifyEqualsHashCode(Object first, Object second) {
		boolean equal = first.equals(second);
		boolean sameHash = first.hashCode() == second.hashCode();

		System.out.println("Equals HashCode: " + (equal == sameHash));
	}

	private static void printOwnerVehicleMap(Map<Owner, Vehicle> map) {
		Iterator<Map.Entry<Owner, Vehicle>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Owner, Vehicle> entry = entries.next();
			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
		}
	}
}
