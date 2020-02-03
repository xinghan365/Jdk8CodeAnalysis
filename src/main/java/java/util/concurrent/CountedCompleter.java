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

package java.util.concurrent;

/**
 * A ForkJoinTask ，当触发时执行完成操作，并且没有剩余的待处理操作。 CountedCompleters通常比其他形式的ForkJoinTasks在子任务停顿和阻塞的情况下更加强大，但是不太直观的编程。 CountedCompleter的使用与其他基于完成的组件（例如CompletionHandler ）的使用类似，但可能需要多个未完成的完成才能触发完成操作onCompletion(CountedCompleter) ，而不仅仅是一个。 除非另有初始化， pending count开始于零，但也可以是（原子），使用方法改变setPendingCount(int) ， addToPendingCount(int)和compareAndSetPendingCount(int, int) 。 在调用tryComplete()时，如果待处理的行动计数不为零，则递减; 否则，执行完成操作，如果完成者本身具有完整性，则该过程将继续完成。 与相关同步组件（如Phaser和Semaphore）的情况一样 ，这些方法仅影响内部计数; 他们没有建立任何进一步的内部簿记。 特别地，未维护未决任务的身份。 如下所示，您可以创建在需要时记录一些或所有待处理任务或其结果的子类。 如下所示，还提供了支持定制完成遍历的实用程序方法。 然而，由于CountedCompleters仅提供基本的同步机制，因此创建进一步的抽象子类可能是有用的，这些子类保持适用于一组相关用法的链接，字段和其他支持方法。
 * 具体的CountedCompleter类必须定义方法compute() ，在大多数情况下（如下所示），在tryComplete()之前调用tryComplete()一次。 该类还可以可选地覆盖方法onCompletion(CountedCompleter)以在正常完成时执行动作，并且方法onExceptionalCompletion(Throwable, CountedCompleter)对任何异常执行动作。
 *
 * CountedCompleter通常不承担结果，在这种情况下，它们通常被声明为CountedCompleter<Void> ，并将始终返回null作为结果值。 在其他情况下，你应该重写方法getRawResult()提供从结果join(), invoke() ，以及相关方法。 一般来说，该方法应该返回在完成后保存结果的CountedCompleter对象的一个字段（或一个或多个字段的函数）的值。 默认方法setRawResult(T)在CountedCompleters中不起作用。 可能但很少适用于覆盖此方法来维护其他对象或保存结果数据的字段。
 *
 * 一个CountedCompleter本身不具有一个完整的（即getCompleter()返回null ）可以用作这个添加的功能的常规ForkJoinTask。 然而，任何完成者又具有另一个完成者仅用作其他计算的内部帮助器，因此其自己的任务状态（如方法如ForkJoinTask.isDone()所报告）是任意的; 这种状况只有在明确调用改变complete(T) ， ForkJoinTask.cancel(boolean) ， ForkJoinTask.completeExceptionally(Throwable)或方法的特殊结束后compute 。 在任何异常完成之后，如果有任何异常可能会被传递到任务的完成者（以及其完成者等），如果存在并且尚未完成。 同样地，取消一个内部的CountedCompleter只对该完成者有局部的影响，所以并不常用。
 *
 * 示例用法
 *
 * 并行递归分解。 CountedCompleters可能被安排在与RecursiveAction经常使用的类似的树中，尽管与设置相关的结构通常有所不同。 这里，每个任务的完成者是其计算树中的父项。 即使它们需要更多的簿记，CountedCompleters可能是更好的选择，当应用可能耗时的操作（不能进一步细分）到数组或集合的每个元素; 特别是当操作对于一些元素的时间要比其他元素完成时要多得多，这是因为内在的变化（例如I / O）或诸如垃圾收集的辅助效应。 因为CountedCompleters提供自己的延续，其他线程不需要阻止等待执行它们。
 *
 * 例如，这是一个初始版本的类，它使用二分法递归分解将工作分成单个部分（叶子任务）。 即使将工作分解为单独的调用，基于树的技术通常比直接分支叶子任务更为可取，因为它们可以减少线程间通信并改善负载平衡。 在递归的情况下，要完成的每对子任务副本中的第二个触发器完成其父进程（因为没有执行结果组合，所以方法onCompletion的默认无操作实现不被覆盖）。 静态实用程序方法设置基本任务并调用它（这里隐含地使用ForkJoinPool.commonPool() ）。
 *
 *    class MyOperation<E> { void apply(E e) { ... } } class ForEach<E> extends CountedCompleter<Void> { public static <E> void forEach(E[] array, MyOperation<E> op) { new ForEach<E>(null, array, op, 0, array.length).invoke(); } final E[] array; final MyOperation<E> op; final int lo, hi; ForEach(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi) { super(p); this.array = array; this.op = op; this.lo = lo; this.hi = hi; } public void compute() { // version 1 if (hi - lo >= 2) { int mid = (lo + hi) >>> 1; setPendingCount(2); // must set pending count before fork new ForEach(this, array, op, mid, hi).fork(); // right child new ForEach(this, array, op, lo, mid).fork(); // left child } else if (hi > lo) op.apply(array[lo]); tryComplete(); } }
 *
 * 通过注意到在递归的情况下，该任务在分配正确的任务后无关，因此可以在返回之前直接调用其左任务，从而可以改善此设计。 （这是一个尾递归删除的模拟。）另外，因为任务在执行其左任务时返回（而不是通过调用tryComplete ），待处理的计数设置为1：
 *    class ForEach<E> ... public void compute() { // version 2 if (hi - lo >= 2) { int mid = (lo + hi) >>> 1; setPendingCount(1); // only one pending new ForEach(this, array, op, mid, hi).fork(); // right child new ForEach(this, array, op, lo, mid).compute(); // direct invoke } else { if (hi > lo) op.apply(array[lo]); tryComplete(); } }
 * 作为一个进一步的改进，注意左边的任务甚至不存在。 而不是创建一个新的，我们可以迭代使用原始任务，并为每个fork添加一个挂起的计数。 另外，因为这个树中没有任务实现onCompletion(CountedCompleter)方法， tryComplete()可以替换为propagateCompletion() 。
 *    class ForEach<E> ... public void compute() { // version 3 int l = lo, h = hi; while (h - l >= 2) { int mid = (l + h) >>> 1; addToPendingCount(1); new ForEach(this, array, op, mid, h).fork(); // right child h = mid; } if (h > l) op.apply(array[l]); propagateCompletion(); }
 * 这些类的额外改进可能需要预先计算待处理的计数，以便它们可以在构造函数中建立，专门用于叶子步骤的类，按每个重复的四个细分，而不是两个细分，并使用自适应阈值，而不是总是细分为单个元素。
 * 搜索。 CountedCompleters的树可以在数据结构的不同部分搜索一个值或属性，一旦发现结果，就会在AtomicReference中报告结果。 其他人可以轮询结果，以避免不必要的工作。 （您可以另外提供cancel其他任务，但通常只需让他们注意到结果被设置，并且如果是跳过进一步处理，则通常更简单和更有效）。再次使用完全分区（再次，在实践中，叶任务）几乎总是处理多个元素）：
 *
 *    class Searcher<E> extends CountedCompleter<E> { final E[] array; final AtomicReference<E> result; final int lo, hi; Searcher(CountedCompleter<?> p, E[] array, AtomicReference<E> result, int lo, int hi) { super(p); this.array = array; this.result = result; this.lo = lo; this.hi = hi; } public E getRawResult() { return result.get(); } public void compute() { // similar to ForEach version 3 int l = lo, h = hi; while (result.get() == null && h >= l) { if (h - l >= 2) { int mid = (l + h) >>> 1; addToPendingCount(1); new Searcher(this, array, result, mid, h).fork(); h = mid; } else { E x = array[l]; if (matches(x) && result.compareAndSet(null, x)) quietlyCompleteRoot(); // root task is now joinable break; } } tryComplete(); // normally complete whether or not found } boolean matches(E e) { ... } // return true if found public static <E> E search(E[] array) { return new Searcher<E>(null, array, new AtomicReference<E>(), 0, array.length).invoke(); } } 在此示例中，以及其他任务除了compareAndSet设置常见结果之外没有其他效果，tryComplete的后续无条件tryComplete可以被设置为有条件的（ if (result.get() == null) tryComplete(); ），因为一旦根任务完成，就不需要进一步的簿记管理完成。
 * 记录子任务 结合多个子任务的CountedCompleter任务通常需要在方法onCompletion(CountedCompleter)中访问这些结果。 如下面的类所示（执行map-reduce的简化形式，其中映射和缩减都是类型为E ），分割和征服设计的一种方法是使每个子任务记录成为兄弟，以便它可以可以在方法onCompletion访问。 这种技术适用于结合左和右结果的顺序无关紧要的减少; 有序减少需要明确的左/右指定。 上述示例中可以看到其他流程图的变体。
 *
 *    class MyMapper<E> { E apply(E v) { ... } } class MyReducer<E> { E apply(E x, E y) { ... } } class MapReducer<E> extends CountedCompleter<E> { final E[] array; final MyMapper<E> mapper; final MyReducer<E> reducer; final int lo, hi; MapReducer<E> sibling; E result; MapReducer(CountedCompleter<?> p, E[] array, MyMapper<E> mapper, MyReducer<E> reducer, int lo, int hi) { super(p); this.array = array; this.mapper = mapper; this.reducer = reducer; this.lo = lo; this.hi = hi; } public void compute() { if (hi - lo >= 2) { int mid = (lo + hi) >>> 1; MapReducer<E> left = new MapReducer(this, array, mapper, reducer, lo, mid); MapReducer<E> right = new MapReducer(this, array, mapper, reducer, mid, hi); left.sibling = right; right.sibling = left; setPendingCount(1); // only right is pending right.fork(); left.compute(); // directly execute left } else { if (hi > lo) result = mapper.apply(array[lo]); tryComplete(); } } public void onCompletion(CountedCompleter<?> caller) { if (caller != this) { MapReducer<E> child = (MapReducer<E>)caller; MapReducer<E> sib = child.sibling; if (sib == null || sib.result == null) result = child.result; else result = reducer.apply(child.result, sib.result); } } public E getRawResult() { return result; } public static <E> E mapReduce(E[] array, MyMapper<E> mapper, MyReducer<E> reducer) { return new MapReducer<E>(null, array, mapper, reducer, 0, array.length).invoke(); } } 这里，方法onCompletion采用了结合结果的许多完成设计共同的形式。 这种回调式方法在每个任务中触发一次，在挂起计数的两个不同上下文中的任一个中，或者当任务本身调用时其挂起的计数为零时变为零：（1），或者tryComplete时的挂起计数为零，或（2）通过任何其子任务，当它们完成并将待处理的计数递减到零时。 caller论证区分案例。 通常，当呼叫方为this时，不需要采取任何行动。 否则，可以使用调用者参数（通常通过转换）来提供要组合的值（和/或链接到其他值）。 假设正确使用挂起计数，里面的动作onCompletion发生（一次）一个任务，其子任务完成时。 在此方法中不需要额外的同步来确保对此任务或其他完成任务的字段的访问的线程安全性。
 * 完成遍历 。 如果使用onCompletion处理完成不适用或不方便，您可以使用方法firstComplete()和nextComplete()创建自定义遍历。 例如，要定义一个仅以第三个ForEach示例的形式分割右侧任务的MapReducer，完成必须按照未用尽的子任务链接合作减少，可以如下完成：
 *
 *    class MapReducer<E> extends CountedCompleter<E> { // version 2 final E[] array; final MyMapper<E> mapper; final MyReducer<E> reducer; final int lo, hi; MapReducer<E> forks, next; // record subtask forks in list E result; MapReducer(CountedCompleter<?> p, E[] array, MyMapper<E> mapper, MyReducer<E> reducer, int lo, int hi, MapReducer<E> next) { super(p); this.array = array; this.mapper = mapper; this.reducer = reducer; this.lo = lo; this.hi = hi; this.next = next; } public void compute() { int l = lo, h = hi; while (h - l >= 2) { int mid = (l + h) >>> 1; addToPendingCount(1); (forks = new MapReducer(this, array, mapper, reducer, mid, h, forks)).fork(); h = mid; } if (h > l) result = mapper.apply(array[l]); // process completions by reducing along and advancing subtask links for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) { for (MapReducer t = (MapReducer)c, s = t.forks; s != null; s = t.forks = s.next) t.result = reducer.apply(t.result, s.result); } } public E getRawResult() { return result; } public static <E> E mapReduce(E[] array, MyMapper<E> mapper, MyReducer<E> reducer) { return new MapReducer<E>(null, array, mapper, reducer, 0, array.length, null).invoke(); } }
 * 触发器 一些CountedCompleters本身从来没有分叉，而是作为其他设计中的一些管道; 包括完成一个或多个异步任务触发另一个异步任务。 例如：
 *
 *    class HeaderBuilder extends CountedCompleter<...> { ... } class BodyBuilder extends CountedCompleter<...> { ... } class PacketSender extends CountedCompleter<...> { PacketSender(...) { super(null, 1); ... } // trigger on second completion public void compute() { } // never called public void onCompletion(CountedCompleter<?> caller) { sendPacket(); } } // sample use: PacketSender p = new PacketSender(); new HeaderBuilder(p, ...).fork(); new BodyBuilder(p, ...).fork();
 *
 *
 * A {@link ForkJoinTask} with a completion action performed when
 * triggered and there are no remaining pending actions.
 * CountedCompleters are in general more robust in the
 * presence of subtask stalls and blockage than are other forms of
 * ForkJoinTasks, but are less intuitive to program.  Uses of
 * CountedCompleter are similar to those of other completion based
 * components (such as {@link java.nio.channels.CompletionHandler})
 * except that multiple <em>pending</em> completions may be necessary
 * to trigger the completion action {@link #onCompletion(CountedCompleter)},
 * not just one.
 * Unless initialized otherwise, the {@linkplain #getPendingCount pending
 * count} starts at zero, but may be (atomically) changed using
 * methods {@link #setPendingCount}, {@link #addToPendingCount}, and
 * {@link #compareAndSetPendingCount}. Upon invocation of {@link
 * #tryComplete}, if the pending action count is nonzero, it is
 * decremented; otherwise, the completion action is performed, and if
 * this completer itself has a completer, the process is continued
 * with its completer.  As is the case with related synchronization
 * components such as {@link java.util.concurrent.Phaser Phaser} and
 * {@link java.util.concurrent.Semaphore Semaphore}, these methods
 * affect only internal counts; they do not establish any further
 * internal bookkeeping. In particular, the identities of pending
 * tasks are not maintained. As illustrated below, you can create
 * subclasses that do record some or all pending tasks or their
 * results when needed.  As illustrated below, utility methods
 * supporting customization of completion traversals are also
 * provided. However, because CountedCompleters provide only basic
 * synchronization mechanisms, it may be useful to create further
 * abstract subclasses that maintain linkages, fields, and additional
 * support methods appropriate for a set of related usages.
 *
 * <p>A concrete CountedCompleter class must define method {@link
 * #compute}, that should in most cases (as illustrated below), invoke
 * {@code tryComplete()} once before returning. The class may also
 * optionally override method {@link #onCompletion(CountedCompleter)}
 * to perform an action upon normal completion, and method
 * {@link #onExceptionalCompletion(Throwable, CountedCompleter)} to
 * perform an action upon any exception.
 *
 * <p>CountedCompleters most often do not bear results, in which case
 * they are normally declared as {@code CountedCompleter<Void>}, and
 * will always return {@code null} as a result value.  In other cases,
 * you should override method {@link #getRawResult} to provide a
 * result from {@code join(), invoke()}, and related methods.  In
 * general, this method should return the value of a field (or a
 * function of one or more fields) of the CountedCompleter object that
 * holds the result upon completion. Method {@link #setRawResult} by
 * default plays no role in CountedCompleters.  It is possible, but
 * rarely applicable, to override this method to maintain other
 * objects or fields holding result data.
 *
 * <p>A CountedCompleter that does not itself have a completer (i.e.,
 * one for which {@link #getCompleter} returns {@code null}) can be
 * used as a regular ForkJoinTask with this added functionality.
 * However, any completer that in turn has another completer serves
 * only as an internal helper for other computations, so its own task
 * status (as reported in methods such as {@link ForkJoinTask#isDone})
 * is arbitrary; this status changes only upon explicit invocations of
 * {@link #complete}, {@link ForkJoinTask#cancel},
 * {@link ForkJoinTask#completeExceptionally(Throwable)} or upon
 * exceptional completion of method {@code compute}. Upon any
 * exceptional completion, the exception may be relayed to a task's
 * completer (and its completer, and so on), if one exists and it has
 * not otherwise already completed. Similarly, cancelling an internal
 * CountedCompleter has only a local effect on that completer, so is
 * not often useful.
 *
 * <p><b>Sample Usages.</b>
 *
 * <p><b>Parallel recursive decomposition.</b> CountedCompleters may
 * be arranged in trees similar to those often used with {@link
 * RecursiveAction}s, although the constructions involved in setting
 * them up typically vary. Here, the completer of each task is its
 * parent in the computation tree. Even though they entail a bit more
 * bookkeeping, CountedCompleters may be better choices when applying
 * a possibly time-consuming operation (that cannot be further
 * subdivided) to each element of an array or collection; especially
 * when the operation takes a significantly different amount of time
 * to complete for some elements than others, either because of
 * intrinsic variation (for example I/O) or auxiliary effects such as
 * garbage collection.  Because CountedCompleters provide their own
 * continuations, other threads need not block waiting to perform
 * them.
 *
 * <p>For example, here is an initial version of a class that uses
 * divide-by-two recursive decomposition to divide work into single
 * pieces (leaf tasks). Even when work is split into individual calls,
 * tree-based techniques are usually preferable to directly forking
 * leaf tasks, because they reduce inter-thread communication and
 * improve load balancing. In the recursive case, the second of each
 * pair of subtasks to finish triggers completion of its parent
 * (because no result combination is performed, the default no-op
 * implementation of method {@code onCompletion} is not overridden).
 * A static utility method sets up the base task and invokes it
 * (here, implicitly using the {@link ForkJoinPool#commonPool()}).
 *
 * <pre> {@code
 * class MyOperation<E> { void apply(E e) { ... }  }
 *
 * class ForEach<E> extends CountedCompleter<Void> {
 *
 *   public static <E> void forEach(E[] array, MyOperation<E> op) {
 *     new ForEach<E>(null, array, op, 0, array.length).invoke();
 *   }
 *
 *   final E[] array; final MyOperation<E> op; final int lo, hi;
 *   ForEach(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi) {
 *     super(p);
 *     this.array = array; this.op = op; this.lo = lo; this.hi = hi;
 *   }
 *
 *   public void compute() { // version 1
 *     if (hi - lo >= 2) {
 *       int mid = (lo + hi) >>> 1;
 *       setPendingCount(2); // must set pending count before fork
 *       new ForEach(this, array, op, mid, hi).fork(); // right child
 *       new ForEach(this, array, op, lo, mid).fork(); // left child
 *     }
 *     else if (hi > lo)
 *       op.apply(array[lo]);
 *     tryComplete();
 *   }
 * }}</pre>
 *
 * This design can be improved by noticing that in the recursive case,
 * the task has nothing to do after forking its right task, so can
 * directly invoke its left task before returning. (This is an analog
 * of tail recursion removal.)  Also, because the task returns upon
 * executing its left task (rather than falling through to invoke
 * {@code tryComplete}) the pending count is set to one:
 *
 * <pre> {@code
 * class ForEach<E> ...
 *   public void compute() { // version 2
 *     if (hi - lo >= 2) {
 *       int mid = (lo + hi) >>> 1;
 *       setPendingCount(1); // only one pending
 *       new ForEach(this, array, op, mid, hi).fork(); // right child
 *       new ForEach(this, array, op, lo, mid).compute(); // direct invoke
 *     }
 *     else {
 *       if (hi > lo)
 *         op.apply(array[lo]);
 *       tryComplete();
 *     }
 *   }
 * }</pre>
 *
 * As a further improvement, notice that the left task need not even exist.
 * Instead of creating a new one, we can iterate using the original task,
 * and add a pending count for each fork.  Additionally, because no task
 * in this tree implements an {@link #onCompletion(CountedCompleter)} method,
 * {@code tryComplete()} can be replaced with {@link #propagateCompletion}.
 *
 * <pre> {@code
 * class ForEach<E> ...
 *   public void compute() { // version 3
 *     int l = lo,  h = hi;
 *     while (h - l >= 2) {
 *       int mid = (l + h) >>> 1;
 *       addToPendingCount(1);
 *       new ForEach(this, array, op, mid, h).fork(); // right child
 *       h = mid;
 *     }
 *     if (h > l)
 *       op.apply(array[l]);
 *     propagateCompletion();
 *   }
 * }</pre>
 *
 * Additional improvements of such classes might entail precomputing
 * pending counts so that they can be established in constructors,
 * specializing classes for leaf steps, subdividing by say, four,
 * instead of two per iteration, and using an adaptive threshold
 * instead of always subdividing down to single elements.
 *
 * <p><b>Searching.</b> A tree of CountedCompleters can search for a
 * value or property in different parts of a data structure, and
 * report a result in an {@link
 * java.util.concurrent.atomic.AtomicReference AtomicReference} as
 * soon as one is found. The others can poll the result to avoid
 * unnecessary work. (You could additionally {@linkplain #cancel
 * cancel} other tasks, but it is usually simpler and more efficient
 * to just let them notice that the result is set and if so skip
 * further processing.)  Illustrating again with an array using full
 * partitioning (again, in practice, leaf tasks will almost always
 * process more than one element):
 *
 * <pre> {@code
 * class Searcher<E> extends CountedCompleter<E> {
 *   final E[] array; final AtomicReference<E> result; final int lo, hi;
 *   Searcher(CountedCompleter<?> p, E[] array, AtomicReference<E> result, int lo, int hi) {
 *     super(p);
 *     this.array = array; this.result = result; this.lo = lo; this.hi = hi;
 *   }
 *   public E getRawResult() { return result.get(); }
 *   public void compute() { // similar to ForEach version 3
 *     int l = lo,  h = hi;
 *     while (result.get() == null && h >= l) {
 *       if (h - l >= 2) {
 *         int mid = (l + h) >>> 1;
 *         addToPendingCount(1);
 *         new Searcher(this, array, result, mid, h).fork();
 *         h = mid;
 *       }
 *       else {
 *         E x = array[l];
 *         if (matches(x) && result.compareAndSet(null, x))
 *           quietlyCompleteRoot(); // root task is now joinable
 *         break;
 *       }
 *     }
 *     tryComplete(); // normally complete whether or not found
 *   }
 *   boolean matches(E e) { ... } // return true if found
 *
 *   public static <E> E search(E[] array) {
 *       return new Searcher<E>(null, array, new AtomicReference<E>(), 0, array.length).invoke();
 *   }
 * }}</pre>
 *
 * In this example, as well as others in which tasks have no other
 * effects except to compareAndSet a common result, the trailing
 * unconditional invocation of {@code tryComplete} could be made
 * conditional ({@code if (result.get() == null) tryComplete();})
 * because no further bookkeeping is required to manage completions
 * once the root task completes.
 *
 * <p><b>Recording subtasks.</b> CountedCompleter tasks that combine
 * results of multiple subtasks usually need to access these results
 * in method {@link #onCompletion(CountedCompleter)}. As illustrated in the following
 * class (that performs a simplified form of map-reduce where mappings
 * and reductions are all of type {@code E}), one way to do this in
 * divide and conquer designs is to have each subtask record its
 * sibling, so that it can be accessed in method {@code onCompletion}.
 * This technique applies to reductions in which the order of
 * combining left and right results does not matter; ordered
 * reductions require explicit left/right designations.  Variants of
 * other streamlinings seen in the above examples may also apply.
 *
 * <pre> {@code
 * class MyMapper<E> { E apply(E v) {  ...  } }
 * class MyReducer<E> { E apply(E x, E y) {  ...  } }
 * class MapReducer<E> extends CountedCompleter<E> {
 *   final E[] array; final MyMapper<E> mapper;
 *   final MyReducer<E> reducer; final int lo, hi;
 *   MapReducer<E> sibling;
 *   E result;
 *   MapReducer(CountedCompleter<?> p, E[] array, MyMapper<E> mapper,
 *              MyReducer<E> reducer, int lo, int hi) {
 *     super(p);
 *     this.array = array; this.mapper = mapper;
 *     this.reducer = reducer; this.lo = lo; this.hi = hi;
 *   }
 *   public void compute() {
 *     if (hi - lo >= 2) {
 *       int mid = (lo + hi) >>> 1;
 *       MapReducer<E> left = new MapReducer(this, array, mapper, reducer, lo, mid);
 *       MapReducer<E> right = new MapReducer(this, array, mapper, reducer, mid, hi);
 *       left.sibling = right;
 *       right.sibling = left;
 *       setPendingCount(1); // only right is pending
 *       right.fork();
 *       left.compute();     // directly execute left
 *     }
 *     else {
 *       if (hi > lo)
 *           result = mapper.apply(array[lo]);
 *       tryComplete();
 *     }
 *   }
 *   public void onCompletion(CountedCompleter<?> caller) {
 *     if (caller != this) {
 *       MapReducer<E> child = (MapReducer<E>)caller;
 *       MapReducer<E> sib = child.sibling;
 *       if (sib == null || sib.result == null)
 *         result = child.result;
 *       else
 *         result = reducer.apply(child.result, sib.result);
 *     }
 *   }
 *   public E getRawResult() { return result; }
 *
 *   public static <E> E mapReduce(E[] array, MyMapper<E> mapper, MyReducer<E> reducer) {
 *     return new MapReducer<E>(null, array, mapper, reducer,
 *                              0, array.length).invoke();
 *   }
 * }}</pre>
 *
 * Here, method {@code onCompletion} takes a form common to many
 * completion designs that combine results. This callback-style method
 * is triggered once per task, in either of the two different contexts
 * in which the pending count is, or becomes, zero: (1) by a task
 * itself, if its pending count is zero upon invocation of {@code
 * tryComplete}, or (2) by any of its subtasks when they complete and
 * decrement the pending count to zero. The {@code caller} argument
 * distinguishes cases.  Most often, when the caller is {@code this},
 * no action is necessary. Otherwise the caller argument can be used
 * (usually via a cast) to supply a value (and/or links to other
 * values) to be combined.  Assuming proper use of pending counts, the
 * actions inside {@code onCompletion} occur (once) upon completion of
 * a task and its subtasks. No additional synchronization is required
 * within this method to ensure thread safety of accesses to fields of
 * this task or other completed tasks.
 *
 * <p><b>Completion Traversals</b>. If using {@code onCompletion} to
 * process completions is inapplicable or inconvenient, you can use
 * methods {@link #firstComplete} and {@link #nextComplete} to create
 * custom traversals.  For example, to define a MapReducer that only
 * splits out right-hand tasks in the form of the third ForEach
 * example, the completions must cooperatively reduce along
 * unexhausted subtask links, which can be done as follows:
 *
 * <pre> {@code
 * class MapReducer<E> extends CountedCompleter<E> { // version 2
 *   final E[] array; final MyMapper<E> mapper;
 *   final MyReducer<E> reducer; final int lo, hi;
 *   MapReducer<E> forks, next; // record subtask forks in list
 *   E result;
 *   MapReducer(CountedCompleter<?> p, E[] array, MyMapper<E> mapper,
 *              MyReducer<E> reducer, int lo, int hi, MapReducer<E> next) {
 *     super(p);
 *     this.array = array; this.mapper = mapper;
 *     this.reducer = reducer; this.lo = lo; this.hi = hi;
 *     this.next = next;
 *   }
 *   public void compute() {
 *     int l = lo,  h = hi;
 *     while (h - l >= 2) {
 *       int mid = (l + h) >>> 1;
 *       addToPendingCount(1);
 *       (forks = new MapReducer(this, array, mapper, reducer, mid, h, forks)).fork();
 *       h = mid;
 *     }
 *     if (h > l)
 *       result = mapper.apply(array[l]);
 *     // process completions by reducing along and advancing subtask links
 *     for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
 *       for (MapReducer t = (MapReducer)c, s = t.forks;  s != null; s = t.forks = s.next)
 *         t.result = reducer.apply(t.result, s.result);
 *     }
 *   }
 *   public E getRawResult() { return result; }
 *
 *   public static <E> E mapReduce(E[] array, MyMapper<E> mapper, MyReducer<E> reducer) {
 *     return new MapReducer<E>(null, array, mapper, reducer,
 *                              0, array.length, null).invoke();
 *   }
 * }}</pre>
 *
 * <p><b>Triggers.</b> Some CountedCompleters are themselves never
 * forked, but instead serve as bits of plumbing in other designs;
 * including those in which the completion of one or more async tasks
 * triggers another async task. For example:
 *
 * <pre> {@code
 * class HeaderBuilder extends CountedCompleter<...> { ... }
 * class BodyBuilder extends CountedCompleter<...> { ... }
 * class PacketSender extends CountedCompleter<...> {
 *   PacketSender(...) { super(null, 1); ... } // trigger on second completion
 *   public void compute() { } // never called
 *   public void onCompletion(CountedCompleter<?> caller) { sendPacket(); }
 * }
 * // sample use:
 * PacketSender p = new PacketSender();
 * new HeaderBuilder(p, ...).fork();
 * new BodyBuilder(p, ...).fork();
 * }</pre>
 *
 * @since 1.8
 * @author Doug Lea
 */
public abstract class CountedCompleter<T> extends ForkJoinTask<T> {
    private static final long serialVersionUID = 5232453752276485070L;

    /** This task's completer, or null if none */
    final CountedCompleter<?> completer;
    /** The number of pending tasks until completion */
    volatile int pending;

    /**
     * Creates a new CountedCompleter with the given completer
     * and initial pending count.
     *
     * @param completer this task's completer, or {@code null} if none
     * @param initialPendingCount the initial pending count
     */
    protected CountedCompleter(CountedCompleter<?> completer,
                               int initialPendingCount) {
        this.completer = completer;
        this.pending = initialPendingCount;
    }

    /**
     * Creates a new CountedCompleter with the given completer
     * and an initial pending count of zero.
     *
     * @param completer this task's completer, or {@code null} if none
     */
    protected CountedCompleter(CountedCompleter<?> completer) {
        this.completer = completer;
    }

    /**
     * Creates a new CountedCompleter with no completer
     * and an initial pending count of zero.
     */
    protected CountedCompleter() {
        this.completer = null;
    }

    /**
     * The main computation performed by this task.
     */
    public abstract void compute();

    /**
     * Performs an action when method {@link #tryComplete} is invoked
     * and the pending count is zero, or when the unconditional
     * method {@link #complete} is invoked.  By default, this method
     * does nothing. You can distinguish cases by checking the
     * identity of the given caller argument. If not equal to {@code
     * this}, then it is typically a subtask that may contain results
     * (and/or links to other results) to combine.
     *
     * @param caller the task invoking this method (which may
     * be this task itself)
     */
    public void onCompletion(CountedCompleter<?> caller) {
    }

    /**
     * Performs an action when method {@link
     * #completeExceptionally(Throwable)} is invoked or method {@link
     * #compute} throws an exception, and this task has not already
     * otherwise completed normally. On entry to this method, this task
     * {@link ForkJoinTask#isCompletedAbnormally}.  The return value
     * of this method controls further propagation: If {@code true}
     * and this task has a completer that has not completed, then that
     * completer is also completed exceptionally, with the same
     * exception as this completer.  The default implementation of
     * this method does nothing except return {@code true}.
     *
     * @param ex the exception
     * @param caller the task invoking this method (which may
     * be this task itself)
     * @return {@code true} if this exception should be propagated to this
     * task's completer, if one exists
     */
    public boolean onExceptionalCompletion(Throwable ex, CountedCompleter<?> caller) {
        return true;
    }

    /**
     * Returns the completer established in this task's constructor,
     * or {@code null} if none.
     *
     * @return the completer
     */
    public final CountedCompleter<?> getCompleter() {
        return completer;
    }

    /**
     * Returns the current pending count.
     *
     * @return the current pending count
     */
    public final int getPendingCount() {
        return pending;
    }

    /**
     * Sets the pending count to the given value.
     *
     * @param count the count
     */
    public final void setPendingCount(int count) {
        pending = count;
    }

    /**
     * Adds (atomically) the given value to the pending count.
     *
     * @param delta the value to add
     */
    public final void addToPendingCount(int delta) {
        U.getAndAddInt(this, PENDING, delta);
    }

    /**
     * Sets (atomically) the pending count to the given count only if
     * it currently holds the given expected value.
     *
     * @param expected the expected value
     * @param count the new value
     * @return {@code true} if successful
     */
    public final boolean compareAndSetPendingCount(int expected, int count) {
        return U.compareAndSwapInt(this, PENDING, expected, count);
    }

    /**
     * If the pending count is nonzero, (atomically) decrements it.
     *
     * @return the initial (undecremented) pending count holding on entry
     * to this method
     */
    public final int decrementPendingCountUnlessZero() {
        int c;
        do {} while ((c = pending) != 0 &&
                     !U.compareAndSwapInt(this, PENDING, c, c - 1));
        return c;
    }

    /**
     * Returns the root of the current computation; i.e., this
     * task if it has no completer, else its completer's root.
     *
     * @return the root of the current computation
     */
    public final CountedCompleter<?> getRoot() {
        CountedCompleter<?> a = this, p;
        while ((p = a.completer) != null) {
            a = p;
        }
        return a;
    }

    /**
     * If the pending count is nonzero, decrements the count;
     * otherwise invokes {@link #onCompletion(CountedCompleter)}
     * and then similarly tries to complete this task's completer,
     * if one exists, else marks this task as complete.
     */
    public final void tryComplete() {
        CountedCompleter<?> a = this, s = a;
        for (int c;;) {
            if ((c = a.pending) == 0) {
                a.onCompletion(s);
                if ((a = (s = a).completer) == null) {
                    s.quietlyComplete();
                    return;
                }
            }
            else if (U.compareAndSwapInt(a, PENDING, c, c - 1)) {
                return;
            }
        }
    }

    /**
     * Equivalent to {@link #tryComplete} but does not invoke {@link
     * #onCompletion(CountedCompleter)} along the completion path:
     * If the pending count is nonzero, decrements the count;
     * otherwise, similarly tries to complete this task's completer, if
     * one exists, else marks this task as complete. This method may be
     * useful in cases where {@code onCompletion} should not, or need
     * not, be invoked for each completer in a computation.
     */
    public final void propagateCompletion() {
        CountedCompleter<?> a = this, s = a;
        for (int c;;) {
            if ((c = a.pending) == 0) {
                if ((a = (s = a).completer) == null) {
                    s.quietlyComplete();
                    return;
                }
            }
            else if (U.compareAndSwapInt(a, PENDING, c, c - 1)) {
                return;
            }
        }
    }

    /**
     * Regardless of pending count, invokes
     * {@link #onCompletion(CountedCompleter)}, marks this task as
     * complete and further triggers {@link #tryComplete} on this
     * task's completer, if one exists.  The given rawResult is
     * used as an argument to {@link #setRawResult} before invoking
     * {@link #onCompletion(CountedCompleter)} or marking this task
     * as complete; its value is meaningful only for classes
     * overriding {@code setRawResult}.  This method does not modify
     * the pending count.
     *
     * <p>This method may be useful when forcing completion as soon as
     * any one (versus all) of several subtask results are obtained.
     * However, in the common (and recommended) case in which {@code
     * setRawResult} is not overridden, this effect can be obtained
     * more simply using {@code quietlyCompleteRoot();}.
     *
     * @param rawResult the raw result
     */
    @Override
    public void complete(T rawResult) {
        CountedCompleter<?> p;
        setRawResult(rawResult);
        onCompletion(this);
        quietlyComplete();
        if ((p = completer) != null) {
            p.tryComplete();
        }
    }

    /**
     * If this task's pending count is zero, returns this task;
     * otherwise decrements its pending count and returns {@code
     * null}. This method is designed to be used with {@link
     * #nextComplete} in completion traversal loops.
     *
     * @return this task, if pending count was zero, else {@code null}
     */
    public final CountedCompleter<?> firstComplete() {
        for (int c;;) {
            if ((c = pending) == 0) {
                return this;
            } else if (U.compareAndSwapInt(this, PENDING, c, c - 1)) {
                return null;
            }
        }
    }

    /**
     * If this task does not have a completer, invokes {@link
     * ForkJoinTask#quietlyComplete} and returns {@code null}.  Or, if
     * the completer's pending count is non-zero, decrements that
     * pending count and returns {@code null}.  Otherwise, returns the
     * completer.  This method can be used as part of a completion
     * traversal loop for homogeneous task hierarchies:
     *
     * <pre> {@code
     * for (CountedCompleter<?> c = firstComplete();
     *      c != null;
     *      c = c.nextComplete()) {
     *   // ... process c ...
     * }}</pre>
     *
     * @return the completer, or {@code null} if none
     */
    public final CountedCompleter<?> nextComplete() {
        CountedCompleter<?> p;
        if ((p = completer) != null) {
            return p.firstComplete();
        } else {
            quietlyComplete();
            return null;
        }
    }

    /**
     * Equivalent to {@code getRoot().quietlyComplete()}.
     */
    public final void quietlyCompleteRoot() {
        for (CountedCompleter<?> a = this, p;;) {
            if ((p = a.completer) == null) {
                a.quietlyComplete();
                return;
            }
            a = p;
        }
    }

    /**
     * If this task has not completed, attempts to process at most the
     * given number of other unprocessed tasks for which this task is
     * on the completion path, if any are known to exist.
     *
     * @param maxTasks the maximum number of tasks to process.  If
     *                 less than or equal to zero, then no tasks are
     *                 processed.
     */
    public final void helpComplete(int maxTasks) {
        Thread t; ForkJoinWorkerThread wt;
        if (maxTasks > 0 && status >= 0) {
            if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
                (wt = (ForkJoinWorkerThread)t).pool.
                    helpComplete(wt.workQueue, this, maxTasks);
            } else {
                ForkJoinPool.common.externalHelpComplete(this, maxTasks);
            }
        }
    }

    /**
     * Supports ForkJoinTask exception propagation.
     */
    @Override
    void internalPropagateException(Throwable ex) {
        CountedCompleter<?> a = this, s = a;
        while (a.onExceptionalCompletion(ex, s) &&
               (a = (s = a).completer) != null && a.status >= 0 &&
               a.recordExceptionalCompletion(ex) == EXCEPTIONAL) {
            ;
        }
    }

    /**
     * Implements execution conventions for CountedCompleters.
     */
    @Override
    protected final boolean exec() {
        compute();
        return false;
    }

    /**
     * Returns the result of the computation. By default
     * returns {@code null}, which is appropriate for {@code Void}
     * actions, but in other cases should be overridden, almost
     * always to return a field or function of a field that
     * holds the result upon completion.
     *
     * @return the result of the computation
     */
    @Override
    public T getRawResult() { return null; }

    /**
     * A method that result-bearing CountedCompleters may optionally
     * use to help maintain result data.  By default, does nothing.
     * Overrides are not recommended. However, if this method is
     * overridden to update existing objects or fields, then it must
     * in general be defined to be thread-safe.
     */
    @Override
    protected void setRawResult(T t) { }

    // Unsafe mechanics
    private static final sun.misc.Unsafe U;
    private static final long PENDING;
    static {
        try {
            U = sun.misc.Unsafe.getUnsafe();
            PENDING = U.objectFieldOffset
                (CountedCompleter.class.getDeclaredField("pending"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
