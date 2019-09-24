## 第6章 Java并发容器和框架
    
### 6.1 ConcurrentHashMap的实现原理
    
#### 6.1.1 为什么要使用ConcurrentHashMap

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
    
#### 6.1.2 ConcurrentHashMap的结构
    
    1.ConcurrentHashMap是由Segment数组结构和HashEntry数组结构组成。
    2.Segment是一种可重入锁，一个ConcurrentHashMap包含一个Segment数组，Segment的结构和HashMap类似，是一种数组和链表的结构。
    3.HashEntry用于存储键值对数据。一个Segment包含一个HashEntry数组，每个HashEntry是一个链表结构的元素，每个Segmeent守护着一个HashEntry数组里的元素
    
![avatar](images/chapter06/ConcurrentHashMap_Class_Structure.jpg)
![avatar](images/chapter06/ConcurrentHashMap_Class_Structure_02.jpg)
 
#### 6.1.3 ConcurrentHashMap的初始化(jdk1.7)
  
** 初始化segments、segmentShift和segmentMask、segment**
    
    1.通过initialCapacity,loadFactor和concurrencyLevel等几个参数来初始化segment数组、段偏移量segmentShift，段掩码segmentMask和segment里的HashEntry
    2.segments数组的长度是2的N次方，大于等于concurrencyLevel的最小的2的N次方值，如concurrencyLevel等于14，15或16，那么只能是16
    3.segmentShift用于定位参与散列运算的位数，segmentShift=32-sshift(默认情况下concurrencyLevel=16=1<<4, 可以看成concurrencyLevel= 1<< sshift,所以sshift = 4)
    4.segmentShift=32-sshift，32是由于ConcurrentHashMap里的hash()方法输出的最大数是32位的
    5.segmentMask(散列运算掩码)= ssize(segments的长度) -1; 16<= ssize <= 65536, 15=<segmentMask<=65535
    6.initialCapacity是ConcurrentHashMap初始化容量，cap是HashEntry数组的长度，cap不是1就是2的N次方
    7.loadFactor是segment负载因子
    
    
    初始化Segment
     public ConcurrentHashMap(int initialCapacity,
                                 float loadFactor, int concurrencyLevel) {
        if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0)
            throw new IllegalArgumentException();
        // concurrencyLevel 的最大值65535
        if (concurrencyLevel > MAX_SEGMENTS)
            concurrencyLevel = MAX_SEGMENTS;
        // Find power-of-two sizes best matching arguments
        int sshift = 0;
        int ssize = 1;
        // 计算出ssize刚好是大于或等于concurrencyLevel的2的N次方的最小值
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        // sshift 2的N次方的N的值
        this.segmentShift = 32 - sshift;
        // segmentMask segments数组长度-1
        this.segmentMask = ssize - 1;
        // 初始容量最大值 1073741824 2的30次方
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        // 初始容量对segments长度整除
        int c = initialCapacity / ssize;
        if (c * ssize < initialCapacity)
            ++c;
        int cap = MIN_SEGMENT_TABLE_CAPACITY;
        // cap就是segment里HashEntry数组的长度
        while (cap < c)
            cap <<= 1;
        // create segments and segments[0]
        Segment<K,V> s0 =
            new Segment<K,V>(loadFactor, (int)(cap * loadFactor),
                             (HashEntry<K,V>[])new HashEntry[cap]);
        Segment<K,V>[] ss = (Segment<K,V>[])new Segment[ssize];
        UNSAFE.putOrderedObject(ss, SBASE, s0); // ordered write of segments[0]
        this.segments = ss;
    }
    
    
    
    
    
    
    
      
    
    
