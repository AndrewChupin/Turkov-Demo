#include <string.h>
#include <jni.h>
#include <stdio.h>
#include "vscb.h"
#include "key-wrapper.h"
#include <android/log.h>
//
// Created by Andrew Chupin on 25.03.17.
//
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "Logos", __VA_ARGS__)

JNIEXPORT jbyteArray JNICALL
Java_com_example_santa_anative_util_algorithm_KeyCrypter_encrypt(JNIEnv *env,
                                                                  jobject instance,
                                                                  jint k11,
                                                                  jbyteArray msg) {
    vscb_ctx_t ctx_c;
    jbyteArray result;
    uint8_t *buf;
    uint16_t length;
    uint16_t keyLength;
    uint8_t k1 = (uint8_t) k11;

    jbyte *m = (*env)->GetByteArrayElements(env, msg, JNI_FALSE);
    length = (uint16_t) strlen((const char *) m);
    buf = malloc(length);
    result = (*env)->NewByteArray(env, length);
    keyLength = 3;

    vscb_init(&ctx_c, (uint8_t *) k1, keyLength);
    vscb_encrypt(&ctx_c, m, length, buf) ;

    (*env)->SetByteArrayRegion(env, result, 0, length, (const jbyte *) buf);
    (*env)->ReleaseByteArrayElements(env, msg, m, JNI_ABORT);

    return result;
}

JNIEXPORT void JNICALL
Java_com_example_santa_anative_util_algorithm_KeyCrypter_encrypt1(JNIEnv *env,
                                                                 jobject instance,
                                                                 jint k11,
                                                                 jbyteArray msg) {
    vscb_ctx_t ctx_c;
    vscb_ctx_t ctx_s;
    uint8_t *buf;
    uint16_t keyLength;
    uint16_t length;

    unsigned char m1[] = "Lorem ipsum dolor sit amet.";
    unsigned char k1[] = "4E2";
    keyLength = 3;
    length = sizeof(m1);

    buf = malloc(length);


    vscb_init(&ctx_c, k1, keyLength);
    vscb_init(&ctx_s, k1, keyLength);
    vscb_encrypt(&ctx_c, m1, length, buf);
    for (int i = 0; i < length; i++) printf("%X", (unsigned)buf[i]);

    vscb_decrypt(&ctx_s, buf, length, NULL) ;
    printf("decrypt %s", buf);
}

JNIEXPORT jbyteArray JNICALL
Java_com_example_santa_anative_util_algorithm_KeyCrypter_decrypt(JNIEnv *env,
                                                                 jobject instance,
                                                                 jint k11,
                                                                 jbyteArray msg) {
    vscb_ctx_t ctx_c;
    jbyteArray result;
    uint8_t *buf;
    uint16_t length;
    uint16_t keyLength;

    uint8_t k1 = (uint8_t) k11;

    jbyte *m = (*env)->GetByteArrayElements(env, msg, JNI_FALSE);
    length = (uint16_t) strlen((const char *) m);
    buf = malloc(length);
    result = (*env)->NewByteArray(env, length);
    keyLength = 3;

    vscb_init(&ctx_c, (uint8_t *) k1, keyLength);
    vscb_encrypt(&ctx_c, m, length, buf);

    (*env)->SetByteArrayRegion(env, result, 0, length, (const jbyte *) buf);
    (*env)->ReleaseByteArrayElements(env, msg, m, JNI_ABORT);

    return result;
}