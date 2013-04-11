package de.ahus1.rest.endpoint.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.ahus1.model.general.AbstractEntity;
import de.ahus1.model.general.HasId;
import de.ahus1.model.general.HasVersion;
import de.ahus1.model.sighting.Timezone;

// https://gist.github.com/1367794

/**
 * A first Arquillian test.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
@RunWith(Arquillian.class)
public class TimezoneEndpointTest {

  /**
   * Create a first deployment.
   * 
   * @return archive to be deployed
   */
  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class).addClass(Timezone.class)
        .addClass(AbstractEntity.class).addClass(HasId.class)
        .addClass(HasVersion.class)
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  /**
   * A first sample test without functionality.
   */
  @Test
  public void shouldCreateGreeting() {
    // FIXME: add something sensible here
  }
}