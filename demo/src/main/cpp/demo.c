#include <jni.h>
#include <malloc.h>

JNIEXPORT void JNICALL
Java_com_bytedance_demo_Malloc_testMalloc(JNIEnv *env, jclass clazz) {
    for (int i = 0; i < 200; ++i) {
        malloc(102400);
    }
}