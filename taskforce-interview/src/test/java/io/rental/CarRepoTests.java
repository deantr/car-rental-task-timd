package io.rental;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static io.rental.TestCarCompanyBuilder.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CarRepoTests {
    
    @Test
    public void createsBlendedPrices(){

        CarRepo db = new InMemoryCarRepo();

        db.add(MINI_COOPER_C1_170);
        db.add(VW_GOLF_B2_90);
        db.add(VW_PASSAT_C1_110);
        db.add(VW_POLO_A1_65);
        db.add(VW_POLO_A1_70);

        Map<String, Double> prices = db.getBlendedPrices();

        double avgA1 = (VW_POLO_A1_70.getCostPerDay() + VW_POLO_A1_65.getCostPerDay()) / 2;
        double avgC1 = (MINI_COOPER_C1_170.getCostPerDay() + VW_PASSAT_C1_110.getCostPerDay()) / 2;

        assertThat(prices.get("A1")).isEqualTo(avgA1);
        assertThat(prices.get("B2")).isEqualTo(VW_GOLF_B2_90.getCostPerDay());
        assertThat(prices.get("C1")).isEqualTo(avgC1);
    }

    @Test
    public void customerViewRetrievesBlendedPrice(){
        
        CarRepo db = new InMemoryCarRepo();

        db.add(VW_POLO_A1_65);
        db.add(VW_POLO_A1_70);

        List<CarView> carsView = db.getCustomerViewByCriteria(Criteria.ALL);
        
        double avgA1 = (VW_POLO_A1_70.getCostPerDay() + VW_POLO_A1_65.getCostPerDay()) / 2;

        assertThat(carsView.get(0).getRentalGroupPrice()).isEqualTo(avgA1);
        assertThat(carsView.get(1).getRentalGroupPrice()).isEqualTo(avgA1);        
    }

}
