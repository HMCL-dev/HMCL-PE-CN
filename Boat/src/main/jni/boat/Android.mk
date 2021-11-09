
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := boat
LOCAL_SRC_FILES := boat.c loadme.c
LOCAL_LDLIBS    := -llog -ldl -landroid
LOCAL_CFLAGS += -O2 -std=gnu99 -DBUILD_BOAT
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/
include $(BUILD_SHARED_LIBRARY)


