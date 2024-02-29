package io.rental;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import io.utils.DatePeriod;
import static io.rental.Criteria.ALL;

/**
 * <p>API for Rental Shop</p>
 * 
 */
public interface CarRentalCompany {

    List<Car> getMatchingCars(Criteria criteria);
    List<Car> getAvailableCars(DatePeriod period);
    List<Car> getAvailableCars(Criteria criteria, DatePeriod period);

    void addCar(Car car);
    Booking bookCar(Car car, Renter renter, DatePeriod period) throws Exception;
    boolean cancelBooking(Booking booking) throws Exception;

    List<MaintenanceResult> bookMaintenance(String reason, Car car, DatePeriod period) throws Exception;
    List<Booking> getBookingsForPeriod(DatePeriod period);
}

/**
 * <p>In-process & In-memory Impl. for Rental Shop {@link CarRentalCompany API}, laid out similarly to Spring Boot service Controller</p>
 * 
 * <p>Assumptions / Shortcuts:</p>
 * <ul>
 * <li>Exposing the repos via the protected class interface (would prefer dependency injection).</li>
 * <li>Synchronising on an internal lock obj here, giving a simplistic transaction across repos.</li>
 * </ul>
 * 
  * @see BookingRepo BookingRepo for assumptions on that service
  * @see CarRepo CarRepo for assumptions on that service
 */
class CarRentalCompanyImpl implements CarRentalCompany {

    private final Object lock = new Object();
    
    protected BookingRepo bookingRepo = new InMemoryBookingRepo(); 
    protected CarRepo carRepo = new InMemoryCarRepo();
    
    @Override
    public void addCar(Car car) {
        synchronized(lock){
            carRepo.add(car);
        }
    }
    
    @Override
    public List<Car> getMatchingCars(Criteria criteria) {        
        synchronized(lock){
            return carRepo.getByCriteria(criteria);
        }
    }

    @Override
    public List<Car> getAvailableCars(DatePeriod period) {
        synchronized(lock){
            return getAvailableCars(ALL, period);
        }
    }

    @Override
    public List<Car> getAvailableCars(Criteria criteria, DatePeriod period) {        
        synchronized(lock){

            List<Car> carsWithBookings = bookingRepo.getForPeriod(period).stream()
                .map(b -> b.getCar())            
                .distinct()
                .collect(toList());

            List<Car> matchingCars = carRepo.getByCriteria(
                AndCriteria.of(
                    criteria, 
                    ExclusionListCriteria.of(carsWithBookings))
                    );        
                    
            return matchingCars;
        }
    }

    @Override
    public Booking bookCar(Car car, Renter renter, DatePeriod period) throws Exception{
        synchronized(lock){
            Booking booking = new Booking(car, renter, period);
            bookingRepo.add(booking);
            return booking;
        }
    }

    @Override
    public List<MaintenanceResult> bookMaintenance(String reason, Car car, DatePeriod period/*, boolean canCancel*/) throws Exception{
        synchronized(lock){
            MaintenanceBooking booking = new MaintenanceBooking(car, period);
            
            // This is an 'optimistic' activity, in that it could ultimately
            // fail because another thread adds to the bookings DB. Failed attempts
            // should leave the DB as before.

            List<Booking> conflicts = bookingRepo.getConflicts(car, period);
            List<MaintenanceResult> results = new ArrayList<>();
            

            // Resolve conflicts - I've gone with "do the best we can" and leaving
            // the door open to inform the user if a cancellation was unavoidable.

            for(Booking conflict: conflicts){            
                
                Criteria altCriteria = AndCriteria.of(
                    RentalGroupCriteria.of(booking.getCar().getRentalGroup()),
                    ExclusionListCriteria.of(car)
                );

                List<Car> available = this.getAvailableCars(altCriteria, conflict.getPeriod());
                
                if(available.size()>0){
                    // book first alternative
                    Car alt = available.get(0);
                    this.cancelBooking(conflict);
                    Booking altBooking = this.bookCar(alt, conflict.getRenter(), conflict.getPeriod());
                    results.add(new CustomerBookingMoved(reason, conflict, altBooking));
                } else {
                    // no alternatives exist
                    bookingRepo.remove(conflict);
                    results.add(new CustomerBookingCancelled(reason, conflict));
                }
            }

            bookingRepo.add(booking);
            return results;
        }
    }


    @Override
    public boolean cancelBooking(Booking booking) throws Exception {
        synchronized(lock){
            return bookingRepo.remove(booking);
        }
    }

    @Override
    public List<Booking> getBookingsForPeriod(DatePeriod period){
        synchronized(lock){
            return bookingRepo.getForPeriod(period);
        }
    }

    public void rentCar(Renter renter, Car car) {}

    public void returnCar(Renter renter, Car car) {}

}

