//
// Created by chaos on 2023/12/12.
//

#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_chaos_mediafun_C2JavaActivity_sayHello(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Hello, C++");
}