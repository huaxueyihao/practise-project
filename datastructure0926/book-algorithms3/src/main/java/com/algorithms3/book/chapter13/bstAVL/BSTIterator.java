package com.algorithms3.book.chapter13.bstAVL;

import com.algorithms3.book.chapter13.warmUp.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;

public class BSTIterator {

    private Iterator<Integer> itr;

    public BSTIterator(TreeNode root) {
        ArrayList<Integer> list = new ArrayList<>();
        inOrder(root, list);
        itr = list.iterator();
    }

    private void inOrder(TreeNode p, ArrayList<Integer> list) {
        if (p != null) {
            inOrder(p.left, list);
            list.add(p.val);
            inOrder(p.right, list);
        }
    }

    public boolean hasNext() {
        return itr.hasNext();
    }

    public int next() {
        return itr.next();
    }
}
