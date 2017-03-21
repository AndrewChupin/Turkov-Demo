#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include "lib-base64.h"
#include "lcsx-rand.h"
#include "vssc.h"

#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "Logos", __VA_ARGS__)
#define SIZE 100

JNIEXPORT jintArray JNICALL
Java_com_example_santa_testnative_MainActivity_getRandom(JNIEnv *env, jobject instance) {

    jintArray result;
    rnd_lcsx_ctx_t rs;
    random_lcsx_init(&rs, 0, 0);

    result = (*env)->NewIntArray(env, SIZE);
    if (result == NULL) {
        return NULL; /* out of memory error thrown */
    }
    int i;
    // fill a temp structure to use to populate the java int array
    jint fill[256];
    for (i = 0; i < SIZE; i++) {
        fill[i] = random_lcsx_v32(&rs); // put whatever logic you want to populate the values here.
    }
    // move from the temp structure to the java structure
    (*env)->SetIntArrayRegion(env, result, 0, SIZE, fill);

    return result;
}


JNIEXPORT jstring JNICALL
Java_com_example_santa_testnative_MainActivity_encrypt(JNIEnv *env, jobject instance, jstring msg) {
    vssc_ctx_t ctx_c;
    vssc_ctx_t ctx_s; // Delete

    uint8_t *buf;
    uint32_t length;
    uint32_t k1 = 0x55555555;
    uint32_t k2 = 0xAAAAAAAA;

    jstring result;

    int i;
    const char *m;

    m = (*env)->GetStringUTFChars(env, msg, 0); // good

    vssc_init(&ctx_c, k1, k2);
    vssc_init(&ctx_s, k1, k2); // Delete
    length = (uint32_t) strlen(m); // good
    buf = malloc(length);

    printf("encrypt %s %d %d", m, length, length);
    vssc_encrypt(&ctx_c, (void *) m, length, buf) ;
    for (i = 0; i < length; i++) {
        printf("%X", (unsigned)buf[i]);
    }

    vssc_decrypt(&ctx_s, buf, length, NULL);
    printf("decrypt %s", buf);

    (*env)->ReleaseStringUTFChars(env, msg, m);
    result = (*env)->NewStringUTF(env, (const char *) buf); // good
    return result; // good
}

JNIEXPORT void JNICALL
Java_com_example_santa_testnative_MainActivity_decrypt(JNIEnv *env, jobject instance, jstring msg) {

    vssc_ctx_t ctx_s;
    uint8_t *buf;
    uint32_t length;
    uint32_t k1;
    uint32_t k2;
    char m[] = "";

    const char *message;
    message = (*env)->GetStringUTFChars(env, msg, 0);

    vssc_init(&ctx_s, k1, k2);
    length = sizeof(m);
    buf = malloc(length);

    vssc_decrypt(&ctx_s, buf, length, NULL);
    printf("decrypt %s", buf);

}

JNIEXPORT jstring JNICALL
Java_com_example_santa_anative_ui_activity_TestActivity_encrypt(JNIEnv *env, jobject instance,
                                                                jstring msg) {
    vssc_ctx_t ctx_c;
    vssc_ctx_t ctx_s; // Delete

    uint8_t *buf;
    jbyteArray buf1;
    uint32_t length;
    uint32_t k1 = 0x55555555;
    uint32_t k2 = 0xAAAAAAAA;

    jstring result;

    int i;
    const char *m;

    m = (*env)->GetStringUTFChars(env, msg, 0); // good

    vssc_init(&ctx_c, k1, k2);
    vssc_init(&ctx_s, k1, k2); // Delete
    length = (uint32_t) strlen(m); // good
    buf = malloc(length);
    buf1 = (*env)->NewByteArray(env, length);

    printf("encrypt %s %d %d", m, length, length);
    vssc_encrypt(&ctx_c, (void *) m, length, buf) ;
    for (i = 0; i < length; i++) {
        printf("%X", (unsigned)buf[i]);
    }


    vssc_decrypt(&ctx_s, buf, length, NULL);
    printf("decrypt1 %.*s",length, buf);

    (*env)->GetByteArrayRegion(env, buf1, 0, length, (jbyte *) buf);

    (*env)->ReleaseStringUTFChars(env, msg, m);
    result = (*env)->NewStringUTF(env, (const char *) buf); // good
    return result; // good
}