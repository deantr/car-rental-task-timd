package io.rental;

import java.time.LocalDate;

import javax.annotation.concurrent.Immutable;

/**
 * <p>Identity of the renter</p>
 * <p>Assumptions/Notes:</p>
 * <ul>
 * <li>Hashcode/Equality assumes the driving-license number is unique to customer (would need something stronger to manage long-term relationships!)</li>
 * </ul>
  */
@Immutable
public class Renter {

    private final String lastName;
    private final String firstName;
    private final String drivingLicenseNumber;
    private final LocalDate dateOfBirth;

    public Renter(String lastName, String firstName, String drivingLicenseNumber, LocalDate dateOfBirth) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((drivingLicenseNumber == null) ? 0 : drivingLicenseNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Renter other = (Renter) obj;
        if (drivingLicenseNumber == null) {
            if (other.drivingLicenseNumber != null)
                return false;
        } else if (!drivingLicenseNumber.equals(other.drivingLicenseNumber))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Renter [" +firstName + " " + lastName + "]";
    }

    
}
