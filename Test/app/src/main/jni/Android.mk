LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := JNITestSample
LOCAL_SRC_FILES := com_example_administrator_test_utils_JniUtil.cpp
include $(BUILD_SHARED_LIBRARY)