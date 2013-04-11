package de.ahus1.model.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handling of constraint violation exceptions that can be thrown by bean
 * validation. Each key will be an XML element, each value will be the value of
 * the element.
 * 
 * @author Alexander Schwartz (2012)
 * 
 */
@Provider
@Produces("text/xml")
public class XMLMessageBodyWriterHashMap implements
    MessageBodyWriter<HashMap<String, ?>> {

  @Override
  public boolean isWriteable(Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType) {
    return HashMap.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(HashMap<String, ?> t, Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType) {
    // not supported
    return -1;
  }

  @Override
  public void writeTo(HashMap<String, ?> t, Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new WebApplicationException(e);
    }
    Document document = db.newDocument();

    for (Entry<String, ?> entry : t.entrySet()) {
      Element mapElement = document.createElement(entry.getKey());
      mapElement.setTextContent(entry.getValue().toString());
      document.appendChild(mapElement);
    }
    Source xmlSource = new DOMSource(document);
    Result outputTarget = new StreamResult(entityStream);
    try {
      TransformerFactory.newInstance().newTransformer()
          .transform(xmlSource, outputTarget);
    } catch (TransformerException e) {
      throw new WebApplicationException(e);
    }

  }

}
