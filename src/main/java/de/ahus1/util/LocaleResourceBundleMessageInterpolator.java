package de.ahus1.util;

import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

/**
 * This allows a Locale to be set for the current context.
 * 
 * @author Alexander Schwartz
 * 
 */
public class LocaleResourceBundleMessageInterpolator extends
    ResourceBundleMessageInterpolator {

  private static ThreadLocal<Locale> locale = new ThreadLocal<Locale>();

  /**
   * Set the locale for this thread.
   * 
   * @param locale
   *          locale to be set
   */
  public static void setLocale(Locale locale) {
    LocaleResourceBundleMessageInterpolator.locale.set(locale);
  }

  /**
   * Clear the locale for this thread.
   */
  public static void clearLocale() {
    LocaleResourceBundleMessageInterpolator.locale.remove();
  }

  /**
   * Use the locale that has been set for this thread.
   * 
   * @param message
   *          message to be interpolated
   * @param context
   *          context for interpolation
   * @return interpolated message
   */
  @Override
  public String interpolate(String message, Context context) {
    Locale l = locale.get();
    if (l != null) {
      return super.interpolate(message, context, l);
    } else {
      return super.interpolate(message, context);
    }
  }
}
