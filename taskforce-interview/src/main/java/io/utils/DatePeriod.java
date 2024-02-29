package io.utils;

import java.time.LocalDate;

import javax.annotation.concurrent.Immutable;

/**
 * <p>Notes</p>
 * <ul>
 * <li>Added (auto-generated) {@link DatePeriod#equals} and {@link DatePeriod#hashCode} to ensure equality works for objects that depend on it.</li>
 * </ul>
 * @see Booking 
 */
@Immutable
public class DatePeriod {
    
    public static final DatePeriod ALL_TIME = new DatePeriod(LocalDate.MIN, LocalDate.MAX);

    private LocalDate start;
    private LocalDate end;

    public DatePeriod(LocalDate start, LocalDate end) {
        assert start.isBefore(end) || start.isEqual(end);
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
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
        DatePeriod other = (DatePeriod) obj;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        return true;
    }

    
}
