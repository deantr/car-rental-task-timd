package io.rental;

import java.time.LocalDate;

import io.utils.DatePeriod;
    
public class TestCarCompanyBuilder extends CarRentalCompanyImpl {
    
    public static final Car VW_GOLF_B2_90 = new Car("VW", "Golf", "XX11 1UR", "B2", 90);
    public static final Car VW_PASSAT_C1_110 = new Car("VW", "Passat", "XX12 2UR",  "C1", 110);
    public static final Car VW_POLO_A1_65 = new Car("VW", "Polo", "XX13 3UR",  "A1", 65);
    public static final Car VW_POLO_A1_70 = new Car("VW", "Polo", "XX14 4UR",  "A1", 70);
    public static final Car MINI_COOPER_C1_170 = new Car("Mini", "Cooper", "XX15 5UR",  "C1", 70);

    public static final Renter RENTER_JOE = new Renter("Hydrogen", "Joe", "HYDRO010190JX8NM", LocalDate.of(1990, 1, 1));
    public static final Renter RENTER_SAM = new Renter("Calcium", "Sam", "CALCI010203SX8NM", LocalDate.of(2003, 2, 1));
    public static final Renter RENTER_MAISY = new Renter("Neon", "Maisy", "NEONN010398MX8NM", LocalDate.of(1998, 3, 1));
    public static final Renter RENTER_GRETA = new Renter("Carbon", "Greta", "CARBO010497GX8NM", LocalDate.of(1997, 4, 1));

    public static final DatePeriod LAST_WEEK = new DatePeriod(LocalDate.of(2024, 02, 19), LocalDate.of(2024, 02, 25));
    public static final DatePeriod THIS_WEEK = new DatePeriod(LocalDate.of(2024, 02, 26), LocalDate.of(2024, 03, 03));
    public static final DatePeriod NEXT_WEEK = new DatePeriod(LocalDate.of(2024, 03, 04), LocalDate.of(2024, 03, 10));
    public static final DatePeriod THIS_WEEK_AND_NEXT = new DatePeriod(LocalDate.of(2024, 02, 26), LocalDate.of(2024, 03, 10));
    
    private TestCarRentalCompany carRentalCompany = new TestCarRentalCompany();
    
    public static TestCarCompanyBuilder create() {
        return new TestCarCompanyBuilder();
    }

    /**
     * Adds a bunch of test cars...
     * @return TestCarCompanyBuilder for fluent calling style
     */
    public TestCarCompanyBuilder withCars() {        
        carRentalCompany.addCar(VW_GOLF_B2_90);
        carRentalCompany.addCar(VW_PASSAT_C1_110);
        carRentalCompany.addCar(VW_POLO_A1_65);
        carRentalCompany.addCar(VW_POLO_A1_70);
        carRentalCompany.addCar(MINI_COOPER_C1_170);    
        return this;
    }

    /**
     * Adds a bunch of bookings
     * @return TestCarCompanyBuilder for fluent calling style
     * @throws Exception 
     */
    public TestCarCompanyBuilder withBookings(Booking... bookings) throws Exception {        
        for(Booking b: bookings){
            carRentalCompany.bookCar(b.getCar(), b.getRenter(), b.getPeriod());
        }
        return this;
    }

    public TestCarRentalCompany build() {
        return carRentalCompany;
    }    
}

class TestCarRentalCompany extends CarRentalCompanyImpl {

}
