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
| boolean tryLock() |  |
