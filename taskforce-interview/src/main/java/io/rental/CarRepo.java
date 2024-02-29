package io.rental;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * <p>Interface for Car Repository<p>
 * 
 * <p>Assumptions/Notes</p>
 * <ul>
 * <li>Deliberately keeping the naming similar to JPA's magic-method syntax)</li>
 * </ul>
 */
public interface CarRepo {
    List<Car> getAll();
    Optional<Car> getByRegistration(String reg);
    List<Car> getByCriteria(Criteria criteria);
    void add(Car car);
}

/**
 * <p>In-Memory (Test) DB for Cars owned by the Rental Shop<p>
 * 
 * <p>Assumptions / Notes:</p>
 * <ul>
 * <li>We can control the instantiation of this object to exactly-once (for example in Spring.Boot injection).</li>
 * <li>Number of bookings & cars fairly low in this example, so we'll brute-force any searches on this repo.</li>
 * <li>Simple locking scheme for internal RW consistency </li>
 * </ul>
 */
class InMemoryCarRepo implements CarRepo {

    private final List<Car> db = new LinkedList<>();  // Since we'll only be scanning-through...
    private final Object lock = new Object();

    @Override
    public synchronized List<Car> getAll() {
        synchronized(lock){
            return db;
        }
    }

    public List<Car> getByCriteria(Criteria criteria) {        
        synchronized(lock){
            return db.stream().filter(criteria).toList();
        }
    }

    @Override
    public Optional<Car> getByRegistration(String reg) {
        synchronized(lock){
            return db.stream().filter(c -> c.getRegistrationNumber().equals(reg)).findFirst();
        }
    }

    @Override
    public synchronized void add(Car car) {
        synchronized(lock){
            db.add(car);
        }
    }
}