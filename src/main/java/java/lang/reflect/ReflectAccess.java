/*
 * Copyright (c) 2001, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.lang.reflect;

import sun.reflect.MethodAccessor;
import sun.reflect.ConstructorAccessor;

/** Package-private class implementing the
    sun.reflect.LangReflectAccess interface, allowing the java.lang
    package to instantiate objects in this package. */

class ReflectAccess implements sun.reflect.LangReflectAccess {
    @Override
    public Field newField(Class<?> declaringClass,
                          String name,
                          Class<?> type,
                          int modifiers,
                          int slot,
                          String signature,
                          byte[] annotations)
    {
        return new Field(declaringClass,
                         name,
                         type,
                         modifiers,
                         slot,
                         signature,
                         annotations);
    }

    @Override
    public Method newMethod(Class<?> declaringClass,
                            String name,
                            Class<?>[] parameterTypes,
                            Class<?> returnType,
                            Class<?>[] checkedExceptions,
                            int modifiers,
                            int slot,
                            String signature,
                            byte[] annotations,
                            byte[] parameterAnnotations,
                            byte[] annotationDefault)
    {
        return new Method(declaringClass,
                          name,
                          parameterTypes,
                          returnType,
                          checkedExceptions,
                          modifiers,
                          slot,
                          signature,
                          annotations,
                          parameterAnnotations,
                          annotationDefault);
    }

    @Override
    public <T> Constructor<T> newConstructor(Class<T> declaringClass,
                                             Class<?>[] parameterTypes,
                                             Class<?>[] checkedExceptions,
                                             int modifiers,
                                             int slot,
                                             String signature,
                                             byte[] annotations,
                                             byte[] parameterAnnotations)
    {
        return new Constructor<>(declaringClass,
                                  parameterTypes,
                                  checkedExceptions,
                                  modifiers,
                                  slot,
                                  signature,
                                  annotations,
                                  parameterAnnotations);
    }

    @Override
    public MethodAccessor getMethodAccessor(Method m) {
        return m.getMethodAccessor();
    }

    @Override
    public void setMethodAccessor(Method m, MethodAccessor accessor) {
        m.setMethodAccessor(accessor);
    }

    @Override
    public ConstructorAccessor getConstructorAccessor(Constructor<?> c) {
        return c.getConstructorAccessor();
    }

    @Override
    public void setConstructorAccessor(Constructor<?> c,
                                       ConstructorAccessor accessor)
    {
        c.setConstructorAccessor(accessor);
    }

    @Override
    public int getConstructorSlot(Constructor<?> c) {
        return c.getSlot();
    }

    @Override
    public String getConstructorSignature(Constructor<?> c) {
        return c.getSignature();
    }

    @Override
    public byte[] getConstructorAnnotations(Constructor<?> c) {
        return c.getRawAnnotations();
    }

    @Override
    public byte[] getConstructorParameterAnnotations(Constructor<?> c) {
        return c.getRawParameterAnnotations();
    }

    @Override
    public byte[] getExecutableTypeAnnotationBytes(Executable ex) {
        return ex.getTypeAnnotationBytes();
    }

    //
    // Copying routines, needed to quickly fabricate new Field,
    // Method, and Constructor objects from templates
    //
    @Override
    public Method      copyMethod(Method arg) {
        return arg.copy();
    }

    @Override
    public Field       copyField(Field arg) {
        return arg.copy();
    }

    @Override
    public <T> Constructor<T> copyConstructor(Constructor<T> arg) {
        return arg.copy();
    }
}
