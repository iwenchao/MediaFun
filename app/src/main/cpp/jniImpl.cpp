//
// Created by chaos on 2024/1/2.
//


#include <jni.h>
#include "utils/logger.h"

extern "C" {
#include <libavcodec/jni.h>

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
//    av_jni_set_java_vm(vm, reserved);
    LOG_INFO("JNI_OnLoad", "---------", "");
    return JNI_VERSION_1_4;
}

}