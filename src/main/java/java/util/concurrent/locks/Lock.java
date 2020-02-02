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
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.locks;
import java.util.concurrent.TimeUnit;

/**
 * Lock实现提供比使用synchronized方法和语句可以获得的更广泛的锁定操作。 它们允许更灵活的结构化，可能具有完全不同的属性，并且可以支持多个相关联的对象Condition 。
 * 锁是用于通过多个线程控制对共享资源的访问的工具。 通常，锁提供对共享资源的独占访问：一次只能有一个线程可以获取锁，并且对共享资源的所有访问都要求首先获取锁。 但是，一些锁可能允许并发访问共享资源，如ReadWriteLock的读锁。
 *
 * 使用synchronized方法或语句提供对与每个对象相关联的隐式监视器锁的访问，但是强制所有锁获取和释放以块结构的方式发生：当获取多个锁时，它们必须以相反的顺序被释放，并且所有的锁都必须被释放在与它们相同的词汇范围内。
 *
 * 虽然synchronized方法和语句的范围机制使得使用监视器锁更容易编程，并且有助于避免涉及锁的许多常见编程错误，但是有时您需要以更灵活的方式处理锁。 例如，用于遍历并发访问的数据结构的一些算法需要使用“手动”或“链锁定”：您获取节点A的锁定，然后获取节点B，然后释放A并获取C，然后释放B并获得D等。 所述的实施方式中Lock接口通过允许获得并在不同的范围释放的锁，并允许获得并以任何顺序释放多个锁使得能够使用这样的技术。
 *
 * 随着这种增加的灵活性，额外的责任。 没有块结构化锁定会删除使用synchronized方法和语句发生的锁的自动释放。 在大多数情况下，应使用以下惯用语：
 *
 *    Lock l = ...; l.lock(); try { // access the resource protected by this lock } finally { l.unlock(); } 当在不同范围内发生锁定和解锁时，必须注意确保在锁定时执行的所有代码由try-finally或try-catch保护，以确保在必要时释放锁定。
 * Lock实现提供了使用synchronized方法和语句的附加功能，通过提供非阻塞尝试来获取锁（ tryLock() ），尝试获取可被中断的锁（ lockInterruptibly()） ，以及尝试获取可以超时（ tryLock(long, TimeUnit) ）。
 *
 * 一个Lock类还可以提供与隐式监视锁定的行为和语义完全不同的行为和语义，例如保证排序，非重入使用或死锁检测。 如果一个实现提供了这样的专门的语义，那么实现必须记录这些语义。
 *
 * 请注意， Lock实例只是普通对象，它们本身可以用作synchronized语句中的目标。 获取Lock实例的监视器锁与调用该实例的任何lock()方法没有特定关系。 建议为避免混淆，您不要以这种方式使用Lock实例，除了在自己的实现中。
 *
 * 除非另有说明，传递任何参数的null值将导致NullPointerException被抛出。
 *
 * 内存同步
 * 所有Lock实施必须执行与内置监视器锁相同的内存同步语义，如The Java Language Specification (17.4 Memory Model) 所述 ：
 *
 * 成功的lock操作具有与成功锁定动作相同的内存同步效果。
 * 成功的unlock操作具有与成功解锁动作相同的内存同步效果。
 * 不成功的锁定和解锁操作以及重入锁定/解锁操作，不需要任何内存同步效果。
 * 实施注意事项
 * 锁定采集（可中断，不可中断和定时）的三种形式在性能特征，排序保证或其他实施质量方面可能不同。 此外，在给定的Lock课程中，中断正在获取锁的能力可能不可用。 因此，不需要实现对所有三种形式的锁获取完全相同的保证或语义，也不需要支持正在进行的锁获取的中断。 需要一个实现来清楚地记录每个锁定方法提供的语义和保证。 它还必须遵守此接口中定义的中断语义，只要支持锁获取的中断，即全部或仅在方法输入。
 *
 * 由于中断通常意味着取消，并且检查中断通常是不频繁的，所以实现可以有利于通过正常方法返回来响应中断。 即使可以显示中断发生在另一个动作可能已经解除了线程之后，这是真的。 一个实现应该记录这个行为。
 *
 * {@code Lock} implementations provide more extensive locking
 * operations than can be obtained using {@code synchronized} methods
 * and statements.  They allow more flexible structuring, may have
 * quite different properties, and may support multiple associated
 * {@link Condition} objects.
 *
 * <p>A lock is a tool for controlling access to a shared resource by
 * multiple threads. Commonly, a lock provides exclusive access to a
 * shared resource: only one thread at a time can acquire the lock and
 * all access to the shared resource requires that the lock be
 * acquired first. However, some locks may allow concurrent access to
 * a shared resource, such as the read lock of a {@link ReadWriteLock}.
 *
 * <p>The use of {@code synchronized} methods or statements provides
 * access to the implicit monitor lock associated with every object, but
 * forces all lock acquisition and release to occur in a block-structured way:
 * when multiple locks are acquired they must be released in the opposite
 * order, and all locks must be released in the same lexical scope in which
 * they were acquired.
 *
 * <p>While the scoping mechanism for {@code synchronized} methods
 * and statements makes it much easier to program with monitor locks,
 * and helps avoid many common programming errors involving locks,
 * there are occasions where you need to work with locks in a more
 * flexible way. For example, some algorithms for traversing
 * concurrently accessed data structures require the use of
 * &quot;hand-over-hand&quot; or &quot;chain locking&quot;: you
 * acquire the lock of node A, then node B, then release A and acquire
 * C, then release B and acquire D and so on.  Implementations of the
 * {@code Lock} interface enable the use of such techniques by
 * allowing a lock to be acquired and released in different scopes,
 * and allowing multiple locks to be acquired and released in any
 * order.
 *
 * <p>With this increased flexibility comes additional
 * responsibility. The absence of block-structured locking removes the
 * automatic release of locks that occurs with {@code synchronized}
 * methods and statements. In most cases, the following idiom
 * should be used:
 *
 *  <pre> {@code
 * Lock l = ...;
 * l.lock();
 * try {
 *   // access the resource protected by this lock
 * } finally {
 *   l.unlock();
 * }}</pre>
 *
 * When locking and unlocking occur in different scopes, care must be
 * taken to ensure that all code that is executed while the lock is
 * held is protected by try-finally or try-catch to ensure that the
 * lock is released when necessary.
 *
 * <p>{@code Lock} implementations provide additional functionality
 * over the use of {@code synchronized} methods and statements by
 * providing a non-blocking attempt to acquire a lock ({@link
 * #tryLock()}), an attempt to acquire the lock that can be
 * interrupted ({@link #lockInterruptibly}, and an attempt to acquire
 * the lock that can timeout ({@link #tryLock(long, TimeUnit)}).
 *
 * <p>A {@code Lock} class can also provide behavior and semantics
 * that is quite different from that of the implicit monitor lock,
 * such as guaranteed ordering, non-reentrant usage, or deadlock
 * detection. If an implementation provides such specialized semantics
 * then the implementation must document those semantics.
 *
 * <p>Note that {@code Lock} instances are just normal objects and can
 * themselves be used as the target in a {@code synchronized} statement.
 * Acquiring the
 * monitor lock of a {@code Lock} instance has no specified relationship
 * with invoking any of the {@link #lock} methods of that instance.
 * It is recommended that to avoid confusion you never use {@code Lock}
 * instances in this way, except within their own implementation.
 *
 * <p>Except where noted, passing a {@code null} value for any
 * parameter will result in a {@link NullPointerException} being
 * thrown.
 *
 * <h3>Memory Synchronization</h3>
 *
 * <p>All {@code Lock} implementations <em>must</em> enforce the same
 * memory synchronization semantics as provided by the built-in monitor
 * lock, as described in
 * <a href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.4">
 * The Java Language Specification (17.4 Memory Model)</a>:
 * <ul>
 * <li>A successful {@code lock} operation has the same memory
 * synchronization effects as a successful <em>Lock</em> action.
 * <li>A successful {@code unlock} operation has the same
 * memory synchronization effects as a successful <em>Unlock</em> action.
 * </ul>
 *
 * Unsuccessful locking and unlocking operations, and reentrant
 * locking/unlocking operations, do not require any memory
 * synchronization effects.
 *
 * <h3>Implementation Considerations</h3>
 *
 * <p>The three forms of lock acquisition (interruptible,
 * non-interruptible, and timed) may differ in their performance
 * characteristics, ordering guarantees, or other implementation
 * qualities.  Further, the ability to interrupt the <em>ongoing</em>
 * acquisition of a lock may not be available in a given {@code Lock}
 * class.  Consequently, an implementation is not required to define
 * exactly the same guarantees or semantics for all three forms of
 * lock acquisition, nor is it required to support interruption of an
 * ongoing lock acquisition.  An implementation is required to clearly
 * document the semantics and guarantees provided by each of the
 * locking methods. It must also obey the interruption semantics as
 * defined in this interface, to the extent that interruption of lock
 * acquisition is supported: which is either totally, or only on
 * method entry.
 *
 * <p>As interruption generally implies cancellation, and checks for
 * interruption are often infrequent, an implementation can favor responding
 * to an interrupt over normal method return. This is true even if it can be
 * shown that the interrupt occurred after another action may have unblocked
 * the thread. An implementation should document this behavior.
 *
 * @see ReentrantLock
 * @see Condition
 * @see ReadWriteLock
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface Lock {

    /**
     * 获得锁。
     * 如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到获取锁。
     * 实施注意事项
     *
     * Lock实现可能能够检测锁的错误使用，例如将导致死锁的调用，并且可能在这种情况下抛出（未检查）异常。
     * 情况和异常类型必须由Lock实现记录。
     * Acquires the lock.
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>A {@code Lock} implementation may be able to detect erroneous use
     * of the lock, such as an invocation that would cause deadlock, and
     * may throw an (unchecked) exception in such circumstances.  The
     * circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     */
    void lock();

    /**
     * 获取锁定，除非当前线程是interrupted 。
     * 获取锁，如果可用并立即返回。
     *
     * 如果锁不可用，那么当前线程将被禁用以进行线程调度，并且处于休眠状态，直到发生两件事情之一：
     *
     * 锁是由当前线程获取的; 要么
     * 一些其他线程interrupts当前线程，并且支持中断锁获取。
     * 如果当前线程：
     *
     * 在进入该方法时设置了中断状态; 要么
     * 是interrupted ，同时获取锁，并支持锁中断，
     * 然后InterruptedException被关上，当前线程的中断状态被清除。
     * 实施注意事项
     *
     * 在某些实现中中断锁获取的能力可能是不可能的，如果可能的话可能是昂贵的操作。 程序员应该知道，可能是这种情况。 在这种情况下，实施应该记录。
     *
     * 一个实现可以有利于通过正常的方法返回来响应中断。
     *
     * Lock实现可能能够检测到锁的错误使用，例如将导致死锁的调用，并且可能在这种情况下抛出（未检查）异常。 情况和异常类型必须由Lock实现记录。
     *
     * 异常
     * InterruptedException - 如果当前线程在获取锁定期间中断（并且支持锁定获取的中断）
     *
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     *
     * <p>Acquires the lock if it is available and returns immediately.
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
     * one of two things happens:
     *
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported.
     * </ul>
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring the
     * lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The ability to interrupt a lock acquisition in some
     * implementations may not be possible, and if possible may be an
     * expensive operation.  The programmer should be aware that this
     * may be the case. An implementation should document when this is
     * the case.
     *
     * <p>An implementation can favor responding to an interrupt over
     * normal method return.
     *
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would
     * cause deadlock, and may throw an (unchecked) exception in such
     * circumstances.  The circumstances and the exception type must
     * be documented by that {@code Lock} implementation.
     *
     * @throws InterruptedException if the current thread is
     *         interrupted while acquiring the lock (and interruption
     *         of lock acquisition is supported)
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * 只有在调用时才可以获得锁。
     * 如果可用，则获取锁定，并立即返回值为true 。 如果锁不可用，则此方法将立即返回值为false 。
     *
     * 此方法的典型用法是：
     *
     *    Lock lock = ...; if (lock.tryLock()) { try { // manipulate protected state } finally { lock.unlock(); } } else { // perform alternative actions } 此用法可确保锁定已被取消，如果未获取锁定，则不会尝试解锁。
     * 结果
     * true如果锁被收购， false false
     *
     * Acquires the lock only if it is free at the time of invocation.
     *
     * <p>Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
     *
     * <p>A typical usage idiom for this method would be:
     *  <pre> {@code
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     *
     * This usage ensures that the lock is unlocked if it was acquired, and
     * doesn't try to unlock if the lock was not acquired.
     *
     * @return {@code true} if the lock was acquired and
     *         {@code false} otherwise
     */
    boolean tryLock();

    /**
     * 如果在给定的等待时间内是空闲的，并且当前的线程还没有被解锁，那么获取锁定的时间是interrupted 。
     * 如果锁可用，此方法将立即返回值为true 。 如果锁不可用，则当前线程将被禁用以进行线程调度，并且处于休眠状态，直至发生三件事情之一：
     *
     * 锁是由当前线程获取的; 要么
     * 一些其他线程interrupts当前线程，并且中断锁获取被支持; 要么
     * 指定的等待时间过去了
     * 如果锁获取，则返回值true 。
     *
     * 如果当前线程：
     *
     * 在进入该方法时设置了中断状态; 要么
     * 是interrupted ，同时获取锁，并支持锁中断，
     * 然后InterruptedException被关上，当前线程的中断状态被清除。
     * 如果指定的等待时间过去，则返回值false 。 如果时间小于或等于零，该方法根本不会等待。
     *
     * 实施注意事项
     *
     * 在某些实现中中断锁获取的能力可能是不可能的，如果可能的话可能是昂贵的操作。 程序员应该知道，可能是这种情况。 在这种情况下，实施应该记录。
     *
     * 实现可以有助于通过正常的方法返回来响应中断或报告超时。
     *
     * Lock实现可能能够检测到锁的错误使用，例如将导致死锁的调用，并且可能在这种情况下抛出（未检查）异常。 情况和异常类型必须由Lock实现记录。
     *
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     *
     * <p>If the lock is available this method returns immediately
     * with the value {@code true}.
     * If the lock is not available then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported; or
     * <li>The specified waiting time elapses
     * </ul>
     *
     * <p>If the lock is acquired then the value {@code true} is returned.
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
     * the lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.
     * If the time is
     * less than or equal to zero, the method will not wait at all.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The ability to interrupt a lock acquisition in some implementations
     * may not be possible, and if possible may
     * be an expensive operation.
     * The programmer should be aware that this may be the case. An
     * implementation should document when this is the case.
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return, or reporting a timeout.
     *
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would cause
     * deadlock, and may throw an (unchecked) exception in such circumstances.
     * The circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return {@code true} if the lock was acquired and {@code false}
     *         if the waiting time elapsed before the lock was acquired
     *
     * @throws InterruptedException if the current thread is interrupted
     *         while acquiring the lock (and interruption of lock
     *         acquisition is supported)
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     *
     * 释放锁。
     * 实施注意事项
     *
     * 一个Lock实现通常会限制哪个线程可以释放锁（通常只有锁的持有者可以释放它），并且如果限制被违反则可能会引发（未检查）异常。 任何限制和异常类型必须由Lock实现记录。
     * Releases the lock.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>A {@code Lock} implementation will usually impose
     * restrictions on which thread can release a lock (typically only the
     * holder of the lock can release it) and may throw
     * an (unchecked) exception if the restriction is violated.
     * Any restrictions and the exception
     * type must be documented by that {@code Lock} implementation.
     */
    void unlock();

    /**
     * 返回一个新Condition绑定到该实例Lock实例。
     * 在等待条件之前，锁必须由当前线程保持。 呼叫Condition.await()将在等待之前将原子释放锁，并在等待返回之前重新获取锁。
     *
     * 实施注意事项
     *
     * Condition实例的确切操作取决于Lock实现，必须由该实现记录。
     * Returns a new {@link Condition} instance that is bound to this
     * {@code Lock} instance.
     *
     * <p>Before waiting on the condition the lock must be held by the
     * current thread.
     * A call to {@link Condition#await()} will atomically release the lock
     * before waiting and re-acquire the lock before the wait returns.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>The exact operation of the {@link Condition} instance depends on
     * the {@code Lock} implementation and must be documented by that
     * implementation.
     *
     * @return A new {@link Condition} instance for this {@code Lock} instance
     * @throws UnsupportedOperationException if this {@code Lock}
     *         implementation does not support conditions
     */
    Condition newCondition();
}
