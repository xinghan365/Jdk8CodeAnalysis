/*
 * Copyright (c) 1997, 2008, Oracle and/or its affiliates. All rights reserved.
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
package javax.swing.text.rtf;

import java.util.Dictionary;
import java.util.Enumeration;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;


/* This AttributeSet is made entirely out of tofu and Ritz Crackers
   and yet has a remarkably attribute-set-like interface! */
class MockAttributeSet
    implements AttributeSet, MutableAttributeSet
{
    public Dictionary<Object, Object> backing;

    public boolean isEmpty()
    {
         return backing.isEmpty();
    }

    @Override
    public int getAttributeCount()
    {
         return backing.size();
    }

    @Override
    public boolean isDefined(Object name)
    {
         return ( backing.get(name) ) != null;
    }

    @Override
    public boolean isEqual(AttributeSet attr)
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override
    public AttributeSet copyAttributes()
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override
    public Object getAttribute(Object name)
    {
        return backing.get(name);
    }

    @Override
    public void addAttribute(Object name, Object value)
    {
        backing.put(name, value);
    }

    @Override
    public void addAttributes(AttributeSet attr)
    {
        Enumeration as = attr.getAttributeNames();
        while(as.hasMoreElements()) {
            Object el = as.nextElement();
            backing.put(el, attr.getAttribute(el));
        }
    }

    @Override
    public void removeAttribute(Object name)
    {
        backing.remove(name);
    }

    @Override
    public void removeAttributes(AttributeSet attr)
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override
    public void removeAttributes(Enumeration<?> en)
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override
    public void setResolveParent(AttributeSet pp)
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }


    @Override
    public Enumeration getAttributeNames()
    {
         return backing.keys();
    }

    @Override
    public boolean containsAttribute(Object name, Object value)
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override
    public boolean containsAttributes(AttributeSet attr)
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override
    public AttributeSet getResolveParent()
    {
         throw new InternalError("MockAttributeSet: charade revealed!");
    }
}
