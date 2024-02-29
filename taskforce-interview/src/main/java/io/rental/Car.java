package io.rental;

import javax.annotation.concurrent.Immutable;

/**
 * <p>Represents a unique Car at the Rental Shop</p>
 * 
 * <p>Notes / Assumptions: </p>
 * <ul>
 * <li>Assumption that registration number is unique - so will be used for object-equality and hash</li>
 * </ul>
 */
@Immutable
public class Car {

    private final String make;
    private final String model;
    private final String registrationNumber;
    private final String rentalGroup;
    private final double costPerDay;

    public Car(String make, String model, String registrationNumber, String rentalGroup, double costPerDay) {
        this.make = make;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.rentalGroup = rentalGroup;
        this.costPerDay = costPerDay;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getRentalGroup() {
        return rentalGroup;
    }

    public double getCostPerDay() {
        return costPerDay;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
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
        Car other = (Car) obj;
        if (registrationNumber == null) {
            if (other.registrationNumber != null)
                return false;
        } else if (!registrationNumber.equals(other.registrationNumber))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Car [" + make + " " + model + ", registration=" + registrationNumber + ", rental=(" + rentalGroup + ") GBP" + costPerDay + "pd]";
    }

    

}
