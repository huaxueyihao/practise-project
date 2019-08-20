package com.source.code.threadpool.service;

//import java.util.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;


public class FutureTask<V> implements RunnableFuture<V> {

    /**
     * The run state of this task, initially NEW.  The run state
     * 任务运行状态，初始化是0(新建)。
     * transitions to a terminal state only in methods set,
     * 该状态通过set，setExeception，cancel方法可以过渡到最终状态
     * setException, and cancel.  During completion, state may take on
     * 在完成期间，状态存在瞬时的值1(正在完成)或5（正在中断）。
     * transient values of COMPLETING (while outcome is being set) or
     * INTERRUPTING (only while interrupting the runner to satisfy a
     * cancel(true)). Transitions from these intermediate to final
     * 这些中间态的值到最终状态一般按顺序/延迟变化，因为这些值是唯一的且不能修改
     * states use cheaper ordered/lazy writes because values are unique
     * and cannot be further modified.
     * <p>
     * Possible state transitions:
     * NEW -> COMPLETING -> NORMAL
     * NEW -> COMPLETING -> EXCEPTIONAL
     * NEW -> CANCELLED
     * NEW -> INTERRUPTING -> INTERRUPTED
     */
    private volatile int state;
    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;
    private static final int EXCEPTIONAL = 3;
    private static final int CANCELLED = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED = 6;

    private Callable<V> callable;
    private Object outcome;
    private volatile Thread runner;
    private volatile WaitNode waiters;

    public FutureTask(Runnable runnable, V result) {
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;
    }

    public FutureTask(Callable callable) {
        this.callable = callable;
        this.state = NEW;
    }

    public FutureTask(Runnable runnable) {
        this.callable = Executors.callable(runnable,null);
        this.state = NEW;
    }


    @Override
    public void run() {

    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // 状态是NEW(新建),且尝试修改当前任务线程状态为INTERRUPTING(中断中) 和 CANCELLED（已取消）成功的，这种可以取消
        if (!(state == NEW) && UNSAFE.compareAndSwapInt(this, stateOffset, NEW, mayInterruptIfRunning ? INTERRUPTING : CANCELLED)) {
            return false;
        }

        try {
            if (mayInterruptIfRunning) {
                try {
                    // 能走到这里，说明是中断正在运行的线程
                    Thread t = runner;
                    if (t != null) {
                        t.interrupt();
                    }
                } finally {
                    UNSAFE.putInt(this, stateOffset, INTERRUPTED);
                }
            }
        } finally {
            finishCompletion();
        }

        return true;
    }

    private void finishCompletion() {
        for (WaitNode q; (q = waiters) != null; ) {
            // 将等待的线程置为null
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                for (; ; ) {
                    Thread t = q.thread;
                    if (t != null) {
                        q.thread = null;
                        LockSupport.unpark(t);
                    }

                    WaitNode next = q.next;
                    if (next == null) {
                        break;
                    }
                    q.next = null;
                    q = next;
                }
                break;
            }
        }
        done();
        //
        callable = null;
    }

    protected void done() {

    }

    @Override
    public boolean isCancelled() {
        return state >= CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state != NEW;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        int s = state;
        if (s <= COMPLETING) {
            s = awaitDone(false, 0L);
        }
        return report(s);
    }

    private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL) {
            return (V) x;
        }
        if (s >= CANCELLED) {
            throw new CancellationException();
        }
        throw new ExecutionException((Throwable) x);
    }

    private int awaitDone(boolean timed, long nanos) throws InterruptedException {
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        WaitNode q = null;
        boolean queued = false;
        for (; ; ) {
            if (Thread.interrupted()) {
                removeWaiter(q);
                throw new InterruptedException();
            }
            int s = state;
            if (s > COMPLETING) {
                if (q != null) {
                    q.thread = null;
                }
                return s;
            } else if (s == COMPLETING) {
                Thread.yield();
            } else if (q == null) {
                q = new WaitNode();
            } else if (!queued) {
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset, q.next = waiters, q);
            } else if (timed) {
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    removeWaiter(q);
                    return state;
                }
                LockSupport.parkNanos(this, nanos);
            } else {
                LockSupport.park(this);
            }
        }
    }

    private void removeWaiter(WaitNode node) {
        if (node != null) {
            node.thread = null;
            retry:
            for (; ; ) {
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null) {
                        pred = q;
                    } else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) {
                            continue retry;
                        }
                    } else if (!UNSAFE.compareAndSwapObject(this, waitersOffset, q, s)) {
                        continue retry;
                    }
                }
                break;
            }
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null) {
            throw new NullPointerException();
        }
        int s = state;
        if (s <= COMPLETING && (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING) {
            throw new TimeoutException();
        }
        return report(s);
    }

    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = FutureTask.class;
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
