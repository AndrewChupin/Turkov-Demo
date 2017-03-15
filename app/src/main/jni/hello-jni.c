#include <string.h>
#include <jni.h>

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   hello-jni/app/src/main/java/com/example/hellojni/HelloJni.java
 */
JNIEXPORT jstring JNICALL
Java_com_example_santa_anative_util_algorithm_Native_stringFromJNI( JNIEnv* env,
                                                 jobject thiz )
{
    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI ");
}
