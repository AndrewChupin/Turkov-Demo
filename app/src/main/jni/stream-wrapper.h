//
// Created by Andrew Chupin on 25.03.17.
//

#ifndef NATIVE_STREAM_WRAPPER_H
#define NATIVE_STREAM_WRAPPER_H

#include <jni.h>

jbyteArray Java_com_example_santa_anative_util_algorithm_StreamCrypt_encrypt(JNIEnv *env,
jobject instance,
        jint k11,
        jint k21,
        jbyteArray msg);

jbyteArray Java_com_example_santa_anative_util_algorithm_StreamCrypt_decrypt(JNIEnv *env,
                                                                             jobject instance,
                                                                             jint k11,
                                                                             jint k21,
                                                                             jbyteArray msg);

#endif //NATIVE_STREAM_WRAPPER_H
