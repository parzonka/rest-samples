package de.ahus1.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Ensure that the client re-validates the resources served by the server. This
 * is quite handy for development purposes.
 * 
 * @author Alexander Schwartz
 * 
 */
// START SNIPPET: nocache1
@WebFilter(filterName = "nocache")
public class NoCacheFilter implements Filter {
  // END SNIPPET: nocache1

  /**
   * Do nothing here.
   */
  public void destroy() {
  }

  /**
   * Add a Cache-Control header to ensure that the client retrieves the sources
   * again on next request. Will only do this if we have a HttpServlet here.
   * 
   * @param servletRequest
   *          servlet request
   * @param servletResponse
   *          servlet response
   * @param chain
   *          next filter to be called after this
   * @throws IOException
   *           on problems
   * @throws ServletException
   *           on problems
   */
  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain chain) throws IOException,
      ServletException {
    // START SNIPPET: nocache2
    if (servletResponse instanceof HttpServletResponse) {
      HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
      httpServletResponse.setHeader("Cache-Control", "max-age=0");
    }
    chain.doFilter(servletRequest, servletResponse);
    // END SNIPPET: nocache2
  }

  /**
   * Do nothing here.
   * 
   * @param config
   *          configuration
   */
  public void init(FilterConfig config) {
  }

}
