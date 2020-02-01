/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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
package javax.swing.text;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collections;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.LinkedHashMap;

/**
 * A straightforward implementation of MutableAttributeSet using a
 * hash table.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans&trade;
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author Tim Prinzing
 */
public class SimpleAttributeSet implements MutableAttributeSet, Serializable, Cloneable
{
    private static final long serialVersionUID = -6631553454711782652L;

    /**
     * An empty attribute set.
     */
    public static final AttributeSet EMPTY = new EmptyAttributeSet();

    private transient LinkedHashMap<Object, Object> table = new LinkedHashMap<>(3);

    /**
     * Creates a new attribute set.
     */
    public SimpleAttributeSet() {
    }

    /**
     * Creates a new attribute set based on a supplied set of attributes.
     *
     * @param source the set of attributes
     */
    public SimpleAttributeSet(AttributeSet source) {
        addAttributes(source);
    }

    /**
     * Checks whether the set of attributes is empty.
     *
     * @return true if the set is empty else false
     */
    public boolean isEmpty()
    {
        return table.isEmpty();
    }

    /**
     * Gets a count of the number of attributes.
     *
     * @return the count
     */
    @Override
    public int getAttributeCount() {
        return table.size();
    }

    /**
     * Tells whether a given attribute is defined.
     *
     * @param attrName the attribute name
     * @return true if the attribute is defined
     */
    @Override
    public boolean isDefined(Object attrName) {
        return table.containsKey(attrName);
    }

    /**
     * Compares two attribute sets.
     *
     * @param attr the second attribute set
     * @return true if the sets are equal, false otherwise
     */
    @Override
    public boolean isEqual(AttributeSet attr) {
        return ((getAttributeCount() == attr.getAttributeCount()) &&
                containsAttributes(attr));
    }

    /**
     * Makes a copy of the attributes.
     *
     * @return the copy
     */
    @Override
    public AttributeSet copyAttributes() {
        return (AttributeSet) clone();
    }

    /**
     * Gets the names of the attributes in the set.
     *
     * @return the names as an <code>Enumeration</code>
     */
    @Override
    public Enumeration<?> getAttributeNames() {
        return Collections.enumeration(table.keySet());
    }

    /**
     * Gets the value of an attribute.
     *
     * @param name the attribute name
     * @return the value
     */
    @Override
    public Object getAttribute(Object name) {
        Object value = table.get(name);
        if (value == null) {
            AttributeSet parent = getResolveParent();
            if (parent != null) {
                value = parent.getAttribute(name);
            }
        }
        return value;
    }

    /**
     * Checks whether the attribute list contains a
     * specified attribute name/value pair.
     *
     * @param name the name
     * @param value the value
     * @return true if the name/value pair is in the list
     */
    @Override
    public boolean containsAttribute(Object name, Object value) {
        return value.equals(getAttribute(name));
    }

    /**
     * Checks whether the attribute list contains all the
     * specified name/value pairs.
     *
     * @param attributes the attribute list
     * @return true if the list contains all the name/value pairs
     */
    @Override
    public boolean containsAttributes(AttributeSet attributes) {
        boolean result = true;

        Enumeration names = attributes.getAttributeNames();
        while (result && names.hasMoreElements()) {
            Object name = names.nextElement();
            result = attributes.getAttribute(name).equals(getAttribute(name));
        }

        return result;
    }

    /**
     * Adds an attribute to the list.
     *
     * @param name the attribute name
     * @param value the attribute value
     */
    @Override
    public void addAttribute(Object name, Object value) {
        table.put(name, value);
    }

    /**
     * Adds a set of attributes to the list.
     *
     * @param attributes the set of attributes to add
     */
    @Override
    public void addAttributes(AttributeSet attributes) {
        Enumeration names = attributes.getAttributeNames();
        while (names.hasMoreElements()) {
            Object name = names.nextElement();
            addAttribute(name, attributes.getAttribute(name));
        }
    }

    /**
     * Removes an attribute from the list.
     *
     * @param name the attribute name
     */
    @Override
    public void removeAttribute(Object name) {
        table.remove(name);
    }

    /**
     * Removes a set of attributes from the list.
     *
     * @param names the set of names to remove
     */
    @Override
    public void removeAttributes(Enumeration<?> names) {
        while (names.hasMoreElements()) {
            removeAttribute(names.nextElement());
        }
    }

    /**
     * Removes a set of attributes from the list.
     *
     * @param attributes the set of attributes to remove
     */
    @Override
    public void removeAttributes(AttributeSet attributes) {
        if (attributes == this) {
            table.clear();
        }
        else {
            Enumeration names = attributes.getAttributeNames();
            while (names.hasMoreElements()) {
                Object name = names.nextElement();
                Object value = attributes.getAttribute(name);
                if (value.equals(getAttribute(name))) {
                    removeAttribute(name);
                }
            }
        }
    }

    /**
     * Gets the resolving parent.  This is the set
     * of attributes to resolve through if an attribute
     * isn't defined locally.  This is null if there
     * are no other sets of attributes to resolve
     * through.
     *
     * @return the parent
     */
    @Override
    public AttributeSet getResolveParent() {
        return (AttributeSet) table.get(StyleConstants.ResolveAttribute);
    }

    /**
     * Sets the resolving parent.
     *
     * @param parent the parent
     */
    @Override
    public void setResolveParent(AttributeSet parent) {
        addAttribute(StyleConstants.ResolveAttribute, parent);
    }

    // --- Object methods ---------------------------------

    /**
     * Clones a set of attributes.
     *
     * @return the new set of attributes
     */
    @Override
    public Object clone() {
        SimpleAttributeSet attr;
        try {
            attr = (SimpleAttributeSet) super.clone();
            attr.table = (LinkedHashMap) table.clone();
        } catch (CloneNotSupportedException cnse) {
            attr = null;
        }
        return attr;
    }

    /**
     * Returns a hashcode for this set of attributes.
     * @return     a hashcode value for this set of attributes.
     */
    @Override
    public int hashCode() {
        return table.hashCode();
    }

    /**
     * Compares this object to the specified object.
     * The result is <code>true</code> if the object is an equivalent
     * set of attributes.
     * @param     obj   the object to compare this attribute set with
     * @return    <code>true</code> if the objects are equal;
     *            <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AttributeSet) {
            AttributeSet attrs = (AttributeSet) obj;
            return isEqual(attrs);
        }
        return false;
    }

    /**
     * Converts the attribute set to a String.
     *
     * @return the string
     */
    @Override
    public String toString() {
        String s = "";
        Enumeration names = getAttributeNames();
        while (names.hasMoreElements()) {
            Object key = names.nextElement();
            Object value = getAttribute(key);
            if (value instanceof AttributeSet) {
                // don't go recursive
                s = s + key + "=**AttributeSet** ";
            } else {
                s = s + key + "=" + value + " ";
            }
        }
        return s;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        StyleContext.writeAttributeSet(s, this);
    }

    private void readObject(ObjectInputStream s)
      throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        table = new LinkedHashMap<>(3);
        StyleContext.readAttributeSet(s, this);
    }

    /**
     * An AttributeSet that is always empty.
     */
    static class EmptyAttributeSet implements AttributeSet, Serializable {
        static final long serialVersionUID = -8714803568785904228L;

        @Override
        public int getAttributeCount() {
            return 0;
        }
        @Override
        public boolean isDefined(Object attrName) {
            return false;
        }
        @Override
        public boolean isEqual(AttributeSet attr) {
            return (attr.getAttributeCount() == 0);
        }
        @Override
        public AttributeSet copyAttributes() {
            return this;
        }
        @Override
        public Object getAttribute(Object key) {
            return null;
        }
        @Override
        public Enumeration getAttributeNames() {
            return Collections.emptyEnumeration();
        }
        @Override
        public boolean containsAttribute(Object name, Object value) {
            return false;
        }
        @Override
        public boolean containsAttributes(AttributeSet attributes) {
            return (attributes.getAttributeCount() == 0);
        }
        @Override
        public AttributeSet getResolveParent() {
            return null;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return ((obj instanceof AttributeSet) &&
                    (((AttributeSet)obj).getAttributeCount() == 0));
        }
        @Override
        public int hashCode() {
            return 0;
        }
    }
}
