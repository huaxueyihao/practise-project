10 基本数据结构

### 10.1 栈和对列

>1.栈和对列都是动态集合 <br/>
>2.栈中，被删除的是最近插入的元素，栈实现的是一种后进先出(last-in,first-out,LIFO)策略<br/>
>3.对列中，被删除的总是在集合中存在时间最长的那个元素：对列实现的是一种先进先出(first-in,first-out,FIFO)策略<br/>

**栈**
    
>1.栈上的INSERT操作称为压入(PUSH)，无参的DELETE操作称为弹出(POP)<br/>
>2.数组S[1..n]，最多容纳n个元素，该数组有个属性S.top，指向最新插入的元素，栈中元素S[1..S.top],S[1]栈底元素，S[S.top]是栈顶元素<br/>
![avatar](images/01_stack_top.jpg)<br/>
>3.试图对空栈执行弹出，称为下溢(underflow),若是S.top超过了n，则称为栈上溢(overflow)<br/>
>4.入栈和出栈的执行时间都为O(1);

```
// 空栈判断伪代码
STACK-EMPTY(S) 
if S.top == 0
    return TRUE
else 
    return FALSE
 
// 进栈伪代码
PUSH(S,x)
S.top = S.top + 1
S[S.top] = x

// 出栈伪代码
POP(S)
if STACK-EMPTY(S) 
    error "underflow"
else S.top = S.top - 1
    return S[S.top + 1]
```

**对列**

>1.对列上的INSERT操作称为入队(ENQUEUE)，DELETE操作称为出队(DEQUEUE)<br/>
>2.对列有对头(head)和对尾(tail),当一个元素入队时，被放置在对尾，出队，则是队头元素<br/>
>3.数组Q[1..n]表示n-1元素的对列，对列有属性Q.head指向对头元素，Q.tail指向对尾元素<br/>
>4.对列Q.head,Q.head+1,...,Qtail-1，位置1紧邻在位置n后面形成一个环序，Q.head=Q.tail,对列为空，Q.head=Q.tail+1,对列是满的<br/>
>5.入队和出队的执行时间都为O(1);<br/>
![avatar](images/02_Queue_structure.jpg)

```$xslt

ENQUEUE(Q.x)
Q[Q.tail] = x
if Q.tail == Q.length 
    Q.tail = 1
else
    Q.tail = Q.tail + 1
    
    
DEQUEUE(Q)
x = Q[Q.head]
if Q.heead == Q.length
    Q.head = 1
else Q.head = Q.head + 1
return x

```



