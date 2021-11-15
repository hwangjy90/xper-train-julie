/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_xper_acq_comedi_ComediAnalogStreamingDevice */

#ifndef _Included_org_xper_acq_comedi_ComediAnalogStreamingDevice
#define _Included_org_xper_acq_comedi_ComediAnalogStreamingDevice
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nStart
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nStart
  (JNIEnv *, jobject, jobject);

/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nStop
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nStop
  (JNIEnv *, jobject, jobject);

/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nCreateTask
 * Signature: (Ljava/lang/String;IDI)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nCreateTask
  (JNIEnv *, jobject, jstring, jint, jdouble, jint);

/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nDestroy
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nDestroy
  (JNIEnv *, jobject, jobject);

/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nCreateChannels
 * Signature: (Ljava/nio/ByteBuffer;ISDDLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nCreateChannels
  (JNIEnv *, jobject, jobject, jint, jshort, jdouble, jdouble, jstring);

/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nConfigTask
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nConfigTask
  (JNIEnv *, jobject, jobject);

/*
 * Class:     org_xper_acq_comedi_ComediAnalogStreamingDevice
 * Method:    nScan
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_org_xper_acq_comedi_ComediAnalogStreamingDevice_nScan
  (JNIEnv *, jobject, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
