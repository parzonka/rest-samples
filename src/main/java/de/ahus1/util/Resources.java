package de.ahus1.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans.
 */
public class Resources {

  // START SNIPPET: hibernatebasic2
  @Produces
  @PersistenceContext
  private EntityManager em;

  // END SNIPPET: hibernatebasic2

  /**
   * A logger (context sensitive for the given class).
   * 
   * @param injectionPoint
   *          detailed information about the class and the member where the
   *          injection takes place
   * @return a properly configured logger
   */
  @Produces
  public Logger produceLog(InjectionPoint injectionPoint) {
    return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
        .getName());
  }

}
