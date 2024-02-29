package io.rental;

import java.util.List;
import java.util.function.Predicate;

/**
 * <p>Simple (functional) filter for Car queries</P
 * 
 * <p>Assumptions/Notes</p>
 * <ul>
 * <li>Extends {@link java.util.function.Predicate Predicate} so that it can be used with Java 8+ stream library</li>
 * </ul>
 */
public interface Criteria extends Predicate<Car> {    
    /**
     * A {@link Criteria} that matches all Cars
     */
    public final static Criteria ALL = new Criteria() {
        @Override
        public boolean test(Car car) {
            return true;
        }
    };
}

/**
 * Used to compose chains of {@link Criteria} from simple inputs.
 */
class CriteriaBuilder {

    private Criteria criteria = Criteria.ALL;

    public static CriteriaBuilder create() {
        return new CriteriaBuilder();
    }

    public Criteria build(){
        return criteria;
    }

    public CriteriaBuilder make(String make){
        criteria = new AndCriteria(criteria, new MakeCriteria(make));
        return this;
    }

    public CriteriaBuilder rentalGroup(String rentalGroup){
        criteria = new AndCriteria(criteria, new RentalGroupCriteria(rentalGroup));
        return this;
    }    
}

/**
 * A {@link Criteria} that provides a logical AND between two operands (is true when both operands are true).
 */
class AndCriteria implements Criteria {

    static AndCriteria of(Criteria lhs, Criteria rhs){
        return new AndCriteria(lhs, rhs);
    }

    private Criteria left;
    private Criteria right;
    
    public AndCriteria(Criteria left, Criteria right) {
        this.left = left;
        this.right = right;
    }

    public Criteria getLeft() {
        return left;
    }
    public Criteria getRight() {
        return right;
    }

    @Override
    public boolean test(Car car) {
        return left.test(car) && right.test(car);
    }
}

/**
 * A {@link Criteria} that is true when a {@link Car}'s make contains the specified string.
 */
class MakeCriteria implements Criteria {

    static MakeCriteria of(String make){
        return new MakeCriteria(make);
    }

    private String make;

    public MakeCriteria(String make) {
        this.make = make;
    }

    public String getMake() {
        return make;
    }

    @Override
    public boolean test(Car car) {
        return car.getMake().contains(make);
    }  
}

/**
 * A {@link Criteria} that is true when a {@link Car}'s rental group equals the specified string.
 */
class RentalGroupCriteria implements Criteria {
    
    static RentalGroupCriteria of(String group){
        return new RentalGroupCriteria(group);
    }

    private String group;

    public RentalGroupCriteria(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
       
    @Override
    public boolean test(Car car) {
        return car.getRentalGroup().equals(group);
    }  
}

/**
 * A {@link Criteria} that is true when a {@link Car} is not in the exclusion list.
 */
class ExclusionListCriteria implements Criteria {

    static ExclusionListCriteria of(List<Car> excludeCars){
        return new ExclusionListCriteria(excludeCars);
    }

    static ExclusionListCriteria of(Car... excludeCars){
        return new ExclusionListCriteria(List.of(excludeCars));
    }

    private List<Car> excludeCars;

    public ExclusionListCriteria(List<Car> excludeCars) {
        this.excludeCars = excludeCars;
    }

    @Override
    public boolean test(Car car) {
        return excludeCars.contains(car) == false;
    }
    
}
