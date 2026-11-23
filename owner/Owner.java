package edu.monmouth.owner;

public class Owner {
    private String name;
    private int yearOfBirth;

    public Owner(String name, int yearOfBirth) throws OwnerException {
        setName(name);
        setYearOfBirth(yearOfBirth);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws OwnerException {
        if (name == null) {
            throw new OwnerException(OwnerConstants.NULLNAME);
        }
        if (name.isBlank()) {
            throw new OwnerException(OwnerConstants.EMPTYNAME);
        }
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) throws OwnerException {
        if (yearOfBirth < OwnerConstants.MINYEAROFBIRTH) {
            throw new OwnerException(OwnerConstants.YEAROFBIRTHERROR + yearOfBirth);
        }
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Owner other = (Owner) obj;
        return yearOfBirth == other.yearOfBirth && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
		result = 31 * result + yearOfBirth;
		return result;
    }

    @Override
    public String toString() {
        return "Owner [name=" + name + ", yearOfBirth=" + yearOfBirth + "]";
    }
}
