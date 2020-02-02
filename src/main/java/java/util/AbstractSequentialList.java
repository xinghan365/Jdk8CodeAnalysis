/*
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

/**
 * 此类提供的骨干实现List界面最小化以实现此接口由“连续访问”数据存储备份所需的工作（如链接列表）。 对于随机访问数据（如数组）， AbstractList应优先于此类。
 * 这个类是在这个意义上AbstractList类相反，它实现了对列表的列表迭代器顶部的“随机访问”方法（get(int index)，set(int index, E element)，add(int index, E element)和remove(int index)），而不是周围的其他方法。
 *
 * 为了实现一个列表，程序员只需要扩展这个类，并提供listIterator和size方法的实现。 对于一个不可修改的列表，程序员只需要实现列表迭代器的hasNext，next，hasPrevious，previous和index方法。
 *
 * 对于可修改的列表，程序员应该另外实现list iterator的set方法。 对于可变大小的列表，程序员应该另外实现list iterator的remove和add方法。
 *
 * 根据Collection接口规范中的建议，程序员通常应该提供一个void（无参数）和集合构造函数。
 *
 * This class provides a skeletal implementation of the <tt>List</tt>
 * interface to minimize the effort required to implement this interface
 * backed by a "sequential access" data store (such as a linked list).  For
 * random access data (such as an array), <tt>AbstractList</tt> should be used
 * in preference to this class.<p>
 *
 * This class is the opposite of the <tt>AbstractList</tt> class in the sense
 * that it implements the "random access" methods (<tt>get(int index)</tt>,
 * <tt>set(int index, E element)</tt>, <tt>add(int index, E element)</tt> and
 * <tt>remove(int index)</tt>) on top of the list's list iterator, instead of
 * the other way around.<p>
 *
 * To implement a list the programmer needs only to extend this class and
 * provide implementations for the <tt>listIterator</tt> and <tt>size</tt>
 * methods.  For an unmodifiable list, the programmer need only implement the
 * list iterator's <tt>hasNext</tt>, <tt>next</tt>, <tt>hasPrevious</tt>,
 * <tt>previous</tt> and <tt>index</tt> methods.<p>
 *
 * For a modifiable list the programmer should additionally implement the list
 * iterator's <tt>set</tt> method.  For a variable-size list the programmer
 * should additionally implement the list iterator's <tt>remove</tt> and
 * <tt>add</tt> methods.<p>
 *
 * The programmer should generally provide a void (no argument) and collection
 * constructor, as per the recommendation in the <tt>Collection</tt> interface
 * specification.<p>
 *
 * This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @see List
 * @see AbstractList
 * @see AbstractCollection
 * @since 1.2
 */

public abstract class AbstractSequentialList<E> extends AbstractList<E> {
    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractSequentialList() {
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it gets
     * the element using <tt>ListIterator.next</tt> and returns it.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public E get(int index) {
        try {
            return listIterator(index).next();
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it gets
     * the current element using <tt>ListIterator.next</tt> and replaces it
     * with <tt>ListIterator.set</tt>.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the list iterator does not
     * implement the <tt>set</tt> operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public E set(int index, E element) {
        try {
            ListIterator<E> e = listIterator(index);
            E oldVal = e.next();
            e.set(element);
            return oldVal;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    /**
     * 将指定的元素插入此列表中的指定位置（可选操作）。 将当前位于该位置的元素（如果有）和任何后续元素（向其索引添加一个）移动。
     * 此实现首先获取指向索引元素的列表迭代器（具有listIterator(index) ）。 然后，它插入与ListIterator.add指定的元素。
     *
     * 注意，此实现将抛出UnsupportedOperationException如果列表迭代器没有实现add操作。
     *
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it
     * inserts the specified element with <tt>ListIterator.add</tt>.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the list iterator does not
     * implement the <tt>add</tt> operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public void add(int index, E element) {
        try {
            listIterator(index).add(element);
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    /**
     * 删除该列表中指定位置的元素（可选操作）。 将任何后续元素移动到左侧（从其索引中减去一个元素）。 返回从列表中删除的元素。
     * 此实现首先获取指向索引元素的列表迭代器（使用listIterator(index) ）。 然后，它用ListIterator.remove删除元素。
     *
     * 注意，此实现将抛出UnsupportedOperationException如果列表迭代器没有实现remove操作。
     *
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it removes
     * the element with <tt>ListIterator.remove</tt>.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the list iterator does not
     * implement the <tt>remove</tt> operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public E remove(int index) {
        try {
            ListIterator<E> e = listIterator(index);
            E outCast = e.next();
            e.remove();
            return outCast;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }


    // Bulk Operations

    /**
     * 将指定集合中的所有元素插入到此列表中的指定位置（可选操作）。 将当前位于该位置（如果有的话）的元素和随后的任何元素移动到右边（增加其索引）。 新元素将按照指定集合的迭代器返回的顺序显示在此列表中。 如果在操作进行中修改了指定的集合，则此操作的行为是未定义的。 （注意，如果指定的集合是此列表，并且它是非空的，则会发生这种情况。）
     * 该实现在指定的集合上获得一个迭代器，并通过该列表指向索引元素（具有listIterator(index) ）的列表迭代器。 然后，它迭代指定的集合，将从迭代器获取的元素插入到此列表中，一次一个，使用ListIterator.add后跟ListIterator.next （跳过添加的元素）。
     *
     * 注意，此实现将抛出UnsupportedOperationException如果由listIterator方法返回的列表迭代器没有实现add操作。
     *
     * Inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation).  Shifts the
     * element currently at that position (if any) and any subsequent
     * elements to the right (increases their indices).  The new elements
     * will appear in this list in the order that they are returned by the
     * specified collection's iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the
     * operation is in progress.  (Note that this will occur if the specified
     * collection is this list, and it's nonempty.)
     *
     * <p>This implementation gets an iterator over the specified collection and
     * a list iterator over this list pointing to the indexed element (with
     * <tt>listIterator(index)</tt>).  Then, it iterates over the specified
     * collection, inserting the elements obtained from the iterator into this
     * list, one at a time, using <tt>ListIterator.add</tt> followed by
     * <tt>ListIterator.next</tt> (to skip over the added element).
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the list iterator returned by
     * the <tt>listIterator</tt> method does not implement the <tt>add</tt>
     * operation.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        try {
            boolean modified = false;
            ListIterator<E> e1 = listIterator(index);
            Iterator<? extends E> e2 = c.iterator();
            while (e2.hasNext()) {
                e1.add(e2.next());
                modified = true;
            }
            return modified;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }


    // Iterators

    /**
     * Returns an iterator over the elements in this list (in proper
     * sequence).<p>
     *
     * This implementation merely returns a list iterator over the list.
     *
     * @return an iterator over the elements in this list (in proper sequence)
     */
    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @param  index index of first element to be returned from the list
     *         iterator (by a call to the <code>next</code> method)
     * @return a list iterator over the elements in this list (in proper
     *         sequence)
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public abstract ListIterator<E> listIterator(int index);
}
