package de.ahus1.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * Create a cache manifest to be used with HTML5. It has a fixed URL
 * 
 * <pre>
 * /cache.appcache
 * </pre>
 * 
 * relative to the application. The cache manifest contains a hash code that is
 * calculated from the contents of all files to show if any of the resources has
 * changed.
 * <p />
 * You can specify the paths to be included in the cache manifest separated by
 * spaces in a initialization parameter <tt>paths</tt> like this (default would
 * be the root directory only):
 * 
 * <pre>
 *   &lt;servlet&gt;
 *     &lt;servlet-name&gt;cachemanifest&lt;/servlet-name&gt;
 *     &lt;init-param&gt;
 *       &lt;param-name&gt;paths&lt;/param-name&gt;
 *       &lt;param-value&gt;/ /js/libs /js /css /css/libs /js/modules /templates&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *   &lt;/servlet&gt;
 * </pre>
 * <p />
 * Future versions might use the time stamp and size of the files to determine
 * if they have changed. This might be a bit faster, but might not catch most
 * frequent changes.
 * 
 * @author Alexander Schwartz 2013
 * 
 */
@WebServlet(name = "cachemanifest", description = "create cache manifest", urlPatterns = "/cache.appcache", initParams = { @WebInitParam(name = "paths", value = "/") })
public class CacheManifest extends HttpServlet {

  private Logger log = Logger.getLogger(CacheManifest.class.getName());

  private static final long serialVersionUID = 1L;

  @Override
  public void service(ServletRequest req, ServletResponse res)
      throws ServletException, IOException {

    // send the correct mime type for the cache manifest
    res.setContentType("text/cache-manifest");

    /*
     * ensure that it's cached, but not more than two seconds. Chrome respects
     * that, proxys probably as well, but not Firefox (yet)
     */
    HttpServletResponse s = (HttpServletResponse) res;
    s.setHeader("Cache-Control", "max-age=2");

    /*
     * create a message from all resources to show it to the client to that it
     * will know if any of the resources has changed.
     */
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    Writer w = res.getWriter();
    w.append("CACHE MANIFEST\n");

    // cache all resources specified in the paths
    w.append("CACHE:\n");

    String[] paths = this.getInitParameter("paths").split(" ");
    byte[] dataBytes = new byte[1024];

    for (String path : paths) {
      ServletContext c = req.getServletContext();
      Set<String> resources = c.getResourcePaths(path);
      for (String r : resources) {
        int nread = 0;
        InputStream is = c.getResourceAsStream(r);
        if (is != null) {
          // only do this for real files, not directories
          w.append(c.getContextPath()).append(r).append("\n");
          while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
          }
          is.close();
        }
      }
    }

    // fall back to network for everything else
    w.append("NETWORK:\n*\n");

    byte[] mdbytes = md.digest();

    /*
     * convert the byte to hex and append it to the cache manifest so the
     * browsers can find out if something has changed.
     */
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    w.append("# hash:").append(sb.toString());

    log.info("cache manifest with hash:" + sb.toString());

  }

}
