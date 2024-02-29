package io.rental;

/**
 * Reports the results of the creation of a {@link MaintenanceBooking}, which can result in {@link Renter}'s booking being changed to another {@link Car}, or even cancelled.
 * 
 * @see CarRentalCompany#bookMaintenance
 */
public abstract class MaintenanceResult {    
    private final String reason;

    public MaintenanceResult(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

/**
 * Produced by {@link CarRentalCompany#bookMaintenance} when a {@link Renter}'s booking has been <b>Changed</b>
 */
class CustomerBookingMoved extends MaintenanceResult {    
    private final Booking oldBooking;
    private final Booking newBooking;

    public CustomerBookingMoved(String reason, Booking oldBooking, Booking newBooking) {
        super(reason);
        this.oldBooking = oldBooking;
        this.newBooking = newBooking;        
    }

    public Booking getOldBooking() {
        return oldBooking;
    }

    public Booking getNewBooking() {
        return newBooking;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((oldBooking == null) ? 0 : oldBooking.hashCode());
        result = prime * result + ((newBooking == null) ? 0 : newBooking.hashCode());
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
        CustomerBookingMoved other = (CustomerBookingMoved) obj;
        if (oldBooking == null) {
            if (other.oldBooking != null)
                return false;
        } else if (!oldBooking.equals(other.oldBooking))
            return false;
        if (newBooking == null) {
            if (other.newBooking != null)
                return false;
        } else if (!newBooking.equals(other.newBooking))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CustomerBookingMoved [oldBooking=" + oldBooking + ", newBooking=" + newBooking + "]";
    }    

    
}

/**
 * Produced by {@link CarRentalCompany#bookMaintenance} when a {@link Renter}'s booking has been <b>Cancelled</b>
 */
class CustomerBookingCancelled extends MaintenanceResult {    
    private final Booking oldBooking;

    public CustomerBookingCancelled(String reason, Booking oldBooking) {
        super(reason);
        this.oldBooking = oldBooking;
    }

    public Booking getOldBooking() {
        return oldBooking;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((oldBooking == null) ? 0 : oldBooking.hashCode());
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
        CustomerBookingCancelled other = (CustomerBookingCancelled) obj;
        if (oldBooking == null) {
            if (other.oldBooking != null)
                return false;
        } else if (!oldBooking.equals(other.oldBooking))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CustomerBookingCancelled [oldBooking=" + oldBooking + "]";
    }
    
}



