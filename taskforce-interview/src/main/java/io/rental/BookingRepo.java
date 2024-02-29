package io.rental;

import java.util.LinkedList;
import java.util.List;

import io.utils.DatePeriod;
import io.utils.DatePeriodUtil;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * Interface for booking repo
 * </p>
 * 
 * <p>
 * Assumptions / Notes:
 * <p>
 * <ol>
 * <li>Deliberately keeping the naming similar to JPA's magic-method syntax</li>
 * </ol>
 */
public interface BookingRepo {
    List<Booking> getAll();
    List<Booking> getByRegistration(String reg);
    List<Booking> getForPeriod(DatePeriod period);
    List<Booking> getForPeriodAndCar(DatePeriod period, Car car);
    List<Booking> getConflicts(Car car, DatePeriod period);

    void add(Booking booking) throws Exception;
    void maintenanceSwap(MaintenanceBooking booking, Booking customer_old, Booking customer_new) throws Exception;
    boolean remove(Booking booking) throws Exception;
    void move(Booking booking_old, Booking booking_new) throws Exception;
    void removeAll();
}

/**
 * <p>
 * In-Memory Test DB for Rental bookings
 * </p>
 * 
 * <p>
 * Assumptions / Notes:
 * </p>
 * <ol>
 * <li>We can control the instantiation of this object to exactly-once (for
 * example in Spring.Boot injection).</li>
 * <li>Number of bookings & cars fairly low in this example, so we'll
 * brute-force any searches on this repo.</li>
 * <li>No local locking - to prevent deadlocks across the two repos.
 * Synchronisation is mediated by the API impl, ideally move this to (a) a DB
 * with transactions, or (b) CQRS if we arent as worried about e.g. rental shop
 * seeing stale car listings</li>*
 * <li>The above implies ALL access to this class must be via the API impl.</li>
 * </ol>
 */
class InMemoryBookingRepo implements BookingRepo {

    private final List<Booking> db = new LinkedList<>(); // Since we'll only be scanning-through...

    @Override
    public List<Booking> getAll() {
        return db;
    }

    @Override
    public List<Booking> getByRegistration(String reg) {
        return db.stream().filter(b -> b.getCar().getRegistrationNumber().equals(reg)).collect(toList());
    }

    @Override
    public List<Booking> getForPeriod(DatePeriod period) {
        return db.stream().filter(b -> DatePeriodUtil.areOverlapping(period, b.getPeriod())).collect(toList());
    }

    @Override
    public List<Booking> getForPeriodAndCar(DatePeriod period, Car car) {
        return db.stream().filter(b -> (DatePeriodUtil.areOverlapping(period, b.getPeriod()) &&
                b.getCar().equals(car))).collect(toList());
    }

    @Override
    public List<Booking> getConflicts(Car car, DatePeriod period) {
        return this.getForPeriodAndCar(period, car);
    }

    @Override
    public void add(Booking booking) throws Exception {
        if (getConflicts(booking.getCar(), booking.getPeriod()).size() > 0) {
            throw new Exception("Unable to book: conflicting bookings");
        }
        db.add(booking);
    }

    @Override
    public void maintenanceSwap(MaintenanceBooking maintenance, Booking customer_old, Booking customer_new)
            throws Exception {
        try {
            this.remove(customer_old);
            this.add(customer_new);
            this.add(maintenance);
        } catch (Exception e) {
            // attempt some basic tx rollback here
            db.remove(maintenance);
            db.remove(customer_new);
            db.remove(customer_old);
            throw new Exception("Could not swap customer's booking", e);
        }
    }

    @Override
    public boolean remove(Booking booking) throws Exception {
        return db.remove(booking);
    }

    @Override
    public void move(Booking booking_old, Booking booking_new) throws Exception {
        try {
            remove(booking_old);
            add(booking_new);
        } catch (Exception x) {
            // try to clean-up //
            db.remove(booking_new);
            db.remove(booking_old);
            db.add(booking_old);
            throw new Exception("Unable to move booking", x);
        }
    }

    @Override
    public void removeAll() {
        db.clear();
    }

}
