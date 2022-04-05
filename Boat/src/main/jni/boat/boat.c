
#include <stdlib.h>
#include <string.h>

#include <boat.h>

ANativeWindow* boatGetNativeWindow(){
    return mBoat.window;
}

void* boatGetNativeDisplay(){
	return mBoat.display;
}

char* boatGetGLName(){
	__android_log_print(ANDROID_LOG_ERROR, "Boat", "boatGetGLName");
	return mBoat.gl_lib_name;
}

void printLog(char *str){
	__android_log_print(ANDROID_LOG_ERROR, "BoatPrintLog", "%s",str);
}

void boatGetCurrentEvent(BoatInputEvent* event){
	memcpy(event, &mBoat.current_event, sizeof(BoatInputEvent));
}

void boatSetCurrentEventProcessor(BoatEventProcessor processor){
	mBoat.current_event_processor = processor;
}

void boatSetCursorMode(int mode){
	if (mBoat.android_jvm == 0){
		return;
	}
	JNIEnv* env = 0;

	jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

	if (result != JNI_OK || env == 0){
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
		abort();
	}

	jclass class_BoatActivity = mBoat.class_BoatActivity;

	if (class_BoatActivity == 0){
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatActivity.");
		abort();
	}

	jmethodID BoatActivity_setCursorMode = mBoat.setCursorMode;

	if (BoatActivity_setCursorMode == 0){
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find method BoatActivity::setCursorMode");
		abort();
	}
	(*env)->CallVoidMethod(env, mBoat.boatActivity, BoatActivity_setCursorMode, mode);


	(*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);
}

JNIEXPORT void JNICALL Java_cosine_boat_BoatInput_send(JNIEnv* env, jclass clazz, jlong time, jint type, jint p1, jint p2){
	
	mBoat.current_event.time = time;
	mBoat.current_event.type = type;
	
	if (type == ButtonPress || type == ButtonRelease){
		mBoat.current_event.mouse_button = p1;
	}
	else if (type == KeyPress || type == KeyRelease){
		mBoat.current_event.keycode = p1;
		mBoat.current_event.keychar = p2;
	}
	else if (type == MotionNotify){
		mBoat.current_event.x = p1;
		mBoat.current_event.y = p2;
	}

	if (mBoat.current_event_processor != 0){
		mBoat.current_event_processor();
	}
	
}
void boatSetPrimaryClipString(const char* string) {
	__android_log_print(ANDROID_LOG_ERROR, "测试", "boatSetPrimaryClipString");
	if (mBoat.android_jvm == 0) {
		return;
	}
	JNIEnv* env = 0;

	jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

	if (result != JNI_OK || env == 0) {
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
		abort();
	}

	jclass class_BoatInput = mBoat.class_BoatInput;

	if (class_BoatInput == 0) {
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
		abort();
	}

	jmethodID BoatInput_setPrimaryClipString = (*env)->GetStaticMethodID(env, class_BoatInput, "setPrimaryClipString", "(Ljava/lang/String;)V");

	if (BoatInput_setPrimaryClipString == 0) {
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find static method BoatInput::setPrimaryClipString");
		abort();
	}
	(*env)->CallStaticVoidMethod(env, class_BoatInput, BoatInput_setPrimaryClipString, (*env)->NewStringUTF(env, string));

	(*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);
}
void boatSetCursorPos(int x, int y) {

}
const char* boatGetPrimaryClipString() {
	__android_log_print(ANDROID_LOG_ERROR, "测试", "boatGetPrimaryClipString");
	if (mBoat.android_jvm == 0){
		return NULL;
	}
	JNIEnv* env = 0;

	jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

	if (result != JNI_OK || env == 0) {
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
		abort();
	}

	jclass class_BoatInput = mBoat.class_BoatInput;

	if (class_BoatInput == 0) {
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
		abort();
	}

	jmethodID BoatInput_getPrimaryClipString = (*env)->GetStaticMethodID(env, class_BoatInput, "getPrimaryClipString", "()Ljava/lang/String;");

	if (BoatInput_getPrimaryClipString == 0) {
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find static method BoatInput::getPrimaryClipString");
		abort();
	}

	if (mBoat.clipboard_string != NULL) {
		free(mBoat.clipboard_string);
		mBoat.clipboard_string = NULL;
	}

	jstring clipstr = (jstring)(*env)->CallStaticObjectMethod(env, class_BoatInput, BoatInput_getPrimaryClipString);

	const char* string = NULL;
	if (clipstr != NULL) {
		string = (*env)->GetStringUTFChars(env, clipstr, NULL);
		if (string != NULL) {
			mBoat.clipboard_string = strdup(string);
		}
	}

	(*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);

	return mBoat.clipboard_string;
}
JNIEXPORT void JNICALL Java_cosine_boat_LoadMe_setGLName(JNIEnv* env, jclass clazz, jstring name) {
	char const* mName;
	mName=(*env)->GetStringUTFChars(env,name,0);
	mBoat.gl_lib_name=strdup(mName);
	(*env)->ReleaseStringUTFChars(env, name, mName);
	__android_log_print(ANDROID_LOG_ERROR, "Boat GLlibname", "%s", mBoat.gl_lib_name);
}

JNIEXPORT void JNICALL Java_cosine_boat_BoatActivity_setBoatNativeWindow(JNIEnv* env, jclass clazz, jobject surface) {
	
	mBoat.window = ANativeWindow_fromSurface(env, surface);
	__android_log_print(ANDROID_LOG_ERROR, "Boat", "setBoatNativeWindow : %p", mBoat.window);
	mBoat.display = 0;
}

JNIEXPORT void JNICALL Java_cosine_boat_BoatActivity_nOnCreate(JNIEnv *env, jobject thiz) {

    // Get the BoatActivity class
    jclass class_BoatActivity = (*env)->GetObjectClass(env, thiz);
    mBoat.class_BoatActivity = (*env)->NewGlobalRef(env, class_BoatActivity);

    // Get the setCursorMode function from the BoatActivity class
    mBoat.setCursorMode = (*env)->GetMethodID(env,mBoat.class_BoatActivity, "setCursorMode","(I)V");

    mBoat.boatActivity = (*env)->NewGlobalRef(env, thiz);
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	
	mBoat.android_jvm = vm;
	
	JNIEnv* env = 0;

	jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

	if (result != JNI_OK || env == 0){
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
		abort();
	}

	jclass class_BoatInput = (*env)->FindClass(env, "cosine/boat/BoatInput");
	if (class_BoatInput == 0){
		__android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
		abort();
	}
	mBoat.class_BoatInput = (jclass)(*env)->NewGlobalRef(env, class_BoatInput);
	
	return JNI_VERSION_1_6;
}
