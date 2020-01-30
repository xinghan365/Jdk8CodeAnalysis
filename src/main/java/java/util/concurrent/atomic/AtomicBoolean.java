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

package java.util.concurrent.atomic;

import sun.misc.Unsafe;

/**
 * 一个 {@code boolean} 值可以用原子更新。 有关原子变量属性的描述，请参阅
 * {@link java.util.concurrent.atomic}包规范。 一个
 * {@code AtomicBoolean} 用于诸如原子更新标志的应用程序，不能用作替代
 * {@link java.lang.Boolean}.
 *
 * @author Doug Lea
 * @since 1.5
 */
public class AtomicBoolean implements java.io.Serializable {
    private static final long serialVersionUID = 4654671469794556979L;
    // setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (AtomicBoolean.class.getDeclaredField("value"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private volatile int value;

    /**
     * 用给定的初始值创建一个新的 {@code AtomicBoolean}.
     *
     * @param initialValue the initial value
     */
    public AtomicBoolean(boolean initialValue) {
        value = initialValue ? 1 : 0;
    }

    /**
     * 创建一个新的 {@code AtomicBoolean} 初始值为 {@code false}.
     */
    public AtomicBoolean() {
    }

    /**
     * 返回当前值.
     *
     * @return the current value
     */
    public final boolean get() {
        return value != 0;
    }

    /**
     * * 将原子设置为给定值并返回上一个值
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(boolean expect, boolean update) {
        int e = expect ? 1 : 0;
        int u = update ? 1 : 0;
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    /**
     * 如果当前值为==为预期值，则将该值原子设置为给定的更新值。
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * <p><a href="package-summary.html#weakCompareAndSet">May fail
     * spuriously and does not provide ordering guarantees</a>, so is
     * only rarely an appropriate alternative to {@code compareAndSet}.
     *
     * @param expect the expected value 预期值
     * @param update the new value      新值
     * @return {@code true} if successful
     */
    public boolean weakCompareAndSet(boolean expect, boolean update) {
        int e = expect ? 1 : 0;
        int u = update ? 1 : 0;
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    /**
     * 无条件地设置为给定的值。
     * Unconditionally sets to the given value.
     *
     * @param newValue the new value
     */
    public final void set(boolean newValue) {
        value = newValue ? 1 : 0;
    }

    /**
     * 最终设定为给定值。
     * Eventually sets to the given value.
     *
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(boolean newValue) {
        int v = newValue ? 1 : 0;
        unsafe.putOrderedInt(this, valueOffset, v);
    }

    /**
     * 将原子设置为给定值并返回上一个值。
     * Atomically sets to the given value and returns the previous value.
     *
     * @param newValue the new value 新值
     * @return the previous value 以前的值
     */
    public final boolean getAndSet(boolean newValue) {
        boolean prev;
        do {
            prev = get();
        } while (!compareAndSet(prev, newValue));
        return prev;
    }

    /**
     * Returns the String representation of the current value.
     *
     * @return the String representation of the current value
     */
    public String toString() {
        return Boolean.toString(get());
    }

}
