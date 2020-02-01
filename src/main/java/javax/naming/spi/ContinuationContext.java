/*
 * Copyright (c) 1999, 2011, Oracle and/or its affiliates. All rights reserved.
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

package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.*;

/**
  * This class is for dealing with federations/continuations.
  *
  * @author Rosanna Lee
  * @author Scott Seligman
  * @since 1.3
  */

class ContinuationContext implements Context, Resolver {
    protected CannotProceedException cpe;
    protected Hashtable<?,?> env;
    protected Context contCtx = null;

    protected ContinuationContext(CannotProceedException cpe,
                        Hashtable<?,?> env) {
        this.cpe = cpe;
        this.env = env;
    }

    protected Context getTargetContext() throws NamingException {
        if (contCtx == null) {
            if (cpe.getResolvedObj() == null) {
                throw (NamingException)cpe.fillInStackTrace();
            }

            contCtx = NamingManager.getContext(cpe.getResolvedObj(),
                                               cpe.getAltName(),
                                               cpe.getAltNameCtx(),
                                               env);
            if (contCtx == null) {
                throw (NamingException)cpe.fillInStackTrace();
            }
        }
        return contCtx;
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.lookup(name);
    }

    @Override
    public Object lookup(String name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.lookup(name);
    }

    @Override
    public void bind(Name name, Object newObj) throws NamingException {
        Context ctx = getTargetContext();
        ctx.bind(name, newObj);
    }

    @Override
    public void bind(String name, Object newObj) throws NamingException {
        Context ctx = getTargetContext();
        ctx.bind(name, newObj);
    }

    @Override
    public void rebind(Name name, Object newObj) throws NamingException {
        Context ctx = getTargetContext();
        ctx.rebind(name, newObj);
    }
    @Override
    public void rebind(String name, Object newObj) throws NamingException {
        Context ctx = getTargetContext();
        ctx.rebind(name, newObj);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        Context ctx = getTargetContext();
        ctx.unbind(name);
    }
    @Override
    public void unbind(String name) throws NamingException {
        Context ctx = getTargetContext();
        ctx.unbind(name);
    }

    @Override
    public void rename(Name name, Name newName) throws NamingException {
        Context ctx = getTargetContext();
        ctx.rename(name, newName);
    }
    @Override
    public void rename(String name, String newName) throws NamingException {
        Context ctx = getTargetContext();
        ctx.rename(name, newName);
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.list(name);
    }
    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.list(name);
    }


    @Override
    public NamingEnumeration<Binding> listBindings(Name name)
        throws NamingException
    {
        Context ctx = getTargetContext();
        return ctx.listBindings(name);
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.listBindings(name);
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        Context ctx = getTargetContext();
        ctx.destroySubcontext(name);
    }
    @Override
    public void destroySubcontext(String name) throws NamingException {
        Context ctx = getTargetContext();
        ctx.destroySubcontext(name);
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.createSubcontext(name);
    }
    @Override
    public Context createSubcontext(String name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.createSubcontext(name);
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.lookupLink(name);
    }
    @Override
    public Object lookupLink(String name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.lookupLink(name);
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.getNameParser(name);
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        Context ctx = getTargetContext();
        return ctx.getNameParser(name);
    }

    @Override
    public Name composeName(Name name, Name prefix)
        throws NamingException
    {
        Context ctx = getTargetContext();
        return ctx.composeName(name, prefix);
    }

    @Override
    public String composeName(String name, String prefix)
            throws NamingException {
        Context ctx = getTargetContext();
        return ctx.composeName(name, prefix);
    }

    @Override
    public Object addToEnvironment(String propName, Object value)
        throws NamingException {
        Context ctx = getTargetContext();
        return ctx.addToEnvironment(propName, value);
    }

    @Override
    public Object removeFromEnvironment(String propName)
        throws NamingException {
        Context ctx = getTargetContext();
        return ctx.removeFromEnvironment(propName);
    }

    @Override
    public Hashtable<?,?> getEnvironment() throws NamingException {
        Context ctx = getTargetContext();
        return ctx.getEnvironment();
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        Context ctx = getTargetContext();
        return ctx.getNameInNamespace();
    }

    @Override
    public ResolveResult
        resolveToClass(Name name, Class<? extends Context> contextType)
        throws NamingException
    {
        if (cpe.getResolvedObj() == null) {
            throw (NamingException)cpe.fillInStackTrace();
        }

        Resolver res = NamingManager.getResolver(cpe.getResolvedObj(),
                                                 cpe.getAltName(),
                                                 cpe.getAltNameCtx(),
                                                 env);
        if (res == null) {
            throw (NamingException)cpe.fillInStackTrace();
        }
        return res.resolveToClass(name, contextType);
    }

    @Override
    public ResolveResult
        resolveToClass(String name, Class<? extends Context> contextType)
        throws NamingException
    {
        if (cpe.getResolvedObj() == null) {
            throw (NamingException)cpe.fillInStackTrace();
        }

        Resolver res = NamingManager.getResolver(cpe.getResolvedObj(),
                                                 cpe.getAltName(),
                                                 cpe.getAltNameCtx(),
                                                 env);
        if (res == null) {
            throw (NamingException)cpe.fillInStackTrace();
        }
        return res.resolveToClass(name, contextType);
    }

    @Override
    public void close() throws NamingException {
        cpe = null;
        env = null;
        if (contCtx != null) {
            contCtx.close();
            contCtx = null;
        }
    }
}
