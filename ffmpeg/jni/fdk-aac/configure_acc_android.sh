#!/bin/bash

CROSS_PREFIX=$NDK/toolchains/arm-linux-androideabi-4.8/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-
PLATFORM=$NDK/platforms/android-9/arch-arm
CPU=arm
PREFIX=$PWD/build/android
OPTIMIZE_CFLAGS="-mfloat-abi=softfp -mfpu=vfpv3-d16 -marm -march=armv7-a -mthumb -D__thumb__"

./configure --prefix=$PREFIX \
    --with-sysroot=$PLATFORM \
    --host=arm-linux \
    --enable-static \
    --with-pic=no \
    CC="${CROSS_PREFIX}gcc --sysroot=$PLATFORM" \
    CXX="${CROSS_PREFIX}g++ --sysroot=$PLATFORM" \
    RANLIB="${CROSS_PREFIX}ranlib" \
    AR="${CROSS_PREFIX}ar" \
    STRIP="${CROSS_PREFIX}strip" \
    NM="${CROSS_PREFIX}nm" \
    CFLAGS="-O3 $OPTIMIZE_CFLAGS --sysroot=$PLATFORM" \
    CXXFLAGS="-O3 $OPTIMIZE_CFLAGS --sysroot=$PLATFORM"