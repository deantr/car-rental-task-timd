package io.rental;

/**
 * Customer view of Car, showing a 'blended' price for the Rental Group
 */
public class CarView {
    
    public static CarView fromCar(Car car, double rentalGroupPrice){
        return new CarView(
            car.getMake(), 
            car.getModel(),
            car.getRentalGroup(),
            rentalGroupPrice
        );
    }

    private final String make;
    private final String model;
    private final String rentalGroup;
    private final double rentalGroupPrice;
    
    public CarView(String make, String model, String rentalGroup, double rentalGroupPrice) {
        this.make = make;
        this.model = model;
        this.rentalGroup = rentalGroup;
        this.rentalGroupPrice = rentalGroupPrice;
    }

    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public String getRentalGroup() {
        return rentalGroup;
    }
    public double getRentalGroupPrice() {
        return rentalGroupPrice;
    }

    @Override
    public String toString() {
        return "CarView [" + make + " " + model + " rentalGroup=" + rentalGroup + ", rentalGroupPrice="+ rentalGroupPrice + "]";
    }
   
}
