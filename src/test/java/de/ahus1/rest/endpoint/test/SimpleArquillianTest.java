package de.ahus1.rest.endpoint.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
import de.ahus1.model.general.Translation;

/**
 * A first Arquillian test.
 * 
 * @author Alexander Schwartz 2012
 * 
 */
// START SNIPPET arquillianbasic3
@RunWith(Arquillian.class)
public class SimpleArquillianTest {

  /**
   * Create a first deployment.
   * 
   * @return archive to be deployed
   */
  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class).addClass(Translation.class)
        .addClass(HasId.class).addClass(AbstractEntity.class)
        .addClass(AbstractEntity.Extended.class).addClass(HasVersion.class)
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  /**
   * A first sample test without real functionality.
   */
  @Test
  public void testGetterSetter() {
    Translation t = new Translation();
    t.setTranslationId((long) 1);
    assertThat("translation ID is the same", t.getId(), equalTo((long) 1));
  }
}
// END SNIPPET arquillianbasic3
