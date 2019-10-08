LinkedList
### 1 介绍
### 1.1 概念介绍

略

### 1.2 类图结构
![avatar](images/07_LinkedList_Structure.jpg)


### 2 源码额分析
#### 2.1 字段
> 1. transient int size = 0 ,链表大小
> 2. transient Node<E> first ,链表头部,初始值为null
> 3. transient Node<E> last ,链表尾部,初始值为null
#### 2.2 内部类

```

// 链表的节点，
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}


```

#### 2.3 方法
##### 2.3.1 构造方法

```

// 无参构造方法，说明为长度为0的链表，fisrt=last=null，若是链表长度为1，则first=last，都指向第一个Node
public LinkedList() {
}

// 传入目标集合转化为链表的1个参数的构造方法
public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);
}

public boolean addAll(Collection<? extends E> c) {
    // size = 0 ,初始size为0
    return addAll(size, c);
}

public boolean addAll(int index, Collection<? extends E> c) {
    // 校验索引的合法性
    checkPositionIndex(index);
    // 将集合转化为数组
    Object[] a = c.toArray();
    int numNew = a.length;
    if (numNew == 0)
        return false;

    // 构造两个节点，前驱(pred)和后继(succ)
    Node<E> pred, succ;
    // 若是指定的位置index与size相等，则是从size处开始，向后进行增加节点
    if (index == size) {
        // 这里后继肯定是null,前驱(即则增加开始出是最后一个节点)
        // 初始构造 index = 0 ，size = 0，所以肯定走这里的逻辑
        succ = null;
        pred = last;
    } else {
        // 否则，获得指定index处的节点先
        succ = node(index);
        pred = succ.prev;
    }
    
    // 循环将集合的元素，添加到链表中
    for (Object o : a) {
        @SuppressWarnings("unchecked") E e = (E) o;
        // 构造新的节点，新节点的后继是null
        Node<E> newNode = new Node<>(pred, e, null);
        // 若前驱pred == null,说明是链表是空的，那么将新节点赋值个first，这里的逻辑肯定是空链表
        if (pred == null)
            first = newNode;
        else
            // pred != null,说明不是空链表了，那么将前驱节点的后继succ指向新构造的节点newNode
            pred.next = newNode;
        // 将新节点赋值给前驱节点，便于下次循环将新节点添加在新的pred后面
        pred = newNode;
    }
    
    // succ == null ,说明是空链表
    if (succ == null) {
        // 那么上面循环走完后，最后一个pred赋值给last
        last = pred;
    } else {
        // 否则，走完循环的最后一个节点pred的后继要指向succ， succ的前驱指向pred，
        pred.next = succ;
        succ.prev = pred;
    }
    
    // 维护链表长度
    size += numNew;
    modCount++;
    return true;
}


// 获得指定index的节点
Node<E> node(int index) {
        // assert isElementIndex(index);
    // size >> 1相当于 size除以2，其实这里是为了提升查找速度，将链表一分为2，分成了从前往后遍历查找和从后往前查找两部分
    // 有点类似2分查找，但这里值2分了一次
    if (index < (size >> 1)) {
        // 这里从前往后查找目标节点
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        // 从后往前查找目标元素
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}

```
    
