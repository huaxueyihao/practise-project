Stack
###1 介绍
###1.1 概念介绍
>在index.md文件里，看Stack之前，先了解Vector
###1.2 类图结构
![avatar](images/Stack.jpg)
>这张图和Vector里是同一张，因为Stack是Vector的子类

###2 源码额分析
####2.1 字段
>无
####2.2 方法
#####2.2.1 构造方法

```$xslt
// 就一个空参构造方法
public Stack() {
}
```

#####2.2.2 push(入栈，进栈)
```$xslt
// 这个没有加同步synchronized关键字，是因为addElement加了
// 进栈，加入数组末尾，这里叫栈顶
public E push(E item) {
    // 调用父类Vector的方法
    addElement(item);
    return item;
}
```
#####2.2.3 pop(查看栈顶元素)
```$xslt

public synchronized E peek() {
    int     len = size();

    if (len == 0)
        throw new EmptyStackException();
    // 父类的elementAt方法(其加了同步，锁重入性体现)，
    return elementAt(len - 1);
}
```

#####2.2.3 pop(出栈)
```$xslt
// 出栈，最后一个元素被移除
public synchronized E pop() {
    E       obj;
    int     len = size();
    // 获得栈顶元素
    obj = peek();
    // 删除(加了同步，锁重入性体现)
    removeElementAt(len - 1);

    return obj;
}
```

####2.3 小结
>栈的底层也是个动态数组，只是进栈出栈，始终操作的是最后一个元素，top其实就是elementCount，即栈顶，如下图

![avatar](images/01_stack_top.jpg)

