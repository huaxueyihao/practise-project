## 第5章 Java中的锁
### 5.1 Lock接口
**1 与synchronized相比**

    1.synchronized隐式的获取锁和释放锁，Lock显示的获取锁和释放锁
    2.Lock拥有获取锁与释放锁的可操作性，可中断的获取锁以及超时获取锁等synchronized关键字不剧本的同步特性
    
**2 Lock获取锁和释放锁代码示例**
    
    Lock lock = new ReentrantLock();
    lock.lock();
    try{
    } finally {
        lock.unlock();
    }  
    
    注意：获取锁不要在try块中，因为如果在获取锁时发生异常，异常抛出的同事，也会导致锁无故释放
    
**3 Lock 接口提供了synchronized关键字所不具备的主要特性**  
 
| 特性 | 描述 |
| :----| :---- | 
| 尝试非阻塞地获取锁 | 当前线程尝试获取锁，如果这一时刻锁没有被<br/>其他线程获取到，则成功获取并持有锁 | 
| 能被中断地获取锁 | 与synchronize不同，获取到锁的线程能够响应<br/>中断，当获取到锁的编程被中断时，中断异常<br/>将会抛出，同时锁会被释放 | 
| 超时获取锁 | 在指定的截止时间之前获取锁，如果截止时间到<br/>了人就无法获取锁，则返回 | 

**4 Lock的API**  

| 方法名称 | 描述 |
| :---- | :---- |
| void lock() | 获取锁，调用该方法当前线程将会获取锁，获得锁后，返回|
| void lockInterruptibly()<br/> throws InterruptedEception | 可中断的获取锁，和lock()不同在该方法响应中断，在锁获取<br/>中可以响应中断当前线程 |
| boolean tryLock() | 尝试非阻塞的获取锁，调用该方法后立刻返回，如果能够获取则返回true，否则返回false |
| boolean tryLock(long time,<br/>TimeUnit unit) throws Interrupted Exception | 超时的获取锁，当前线程在一下3中情况会返回：<br/>1 当前线程在超时时间内获取锁<br/>2 当前线程在超时时间内被中断 <br/>3 超时时间结束，返回false |
| void unlock() | 释放锁 |
| Condition newCondition()| 获取等待通知组件，该组件和当前的锁绑定，当前线程只有获得了锁，才能调用该组件的wait()方法，而调用后，当前线程将释放锁 |


### 5.2 对列同步器
**简介**

    1.对列同步器AbstractQueuedSynchronizer,用来构建锁和其他同步器的基础框架。
    2.使用一个int成员变量表示同步状态，内置FIFO对列来完成资源获取线程的排对工作。
    3.提供(getState()、setState(int newState)、compareAndSetState(int expect,int update)),来进行状态操作，保证安全改变状态
    4.同步器自身没实现任何接口，推荐子类被定义为自定义同步组件的静态内部内(比如ReentrantLock里的Sync类)
    5.同步器既可以支持独占式，也可以支持共享式地获取同步状态
    6.同步器的设计师基于模板方法模式的
 
#### 5.2.1 对列同步器的接口与示例
**1 同步器可重写的方法**  

|方法名称|描述|
|:----|:----|
|protected boolean tryAcquire(int arg)|独占式获取同步状态，实现该方法需要查询当前状态并判断同步状态是否符合预期，然后再进行CAS设置同步状态|
|protected boolean tryRelease(int arg)|独占式释放同步状态，等待获取同步状态的线程将有机会获取同步状态|
|protected int tryAcquireShared(int arg)|共享式获取同步状态，返回大于0的值，标识获取成功，泛指，获取失败 |
|protected boolean tryReleaseShare(int args)|共享式释放同步状态|
|protected boolean isHeldExclusively()|当前同步器是否在独占模式下被线程占用，一般该方法表示是否被当前线程所独占|

**2 同步器提供的模板方法**  

|方法名称|描述|
|:----|:----|
|void acquire(int arg)|独占式获取同步状态，如果当前线程获取同步状态成功，则由该方法返回，否则，将会进入同步对列等待，该方法将会调用重写的tryAcquire(int arg)方法|
|void acquireInterruptibly(int arg)|与acquire(int arg)相同，但是该方法响应中断，当前线程未获取到同步状态而进入同步对列中，如果当前线程被中断，则该方法会抛出InterruptedException返回|
|boolean tryAcquireNanos(int arg,long nanos)|在acquireInterruptibly(int arg)基础上增加了超时限制，如果当前线程在超时时间内没有获取到同步状态，那么将会返回false，如果获取到了返回true|
|void acquireShared(int arg)|共享式的获取同步状态，如果当前线程未获取到同步状态，将会进入同步对列等待，与独占式获取的主要区别是在同一时刻可以有多个线程获取到同步状态|
|void acquireSharedInterruptibly(int age)|与acquireShared(int arg)相同，该方法响应中断|
|boolean tryAcquireSharedNanos(int arg,long nanos)|在acquireSharedInterruptibly(int arg)基础上增加了超时限制|
|boolean release(int arg)|独占式的释放同步状态，该方法会在释放同步状态之后，将同步对垒中第一个节点包含的线程唤醒|
|boolean release(int arg)|共享式的释放同步状态|
|Collection<Thread> getQueuedThread()|获取等待在同步对列上的线程集合|

    同步器提供的模板方法基本上分3类：独占式获取与释放同步状态、共享式获取与释放同步状态和查询同步对垒中的等待线程情况。

#### 5.2.2 对列同步器的实现分析
**1.1同步对列**
    
    1.同步器依赖内部的同步对列（FIFO）完成同步状态的管理，
    2.当前线程获取同步状态失败时，同步器会将该线程以及等待状态信息构造成一个节点
    (Node)加入到同步对列，同时阻塞该线程
    3.当同步状态释放时，会把首节点中的线程唤醒，使其获取同步状态
    
**1.2同步对列中节点类属性与名称以及描述**  
  
|属性类型与名称|描述|
|:----|:-----|
|属性类型|描述|
|int waitStatus| 等待状态，包含如下状态。<br/> 1、CANCELLED,值为1，由于在同步对列中等待的线程等待超时或者被中断，需要从同步对列中取消等待，节点进入该状态将不会变化<br/>2、SIGNAL，值为-1，后继节点的线程处于等待状态，而当前节点的线程如果释放了同步状态或者被取消，将会通知后继节点，使后继节点的线程得以运行<br/>3、CONDITION，值为-2，节点在等待对列中，节点线程等待在Condition上，当其他线程对Condition调用了signal()方法后，该节点将会从等待对列中转移到同步对列中，加入到同步状态的获取中<br/>4、PROPAGATE，值为-3，表示下一次共享式同步状态获取将会无条件地被传播下去<br/>5、INITIAL，值为0，初始状态|
|Node prev| 前驱节点，当节点加入同步对列被设置（尾部添加）|
|Node next| 后继节点|
|Node nextWaiter|等待对列中的后继节点。如果当前节点是共享的，那么这个字段将是一个SHARED常量，也就是说节点类型（独占和共享）和等待对列中的后继节点共用同一个字段|
|Thread thread|获取同步状态的线程|

**1.3 同步对列结构图及加入过程**
    
    同步器拥有首节点(head)和尾节点(tail)，没有成功获取同步状态的线程将会成为节点加入该对列的尾部。

![avatar](images/AbstractQueuedSynchroizer_Node_Structure.jpg)
    
    当一个线程成功获取到了同步状态(或者锁)，其他线程将无法获取到同步状态而被构造成节点并加入到同步对列中，
    这个加入的过程通过CAS：compareAndSetTail(Node expect,Node update)。
    
![avatar](images/AbstractQueuedSynchronizer_add_tail.jpg)
    
    首节点是获取同步状态成功的节点，首节点的线程在释放同步状态时，将会唤醒后继节点，而后继节点将会在获取同步状态成功时将自己设置为首节点，如下图
    由于只有一个线程能够获取到同步状态，所以设置头结点不需要CAS保证

![avatar](images/AbstractQueuedSynchronizer_Node_Release_Head.jpg)


**2 独占式同步状态的获取与释放**
**2.1 acquire(int arg)方法**

    acquire(int arg)获取同步状态，对中断不敏感，获取同步状态失败进入同步对列，后续对线程进行中断操作，不会从对列中移除
    public final void acquire(int arg){
        if(!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE),arg))
            selfInterrupt();
    }
    1.tryAcquire(arg)获取线程安全的获得同步状态，失败则构造独占式(Node.EXCLUSIVE)并通过addWaiter(Node node)方法将节点加入同步对列尾部
    2.调用acquireQueued(Node node,int arg)方法，使得该节点以"死循环"的方式获取同步状态。获取不到，则阻塞节点中的线程，被阻塞的线程唤醒主要依靠前去节点的出队会阻塞线程被中断来实现。
    
**2.2 addWaiter和enq方法**

    同步器的addWaiter和enq方法
    private Node addWaiter(Node node){
        Node node = new Node(Thread.currentThread(),node);
        // 快速尝试在尾部添加
        Node pred = tail;
        if(pred != null){
            node.prev = pred;
            if(compareAndSetTail(pred,node)){
                pred.next = node;
                return node;
            }
        }
        // 用于初始化
        enq(node);
        return node;
    }
    
    private Node enq(final Node node){
        for(;;){
            Node t = tail;
            if(t == null){
                if(compareAndSetHead(new Node())){
                    tail = head;
                }
            }else{
                node.prev = t;
                if(compareAndSetTail(t,node)){
                    t.next = node;
                    return t;
                }
            }
        }
    }

**2.4 acquireQueued方法**

    节点进入到同步对列后，就进入一个自旋的过程，每个节点都在自省的观察，当条件满足的时候，就获去到了同步状态。就可以从自旋过程中退出
    
    final boolean acquireQueued(final Node node,int arg){
        boolean failed  = true;
        try{
            boolean interrupted = false;
            for(;;){
                final Node p = node.predecessor();
                if(p == head && tryAcquire(arg)){
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if(shouldParkAfterFailedAcquire(p,node) && parkAndCheckInterrupt()){
                    interrupted - true;
                }
            }
        } finally {
            if(failed){
                cancelAcquire(node);
            }
        }
    }
    
    当前线程在"死循环"中尝试获取同步状态，而只有前驱节点是头节点才能够尝试获取同步状态，原因如下？
    1.头节点是成功获取到同步状态的节点，而头结点的线程释放了同步状态之后，将会唤醒其后继节点，后继节点的线程被唤醒后需要检查自己的前驱节点是否是头结点。
    2.维护同步对列的FIFO原则。该方法中，节点自旋获取同步状态的行为如下图

![avatar](images/AbstractQueuedSynchronizer_CAS.jpg)   
    
    由于非首节点线程前驱节点出队或者被中断而从等待状态返回，随后检查自己的前驱节点是否是头结点，如果是则尝试获取同步状态。可以看到节点值之间在循环内检查
    的过程中基本不互相通信，而是简单地判断自己的前驱是否为头结点，这样使得节点的释放过程复合FIFO，且也便于对过早通知的处理（过早通知是指前驱节点不是头节
    点的线程由于终端而被唤醒）。
    
    独占式同步状态获取流程如下图
![avatar](images/AbstractQueuedSynchronizer_acquire_lock_flow.jpg)
    
**2.5 release方法**

    调用同步器的release(int arg)方法可以释放同步状态，该方法在释放了同步状态之后，会唤醒其后继结点。
    public final boolean release(int arg){
        if(tryRelease(arg)){
            Node h = head;
            if(h != null && h.waitStatus != 0){
                unparkSuccessor(h);
            }
            retrun true;
        }
        return false;
    }

**2.6 总结**

    在获取同步状态时，同步器维护一个对列，获取状态失败的线程都会被加入到对列中并在对列中进行自旋；
    移除对列的条件是前驱节点为头节点且成功获取了同步状态。在释放同步状态时，听不器调用tryReleas(int arg)
    方法释放同步状态，然后唤醒头节点的后继节点。
    
    
**3 共享式同步状态的获取与释放**

    共享式获取与独占式获取最最要的区别在与同一时刻能否有多个线程同时获取到同步状态。以文件的读写为例，
    如果一个程序在对文件进行读写操作，那么这一时刻对于该文件的写操作军备阻塞，而读操作能够同时进行。
    写操作要求对资源的独占式访问，而读操作可以共享式访问，两种不同的访问模式在同一时刻对文件或资源的
    访问情况如下图：
    
![avatar](images/AbstractQueuedSynchronizer_Shared_Exclusive_Compare.jpg)
**3.1 同步器acquireShared(int arg)方法**

    public final void acquireShared(int arg){
        if(tryAcquireShared(arg) < 0){
            doAcquireShared(arg);
        }
        private void doAcquireShared(int arg){
            final Node node = addWaiter(Node.SHARED);
            boolean failed = true;
            try{
                boolean interrupted = false;
                for(;;){
                    final Node p = node.predecessor();
                    if(p == head){
                        int r = tryAcquireShared(arg);
                        if(r >= 0){
                            setHeadAndPropagate(node,r);
                            p.next = null;
                            if(interrupted){
                                selfInterrupt();
                            }
                            failed = false;
                            return;
                        }
                    }
                    if(shouldParkAfterFailedAcquire(p,node) && parkAndCheckInterrupt()){
                        interrupted = true;
                    }
                }
            
            } finally {
                if(failed){
                    cancelAcquired(node);
                }
            }
        }
    }
    
    1.tryAcquireShared(int arg)方法返回值>=0时，获取同步状态
    2.doAcquireShared(int arg)方法在自旋过程中，若果当前节点的前驱节点为头结点时，调用tryAcquireShared(int arg)方法尝试获取同步状态
    
**3.2 releaseShared(int arg)释放同步状态方法**
  
    public final boolean releaseShared(int arg){
        if(tryReleaseShared(arg)){
            doReleaseShared();
            return true;
        }
        return false;
    }
    1.该方法用于释放同步状态，将会唤醒后续处于等待状态的节点。
    2.和独占式主要区别在于tryReleaseShared(int arg)方法必须确保同步状态线程安全释放，一般是通过循环和CAS来保证
    
**4 独占式超时获取同步状态**    
**4.1 doAcquireNanos(int arg,long nanosTimeout)**
    
       1.在指定的时间段内获取同步状态，获取到返回true，否则，返回false
       2.synchronized 响应中断，是修改中断线程中断标志位，但线程依旧被阻塞在synchronized上，等待着获取锁 。
       3.同步器提供acquireInterruptibly(int arg)方法，这个方法在等待获取同步状态时，如果当前线程被中断，会立刻返回，并抛出InterruptedException
       
       private boolean doAcquireNanos(long arg, long nanosTimeout)
               throws InterruptedException {
           if (nanosTimeout <= 0L)
               return false;
           final long deadline = System.nanoTime() + nanosTimeout;
           final Node node = addWaiter(Node.EXCLUSIVE);
           boolean failed = true;
           try {
               for (;;) {
                   final Node p = node.predecessor();
                   if (p == head && tryAcquire(arg)) {
                       // 获取到同步状态
                       setHead(node);
                       p.next = null; // help GC
                       failed = false;
                       return true;
                   }
                   // 没有获取到同步状态，检验是否超时
                   nanosTimeout = deadline - System.nanoTime();
                   if (nanosTimeout <= 0L)
                       return false;
                   if (shouldParkAfterFailedAcquire(p, node) &&
                       nanosTimeout > spinForTimeoutThreshold)
                       LockSupport.parkNanos(this, nanosTimeout);
                   if (Thread.interrupted())
                       throw new InterruptedException();
               }
           } finally {
               if (failed)
                   cancelAcquire(node);
           }
       }
 
![avatar](images/AbstractQueuedSynchronizer_Timeout_Exclusive_Flow.jpg)      
       
       
**5 自定义同步组件---TwinsLock** 
    
    设计同步工具：该工具在同一时刻，只允许之多两个线程同时访问，超过两个线程的访问将被阻塞
    1.确定其是共享式的
    2.定义资源数。至多两个线程同时访问，设置字段status(类似计数器)初始值为2，一个线程获得同步状态，status减1，该线程释放，则status加1
    
    
### 5.3 重入锁
    
    1.ReentrantLock,支持重进入,例子Mutex，不支持重入，synchronized隐式的支持重入
    2.锁获取公平性：先请求获取锁的线程优先获得满足，就是公平的，否则就不公平，不公平锁效率高，公平锁一定程度上能减少线程"饥饿"发生的概率

#### 5.3.1 实现重入锁
    
    重进入：任意线程在获取到锁之后能够再次获取该锁而不会被锁锁阻塞
    要解决以下两个问题，才能实现重进入
    1.线程再次获取锁：锁需要去识别获取锁的线程是否为当前占据锁的线程，若果是，则在次成功获取
    2.锁的最终是释放：线程重复n次去识别取了锁，状态值累加n次，被释放时，计数自减，计数等于0，表示成功释放
    
    // 非公平 加锁
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            // 同一个线程，计数器加 acquires，
            int nextc = c + acquires;
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
    
    // 释放锁
    protected final boolean tryRelease(int releases) {
        int c = getState() - releases;
        if (Thread.currentThread() != getExclusiveOwnerThread())
            throw new IllegalMonitorStateException();
        boolean free = false;
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }
    
#### 5.3.2 公平与非公平获取锁的区别
    
     
     protected final boolean tryAcquire(int acquires) {
         final Thread current = Thread.currentThread();
         int c = getState();
         if (c == 0) {
             // 区别在这里，多了个hasQueuedPredecessors()方法的判断条件
             // 表示加入同步对列中当前节点是否有前驱节点，如果该方法返回true，表示有线程比当前线程个更早地请求获取锁，因此需要等待前驱线程获取并释放锁
             if (!hasQueuedPredecessors() &&
                 compareAndSetState(0, acquires)) {
                 setExclusiveOwnerThread(current);
                 return true;
             }
         }
         else if (current == getExclusiveOwnerThread()) {
             int nextc = c + acquires;
             if (nextc < 0)
                 throw new Error("Maximum lock count exceeded");
             setState(nextc);
             return true;
         }
         return false;
     }
     
     
     // 如下，代码可以看出公平和非公平的区别
     package com.book.study.ArtOfJavaConcurrencyProgramming.chapter05;
     import org.junit.Test;
     import java.io.IOException;
     import java.util.ArrayList;
     import java.util.Collection;
     import java.util.Collections;
     import java.util.List;
     import java.util.concurrent.locks.ReentrantLock;
     
     public class FairAndUnfairTest {
     
         private static RenntrantLock2 fairLock = new RenntrantLock2(true);
     
         private static RenntrantLock2 unfairLock = new RenntrantLock2(false);
     
     
         @Test
         public void testFair() throws IOException {
             testLock(fairLock);
         }
     
         @Test
         public void testUnFair() throws IOException {
             testLock(unfairLock);
         }
     
         private void testLock(RenntrantLock2 lock) throws IOException {
             for (int i = 0; i < 5; i++) {
                 new Job(lock, "" + i).start();
             }
             System.in.read();
         }
     
         private static class Job extends Thread {
             private RenntrantLock2 lock;
     
             public Job(RenntrantLock2 lock, String name) {
                 super(name);
                 this.lock = lock;
             }
     
             public void run() {
                 for (int i = 0; i < 5; i++) {
                     lock.lock();
                     try {
                         try {
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         System.out.println("lock by [" + Thread.currentThread().getName() + "],wait by " + lock.getQueuedThreads());
                     } finally {
                         lock.unlock();
                     }
                 }
             }
     
             @Override
             public String toString() {
                 return getName();
             }
         }
     
     
         private static class RenntrantLock2 extends ReentrantLock {
             public RenntrantLock2(boolean fair) {
                 super(fair);
             }
     
             @Override
             protected Collection<Thread> getQueuedThreads() {
                 List<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
                 Collections.reverse(arrayList);
                 return arrayList;
             }
         }
     }
     
     testFair输出如下，明显等待对列中是按请求顺序进行执行的
     
     lock by [0],wait by [1, 2, 3, 4]
     lock by [1],wait by [2, 3, 4, 0]
     lock by [2],wait by [3, 4, 0, 1]
     lock by [3],wait by [4, 0, 1, 2]
     lock by [4],wait by [0, 1, 2, 3]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [1],wait by [2, 3, 4, 0]
     lock by [2],wait by [3, 4, 0, 1]
     lock by [3],wait by [4, 0, 1, 2]
     lock by [4],wait by [0, 1, 2, 3]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [1],wait by [2, 3, 4, 0]
     lock by [2],wait by [3, 4, 0, 1]
     lock by [3],wait by [4, 0, 1, 2]
     lock by [4],wait by [0, 1, 2, 3]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [1],wait by [2, 3, 4, 0]
     lock by [2],wait by [3, 4, 0, 1]
     lock by [3],wait by [4, 0, 1, 2]
     lock by [4],wait by [0, 1, 2, 3]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [1],wait by [2, 3, 4]
     lock by [2],wait by [3, 4]
     lock by [3],wait by [4]
     lock by [4],wait by []
     
     testUnFair输出，由于重入性，同一个线程会连续执行多次
     lock by [0],wait by [1, 2, 3, 4]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [0],wait by [1, 2, 3, 4]
     lock by [1],wait by [2, 3, 4]
     lock by [1],wait by [2, 3, 4]
     lock by [1],wait by [2, 3, 4]
     lock by [1],wait by [2, 3, 4]
     lock by [1],wait by [2, 3, 4]
     lock by [2],wait by [3, 4]
     lock by [3],wait by [4, 2]
     lock by [3],wait by [4, 2]
     lock by [3],wait by [4, 2]
     lock by [4],wait by [2, 3]
     lock by [4],wait by [2, 3]
     lock by [4],wait by [2, 3]
     lock by [4],wait by [2, 3]
     lock by [4],wait by [2, 3]
     lock by [2],wait by [3]
     lock by [2],wait by [3]
     lock by [2],wait by [3]
     lock by [2],wait by [3]
     lock by [3],wait by []
     lock by [3],wait by []

### 5.4 读写锁

    1.Mutex和ReentrantLock都是排他锁，同一时刻只允许一个线程通过
    2.读写锁，读锁和写锁，可以多个线程进行读，但只允许一个线程写（其他的线程，无论读和写都被阻塞）
    3.一般情况，读写锁的性能好于排它锁，多数场景是读多于写
    读写锁的特性如下
    
|特性|说明|
|:----|:----|
|公平选择性|支持非公平(默认)和公平的锁获取方式，吞吐量还是非公平优于公平|
|重进入|该锁支持冲进入，以读写线程为例；读线程在获取了读锁之后，能够再次获取读锁，而写线程在获取了<br/>写锁之后能够再次获取写锁，同时也可以获取读锁|
|锁降级|遵循获取写锁，获取读锁再释放写锁的次序，写锁能够降级为读锁|
    

### 5.4.1 读写锁的接口与示例
     
     1.ReadWriteLock 定义了获取读锁和写锁的两个方法，即readLock()和writeLock()
     2.ReentrantReadWriteLock,除了接口方法之外，还提供了便于监控内部工作状态的方法。
     
 |方法名称|描述|
 |:-----|:-----|
 |int getReadLockCount()|返回当前读锁被获取的次数。该次数不等于获取读锁的线程数，例如，仅一个<br/>线程，它连续获取(重进入)了n次读锁，那么占据读锁的线程数是1，但该方法<br/>返回n|
 |int getReadHoldCount()|返回当前线程获取读锁的次数。该方法在Java6中加入到ReentrantReadWriteLock中，<br/>使用ThreadLocal保存当前线程获取的次数，这也使java6的实现变得复杂|
 |boolean isWriteLocked()|写锁是否被获取|
 |boolean getWriteHoldCount()|返回当前写锁被获取的次数|
        
      缓存示例
      public class Cache {
      
          static Map<String, Object> map = new HashMap<>();
      
          static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
      
          static Lock r = rwl.readLock();
      
          static Lock w = rwl.writeLock();
      
          // 获取一个key对应的value
          public static final Object get(String key) {
              r.lock();
              try {
                  return map.get(key);
              } finally {
                  r.unlock();
              }
          }
      
          // 设置key对应的value，并返回旧的value
          public static final Object put(String key, Object value) {
              w.lock();
              try {
                  return map.put(key, value);
              } finally {
                  w.unlock();
              }
          }
      
          // 清空所有的内容
          public static final void clear() {
              w.lock();
              try {
                  map.clear();
              } finally {
                  w.unlock();
              }
          }
      }
      
      
### 5.4.2 读写锁的实现分析
**1 读写状态设计**

    1.ReentrantLock中自定义同步器实现，同步状态表示被一个线程重复获取的次数
    2.读写锁定义同步器需要在同步状态(一个整型变量)上维护多个线程和一个写线程的状态。
    3.如下图，将同步状态变量切分为高16位（读状态），低16位(写状态)
    4.若同步状态为S，写状态等于$&0x0000FFFF（抹掉高位的16位），读状态等于S>>>16（无符号补0右移16位）
    当写状态加1时，等于S+1，读状态加1时，等于S+(1<<16)，也就是S+0x00010000.
    5.推论:S不等于0时，当写状态($&0x0000FFFF)等于0时，则读状态(S>>>16)大于0，即读锁已被获取
    
    
![avatar](images/ReadWriteReenterantLock_state_design.jpg)

**2 写锁的获取与释放**
    
     protected final boolean tryAcquire(int acquires) {
         /*
          * Walkthrough:
          * 1. If read count nonzero or write count nonzero
          *    and owner is a different thread, fail.
          * 2. If count would saturate, fail. (This can only
          *    happen if count is already nonzero.)
          * 3. Otherwise, this thread is eligible for lock if
          *    it is either a reentrant acquire or
          *    queue policy allows it. If so, update state
          *    and set owner.
          */
         Thread current = Thread.currentThread();
         int c = getState(); // c !=0 说明有锁，需要进一步判断 是读锁还是写锁
         int w = exclusiveCount(c);// w != 0 说明是写锁
         if (c != 0) {
             // (Note: if c != 0 and w == 0 then shared count != 0)
             // 这里说明当前是读锁，w==0(且c!=0 说明读锁获取了同步状态) 说明是不存在写锁 或者（写锁是独占锁，有且只有一个线程持有同步状态）
             // 若是当前线程==同步状态的独占线程，也说明是写锁线程获得了同步状态;反之不相等,则说明是读锁
             if (w == 0 || current != getExclusiveOwnerThread())
                 return false;
             // 走到这里 说明 写锁可以获取同步状态，但是需要校验(低位16位，有个上最大值)写锁独占重入(有待确定是否是重入)次数是否超过最大值
             if (w + exclusiveCount(acquires) > MAX_COUNT)
                 throw new Error("Maximum lock count exceeded");
             // Reentrant acquire
             // 这里设置写锁的独占状态(也即是次数)
             setState(c + acquires);
             return true;
         }
         // 这里
         if (writerShouldBlock() ||
             !compareAndSetState(c, c + acquires))
             return false;
         // 设置独占线程
         setExclusiveOwnerThread(current);
         return true;
     }
     
     NonfairSync非公平锁
     final boolean writerShouldBlock() {
         return false; // writers can always barge
     }
     
     FairSync公平锁(有前置节点，则被阻塞)
     final boolean writerShouldBlock() {
         return hasQueuedPredecessors();
     }
    
**3 读锁的获取与释放** 
    
    1.读锁是一个支持重入的共享锁，它能够被多个线程同时获取
    2.读锁获取到同步状态，读状态增加，读状态是所有线程获取读锁次数的总和
    每个线程获取读锁的次数保存在ThreadLocal中，由线程自身维护
     
    protected final int tryAcquireShared(int unused) {
        /*
         * Walkthrough:
         * 1. If write lock held by another thread, fail.
         * 2. Otherwise, this thread is eligible for
         *    lock wrt state, so ask if it should block
         *    because of queue policy. If not, try
         *    to grant by CASing state and updating count.
         *    Note that step does not check for reentrant
         *    acquires, which is postponed to full version
         *    to avoid having to check hold count in
         *    the more typical non-reentrant case.
         * 3. If step 2 fails either because thread
         *    apparently not eligible or CAS fails or count
         *    saturated, chain to version with full retry loop.
         */
        Thread current = Thread.currentThread();
        int c = getState();
        // 独占状态!=0 说明有写锁，并且独占线程!=当前线程
        if (exclusiveCount(c) != 0 &&
            getExclusiveOwnerThread() != current)
            return -1;
        int r = sharedCount(c);
        // 读锁没有被阻塞，且读状态值没有超过最大值，且compareAndSetState设置读状态成功
        if (!readerShouldBlock() &&
            r < MAX_COUNT &&
            compareAndSetState(c, c + SHARED_UNIT)) {
            
            // 对每个读线程进入同步状态，重入次数维护(这里比较难理解)
            if (r == 0) {
                firstReader = current;
                firstReaderHoldCount = 1;
            } else if (firstReader == current) {
                firstReaderHoldCount++;
            } else {
                HoldCounter rh = cachedHoldCounter;
                if (rh == null || rh.tid != getThreadId(current))
                    cachedHoldCounter = rh = readHolds.get();
                else if (rh.count == 0)
                    readHolds.set(rh);
                rh.count++;
            }
            return 1;
        }
        // 首次获取读锁失败后，重试获取(这里不是很理解，后面回头过来接着看)
        return fullTryAcquireShared(current);
    }
     
**4 锁降级** 

    锁降级指写锁降级称为读锁。当前线程拥有写锁，让后将其释放，最好再获取读锁，这种分段完成的过程
    读锁不能称之为锁降级。锁降级是指把持住（当前拥有的）写锁，再获取到读锁，随后释放(先前拥有的)写锁的过程
    
    呵呵 这个真的懵逼(先放一放)

    
### 5.5 LockSupport工具
    
     LockSupport定义了一组以park开头的方法用来阻塞当前线程，以及unpark(Thread thread)方法来唤醒一个被阻塞的线程，方法如下
     
 |方法名称|描述|
 |:----|:----|
 |void park()|阻塞当前线程，如果调用unpark(Thread thread)方法或者当前线程被中断，才能从park()方法返回|
 |void parkNanos(long nanos)|阻塞当前线程，最长不超过nanos纳秒，返回条件在park()的基础上增加了超时返回|
 |void parkUntil(long deadline)|阻塞当前线程，直到deadline时间(从1970年开始到deadline时间的毫秒数)|
 |void unpark(Thread thread)|唤醒处于阻塞状态的线程thread|
       
### 5.6 Condition接口

    Condition接口提供了类似Object的监视器方法，与Lock配合可以实现等待/通知模式。
    Object的监视器方法与Condition接口的对比
    
|对比项|Object Monitor Methods|Condition|
|:----|:----|:----|
|前置条件|获取对象的锁|调用Lock.lock()获取锁，调用Lock.newCondition()获取Condition对象|
|调用方式|直接调用，如：object.wait()|直接调用，如：condition.await()|
|等待对列个数|一个|多个|
|当前线程释放锁并进入等待状态|支持|支持|
|当前线程释放锁并进入等待状态，在等待状态中不断响应中断|不支持|支持|
|当前线程释放锁并进入超时等待状态|支持|支持|
|当前线程释放锁并进入等待状态到将来的某个时间|不支持|支持|
|唤醒等待对列中的一个线程|支持|支持|
|唤醒等待对列中的全部线程|支持|支持|


#### 5.6.1 Condition接口

    Condition定义了等待/通知两种类型的方法，当前线程调用这些方法时，需要提前获取到Condition对象关联的锁。
    Condition对象是由Lock对象创建的

    Lock lock = new ReeentrantLock();
    Condition condition = lock.newCondition();
    
    public void conditionWait() throws InterruptedException{
        lock.lock();
        try{
            condition.await();
        }finally{
            lock.unlock();
        }
    }
    
    public void conditionSignal() throws InterruptedException {
        lock.lock();
        try{
            condition.signal();
            
        }finally{
            lock.unlock();
        }
    }

**Condition api 方法**  

|方法名称|描述|
|:----|:----|
|void await() throws InterruptedException| 当前线程进入等待状态直到被通知(signal)或中断，当前线程将进入运行状态且从await()方法返回的情况，包括：<br/> 其他线程调用该Condition的额signal()或signalAll()方法，而当前线程被选中唤醒<br/>其他线程(调用interrupt()方法)中断当前线程<br/> 如果当前等待线程从await()方法返回，那么表名该线程已经获取了Condition对象所对应的锁|
|void awaitUninteruptibly()|当前线程进入等待状态被通知，对中断不敏感|
|void awaitNanos(long nanosTimeout) throws InterruptedException|当前线程进入等待状态被通知、中断或者超时。返回值表示剩余的时间，如<br/>果在nanosTimeout纳秒之前被唤醒，那么返回值就是(nanosTimeout-实际耗时)。<br/>如果返回值是0或者负数，那么可以认定已经超时了|
|long awaitUntil(Date deadline) throws InterruptedException|当前线程进入等待状态直到被通知、中断或者到某个时间。如果没有到指定时间<br/>就被通知，方法返回true，否则，表示到了指定时间，方法返回false|
|void signal()|唤醒一个等待在Condition上的线程，该线程从等待方法返回前必须获得与Condition相关联的锁|
|void signalAll()|唤醒所有等待在Condition上的线程，能够从等待方法返回的线程必须获得与Condition相关联的锁|  


    public class BoundedQueue<T> {
    
        private Object[] items;
        private int addIndex, removeIndex, count;
    
        private Lock lock = new ReentrantLock();
        private Condition notEmpty = lock.newCondition();
        private Condition notFull = lock.newCondition();
    
        public BoundedQueue(int size) {
            items = new Object[size];
        }
    
        // 添加有个元素、如果数组满，则添加线程进入等待状态，直到有"空位"
    
        public void add(T t) throws InterruptedException {
            lock.lock();
            try {
                while (count == items.length) {
                    notFull.await();
                }
                if (++addIndex == items.length) {
                    addIndex = 0;
                }
                ++count;
                notEmpty.signal();
    
            } finally {
                lock.unlock();
            }
    
        }
    
        public T remove() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    notEmpty.await();
                }
                Object x = items[removeIndex];
                if (++removeIndex == items.length) {
                    removeIndex = 0;
                }
                --count;
                notFull.signal();
                return (T) x;
    
            } finally {
                lock.unlock();
            }
        }
    }
    
    当数组数量等于数组长度时，表示数组已满，则调用notFull.await()，当前线程随之释放锁并进入等待状态。
    如果数组数量不等于数组长度，表示数组未满数组，则添加元素到数组中，同时通知的鞥带在notEmpty上的线程。

#### 5.6.2 Condition的实现分析

    1.ConditionObject是同步器AbstractQueuedSynchronizer的内部类，因为condition的操作需要获取相关联的锁
    2.每个condition对象都包含着一个对列(一下成为等待对列)，该对列是Condition对象实现等待/通知功能的额关键。

**1 等待对列**
    
    1.等待对列是一个FIFO的对列，在对列中的每个节点都包含了一个线程引用，该线程就是Condition对象上的等待线程
    2.线程调用了Condition.await()方法，将会释放锁，构造节点加入等待对列并进入等待状态。
    3.同步对列和等待对列中节点类型都是同步器的静态内部类
    4.一个Condition包含由首节点fisrtWaiter和尾节点lastWaiter构成的等待对列，如下图
    5.线程调用了Condition.await()方法，已当前线程构造节点，并从尾部加入到等待对列

![avatar](images/Condition_wait_queue_01.jpg) 

    6.Object的监视器模型上，一个对象又有一个同步对列和等待对列，而并发包中的Lock(更确切的说是同步器)拥有一个同步对列和多个等待对列
    如下图：
    
![avatar](images/Condition_wait_queue_02.jpg)
    
**2 等待**  
    
    1.调用Condition的await()(或者以await开头的方法)，会使当前线程进入等待对列并释放锁，同时线程状态变为等待状态。
    2.从await()方法返回时，当前线程一定获取了Condition相关联的锁
    3.从对列(同步对列和等待对列)的角度看await(),当调用await()方法时，相当于同步对列的首节点(获取了锁的节点)移动到Condition的等待对列中。
    4.调用await()方法的线程肯定获取了锁，即同步对列首节点，该方法会将该线程构造成节点加入等待对列中，然后释放同步对列，唤醒同步对列中的后继节点，
    当前线程进入等待状态
    5.等待对列中的节点被唤醒，则唤醒节点的线程开始尝试获取同步状态。
    
    ConditionObject的await方法
    public final void await() throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        // 当前线程加入等待对列
        Node node = addConditionWaiter();
        int savedState = fullyRelease(node);
        int interruptMode = 0;
        // 释放同步状态，也就是释放锁
        while (!isOnSyncQueue(node)) {
            LockSupport.park(this);
            if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                break;
        }
        if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
            interruptMode = REINTERRUPT;
        if (node.nextWaiter != null) // clean up if cancelled
            unlinkCancelledWaiters();
        if (interruptMode != 0)
            reportInterruptAfterWait(interruptMode);
    }
    
**3 通知**   
    
    1.调用Condition的signal()方法，将会唤醒在等待对列中的等待时间最长的节点(首节点)，在
    唤醒之前，会将节点移到同步对列中，如下图
  
![avatar](images/Condition_wait_queue_03.jpg)   
    
    2.signal方法，调用该方法的线程一定获取到了关联的锁。
    
    public final void signal() {
        if (!isHeldExclusively())
            throw new IllegalMonitorStateException();
        Node first = firstWaiter;
        if (first != null)
            doSignal(first);
    }
    
    3.获取等待对列的首节点，将其移动到同步对列并使用LockSupport唤醒节点中的线程
    
![avatar](images/Condition_wait_queue_04.jpg)

    4.被唤醒的线程，将从await()方法中的while循环中退出(isOnSyncQueue(Node node)方法返回true，节点已经在同步对列中)，
    进而调用同步器的acquireQueued()方法加入到获取同步状态的竞争中
    5. signalAll()方法，相当于对等待对列中的每个节点均执行一次signal()方法，效果就是讲等待对列中所有节点全部移动到同步对列中，并唤醒每个节点的线程。  
    
 
    
    



   
    
    
    
    
    

