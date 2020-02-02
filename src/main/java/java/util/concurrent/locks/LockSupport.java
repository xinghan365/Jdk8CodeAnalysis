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
import sun.misc.Unsafe;

/**
 * 用于创建锁和其他同步类的基本线程阻塞原语。
 * 这个类与每个使用它的线程相关联，一个许可证（在Semaphore类的意义上）。 如果许可证可用，则呼叫park将park返回，在此过程中消耗它; 否则可能会阻止。 致电unpark使许可证可用，如果尚不可用。 （与信号量不同，许可证不能累积，最多只有一个。）
 *
 * 方法park和unpark提供了阻止和解除阻塞线程的有效手段，该方法不会遇到导致不推荐使用的方法Thread.suspend和Thread.resume目的不能使用的问题：一个线程调用park和另一个线程之间的尝试unpark线程将保持活跃性，由于许可证。 另外，如果调用者的线程被中断， park将返回，并且支持超时版本。 park方法也可以在任何其他时间返回，因为“无理由”，因此一般必须在返回之前重新检查条件的循环中被调用。 在这个意义上， park作为一个“忙碌等待”的优化，不浪费时间旋转，但必须与unpark配对才能有效。
 *
 * park的三种形式也支持blocker对象参数。 线程被阻止时记录此对象，以允许监视和诊断工具识别线程被阻止的原因。 （此类工具可以使用方法getBlocker(Thread)访问阻止程序 。）强烈鼓励使用这些形式而不是没有此参数的原始形式。 在锁实现中作为blocker提供的正常参数是this 。
 *
 * 这些方法被设计为用作创建更高级同步实用程序的工具，并且本身对于大多数并发控制应用程序本身并不有用。 park方法仅用于形式的构造：
 *
 *    while (!canProceed()) { ... LockSupport.park(this); } 其中既不canProceed也没有任何其他动作之前的呼叫park需要锁定或阻止。 因为只有一个许可证与每个线程相关联， park任何中介使用可能会干扰其预期效果。
 * 样品用法。 这是一个先入先出的非可重入锁类的草图：
 *
 *    class FIFOMutex { private final AtomicBoolean locked = new AtomicBoolean(false); private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>(); public void lock() { boolean wasInterrupted = false; Thread current = Thread.currentThread(); waiters.add(current); // Block while not first in queue or cannot acquire lock while (waiters.peek() != current || !locked.compareAndSet(false, true)) { LockSupport.park(this); if (Thread.interrupted()) // ignore interrupts while waiting wasInterrupted = true; } waiters.remove(); if (wasInterrupted) // reassert interrupt status on exit current.interrupt(); } public void unlock() { locked.set(false); LockSupport.unpark(waiters.peek()); } }
 *
 * Basic thread blocking primitives for creating locks and other
 * synchronization classes.
 *
 * <p>This class associates, with each thread that uses it, a permit
 * (in the sense of the {@link java.util.concurrent.Semaphore
 * Semaphore} class). A call to {@code park} will return immediately
 * if the permit is available, consuming it in the process; otherwise
 * it <em>may</em> block.  A call to {@code unpark} makes the permit
 * available, if it was not already available. (Unlike with Semaphores
 * though, permits do not accumulate. There is at most one.)
 *
 * <p>Methods {@code park} and {@code unpark} provide efficient
 * means of blocking and unblocking threads that do not encounter the
 * problems that cause the deprecated methods {@code Thread.suspend}
 * and {@code Thread.resume} to be unusable for such purposes: Races
 * between one thread invoking {@code park} and another thread trying
 * to {@code unpark} it will preserve liveness, due to the
 * permit. Additionally, {@code park} will return if the caller's
 * thread was interrupted, and timeout versions are supported. The
 * {@code park} method may also return at any other time, for "no
 * reason", so in general must be invoked within a loop that rechecks
 * conditions upon return. In this sense {@code park} serves as an
 * optimization of a "busy wait" that does not waste as much time
 * spinning, but must be paired with an {@code unpark} to be
 * effective.
 *
 * <p>The three forms of {@code park} each also support a
 * {@code blocker} object parameter. This object is recorded while
 * the thread is blocked to permit monitoring and diagnostic tools to
 * identify the reasons that threads are blocked. (Such tools may
 * access blockers using method {@link #getBlocker(Thread)}.)
 * The use of these forms rather than the original forms without this
 * parameter is strongly encouraged. The normal argument to supply as
 * a {@code blocker} within a lock implementation is {@code this}.
 *
 * <p>These methods are designed to be used as tools for creating
 * higher-level synchronization utilities, and are not in themselves
 * useful for most concurrency control applications.  The {@code park}
 * method is designed for use only in constructions of the form:
 *
 *  <pre> {@code
 * while (!canProceed()) { ... LockSupport.park(this); }}</pre>
 *
 * where neither {@code canProceed} nor any other actions prior to the
 * call to {@code park} entail locking or blocking.  Because only one
 * permit is associated with each thread, any intermediary uses of
 * {@code park} could interfere with its intended effects.
 *
 * <p><b>Sample Usage.</b> Here is a sketch of a first-in-first-out
 * non-reentrant lock class:
 *  <pre> {@code
 * class FIFOMutex {
 *   private final AtomicBoolean locked = new AtomicBoolean(false);
 *   private final Queue<Thread> waiters
 *     = new ConcurrentLinkedQueue<Thread>();
 *
 *   public void lock() {
 *     boolean wasInterrupted = false;
 *     Thread current = Thread.currentThread();
 *     waiters.add(current);
 *
 *     // Block while not first in queue or cannot acquire lock
 *     while (waiters.peek() != current ||
 *            !locked.compareAndSet(false, true)) {
 *       LockSupport.park(this);
 *       if (Thread.interrupted()) // ignore interrupts while waiting
 *         wasInterrupted = true;
 *     }
 *
 *     waiters.remove();
 *     if (wasInterrupted)          // reassert interrupt status on exit
 *       current.interrupt();
 *   }
 *
 *   public void unlock() {
 *     locked.set(false);
 *     LockSupport.unpark(waiters.peek());
 *   }
 * }}</pre>
 */
public class LockSupport {
    private LockSupport() {} // Cannot be instantiated.

    private static void setBlocker(Thread t, Object arg) {
        // Even though volatile, hotspot doesn't need a write barrier here.
        UNSAFE.putObject(t, parkBlockerOffset, arg);
    }

    /**
     * 为给定的线程提供许可证（如果尚未提供）。 如果线程在park被阻塞，那么它将被解除阻塞。 否则，其下一次拨打park保证不被阻止。 如果给定的线程尚未启动，则此操作无法保证完全没有任何影响。
     * Makes available the permit for the given thread, if it
     * was not already available.  If the thread was blocked on
     * {@code park} then it will unblock.  Otherwise, its next call
     * to {@code park} is guaranteed not to block. This operation
     * is not guaranteed to have any effect at all if the given
     * thread has not been started.
     *
     * @param thread the thread to unpark, or {@code null}, in which case
     *        this operation has no effect
     */
    public static void unpark(Thread thread) {
        if (thread != null) {
            UNSAFE.unpark(thread);
        }
    }

    /**
     * 禁止当前线程进行线程调度，除非许可证可用。
     * 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态，直至发生三件事情之一：
     *
     * 一些其他线程调用当前线程作为目标的unpark ; 要么
     * 一些其他线程当前线程interrupts ; 要么
     * 电话虚假（也就是说，没有理由）返回。
     * 这种方法不报告是哪个线程导致该方法返回。 来电者应重新检查导致线程首先停放的条件。 呼叫者还可以确定线程在返回时的中断状态。
     *
     * Disables the current thread for thread scheduling purposes unless the
     * permit is available.
     *
     * <p>If the permit is available then it is consumed and the call returns
     * immediately; otherwise
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     *
     * <ul>
     * <li>Some other thread invokes {@link #unpark unpark} with the
     * current thread as the target; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The call spuriously (that is, for no reason) returns.
     * </ul>
     *
     * <p>This method does <em>not</em> report which of these caused the
     * method to return. Callers should re-check the conditions which caused
     * the thread to park in the first place. Callers may also determine,
     * for example, the interrupt status of the thread upon return.
     *
     * @param blocker the synchronization object responsible for this
     *        thread parking
     * @since 1.6
     */
    public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }

    /**
     * 禁用当前线程进行线程调度，直到指定的等待时间，除非许可证可用。
     * 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态，直到发生四件事情之一：
     *
     * 其他一些线程调用当前线程作为目标的unpark ; 要么
     * 其他线程interrupts当前线程; 要么
     * 指定的等待时间过去了; 要么
     * 电话虚假（也就是说，没有理由）返回。
     * 这种方法不报告是哪个线程导致该方法返回。 来电者应重新检查导致线程首先停放的条件。 呼叫者还可以确定线程的中断状态，或者返回时经过的时间。
     *
     * Disables the current thread for thread scheduling purposes, for up to
     * the specified waiting time, unless the permit is available.
     *
     * <p>If the permit is available then it is consumed and the call
     * returns immediately; otherwise the current thread becomes disabled
     * for thread scheduling purposes and lies dormant until one of four
     * things happens:
     *
     * <ul>
     * <li>Some other thread invokes {@link #unpark unpark} with the
     * current thread as the target; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The specified waiting time elapses; or
     *
     * <li>The call spuriously (that is, for no reason) returns.
     * </ul>
     *
     * <p>This method does <em>not</em> report which of these caused the
     * method to return. Callers should re-check the conditions which caused
     * the thread to park in the first place. Callers may also determine,
     * for example, the interrupt status of the thread, or the elapsed time
     * upon return.
     *
     * @param blocker the synchronization object responsible for this
     *        thread parking
     * @param nanos the maximum number of nanoseconds to wait
     * @since 1.6
     */
    public static void parkNanos(Object blocker, long nanos) {
        if (nanos > 0) {
            Thread t = Thread.currentThread();
            setBlocker(t, blocker);
            UNSAFE.park(false, nanos);
            setBlocker(t, null);
        }
    }

    /**
     * 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态，直到发生四件事情之一：
     *
     * 一些其他线程调用当前线程作为目标的unpark ; 要么
     * 一些其他线程当前线程interrupts ; 要么
     * 指定期限通过; 要么
     * 电话虚假（也就是说，没有理由）返回。
     * 这种方法不报告是哪个线程导致该方法返回。 来电者应重新检查导致线程首先停放的条件。 呼叫者还可以确定线程的中断状态，或返回当前的时间。
     *
     * Disables the current thread for thread scheduling purposes, until
     * the specified deadline, unless the permit is available.
     *
     * <p>If the permit is available then it is consumed and the call
     * returns immediately; otherwise the current thread becomes disabled
     * for thread scheduling purposes and lies dormant until one of four
     * things happens:
     *
     * <ul>
     * <li>Some other thread invokes {@link #unpark unpark} with the
     * current thread as the target; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread; or
     *
     * <li>The specified deadline passes; or
     *
     * <li>The call spuriously (that is, for no reason) returns.
     * </ul>
     *
     * <p>This method does <em>not</em> report which of these caused the
     * method to return. Callers should re-check the conditions which caused
     * the thread to park in the first place. Callers may also determine,
     * for example, the interrupt status of the thread, or the current time
     * upon return.
     *
     * @param blocker the synchronization object responsible for this
     *        thread parking
     * @param deadline the absolute time, in milliseconds from the Epoch,
     *        to wait until
     * @since 1.6
     */
    public static void parkUntil(Object blocker, long deadline) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(true, deadline);
        setBlocker(t, null);
    }

    /**
     * 返回提供给最近调用尚未解除阻塞的park方法的阻止程序对象，如果不阻止则返回null。 返回的值只是一个瞬间的快照 - 线程可能已经被阻止或阻止在不同的阻止对象上。
     * Returns the blocker object supplied to the most recent
     * invocation of a park method that has not yet unblocked, or null
     * if not blocked.  The value returned is just a momentary
     * snapshot -- the thread may have since unblocked or blocked on a
     * different blocker object.
     *
     * @param t the thread
     * @return the blocker
     * @throws NullPointerException if argument is null
     * @since 1.6
     */
    public static Object getBlocker(Thread t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return UNSAFE.getObjectVolatile(t, parkBlockerOffset);
    }

    /**
     * 禁止当前线程进行线程调度，除非许可证可用。
     * 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态，直至发生三件事情之一：
     *
     * 一些其他线程调用当前线程作为目标的unpark ; 要么
     * 其他一些线程当前线程为interrupts ; 要么
     * 电话虚假（也就是说，没有理由）返回。
     * 这种方法不报告是哪个线程导致该方法返回。 来电者应重新检查导致线程首先停放的条件。 呼叫者还可以确定线程在返回时的中断状态。
     *
     * Disables the current thread for thread scheduling purposes unless the
     * permit is available.
     *
     * <p>If the permit is available then it is consumed and the call
     * returns immediately; otherwise the current thread becomes disabled
     * for thread scheduling purposes and lies dormant until one of three
     * things happens:
     *
     * <ul>
     *
     * <li>Some other thread invokes {@link #unpark unpark} with the
     * current thread as the target; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The call spuriously (that is, for no reason) returns.
     * </ul>
     *
     * <p>This method does <em>not</em> report which of these caused the
     * method to return. Callers should re-check the conditions which caused
     * the thread to park in the first place. Callers may also determine,
     * for example, the interrupt status of the thread upon return.
     */
    public static void park() {
        UNSAFE.park(false, 0L);
    }

    /**
     * 禁用当前线程进行线程调度，直到指定的等待时间，除非许可证可用。
     * 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态，直到发生四件事情之一：
     *
     * 一些其他线程调用当前线程作为目标的unpark ; 要么
     * 一些其他线程当前线程interrupts ; 要么
     * 指定的等待时间过去了; 要么
     * 电话虚假（也就是说，没有理由）返回。
     * 这种方法不报告是哪个线程导致该方法返回。 来电者应重新检查导致线程首先停放的条件。 呼叫者还可以确定线程的中断状态，或者返回时经过的时间。
     *
     * Disables the current thread for thread scheduling purposes, for up to
     * the specified waiting time, unless the permit is available.
     *
     * <p>If the permit is available then it is consumed and the call
     * returns immediately; otherwise the current thread becomes disabled
     * for thread scheduling purposes and lies dormant until one of four
     * things happens:
     *
     * <ul>
     * <li>Some other thread invokes {@link #unpark unpark} with the
     * current thread as the target; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The specified waiting time elapses; or
     *
     * <li>The call spuriously (that is, for no reason) returns.
     * </ul>
     *
     * <p>This method does <em>not</em> report which of these caused the
     * method to return. Callers should re-check the conditions which caused
     * the thread to park in the first place. Callers may also determine,
     * for example, the interrupt status of the thread, or the elapsed time
     * upon return.
     *
     * @param nanos the maximum number of nanoseconds to wait
     */
    public static void parkNanos(long nanos) {
        if (nanos > 0) {
            UNSAFE.park(false, nanos);
        }
    }

    /**
     * 禁用当前线程进行线程调度，直到指定的截止日期，除非许可证可用。
     * 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态，直到发生四件事情之一：
     *
     * 一些其他线程调用当前线程作为目标的unpark ; 要么
     * 其他一些线程当前线程为interrupts ; 要么
     * 指定期限通过; 要么
     * 电话虚假（也就是说，没有理由）返回。
     * 这种方法不报告是哪个线程导致该方法返回。 来电者应重新检查导致线程首先停放的条件。 呼叫者还可以确定线程的中断状态，或返回当前的时间。
     *
     * Disables the current thread for thread scheduling purposes, until
     * the specified deadline, unless the permit is available.
     *
     * <p>If the permit is available then it is consumed and the call
     * returns immediately; otherwise the current thread becomes disabled
     * for thread scheduling purposes and lies dormant until one of four
     * things happens:
     *
     * <ul>
     * <li>Some other thread invokes {@link #unpark unpark} with the
     * current thread as the target; or
     *
     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The specified deadline passes; or
     *
     * <li>The call spuriously (that is, for no reason) returns.
     * </ul>
     *
     * <p>This method does <em>not</em> report which of these caused the
     * method to return. Callers should re-check the conditions which caused
     * the thread to park in the first place. Callers may also determine,
     * for example, the interrupt status of the thread, or the current time
     * upon return.
     *
     * @param deadline the absolute time, in milliseconds from the Epoch,
     *        to wait until
     */
    public static void parkUntil(long deadline) {
        UNSAFE.park(true, deadline);
    }

    /**
     * Returns the pseudo-randomly initialized or updated secondary seed.
     * Copied from ThreadLocalRandom due to package access restrictions.
     */
    static final int nextSecondarySeed() {
        int r;
        Thread t = Thread.currentThread();
        if ((r = UNSAFE.getInt(t, SECONDARY)) != 0) {
            r ^= r << 13;   // xorshift
            r ^= r >>> 17;
            r ^= r << 5;
        }
        else if ((r = java.util.concurrent.ThreadLocalRandom.current().nextInt()) == 0) {
            r = 1; // avoid zero
        }
        UNSAFE.putInt(t, SECONDARY, r);
        return r;
    }

    // Hotspot implementation via intrinsics API
    private static final sun.misc.Unsafe UNSAFE;
    private static final long parkBlockerOffset;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> tk = Thread.class;
            parkBlockerOffset = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("parkBlocker"));
            SEED = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("threadLocalRandomSeed"));
            PROBE = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception ex) { throw new Error(ex); }
    }

}
