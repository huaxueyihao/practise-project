package com.spring.book.study.chapter07;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class PerfMonAgent {

    private static Instrumentation inst = null;

    public static void premain(String agentArgs,Instrumentation _inst){
        System.out.println("PerfMonAgent.premain() was called.");
        inst = _inst;
        ClassFileTransformer trans = new PerfMonXformer();
        System.out.println("Adding a PerfMonXformer instance to the JVM.");
        inst.addTransformer(trans);
    }


}
