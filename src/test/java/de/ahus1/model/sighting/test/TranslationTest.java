package de.ahus1.model.sighting.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ahus1.model.general.Translation;

/**
 * Testing the segment functionality.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
// START SNIPPET junitbasic2
public class TranslationTest {

  /**
   * Test toString() functionality of {@link Segment}.
   */
  @Test
  public void toStringTest() {
    Translation s = new Translation();
    Long l = new Long(1);
    s.setTranslationId(l);
    assertEquals("getId() and getTranslationId() should return the same",
        s.getTranslationId(), l);
  }
}
// END SNIPPET junitbasic2
