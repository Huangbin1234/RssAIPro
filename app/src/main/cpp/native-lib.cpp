#include <jni.h>
#include <string>

JNIEXPORT jstring JNICALL
Java_com_hb_rssai_view_common_LoadActivity_stringFromJNI(JNIEnv *env, jobject instance) {
    std::string hello = "Loading...";
    return env->NewStringUTF(hello.c_str());
}



