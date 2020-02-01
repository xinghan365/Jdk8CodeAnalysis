/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 */

package javax.xml.stream.util;

import java.io.Reader;
import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

/**
 * This is the base class for deriving an XMLStreamReader filter
 *
 * This class is designed to sit between an XMLStreamReader and an
 * application's XMLStreamReader.   By default each method
 * does nothing but call the corresponding method on the
 * parent interface.
 *
 * @version 1.0
 * @author Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 * @see javax.xml.stream.XMLStreamReader
 * @see EventReaderDelegate
 * @since 1.6
 */

public class StreamReaderDelegate implements XMLStreamReader {
  private XMLStreamReader reader;

  /**
   * Construct an empty filter with no parent.
   */
  public StreamReaderDelegate(){}

  /**
   * Construct an filter with the specified parent.
   * @param reader the parent
   */
  public StreamReaderDelegate(XMLStreamReader reader) {
    this.reader = reader;
  }

  /**
   * Set the parent of this instance.
   * @param reader the new parent
   */
  public void setParent(XMLStreamReader reader) {
    this.reader = reader;
  }

  /**
   * Get the parent of this instance.
   * @return the parent or null if none is set
   */
  public XMLStreamReader getParent() {
    return reader;
  }

  @Override
  public int next()
    throws XMLStreamException
  {
    return reader.next();
  }

  @Override
  public int nextTag()
    throws XMLStreamException
  {
    return reader.nextTag();
  }

  @Override
  public String getElementText()
    throws XMLStreamException
  {
    return reader.getElementText();
  }

  @Override
  public void require(int type, String namespaceURI, String localName)
    throws XMLStreamException
  {
    reader.require(type,namespaceURI,localName);
  }

  @Override
  public boolean hasNext()
    throws XMLStreamException
  {
    return reader.hasNext();
  }

  @Override
  public void close()
    throws XMLStreamException
  {
    reader.close();
  }

  @Override
  public String getNamespaceURI(String prefix)
  {
    return reader.getNamespaceURI(prefix);
  }

  @Override
  public NamespaceContext getNamespaceContext() {
    return reader.getNamespaceContext();
  }

  @Override
  public boolean isStartElement() {
    return reader.isStartElement();
  }

  @Override
  public boolean isEndElement() {
    return reader.isEndElement();
  }

  @Override
  public boolean isCharacters() {
    return reader.isCharacters();
  }

  @Override
  public boolean isWhiteSpace() {
    return reader.isWhiteSpace();
  }

  @Override
  public String getAttributeValue(String namespaceUri,
                                  String localName)
  {
    return reader.getAttributeValue(namespaceUri,localName);
  }

  @Override
  public int getAttributeCount() {
    return reader.getAttributeCount();
  }

  @Override
  public QName getAttributeName(int index) {
    return reader.getAttributeName(index);
  }

  @Override
  public String getAttributePrefix(int index) {
    return reader.getAttributePrefix(index);
  }

  @Override
  public String getAttributeNamespace(int index) {
    return reader.getAttributeNamespace(index);
  }

  @Override
  public String getAttributeLocalName(int index) {
    return reader.getAttributeLocalName(index);
  }

  @Override
  public String getAttributeType(int index) {
    return reader.getAttributeType(index);
  }

  @Override
  public String getAttributeValue(int index) {
    return reader.getAttributeValue(index);
  }

  @Override
  public boolean isAttributeSpecified(int index) {
    return reader.isAttributeSpecified(index);
  }

  @Override
  public int getNamespaceCount() {
    return reader.getNamespaceCount();
  }

  @Override
  public String getNamespacePrefix(int index) {
    return reader.getNamespacePrefix(index);
  }

  @Override
  public String getNamespaceURI(int index) {
    return reader.getNamespaceURI(index);
  }

  @Override
  public int getEventType() {
    return reader.getEventType();
  }

  @Override
  public String getText() {
    return reader.getText();
  }

  @Override
  public int getTextCharacters(int sourceStart,
                               char[] target,
                               int targetStart,
                               int length)
    throws XMLStreamException {
    return reader.getTextCharacters(sourceStart,
                                    target,
                                    targetStart,
                                    length);
  }


  @Override
  public char[] getTextCharacters() {
    return reader.getTextCharacters();
  }

  @Override
  public int getTextStart() {
    return reader.getTextStart();
  }

  @Override
  public int getTextLength() {
    return reader.getTextLength();
  }

  @Override
  public String getEncoding() {
    return reader.getEncoding();
  }

  @Override
  public boolean hasText() {
    return reader.hasText();
  }

  @Override
  public Location getLocation() {
    return reader.getLocation();
  }

  @Override
  public QName getName() {
    return reader.getName();
  }

  @Override
  public String getLocalName() {
    return reader.getLocalName();
  }

  @Override
  public boolean hasName() {
    return reader.hasName();
  }

  @Override
  public String getNamespaceURI() {
    return reader.getNamespaceURI();
  }

  @Override
  public String getPrefix() {
    return reader.getPrefix();
  }

  @Override
  public String getVersion() {
    return reader.getVersion();
  }

  @Override
  public boolean isStandalone() {
    return reader.isStandalone();
  }

  @Override
  public boolean standaloneSet() {
    return reader.standaloneSet();
  }

  @Override
  public String getCharacterEncodingScheme() {
    return reader.getCharacterEncodingScheme();
  }

  @Override
  public String getPITarget() {
    return reader.getPITarget();
  }

  @Override
  public String getPIData() {
    return reader.getPIData();
  }

  @Override
  public Object getProperty(String name) {
    return reader.getProperty(name);
  }
}
