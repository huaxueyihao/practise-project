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
 
