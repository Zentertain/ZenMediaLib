#!/bin/bash
ROOT=$(cd "$(dirname "$0")"; pwd)
source ${ROOT}/util.sh

cd ${ROOT}/x264

if test -z ${NDK}
then
    die "ndk not found.Please set NDK environment variable properly."
fi

HOST_PLATFORM=$(probe_host_platform)
FLAVOR=$1

if test -z ${FLAVOR}
then
    die "No flavor selected.Valid architecture:armv7a armv8a x86 x86_64"
fi

TOOLCHAIN=${NDK}/toolchains/llvm/prebuilt/${HOST_PLATFORM}

if [ ${FLAVOR} == 'armv7a' ]
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/arm-linux-androideabi-
    CLANG_TARGET="armv7-none-linux-androideabi17"
    HOST=arm-linux

    EXTRA_CFLAGS="-mfloat-abi=softfp -mfpu=neon -mtune=cortex-a8"
    EXTRA_CFLAGS+=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -O2"

    EXTRA_LDFLAGS="-Wl,--fix-cortex-a8"
    EXTRA_LDFLAGS+=" -march=armv7-a"

elif [ ${FLAVOR} == 'armv8a' ] 
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/aarch64-linux-android-
    TRIPLE=aarch64-linux-android
    CLANG_TARGET="aarch64-none-linux-android21" 
    HOST=aarch64-linux

    EXTRA_CFLAGS=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -O2"

    EXTRA_LDFLAGS=" -march=armv8-a"

elif [ ${FLAVOR} == 'x86' ] 
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/i686-linux-android-
    CLANG_TARGET="i686-none-linux-android17"
    HOST=x86-linux

    EXTRA_CFLAGS="-mtune=atom -msse3 -mssse3 -mfpmath=sse"
    EXTRA_CFLAGS+=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -finline-limit=64"
    EXTRA_CFLAGS+=" -O2"

elif [ ${FLAVOR} == 'x86_64' ] 
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/x86_64-linux-android-
    CLANG_TARGET="x86_64-none-linux-android21"
    HOST=x86_64-linux

    EXTRA_CFLAGS="-mtune=atom -msse3 -mssse3 -mfpmath=sse"
    EXTRA_CFLAGS+=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -finline-limit=64"
    EXTRA_CFLAGS+=" -O2"
else
    die "Unsupported architecture."
fi

export SYSROOT=$NDK/toolchains/llvm/prebuilt/$HOST_PLATFORM/sysroot
export CC="$NDK/toolchains/llvm/prebuilt/$HOST_PLATFORM/bin/clang -target $CLANG_TARGET"
export CXX="$NDK/toolchains/llvm/prebuilt/$HOST_PLATFORM/bin/clang++ -target $CLANG_TARGET"
export RANLIB=${CROSS_PREFIX}ranlib
export AR=${CROSS_PREFIX}ar

./configure --sysroot=$SYSROOT               \
            --disable-cli                    \
            --disable-opencl                 \
            --enable-static                  \
            --enable-pic                     \
            --enable-strip                   \
            --bit-depth=8                    \
            --chroma-format=420              \
            --host=$HOST                     \
            --extra-cflags="$EXTRA_CFLAGS"   \
            --extra-ldflags="$EXTRA_LDFLAGS"

if test "$?" != 0; then
    die "ERROR: failed to configure x264.${FLAVOR}"
fi

echo "BUILDING x264.${FLAVOR}"
make clean; make -j 4
if test "$?" != 0; then
    die "ERROR: failed to build x264.${FLAVOR}"
fi
