package io.rental;

import java.time.LocalDate;

import com.google.errorprone.annotations.Immutable;

import io.utils.DatePeriod;

/**
 * <p>Specifies a triplet of (Car, Renter, Period of time). </p>
 * <p>Assumptions/Notes:</p>
 * <ul>
 * <li>Hashcode/Equality assumes object-equality is implemented on all 3 items (it is)</li>
 * <li>For <b>Story 5</b> there is currently no way to mark a car as "prepared"<li/>
 * </ul>
 * @see Car
 * @see Renter
 * @see DatePeriod
  */
@Immutable
public class Booking {

    protected Car car;
    protected Renter renter;
    protected DatePeriod period;
    
    public Booking(Car car, Renter renter, DatePeriod period) {
        this.car = car;
        this.renter = renter;
        this.period = period;
    }

    public Car getCar() {
        return car;
    }

    public Renter getRenter() {
        return renter;
    }

    public DatePeriod getPeriod() {
        return period;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((car == null) ? 0 : car.hashCode());
        result = prime * result + ((renter == null) ? 0 : renter.hashCode());
        result = prime * result + ((period == null) ? 0 : period.hashCode());
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
        Booking other = (Booking) obj;
        if (car == null) {
            if (other.car != null)
                return false;
        } else if (!car.equals(other.car))
            return false;
        if (renter == null) {
            if (other.renter != null)
                return false;
        } else if (!renter.equals(other.renter))
            return false;
        if (period == null) {
            if (other.period != null)
                return false;
        } else if (!period.equals(other.period))
            return false;
        return true;        
    }

    @Override
    public String toString() {
        return "Booking [car=" + car + ", renter=" + renter + ", period=" + period + "]";
    }    
}

/**
 * <p>Specialisation of {@link Booking Booking} for when a car needs to be booked-out for maintenance.</p>
 * <p>Assumptions/Notes:</p>
 * <ul>
 * <li>Hashcode/Equality assumes object-equality is implemented on all 3 items (it is)</li>
 * </ul>
 * @see Booking
 * @see Car
 * @see DatePeriod
 * </pre>
  */
@Immutable
class MaintenanceBooking extends Booking {

    public static Renter MAINTENANCE_RENTER = new Renter("", "", "", LocalDate.MIN);

    public MaintenanceBooking(Car car, DatePeriod period) {
        super(car, MAINTENANCE_RENTER, period);        
    }

    @Override
    public String toString() {
        return "MaintenanceBooking [car=" + car + ", period=" + period + "]";
    }    
}
