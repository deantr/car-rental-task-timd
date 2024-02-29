package io.rental;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CriteriaTest {

    static final CarRentalCompany api = TestCarCompanyBuilder.create().withCars().build(); 

    @Test
    public void searchByMake(){
        Criteria criteria = new MakeCriteria("VW");
        List<Car> carsAvailable = api.getMatchingCars(criteria);
        assertThat(carsAvailable.size()).isEqualTo(4);
    }

    @Test
    public void searchByMakeAndGroup(){
        Criteria criteria = CriteriaBuilder.create()
                                .make("VW")
                                .rentalGroup("A1")
                                .build();
        List<Car> carsAvailable = api.getMatchingCars(criteria);
        assertThat(carsAvailable.size()).isEqualTo(2);
    }
        


}
