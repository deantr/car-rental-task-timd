package io.rental;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Interface for Car Repository
 * <p>
 * 
 * <p>
 * Assumptions/Notes
 * </p>
 * <ul>
 * <li>Deliberately keeping the naming similar to JPA's magic-method
 * syntax)</li>
 * </ul>
 */
public interface CarRepo {
    List<Car> getAll();
    Optional<Car> getByRegistration(String reg);
    List<Car> getByCriteria(Criteria criteria);
    void add(Car car);
}

/**
 * <p>
 * In-Memory (Test) DB for Cars owned by the Rental Shop
 * <p>
 * 
 * <p>
 * Assumptions / Notes:
 * </p>
 * <ul>
 * <li>We can control the instantiation of this object to exactly-once (for
 * example in Spring.Boot injection).</li>
 * <li>Number of bookings & cars fairly low in this example, so we'll
 * brute-force any searches on this repo.</li>
 * <li>No local locking - to prevent deadlocks across the two repos.
 * Synchronisation is mediated by the API impl, ideally move this to (a) a DB
 * with transactions, or (b) CQRS if we arent as worried about e.g. rental shop
 * seeing stale car listings</li>*
 * <li>The above implies ALL access to this class must be via the API impl.</li>
 * </ul>
 */
class InMemoryCarRepo implements CarRepo {

    private final List<Car> db = new LinkedList<>(); // Since we'll only be scanning-through...

    @Override
    public List<Car> getAll() {
        return db;
    }

    public List<Car> getByCriteria(Criteria criteria) {
        return db.stream().filter(criteria).toList();
    }

    @Override
    public Optional<Car> getByRegistration(String reg) {
        return db.stream().filter(c -> c.getRegistrationNumber().equals(reg)).findFirst();
    }

    @Override
    public void add(Car car) {
        db.add(car);
    }
}