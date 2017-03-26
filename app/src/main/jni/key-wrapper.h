//
// Created by Andrew Chupin on 25.03.17.
//

#ifndef NATIVE_KEY_WRAPPER_H
#define NATIVE_KEY_WRAPPER_H

#include "jni.h"

jbyteArray Java_com_example_santa_anative_util_algorithm_KeyCrypter_encrypt(JNIEnv *env,
                                                                            jobject instance,
                                                                            jint k11,
                                                                            jbyteArray msg);

jbyteArray Java_com_example_santa_anative_util_algorithm_KeyCrypter_decrypt(JNIEnv *env,
                                                                            jobject instance,
                                                                            jint k11,
                                                                            jbyteArray msg);
#endif //NATIVE_KEY_WRAPPER_H
