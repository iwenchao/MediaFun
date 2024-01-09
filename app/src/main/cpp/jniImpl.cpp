//
// Created by chaos on 2024/1/2.
//


#include <jni.h>
#include "utils/logger.h"


extern "C" {
#include <libavcodec/jni.h>
#include "libavcodec/avcodec.h"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    //    av_jni_set_java_vm(vm, reserved);
    LOG_INFO("JNI_OnLoad", "---------", "");
    return JNI_VERSION_1_4;
}

JNIEXPORT jstring JNICALL
Java_com_chaos_mediafun_FFmpegPlayerActivity_ffmpegInfo(JNIEnv *env, jobject thiz) {
    char info[2048] = {0};
    AVCodec *codec = av_codec_next(NULL);
    while (codec != NULL) {
        if (codec->decode != NULL) {
            sprintf(info, "%sdecode:", info);
        } else {
            sprintf(info, "%sencode:", info);
        }
        switch (codec->type) {
            case AVMEDIA_TYPE_AUDIO:
                sprintf(info, "%saudio:", info);
                break;
            case AVMEDIA_TYPE_VIDEO:
                sprintf(info, "%svideo:", info);
                break;
            default:
                sprintf(info, "%sunknown:", info);
                break;
        }
        sprintf(info, "%s%s\n", info, codec->long_name);
        codec = codec->next;

    }
    return env->NewStringUTF(info);
}

JNIEXPORT jint JNICALL
Java_com_chaos_mediafun_FFmpegPlayerActivity_createPlayer(JNIEnv *env, jobject thiz
                                                          ,jstring filePath,jobject surface) {

    return (jint)0;
}


}