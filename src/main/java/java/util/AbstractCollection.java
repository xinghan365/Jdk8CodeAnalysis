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

package java.util;

/**
 * 该类提供了Collection接口的骨架实现，以尽量减少实现此接口所需的工作量。
 * 为了实现一个不可修改的集合，程序员只需要扩展这个类并提供iterator和size方法的实现。 （ iterator方法返回的迭代器必须实现hasNext和next ）
 *
 * 要实现可修改的集合，程序员必须另外覆盖此类的add方法（否则将抛出UnsupportedOperationException ），并且由iterator方法返回的迭代器必须另外实现其remove方法。
 *
 * 程序员通常应该提供一个空隙（无参数）和Collection构造，按照在Collection接口规范的建议。
 *
 * 该类中每个非抽象方法的文档详细描述了其实现。 如果正在实施的集合承认更有效的实现，则可以覆盖这些方法中的每一种。
 *
 * This class provides a skeletal implementation of the <tt>Collection</tt>
 * interface, to minimize the effort required to implement this interface. <p>
 *
 * To implement an unmodifiable collection, the programmer needs only to
 * extend this class and provide implementations for the <tt>iterator</tt> and
 * <tt>size</tt> methods.  (The iterator returned by the <tt>iterator</tt>
 * method must implement <tt>hasNext</tt> and <tt>next</tt>.)<p>
 *
 * To implement a modifiable collection, the programmer must additionally
 * override this class's <tt>add</tt> method (which otherwise throws an
 * <tt>UnsupportedOperationException</tt>), and the iterator returned by the
 * <tt>iterator</tt> method must additionally implement its <tt>remove</tt>
 * method.<p>
 *
 * The programmer should generally provide a void (no argument) and
 * <tt>Collection</tt> constructor, as per the recommendation in the
 * <tt>Collection</tt> interface specification.<p>
 *
 * The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if
 * the collection being implemented admits a more efficient implementation.<p>
 *
 * This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @since 1.2
 */

public abstract class AbstractCollection<E> implements Collection<E> {
    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractCollection() {
    }

    // Query Operations

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public abstract Iterator<E> iterator();

    @Override
    public abstract int size();

    /**
     * {@inheritDoc}
     *
     * <p>This implementation returns <tt>size() == 0</tt>.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation iterates over the elements in the collection,
     * checking each element in turn for equality with the specified element.
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * 返回一个包含此集合中所有元素的数组。 如果此集合对其迭代器返回的元素的顺序做出任何保证，则此方法必须以相同的顺序返回元素。
     * 返回的数组将是“安全的”，因为该集合不保留对它的引用。 （换句话说，这个方法必须分配一个新的数组，即使这个集合是由数组支持的）。 因此，调用者可以自由地修改返回的数组。
     *
     * 此方法充当基于阵列和基于集合的API之间的桥梁。
     *
     * 此实现返回一个数组，其中包含由该集合的迭代器返回的所有元素，以相同的顺序存储在数组的连续元素中，从索引0开始。 返回的数组的长度等于迭代器返回的元素数量，即使该迭代器的大小在迭代期间发生变化，如果该迭代允许在迭代期间同时进行修改，则可能会发生这种情况。 size方法仅被称为优化提示; 即使迭代器返回不同数量的元素，也会返回正确的结果。
     *
     * 此方法相当于：
     *
     *    List<E> list = new ArrayList<E>(size()); for (E e : this) list.add(e); return list.toArray();
     * {@inheritDoc}
     *
     * <p>This implementation returns an array containing all the elements
     * returned by this collection's iterator, in the same order, stored in
     * consecutive elements of the array, starting with index {@code 0}.
     * The length of the returned array is equal to the number of elements
     * returned by the iterator, even if the size of this collection changes
     * during iteration, as might happen if the collection permits
     * concurrent modification during iteration.  The {@code size} method is
     * called only as an optimization hint; the correct result is returned
     * even if the iterator returns a different number of elements.
     *
     * <p>This method is equivalent to:
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray();
     * }</pre>
     */
    @Override
    public Object[] toArray() {
        // Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
            {
                return Arrays.copyOf(r, i);
            }
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     *
     * 返回包含此集合中所有元素的数组; 返回的数组的运行时类型是指定数组的运行时类型。 如果集合适合指定的数组，则返回其中。 否则，将为指定数组的运行时类型和此集合的大小分配一个新数组。
     * 如果这个集合适合指定的数组，有空余的空间（即，该数组具有比该集合更多的元素），则集合结束后立即数组中的元素设置为null 。 （ 仅当调用者知道此集合不包含任何null元素时，这仅用于确定此集合的长度。）
     *
     * 如果此集合对其迭代器返回的元素的顺序做出任何保证，则此方法必须以相同的顺序返回元素。
     *
     * 像Collection.toArray()方法一样，此方法充当基于阵列和基于集合的API之间的桥梁。 此外，该方法允许精确地控制输出阵列的运行时类型，并且在某些情况下可以用于节省分配成本。
     *
     * 假设x是一个已知只包含字符串的集合。 下面的代码可用于收集转储到的String一个新分配的数组：
     *
     *   String[] y = x.toArray(new String[0]); 请注意， toArray(new Object[0])的功能与toArray()相同 。
     * 此实现返回一个数组，该数组包含该集合的迭代器返回的所有元素，以相同的顺序存储，以索引0开头存储在数组的连续元素中。 如果迭代器返回的元素数量太大而不能适应指定的数组，则元素将返回一个新分配的数组，其长度等于迭代器返回的元素数量，即使此集合的大小更改在迭代期间，如果集合允许在迭代期间同时进行修改，则可能会发生这种情况。 size方法只被称为优化提示; 即使迭代器返回不同数量的元素，也会返回正确的结果。
     *
     * 此方法相当于：
     *
     *    List<E> list = new ArrayList<E>(size()); for (E e : this) list.add(e); return list.toArray(a);
     *
     * {@inheritDoc}
     *
     * <p>This implementation returns an array containing all the elements
     * returned by this collection's iterator in the same order, stored in
     * consecutive elements of the array, starting with index {@code 0}.
     * If the number of elements returned by the iterator is too large to
     * fit into the specified array, then the elements are returned in a
     * newly allocated array with length equal to the number of elements
     * returned by the iterator, even if the size of this collection
     * changes during iteration, as might happen if the collection permits
     * concurrent modification during iteration.  The {@code size} method is
     * called only as an optimization hint; the correct result is returned
     * even if the iterator returns a different number of elements.
     *
     * <p>This method is equivalent to:
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray(a);
     * }</pre>
     *
     * @throws ArrayStoreException  {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        T[] r = a.length >= size ? a :
                  (T[])java.lang.reflect.Array
                  .newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();

        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) { // fewer elements than expected
                if (a == r) {
                    r[i] = null; // null-terminate
                } else if (a.length < i) {
                    return Arrays.copyOf(r, i);
                } else {
                    System.arraycopy(r, 0, a, 0, i);
                    if (a.length > i) {
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (T)it.next();
        }
        // more elements than expected
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Reallocates the array being used within toArray when the iterator
     * returned more elements than expected, and finishes filling it from
     * the iterator.
     *
     * @param r the array, replete with previously stored elements
     * @param it the in-progress iterator over this collection
     * @return array containing the elements in the given array, plus any
     *         further elements returned by the iterator, trimmed to size
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        int i = r.length;
        while (it.hasNext()) {
            int cap = r.length;
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0) {
                    newCap = hugeCapacity(cap + 1);
                }
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T)it.next();
        }
        // trim if overallocated
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
        {
            throw new OutOfMemoryError
                ("Required array size too large");
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    // Modification Operations

    /**
     * 确保此集合包含指定的元素（可选操作）。 如果此集合因呼叫而更改，则返回true 。 （如果此集合不允许重复且已包含指定的元素，则返回false ）。
     * 支持此操作的集合可能会限制可能添加到此集合的元素。 特别是一些集合将拒绝添加null元素，其他集合将对可能添加的元素的类型施加限制。 收集类应在其文档中明确说明可能添加哪些元素的限制。
     *
     * 如果一个集合拒绝添加一个特定的元素，除了它已经包含该元素之外，它必须引发异常（而不是返回false ）。 这保留了一个集合在此调用返回后始终包含指定元素的不变量。
     *
     * 这个实现总是抛出一个UnsupportedOperationException 。
     *
     * {@inheritDoc}
     *
     * <p>This implementation always throws an
     * <tt>UnsupportedOperationException</tt>.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IllegalStateException         {@inheritDoc}
     */
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * 从该集合中删除指定元素的单个实例（如果存在）（可选操作）。 更正式地，删除元素e ，使得(o==null ? e==null : o.equals(e)) ，如果该集合包含一个或多个这样的元素。 如果此集合包含指定的元素（或等效地，如果此集合由于调用而更改），则返回true 。
     * 该实现遍历集合，寻找指定的元素。 如果找到该元素，它将使用迭代器的remove方法从集合中删除该元素。
     *
     * 注意，此实现抛出UnsupportedOperationException如果迭代此collection的iterator方法返回没有实现remove方法，并且此collection包含指定的对象。
     * {@inheritDoc}
     *
     * <p>This implementation iterates over the collection looking for the
     * specified element.  If it finds the element, it removes the element
     * from the collection using the iterator's remove method.
     *
     * <p>Note that this implementation throws an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by this
     * collection's iterator method does not implement the <tt>remove</tt>
     * method and this collection contains the specified object.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }


    // Bulk Operations

    /**
     * 如果此集合包含指定集合中的所有元素，则返回true。
     * 这个实现遍历指定的集合，依次检查迭代器返回的每个元素，以查看它是否包含在该集合中。 如果所有元素如此包含true被退回，否则false 。
     * {@inheritDoc}
     *
     * <p>This implementation iterates over the specified collection,
     * checking each element returned by the iterator in turn to see
     * if it's contained in this collection.  If all elements are so
     * contained <tt>true</tt> is returned, otherwise <tt>false</tt>.
     *
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将指定集合中的所有元素添加到此集合（可选操作）。 如果在操作进行中修改了指定的集合，则此操作的行为是未定义的。 （这意味着如果指定的集合是此集合，此调用的行为是未定义的，并且此集合是非空的。）
     * 这个实现遍历指定的集合，并依次将迭代器返回的每个对象添加到该集合中。
     *
     * 请注意，除非add被覆盖（假定指定的集合不为空），否则此实现将抛出UnsupportedOperationException 。
     * {@inheritDoc}
     *
     * <p>This implementation iterates over the specified collection, and adds
     * each object returned by the iterator to this collection, in turn.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> unless <tt>add</tt> is
     * overridden (assuming the specified collection is non-empty).
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IllegalStateException         {@inheritDoc}
     *
     * @see #add(Object)
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 删除指定集合中包含的所有此集合的元素（可选操作）。 此调用返回后，此集合将不包含与指定集合相同的元素。
     * 这个实现遍历这个集合，依次检查迭代器返回的每个元素，看看它是否包含在指定的集合中。 如果它是如此包含，它将使用迭代器的remove方法从该集合中删除。
     *
     * 注意，此实现将抛出UnsupportedOperationException如果迭代由iterator方法返回没有实现remove方法，并且此集合包含一个或多个共同的元素与指定的集合。
     * {@inheritDoc}
     *
     * <p>This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's so contained, it's removed from
     * this collection with the iterator's <tt>remove</tt> method.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by the
     * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
     * and this collection contains one or more elements in common with the
     * specified collection.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 仅保留此集合中包含在指定集合中的元素（可选操作）。 换句话说，从该集合中删除所有不包含在指定集合中的元素。
     * 这个实现遍历这个集合，依次检查迭代器返回的每个元素，看看它是否包含在指定的集合中。 如果没有这样包含，它将使用迭代器的remove方法从该集合中删除。
     *
     * 注意，此实现将抛出UnsupportedOperationException如果迭代由iterator方法返回没有实现remove方法，并且此collection包含指定集合中不存在一个或多个元素。
     * {@inheritDoc}
     *
     * <p>This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's not so contained, it's removed
     * from this collection with the iterator's <tt>remove</tt> method.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by the
     * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
     * and this collection contains one or more elements not present in the
     * specified collection.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 从此集合中删除所有元素（可选操作）。 此方法返回后，集合将为空。
     * 此实现遍历此集合，使用Iterator.remove操作删除每个元素。 大多数实现可能会选择覆盖此方法的效率。
     *
     * 请注意，如果此集合的iterator方法返回的迭代器不实现remove方法，并且此集合不为空，则此实现将抛出UnsupportedOperationException 。
     * {@inheritDoc}
     *
     * <p>This implementation iterates over this collection, removing each
     * element using the <tt>Iterator.remove</tt> operation.  Most
     * implementations will probably choose to override this method for
     * efficiency.
     *
     * <p>Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by this
     * collection's <tt>iterator</tt> method does not implement the
     * <tt>remove</tt> method and this collection is non-empty.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    @Override
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    //  String conversion

    /**
     * Returns a string representation of this collection.  The string
     * representation consists of a list of the collection's elements in the
     * order they are returned by its iterator, enclosed in square brackets
     * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters
     * <tt>", "</tt> (comma and space).  Elements are converted to strings as
     * by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this collection
     */
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }

}
