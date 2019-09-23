## 第6章 Java并发容器和框架
    
### 6.1 ConcurrentHashMap的实现原理
    
### 6.1.1 为什么要使用ConcurrentHashMap

**1 线程不安全的HashMap**
    
    执行下面代码会造成死循环(笔者没有测试出来)，原因是多线程会导致HashMap的Entry链表形成环形数据结构
    
    public class UnSafeHashMapTest {
        public static void main(String[] args) throws InterruptedException {
            HashMap<String, String> map = new HashMap<>(2);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
    
                    for (int i = 0; i < 10000; i++) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                map.put(UUID.randomUUID().toString(),"");
                            }
                        },"ftf"+i).start();
                    }
                }
            },"ftf");
            t.start();
            t.join();
        }
    } 
          
**2 效率低下的HashTable**

    HashTable 容器使用synchronized来保证线程安全，但在线程竞争激烈的情况下HashTable的效率非常低下。
    一旦有线程获得同步，则HashTable的put和get方法，其他线程均不能使用
 
**3 ConcurrentHashMap的锁分段技术**
    
    1.HashTable使用同一把锁，并发效率低下
    2.ConcurrentHashMap将容器的数据分成多段，每一段配一把锁，当访问不同段数据时，就不存在竞争了
    
### 6.1.2 ConcurrentHashMap的结构
