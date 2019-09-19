 #!/bin/bash
ROOT=$(cd "$(dirname "$0")"; pwd)
source ${ROOT}/util.sh

cd ${ROOT}/ffmpeg-2.8.15

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
    AS=$TOOLCHAIN/bin/armv7a-linux-androideabi17-clang
    LD=$TOOLCHAIN/bin/armv7a-linux-androideabi17-clang
    HOST=arm-linux
    CPU=armv7-a
    ARCH=arm

    EXTRA_CFLAGS="-mfloat-abi=softfp -mfpu=neon -mtune=cortex-a8"
    EXTRA_CFLAGS+=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -O2"

    EXTRA_LDFLAGS="-Wl,--fix-cortex-a8"
    EXTRA_LDFLAGS+=" -march=$CPU"

elif [ ${FLAVOR} == 'armv8a' ] 
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/aarch64-linux-android-
    TRIPLE=aarch64-linux-android
    CLANG_TARGET="aarch64-none-linux-android21" 
    AS=$TOOLCHAIN/bin/aarch64-linux-android21-clang
    LD=$TOOLCHAIN/bin/aarch64-linux-android21-clang
    HOST=aarch64-linux
    CPU=armv8-a
    ARCH=arm64

    EXTRA_CFLAGS=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -O2"

    EXTRA_LDFLAGS=" -march=$CPU"

elif [ ${FLAVOR} == 'x86' ] 
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/i686-linux-android-
    CLANG_TARGET="i686-none-linux-android17"
    AS=$TOOLCHAIN/bin/i686-linux-android17-clang
    LD=$TOOLCHAIN/bin/i686-linux-android17-clang
    HOST=x86-linux
    CPU=atom
    ARCH=x86

    EXTRA_CFLAGS="-mtune=atom -msse3 -mssse3 -mfpmath=sse"
    EXTRA_CFLAGS+=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -finline-limit=64"
    EXTRA_CFLAGS+=" -O2"

elif [ ${FLAVOR} == 'x86_64' ] 
then
    CROSS_PREFIX=${TOOLCHAIN}/bin/x86_64-linux-android-
    CLANG_TARGET="x86_64-none-linux-android21"
    AS=$TOOLCHAIN/bin/x86_64-linux-android21-clang
    LD=$TOOLCHAIN/bin/x86_64-linux-android21-clang
    HOST=x86_64-linux
    CPU=atom
    ARCH=x86_64

    EXTRA_CFLAGS="-mtune=atom -msse3 -mssse3 -mfpmath=sse"
    EXTRA_CFLAGS+=" -fstack-protector -fstrict-aliasing"
    EXTRA_CFLAGS+=" -finline-limit=64"
    EXTRA_CFLAGS+=" -O2"
else
    die "Unsupported architecture."
fi

export SYSROOT=$TOOLCHAIN/sysroot
export CC="$NDK/toolchains/llvm/prebuilt/$HOST_PLATFORM/bin/clang -target $CLANG_TARGET"
export CXX="$NDK/toolchains/llvm/prebuilt/$HOST_PLATFORM/bin/clang++ -target $CLANG_TARGET"
export RANLIB=${CROSS_PREFIX}ranlib
export AR=${CROSS_PREFIX}ar
export NDK_PROJECT_PATH=.

#FFMPEG PROGRAMS
FF_PROGRAM="  --disable-programs"

#FFMPEG DOC
FF_DOC="  --disable-doc"
<<!
FF_DOC+=" --disable-htmlpages"
FF_DOC+=" --disable-manpages"
FF_DOC+=" --disable-podpages"
FF_DOC+=" --disable-txtpages"
!

#FFMPEG DEVICES
FF_DEVICE="  --disable-devices"
FF_DEVICE+=" --disable-avdevice"

#FFMPEG MUXERS 
FF_MUXER="  --disable-muxers"
FF_MUXER+=" --enable-muxer=mp4"
FF_MUXER+=" --enable-muxer=mov"
FF_MUXER+=" --enable-muxer=mp3"
FF_MUXER+=" --enable-muxer=adts"
FF_MUXER+=" --enable-muxer=h264"
FF_MUXER+=" --enable-muxer=png"
FF_MUXER+=" --enable-muxer=gif"
FF_MUXER+=" --enable-muxer=pcm*"

#FFMPEG DEMUXERS 
FF_DEMUXER="  --disable-demuxers"
FF_DEMUXER+=" --enable-demuxer=aac"
FF_DEMUXER+=" --enable-demuxer=h264"
FF_DEMUXER+=" --enable-demuxer=mov"
FF_DEMUXER+=" --enable-demuxer=mp3"
FF_DEMUXER+=" --enable-demuxer=png"
FF_DEMUXER+=" --enable-demuxer=gif"
FF_DEMUXER+=" --enable-demuxer=pcm*"

#FFMPEG ENCODERS
FF_ENCODER="  --disable-encoders"
FF_ENCODER+=" --enable-encoder=libfaac"
FF_ENCODER+=" --enable-libx264"
FF_ENCODER+=" --enable-encoder=libx264rgb"
FF_ENCODER+=" --enable-encoder=png"
FF_ENCODER+=" --enable-encoder=aac"
FF_ENCODER+=" --enable-gpl"
FF_ENCODER+=" --enable-nonfree"
FF_ENCODER+=" --enable-libfdk-aac"
FF_ENCODER+=" --enable-encoder=pcm*"

#FFMPEG DECODERS
FF_DECODER="  --disable-decoders"
FF_DECODER+=" --enable-decoder=aac"
FF_DECODER+=" --enable-decoder=h264"
FF_DECODER+=" --enable-decoder=mp3*"
FF_DECODER+=" --enable-decoder=png"
FF_DECODER+=" --enable-decoder=gif"
FF_DECODER+=" --enable-decoder=pcm*"

#FFMPEG PARSERS
FF_PARSER="  --disable-parsers"
FF_PARSER+=" --enable-parser=aac"
FF_PARSER+=" --enable-parser=h264"

#FFMPEG FILTERS
FF_FILTER="  --disable-filters"

#FFMPEG BSFS
FF_BSF="  --disable-bsfs"

#FFMPEG PROTOCOLS
FF_PROTOCOL="  --disable-protocols"
FF_PROTOCOL+=" --enable-protocol=rtmp"
FF_PROTOCOL+=" --enable-protocol=rtmpt"
FF_PROTOCOL+=" --enable-protocol=file"

#FFMPEG TOOLCHAIN
FF_TOOLCHAIN="  --enable-asm" 
FF_TOOLCHAIN+=" --enable-optimizations" 
FF_TOOLCHAIN+=" --enable-pic" 
FF_TOOLCHAIN+=" --enable-pthreads" 
FF_TOOLCHAIN+=" --enable-static" 
FF_TOOLCHAIN+=" --enable-shared" 
FF_TOOLCHAIN+=" --disable-armv5te" 
FF_TOOLCHAIN+=" --disable-armv6"
FF_TOOLCHAIN+=" --disable-armv6t2"
FF_TOOLCHAIN+=" --disable-debug" 
FF_TOOLCHAIN+=" --disable-shared" 
FF_TOOLCHAIN+=" --disable-symver" 
FF_TOOLCHAIN+=" --arch=${ARCH}" 
FF_TOOLCHAIN+=" --cpu=${CPU}" 
FF_TOOLCHAIN+=" --cross-prefix=${CROSS_PREFIX}"
<<!
FF_TOOLCHAIN+=" --cc='${CC}'"
FF_TOOLCHAIN+=" --cxx='${CXX}'"
FF_TOOLCHAIN+=" --extra-cflags=${EXTRA_CFLAGS}"
FF_TOOLCHAIN+=" --extra-ldflags=${EXTRA_LDFLAGS}"
!
FF_TOOLCHAIN+=" --ld=${LD}"
FF_TOOLCHAIN+=" --nm=${NM}"
FF_TOOLCHAIN+=" --ar=${AR}"
FF_TOOLCHAIN+=" --as=${AS}"
FF_TOOLCHAIN+=" --target-os=android"
FF_TOOLCHAIN+=" --enable-cross-compile" 
FF_TOOLCHAIN+=" --sysroot=${SYSROOT}"

X264="../x264"
FDK_AAC="../fdk-aac/install"
EXTRA_CFLAGS+=" -I${X264}"
EXTRA_CFLAGS+=" -I${FDK_AAC}/include"
EXTRA_LIBS="-L${X264}/ -lx264 -L${FDK_AAC}/lib -lfdk-aac"

./configure ${FF_PROGRAM}                      \
            ${FF_DOC}                          \
            ${FF_DEVICE}                       \
            ${FF_MUXER}                        \
            ${FF_DEMUXER}                      \
            ${FF_ENCODER}                      \
            ${FF_DECODER}                      \
            ${FF_PARSER}                       \
            ${FF_FILTER}                       \
            ${FF_BSF}                          \
            ${FF_PROTOCOL}                     \
            ${FF_TOOLCHAIN}                    \
            --cc="$CC"                         \
            --cxx="$CXX"                       \
            --extra-cflags="${EXTRA_CFLAGS}"   \
            --extra-libs="${EXTRA_LIBS}"       \
            --extra-ldflags="${EXTRA_LDFLAGS}"

if test "$?" != 0; then
    die "ERROR: failed to configure ffmpeg.${FLAVOR}"
fi

echo "BUILDING ffmpeg.${FLAVOR}"
make clean; make -j 4
if test "$?" != 0; then
    die "ERROR: failed to build ffmpeg.${FLAVOR}"
fi

cd ..
./build.sh zenffmpeg build ${FLAVOR}
