package de.ahus1.rest.endpoint.test;

import java.io.File;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.ahus1.model.general.AbstractEntity;
import de.ahus1.model.general.HasVersion;
import de.ahus1.model.general.IdView;
import de.ahus1.model.sighting.Timezone;
import de.ahus1.util.Resources;

/**
 * A little more advanced arquillian test. This names all the single files one
 * by one that are needed to make it work.
 * 
 * @author Alexander Schwartz 2013
 * 
 */
@RunWith(Arquillian.class)
public class TimezoneEndpointTest {

  @Inject
  private EntityManager em;

  /*
   * you'll need a user transaction here, as the JUnit test will not use any
   * transactions on its own.
   */
  @Inject
  private UserTransaction utx;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  /**
   * Create a first deployment.
   * 
   * @return archive to be deployed
   */
  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap
        .create(JavaArchive.class)
        .addClass(Timezone.class)
        .addClass(AbstractEntity.class)
        .addClass(AbstractEntity.Extended.class)
        .addClass(IdView.class)
        .addClass(HasVersion.class)
        .addClass(Resources.class)
        .addAsResource(new File("src/main/resources/META-INF/persistence.xml"),
            "META-INF/persistence.xml")
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  /**
   * A this one fails validation and throws an exception.
   * 
   * @throws Exception
   *           on problems
   */
  @Test
  public void testPersistence() throws Exception {
    utx.begin();
    Timezone tz = new Timezone();
    tz.setTimezoneName("no/where");
    em.persist(tz);
    utx.commit();
  }

  /**
   * A first test to persist an entity instance.
   * 
   * @throws Exception
   *           on problems
   */
  @Test
  public void testExceptionOnPersistence() throws Exception {
    expectedException.expect(ConstraintViolationException.class);
    utx.begin();
    Timezone tz = new Timezone();
    em.persist(tz);
    utx.commit();
  }
}