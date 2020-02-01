/*
 * Copyright (c) 1997, 2003, Oracle and/or its affiliates. All rights reserved.
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

package java.lang.ref;


/**
 *
 * 软参考对象，由垃圾收集器根据内存需求自行清除。 软引用通常用于实现内存敏感缓存。
 *
 * 假设垃圾收集器在某个时间点确定对象是softly reachable 。
 * 那时候，它可能会选择原子地清除对该对象的所有软引用，以及对任何其他可轻松访问的对象的所有软引用，
 * 该对象可以通过一个强引用链来访问该对象。 在同一时间或稍后的时间，它将排入在引用队列中注册的新清除的软引用。
 *
 * 在虚拟机抛出OutOfMemoryError之前，所有软引用对象可以保证被清除。
 * 否则，在清除软引用的时间或者对一组对不同对象的引用将被清除的顺序没有约束。
 * 但是，鼓励虚拟机实现偏离清除最近创建或最近使用的软参考。
 *
 * 此类的直接实例可用于实现简单的缓存; 此类或派生子类也可用于较大的数据结构以实现更复杂的高速缓存。
 * 只要软参考的指示是强有力的，即实际使用中，软参考将不会被清除。
 * 因此，复杂的缓存可以例如阻止其最近使用的条目被丢弃，通过保持对这些条目的强烈的指示，使剩余的条目由垃圾收集器判断丢弃。
 *
 * Soft reference objects, which are cleared at the discretion of the garbage
 * collector in response to memory demand.  Soft references are most often used
 * to implement memory-sensitive caches.
 *
 * <p> Suppose that the garbage collector determines at a certain point in time
 * that an object is <a href="package-summary.html#reachability">softly
 * reachable</a>.  At that time it may choose to clear atomically all soft
 * references to that object and all soft references to any other
 * softly-reachable objects from which that object is reachable through a chain
 * of strong references.  At the same time or at some later time it will
 * enqueue those newly-cleared soft references that are registered with
 * reference queues.
 *
 * <p> All soft references to softly-reachable objects are guaranteed to have
 * been cleared before the virtual machine throws an
 * <code>OutOfMemoryError</code>.  Otherwise no constraints are placed upon the
 * time at which a soft reference will be cleared or the order in which a set
 * of such references to different objects will be cleared.  Virtual machine
 * implementations are, however, encouraged to bias against clearing
 * recently-created or recently-used soft references.
 *
 * <p> Direct instances of this class may be used to implement simple caches;
 * this class or derived subclasses may also be used in larger data structures
 * to implement more sophisticated caches.  As long as the referent of a soft
 * reference is strongly reachable, that is, is actually in use, the soft
 * reference will not be cleared.  Thus a sophisticated cache can, for example,
 * prevent its most recently used entries from being discarded by keeping
 * strong referents to those entries, leaving the remaining entries to be
 * discarded at the discretion of the garbage collector.
 *
 * @author   Mark Reinhold
 * @since    1.2
 */

public class SoftReference<T> extends Reference<T> {

    /**
     * Timestamp clock, updated by the garbage collector
     */
    static private long clock;

    /**
     * Timestamp updated by each invocation of the get method.  The VM may use
     * this field when selecting soft references to be cleared, but it is not
     * required to do so.
     */
    private long timestamp;

    /**
     * Creates a new soft reference that refers to the given object.  The new
     * reference is not registered with any queue.
     *
     * @param referent object the new soft reference will refer to
     */
    public SoftReference(T referent) {
        super(referent);
        this.timestamp = clock;
    }

    /**
     * Creates a new soft reference that refers to the given object and is
     * registered with the given queue.
     *
     * @param referent object the new soft reference will refer to
     * @param q the queue with which the reference is to be registered,
     *          or <tt>null</tt> if registration is not required
     *
     */
    public SoftReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
        this.timestamp = clock;
    }

    /**
     * Returns this reference object's referent.  If this reference object has
     * been cleared, either by the program or by the garbage collector, then
     * this method returns <code>null</code>.
     *
     * @return   The object to which this reference refers, or
     *           <code>null</code> if this reference object has been cleared
     */
    @Override
    public T get() {
        T o = super.get();
        if (o != null && this.timestamp != clock) {
            this.timestamp = clock;
        }
        return o;
    }

}
