package de.ahus1.rest.sighting;

import de.ahus1.rest.general.DefaultRestEndpoint;

public class DummyIntermediateEndpoint<ENTITY> extends
    DefaultRestEndpoint<ENTITY> {

  public DummyIntermediateEndpoint(Class<ENTITY> clazz) {
    super(clazz);
  }

}
