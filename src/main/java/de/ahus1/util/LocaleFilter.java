package de.ahus1.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Setup locale for bean validator based on user's locale.
 * 
 * @author Alexander Schwartz
 * 
 */
@WebFilter
public class LocaleFilter implements Filter {

  /**
   * Do nothing here.
   */
  public void destroy() {
  }

  // START SNIPPET: hibernatevalidator6
  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain chain) throws IOException,
      ServletException {
    try {
      if (servletRequest instanceof HttpServletRequest) {
        // care for locale set via cookie (from i18next)
        servletRequest = new LocaleCookieWrapper(
            (HttpServletRequest) servletRequest);
      }
      LocaleResourceBundleMessageInterpolator.setLocale(servletRequest
          .getLocale());
      chain.doFilter(servletRequest, servletResponse);
    } finally {
      LocaleResourceBundleMessageInterpolator.clearLocale();
    }
  }

  // END SNIPPET: hibernatevalidator6

  /**
   * Do nothing here.
   * 
   * @param config
   *          configuration
   */
  public void init(FilterConfig config) {
  }

}
