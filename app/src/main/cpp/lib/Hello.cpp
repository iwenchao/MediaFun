//
// Created by chaos on 2023/12/12.
//

#include <jni.h>
#include "string"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_chaos_mediafun_native_NormalUserInfo_sayHello(JNIEnv *env, jobject thiz) {
    std::string str = "hello, C++";
    return env->NewStringUTF(str.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_chaos_mediafun_native_NormalUserInfo_add(JNIEnv *env, jobject thiz, jint a, jint b) {
    return  a+b;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_chaos_mediafun_C2JavaActivity_getPerson(JNIEnv *env, jobject thiz) {
    jclass  objClazz = env->FindClass("com/chaos/mediafun/native/NormalUserInfo");
    jfieldID nameField = env->GetFieldID(objClazz,"name","Ljava/lang/String;");
    jfieldID ageField = env->GetFieldID(objClazz,"age","I");
    jmethodID initMethod = env->GetMethodID(objClazz,"<init>", "(Ljava/lang/String;I)V");
    jobject personObj = env->NewObject(objClazz, initMethod, env->NewStringUTF("chaos"), 18);
    return personObj;
}