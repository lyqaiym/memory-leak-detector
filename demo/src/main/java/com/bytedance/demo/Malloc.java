package com.bytedance.demo;

public class Malloc {
    static {
        System.loadLibrary("demo");
    }
    static native void testMalloc();
}
