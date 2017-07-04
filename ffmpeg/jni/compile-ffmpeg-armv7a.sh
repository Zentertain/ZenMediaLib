#! /usr/bin/env bash

JNI_ROOT_PATH=$PWD
X264_PATH=$PWD/x264
AAC_PATH=$PWD/fdk-aac
FFMPEG_PATH=$PWD/ffmpeg-2.7.1

cd $X264_PATH
sh ./make_x264.sh
cd $JNI_ROOT_PATH

cd $AAC_PATH
sh ./make_fdk_aac.sh
cd $JNI_ROOT_PATH

cd $FFMPEG_PATH
echo "----------- [FFMPEG] Build -----------"
sh ./compile-ffmpeg.sh armv7a

echo "----------- [FFMPEG] Copy output file libzenffmpeg.so to ffmpegapi project -----------"
cp $FFMPEG_PATH/build/ffmpeg-armv7a/output/libzenffmpeg.so $JNI_ROOT_PATH/../../../ZenCommonDroid/ffmpegapi/jni/ffmpeg/ffmpeg-armv7a/output

echo "----------- [BUILD FFMPEGAPI -----]"
cd $JNI_ROOT_PATH/../../../ZenCommonDroid/ffmpegapi/
sh compile-arm-v7a.sh
cd $JNI_ROOT_PATH/
