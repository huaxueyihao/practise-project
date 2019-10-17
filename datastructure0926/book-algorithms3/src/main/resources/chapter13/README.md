红黑树

> 红黑树(red-black-tree)是许多"平衡"搜索树种的一种，可以保证在最坏的情况下基本动态集合操作的时间复杂度为O(lgn)

### 13.1 红黑树的性质

> 树中每个节点包含5个属性：color，key，left，right和p。如果一个结点没有子结点或父结点，则该结点的相应指针属性为NIL。
> 红黑树的性质：    
> 1.每个结点或是红色的，或是黑色的
> 2.根结点是黑色的
> 3.每个叶结点(NIL)是黑色的
> 4.如果一个结点的红色的，则它的两个子结点都是黑色的
> 5.对每个结点，从该结点到其后代叶结点的简单路径上，均包含相同数目的黑色结点。

![avatar](images/01_red_black_tree.jpg)

> 从某个结点x出发(不含该结点)到达一个叶结点的任意一条简单路径上的黑色结点个数称为该结点的<strong font-color="black">黑高</strong>,记为bh(x)。
> 从某个结点出发的所有下降到其叶结点的简单路径的黑结点个数都相同。于是红黑树的黑高为其根结点的黑高
> 定理：一颗有n个内部结点的红黑树的高度至多为2lg(n+1)
> 由定理可知，动态集合操作SEARCH、MINIMUM、MAXIMUM、SUCCESSOR和PREDECESSOR可在红黑树上在O(lgn)时间内执行


### 13.2 旋转

> 指针结构的修改是通过旋转(ratation)来完成的，这是一种能保持二叉搜索树性质的搜索树局部操作。
> 左旋和右旋。当在某个节点x上做左旋时，假设它的右孩子为y而不是T.nil；x可以为其右孩子不是T.nil结点
  的树内任意结点。左旋以x到y的链为"支轴"进行。它使y称为该子树新的根结点，x成为y的左孩子，y的左孩子成为x的右孩子

![avatar](images/02_left_right_ratation.jpg)

```
// 左旋:以x的右孩子y为结点，进行逆时针旋转，旋转后x成为y结点的左孩子，y的左孩子成为了x的右孩子
LEFT-ROTATE(T,x)
y = x.right                 // 拿到结点x的右孩子y这个是要左旋的目标结点，
x.right = y.left            // 将旋转的目标节点y的左孩子当做x的右孩子
if y.left != T.nil          // y的左孩子不是哨兵的话(其实相当于非空节点)，将y的左孩子的父结点指针指向结点x
    y.left.p = x            
y.p = x.p                   // 进行旋转，y的父结点指向x的父结点
if x.p == T.nil             // x的父结点是哨兵，说明x原来是根结点，则将根结点指向y
    T.root = y
elseif x == x.p.left        // 判断x原来若是左结点，这x的父结点的左孩子指向y
    x.p.left = y
else x.p.right = y          // 否则x原来是右结点，则x的父结点的右孩子指向y
y.left = x                  // 将y的左孩子指向x，及x的父结点指向y
x.p = y

// 右旋:将以目标结点的左孩子为支轴，顺时针旋转，旋转后目标节点变为左孩子结点的右节点
RIGHT-ROTATE(T,y)
x = y.left
y.left = x.right
if x.right != nil
    x.right.p = y
x.p = y.p
if y.p == T.nil
    T.root = x
elseif y = y.p.left
    y.p.left = x
else 
    y.p.right = x    
x.right = y
y.p = x

```
![avatar](images/03_left_ratation.jpg)

### 13.3 插入

```
RB-INSERT(T,z)
y = T.nil
x = T.root
// 循环找出z的合适的父结点
while x != T.nil
    y=x 
    if z.key < x.key
    x = x.left
    else x = x.right
z.p = y
// 下面对z进行插入操作
if y == T.nil
    y.left=z
elseif z.key < y.key
    y.left = z
else y.right = z
z.left = T.nil
z.right = T.nil
z.color = RED
RB-INSERT-FIXUP(T,z)


// 保证红黑树性质的方法
RB-INSERT-FIXUP(T,z)
while z.p.color == RED
    if z.p == z.p.p.left
        y = z.p.p.right
        if y.color == RED
            z.p.color = BLACK
            y.color = BLACK
            z.p.p.color = RED
            z = z.p.p
         else if z == z.p.right
            z = z.p
            LEFT-ROTATE(T,z)
         z.p.color = BLACK
         z.p.p.color = RED
         RIGHT-ROTATE(T,z.p.p)
    else (same as then clause with "right" and "left" exchanged)
T.root.color = BLACK

```
![avatar](images/04_rb_insert_fixup.jpg)

> 插入情况：<br/>
> 情况1：z的叔结点是红色的。这种情况在z.p和y都是红色时发生。因为z.p.p是黑色而的，所以将z.p和y都着为黑色，以此解决z和z.p都是红色的问题，<br/>
  将z.p.p着为红色以保持性质5。然后，把z.p.p作为新结点z来重复while循环。指针z在书中上移两层。<br/>

![avatar](images/05_rb_insert_fixup_01.jpg)

> 情况2：z的叔结点y是黑色的且z是一个又孩子<br/>
> 情况3：z的叔结点y是黑色的且z是一个左孩子<br/>
> 在情况2和情况3中，z的叔结点y是褐色的。通过z是z.p的右孩子还是左孩子来区别这两种情况。在情况2中，结点z是它的父结点的右孩子。可以立即使用一个<br/>
  无论是直接进入情况2，还是通过情况3进入情况2，z的树节点y重视黑色的，因为否则就要执行情况1.此外，结点z.p.p存在，因为已经推断在执行第2行和第3行<br/>
  时该节点存在，且在第10行将z往上移一层，然后再第11行将z往下移一层之后，z.p.p的身份保持不变。在情况3中，改变某些结点的颜色并做一次右旋，以保持性质5.<br/>
  这样，由于在一行中不再有两个红色结点，所有处理到此完毕。因为此时z.p是黑色的，所以无需再执行一次while循环。

![avatar](images/06_rb_insert_fixup_02.jpg)


### 13.4 删除

> 从一颗红黑树中删除结点的过程基于TREE-DELETE而来的。首先，需要特别设计一个供TREE-DELETE调用的子过程TRANSPLANT，并将其应用到红黑树上。

```

RB-TRANSPLANT(T,u,v)
if u.p == T.nil
    T.root = v
elseif u == u.p.left
    u.p.left = v
else 
    u.p.right = v
v.p = u.p

```

```
RB-DELETE(T,z)
y = z
y-original-color = y.color
if z.left == T.nil
    x = z.right
    RB-TRANSPLANT(T,z,z.right)
elseif z.right == T.nil
    x = z.left
    RB-TRANSPLANT(T,z,z.left)
else y = TREE-MINIMUM(z.right)        # 找到z的后继，即右子树中的最小的结点
    y-original-color = y.color        # 记下后继的颜色
    x = y.right                       # 后继节点的右子结点
    if y.p == z                       # 若后继节点，刚好是z的子结点，则
        x.p = y                       # 
    else RB-TRANSPLANT(T,y,y.right)   # 先将后继y节点的右子结点变成y的兄弟结点(),
        y.right = z.right             # 后继y的右子节点指向z的右子节点
        y.right.p = y                 # 将z的右子结点的父结点指向y
    RB-TRANSPLANT(T,z,y)              # 将y结点变为z节点的兄弟结点
    y.left = z.left                   # 将z的左子结点赋给y的左子结点
    y.left.p = y                      # 将z的左子结点的父结点指向y，这一行和上一行是为了将z的左子树移给y
    y.color = z.color
if y-original-color == BLACK
    RB-DELETE-FIXUP(T,x)


```
















