package de.ahus1.util;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * There is apparently a race condition in weld. When the entity manager is
 * instantiated in two parallel threads at the same time (i.e. the first page
 * triggers two ajax requests at the same time) there is a class loader
 * exception.
 * 
 * A workaround is to load the relevant classes by touching the entity manager
 * before the first request arrives (here: in the initialization phase of a
 * dummy servlet.
 * 
 * See https://community.jboss.org/message/725632 for a sample.
 */
@WebServlet(name = "helper", loadOnStartup = 1)
public class WeldWorkaroundHelper extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private EntityManager em;

  @Override
  public void init(ServletConfig config) throws ServletException {
    /*
     * here you can check with a breakpoint that the entity manager is really
     * initialized here.
     */
    super.init(config);
  }

}
