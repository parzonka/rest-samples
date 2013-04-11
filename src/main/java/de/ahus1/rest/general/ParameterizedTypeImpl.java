package de.ahus1.rest.general;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Show to everybody the generic type included in this array. Didn't find a real
 * type.
 * 
 * @author Alexander Schwartz
 * 
 */
public class ParameterizedTypeImpl implements ParameterizedType {

  private Type[] actualTypeArguments;

  private Type ownerType;

  private Type rawType;

  /**
   * Constructor with parameters.
   * 
   * @param actualTypeArguments
   *          actual type
   * @param ownerType
   *          owner type
   * @param rawType
   *          raw type
   */
  public ParameterizedTypeImpl(Type[] actualTypeArguments, Type ownerType,
      Type rawType) {
    this.actualTypeArguments = actualTypeArguments;
    this.ownerType = ownerType;
    this.rawType = rawType;
  }

  @Override
  public Type[] getActualTypeArguments() {
    return actualTypeArguments;
  }

  @Override
  public Type getRawType() {
    return rawType;
  }

  @Override
  public Type getOwnerType() {
    return ownerType;
  }

}