package io.rental;

import org.junit.jupiter.api.Test;

import static io.rental.Criteria.ALL;
import static io.utils.DatePeriod.ALL_TIME;
import static io.rental.TestCarCompanyBuilder.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarRentalTest {

    @Test
    public void testListCarsAvailableToRentGivesMoreThanOneCar() {
        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()
            .build();
        Criteria criteria = new MakeCriteria("VW");
        List<Car> carsAvailable = api.getMatchingCars(criteria);
        assertThat(carsAvailable.size()).isGreaterThan(1);
    }

    @Test
    public void s1_findingAnyCar(){
        
        CarRentalCompany api = TestCarCompanyBuilder.create().withCars().build();
        
        // Customer wants a cheap VW
        Criteria criteria = AndCriteria.of(MakeCriteria.of("VW"), RentalGroupCriteria.of("A1"));
        List<Car> matchingCars = api.getMatchingCars(criteria);

        // We have 2
        assertThat(matchingCars.size()).isEqualTo(2);
        assertThat(matchingCars.contains(VW_POLO_A1_65));
        assertThat(matchingCars.contains(VW_POLO_A1_70));
    }

    @Test
    public void s2_findingAnyAvailableCar() throws Exception{

        Booking passatOutAlways = new Booking(VW_PASSAT_C1_110, RENTER_GRETA, ALL_TIME);
        Booking onePoloOutThisWeek = new Booking(VW_POLO_A1_65, RENTER_JOE, THIS_WEEK);

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()
            .withBookings(passatOutAlways, onePoloOutThisWeek)
            .build();
        
        // Customer wants a VW
        Criteria criteria = MakeCriteria.of("VW");
        List<Car> matchingCars = api.getAvailableCars(criteria, THIS_WEEK);

        // We have 2
        assertThat(matchingCars.size()).isEqualTo(2);
        assertThat(matchingCars.contains(VW_GOLF_B2_90));
        assertThat(matchingCars.contains(VW_POLO_A1_70));

        // Customer changes mind about car make
        matchingCars = api.getAvailableCars(ALL, THIS_WEEK);

        // We have 2
        assertThat(matchingCars.size()).isEqualTo(3);
        assertThat(matchingCars.contains(VW_GOLF_B2_90));
        assertThat(matchingCars.contains(VW_POLO_A1_70));
        assertThat(matchingCars.contains(MINI_COOPER_C1_170));
    }

    @Test
    public void s2_whenNoBookingsAllCarsAvailable(){
        
        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()
            .build();

        int numberOwnedCars = api.getMatchingCars(Criteria.ALL).size();
        List<Car> availableCars = api.getAvailableCars(Criteria.ALL, ALL_TIME);
        assertThat(availableCars.size()).isEqualTo(numberOwnedCars);
    }

    @Test
    public void s2_oneBookingInsidePeriod() throws Exception{

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()
            .withBookings(new Booking(MINI_COOPER_C1_170, RENTER_JOE, THIS_WEEK))
            .build();
        
        int numberOwnedCars = api.getMatchingCars(Criteria.ALL).size();
        List<Car> availableCars = api.getAvailableCars(Criteria.ALL, ALL_TIME);
        assertThat(availableCars.size()).isEqualTo(numberOwnedCars-1);
        assertThat(!availableCars.contains(MINI_COOPER_C1_170));
    }

    @Test
    public void s2_oneBookingOutsidePeriod() throws Exception{
        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()
            .withBookings(new Booking(MINI_COOPER_C1_170, RENTER_JOE, NEXT_WEEK))
            .build();
            
        int numberOwnedCars = api.getMatchingCars(Criteria.ALL).size();
        List<Car> availableCars = api.getAvailableCars(Criteria.ALL, THIS_WEEK);

        assertThat(availableCars.size()).isEqualTo(numberOwnedCars);
        assertThat(!availableCars.contains(MINI_COOPER_C1_170));
    }
    
    @Test
    public void s3_bookingACar() throws Exception{       
        
        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .build();

        Booking gretasBooking = api.bookCar(MINI_COOPER_C1_170, RENTER_GRETA, THIS_WEEK);

        List<Booking> bookings = api.getBookingsForPeriod(THIS_WEEK);

        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.contains(gretasBooking));
    }

    @Test
    public void s3_cancellingABooking() throws Exception{       

        Booking gretasBooking = new Booking(MINI_COOPER_C1_170, RENTER_GRETA, THIS_WEEK);
        Booking joesBooking = new Booking(VW_GOLF_B2_90, RENTER_JOE, THIS_WEEK);

        CarRentalCompany api = TestCarCompanyBuilder.create()        
            .withCars()            
            .withBookings(gretasBooking, joesBooking)
            .build();

        List<Booking> bookings = api.getBookingsForPeriod(THIS_WEEK);

        assertThat(api.cancelBooking(joesBooking)).isTrue();

        bookings = api.getBookingsForPeriod(THIS_WEEK);
        
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.contains(gretasBooking));
    }

    @Test
    public void s3_cantBookOverlapping() throws Exception{       
        
        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .build();
            
        api.bookCar(MINI_COOPER_C1_170, RENTER_GRETA, THIS_WEEK_AND_NEXT);

        assertThrows(
            Exception.class, 
            () -> api.bookCar(MINI_COOPER_C1_170, RENTER_JOE, THIS_WEEK)
         );
    }

    @Test
    public void s4_preparingCarsForThisWeek() throws Exception{

        Booking samLastWeek = new Booking(VW_GOLF_B2_90, RENTER_SAM, LAST_WEEK);
        Booking gretaThisWeek = new Booking(VW_GOLF_B2_90, RENTER_GRETA, THIS_WEEK);
        Booking joeThisWeek = new Booking(MINI_COOPER_C1_170, RENTER_JOE, THIS_WEEK);        
        Booking maisyNextWeek = new Booking(VW_PASSAT_C1_110, RENTER_MAISY, NEXT_WEEK);

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .withBookings(
                samLastWeek,
                gretaThisWeek,
                joeThisWeek,
                maisyNextWeek
            )
            .build();

        // check whats coming
        List<Booking> upcomingBookings = api.getBookingsForPeriod(THIS_WEEK);

        // We have 2
        assertThat(upcomingBookings.size()).isEqualTo(2);
        assertThat(upcomingBookings.contains(gretaThisWeek));
        assertThat(upcomingBookings.contains(joeThisWeek));
    }

    @Test
    public void s5_bookingACarForMaintenance() throws Exception{

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()                        
            .build();

        List<MaintenanceResult> results = api.bookMaintenance("REASON", MINI_COOPER_C1_170, NEXT_WEEK);

        List<Booking> bookings = api.getBookingsForPeriod(NEXT_WEEK);

        // We have 2
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.contains(new MaintenanceBooking(MINI_COOPER_C1_170, NEXT_WEEK)));

        // And no side-effects
        assertThat(results.size()).isEqualTo(0);
    }

    @Test
    public void s5_moveCustomerBooking() throws Exception{

        Booking joeThisWeek = new Booking(MINI_COOPER_C1_170, RENTER_JOE, THIS_WEEK);        

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .withBookings(joeThisWeek)
            .build();

        List<MaintenanceResult> results = api.bookMaintenance("Car is broken", MINI_COOPER_C1_170, THIS_WEEK);        
        
        List<Booking> bookings = api.getBookingsForPeriod(THIS_WEEK);

        // We have 2
        assertThat(bookings.size()).isEqualTo(2);
        Booking joesNewBooking = new Booking(VW_PASSAT_C1_110, RENTER_JOE, THIS_WEEK);
        assertThat(bookings.contains(joesNewBooking));
        assertThat(bookings.contains(new MaintenanceBooking(MINI_COOPER_C1_170, THIS_WEEK)));
     
        // And 1 side-effects, customer has a new booking
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isInstanceOf(CustomerBookingMoved.class);
        assertThat(((CustomerBookingMoved) results.get(0)).getOldBooking()).isEqualTo(joeThisWeek);
        assertThat(((CustomerBookingMoved) results.get(0)).getNewBooking()).isEqualTo(joesNewBooking);

    }

    @Test
    public void s5_moveMultipleCustomerBookings() throws Exception{
        
        Booking joeThisWeek = new Booking(MINI_COOPER_C1_170, RENTER_JOE, THIS_WEEK);        
        Booking maisyNextWeek = new Booking(MINI_COOPER_C1_170, RENTER_MAISY, NEXT_WEEK);        

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .withBookings(joeThisWeek, maisyNextWeek)
            .build();

        api.bookMaintenance("", MINI_COOPER_C1_170, THIS_WEEK_AND_NEXT);
    }

    @Test
    public void s5_failIfNoCarAvailableForCustomer() throws Exception{
        Booking joeThisWeek = new Booking(MINI_COOPER_C1_170, RENTER_JOE, THIS_WEEK);   // SWAP
        Booking maisyThisWeek = new Booking(VW_PASSAT_C1_110, RENTER_MAISY, NEXT_WEEK); // KEEP
        Booking samNextWeek = new Booking(MINI_COOPER_C1_170, RENTER_SAM, NEXT_WEEK);   // CANCELLED

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .withBookings(joeThisWeek, maisyThisWeek, samNextWeek)
            .build();

        List<MaintenanceResult> results = api.bookMaintenance("", MINI_COOPER_C1_170, THIS_WEEK_AND_NEXT);
        
        List<Booking> bookings = api.getBookingsForPeriod(THIS_WEEK);

        // We have 2
        assertThat(bookings.size()).isEqualTo(2);
        
        Booking maintenanceBooking = new MaintenanceBooking(MINI_COOPER_C1_170, THIS_WEEK_AND_NEXT);
        Booking joesSwappedBooking = new Booking(VW_PASSAT_C1_110, RENTER_JOE, THIS_WEEK);
        
        assertThat(bookings.contains(joesSwappedBooking));
        assertThat(bookings.contains(maisyThisWeek));
        assertThat(bookings.contains(maintenanceBooking));
     
        // And 2 side-effects, JOE has a new booking, SAM has a cancellation
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.contains(new CustomerBookingMoved("", joeThisWeek, joesSwappedBooking)));
        assertThat(results.contains(new CustomerBookingCancelled("", samNextWeek)));
    }

    @Test
    public void s6_showCustomerCarsWithBlendedPrices(){

        CarRentalCompany api = TestCarCompanyBuilder.create()
            .withCars()            
            .build();

        Criteria vwPolo = CriteriaBuilder.create().make("VW").model("Polo").build();

        List<CarView> carsView = api.getAvailableCarsCustomerView(vwPolo, ALL_TIME);
        
        double avgA1 = (VW_POLO_A1_70.getCostPerDay() + VW_POLO_A1_65.getCostPerDay()) / 2;

        assertThat(carsView.get(0).getRentalGroupPrice()).isEqualTo(avgA1);
        assertThat(carsView.get(1).getRentalGroupPrice()).isEqualTo(avgA1);  
    }
}
