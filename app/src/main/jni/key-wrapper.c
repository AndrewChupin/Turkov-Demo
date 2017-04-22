#include <string.h>
#include <jni.h>
#include <stdio.h>
#include "vscb.h"
#include "key-wrapper.h"
#include "../../../../../../Library/Android/sdk/ndk-bundle/platforms/android-17/arch-arm/usr/include/android/log.h"

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
                                                                 jobject instance) {
    /* Тест 1. Сообщение m1 зашифровывается ключом k1 в контексте отправителя и
     расшифровывается в контексте получателя. Ожидается совпадение сообщений
     */
    static unsigned char m[] =
            "Lorem ipsum dolor sit amet." ;

    unsigned char k[] =
            "4E25E43D64BD3FE7938F38044434220C90B54448344DF1AE6E40CBE57B89E65C";

    vscb_ctx_t
            ctx_s,
            ctx_r;
    uint8_t *buf ;
    int i;

    i = hex2bin(k, (int) strlen(k));

    buf = (uint8_t *)malloc(i);
    memcpy(buf, k, i);
    vscb_init(&ctx_s, buf, i);

    buf = (uint8_t *)malloc(i);
    memcpy(buf, k, i);
    vscb_init(&ctx_r, buf, i);

    //dump(ctx_r.key, ctx_r.key_size);
    buf = malloc(sizeof(m));
    vscb_encrypt( &ctx_s, m, sizeof(m), buf);

    //dump(buf, sizeof(m)) ;
    vscb_decrypt( &ctx_r, buf, sizeof(m), NULL) ;
    printf("ecrypt1 %s", buf);

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