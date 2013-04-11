package de.ahus1.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

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

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain chain) throws IOException,
      ServletException {
    try {
      LocaleResourceBundleMessageInterpolator.setLocale(servletRequest
          .getLocale());
      chain.doFilter(servletRequest, servletResponse);
    } finally {
      LocaleResourceBundleMessageInterpolator.clearLocale();
    }
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
