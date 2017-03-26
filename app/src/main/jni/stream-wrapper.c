#include "stream-wrapper.h"
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include "vssc.h"
#include <android/log.h>

#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "Logos", __VA_ARGS__)


JNIEXPORT jbyteArray JNICALL
Java_com_example_santa_anative_util_algorithm_StreamCrypt_encrypt(JNIEnv *env,
                                                                 jobject instance,
                                                                 jint k11,
                                                                 jint k21,
                                                                 jbyteArray msg) {
    vssc_ctx_t ctx_c;
    jbyteArray result;
    uint8_t *buf;
    uint32_t length;

    uint32_t k1 = (uint32_t) k11;
    uint32_t k2 = (uint32_t) k21;

    jbyte *m = (*env)->GetByteArrayElements(env, msg, JNI_FALSE);
    length = (uint32_t) strlen((const char *) m);
    buf = malloc(length);
    result = (*env)->NewByteArray(env, length);

    vssc_init(&ctx_c, (uint32_t) k1, (uint32_t) k2);
    vssc_encrypt(&ctx_c, m, length, buf) ;

    (*env)->SetByteArrayRegion(env, result, 0, length, (const jbyte *) buf);
    (*env)->ReleaseByteArrayElements(env, msg, m, JNI_ABORT);

    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_example_santa_anative_util_algorithm_StreamCrypt_decrypt(JNIEnv *env,
                                                                 jobject instance,
                                                                 jint k11,
                                                                 jint k21,
                                                                 jbyteArray msg) {
    vssc_ctx_t ctx_c;
    jbyteArray result;
    uint8_t *buf;
    uint32_t length;

    uint32_t k1 = (uint32_t) k11;
    uint32_t k2 = (uint32_t) k21;

    jbyte *m = (*env)->GetByteArrayElements(env, msg, JNI_FALSE);
    length = (uint32_t) strlen((const char *) m); // good
    buf = (uint8_t *) m;
    result = (*env)->NewByteArray(env, length);

    vssc_init(&ctx_c, (uint32_t) k1, (uint32_t) k2);
    vssc_decrypt(&ctx_c, buf, length, NULL);

    (*env)->SetByteArrayRegion(env, result, 0, length, m);
    (*env)->ReleaseByteArrayElements(env, msg, m, JNI_ABORT);

    return result;
}