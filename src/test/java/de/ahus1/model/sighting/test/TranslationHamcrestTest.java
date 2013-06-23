package de.ahus1.model.sighting.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import de.ahus1.model.general.Translation;

/**
 * Testing the segment functionality. This time with using hamcrest matchers.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
// START SNIPPET junithamcrest2
public class TranslationHamcrestTest {

  /**
   * Test toString() functionality of {@link Segment}.
   */
  @Test
  public void toStringTest() {
    Translation s = new Translation();
    Long l = new Long(1);
    s.setTranslationId(l);
    assertThat("getId() returns the value set with setTranslationId()",
        s.getTranslationId(), equalTo(l));
  }
}
// END SNIPPET junithamcrest2
