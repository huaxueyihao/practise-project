package com.algorithms3.book.chapter13.bstAVL;

import com.algorithms3.book.chapter13.bstAVL.AVLEntry;

import java.util.Comparator;

public class AVLMap<K,V> {

    private int size;

    private AVLEntry<K,V> root;

    private Comparator<K> comp;

    private int compare(K a,K b){
        if(comp != null){
            return comp.compare(a,b);
        }else{
            Comparable<K> c = (Comparable<K>) a;
            return c.compareTo(b);
        }
    }

    public AVLMap(Comparator<K> comp) {
        this.comp = comp;
    }

    public AVLMap() {
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public V put(K key ,V value){
        if(root == null){
            root = new AVLEntry<>(key,value);
            size++;
        }else{
            AVLEntry<K,V> p = root;

            while (p!= null){
                int compareResult = compare(key,p.key);
                if(compareResult == 0 ){
                    p.setValue(value);
                    break;
                }else if(compareResult < 0){
                    if(p.left == null){
                        p.left = new AVLEntry<>(key,value);
                        size++;
                        break;
                    }else{
                        p = p.left;
                    }

                }else {
                    if(p.right == null){
                        p.right = new AVLEntry<>(key,value);
                        size++;
                        break;
                    }else{
                        p = p.right;
                    }
                }

            }
        }
        return value;
    }

}
