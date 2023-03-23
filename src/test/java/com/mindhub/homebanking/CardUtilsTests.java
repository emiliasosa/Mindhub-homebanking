package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cvvLess(){
        int cardCvv = CardUtils.getCvv();
        assertThat(cardCvv,lessThan(1000));
    }

    @Test
    public void cvvGreater(){
        int cardCvv = CardUtils.getCvv();
        assertThat(cardCvv,greaterThan(99));
    }

    @Test
    public void cvvEither(){
        int cardCvv = CardUtils.getCvv();
        assertThat(cardCvv,either(greaterThan(99)).or(lessThan(1000)));
    }

    @Test
    public void cvvNull(){
        int cardCvv = CardUtils.getCvv();
        assertThat(cardCvv,notNullValue());
    }

}
