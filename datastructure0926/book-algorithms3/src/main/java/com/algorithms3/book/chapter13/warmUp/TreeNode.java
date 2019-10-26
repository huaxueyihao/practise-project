package com.algorithms3.book.chapter13.warmUp;

public class TreeNode {

    public int val;

    public TreeNode left;
    public TreeNode right;

    TreeNode(int x) {
        val = x;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public TreeNode() {
    }
}
