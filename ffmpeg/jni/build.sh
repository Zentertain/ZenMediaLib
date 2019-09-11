#!/bin/bash
# @note HTC Desire / Motorola Milestone 등의 버그로 v7에서도 armeabi를 로드하여 armeabi에 이름을 바꿔 넣음
# @see http://groups.google.com/group/android-ndk/browse_thread/thread/464bab5543c672d4

# API 21이전에는 libc.so에 rand, atof등의 함수가 누락되어있어 x86_64를 제외하고는 API 16을 사용한다.

TARGET=$1
COPY=0
BUILD=0
PROFILE=0
CPU_CORE=8
PLATFORM=19

MAKE=$NDK/ndk-build

prepare()
{
	rm -r ../obj/local
}

prepare_mips()
{
	rm -r ../obj/local
}

prepare_x86()
{
	rm -r ../obj/local
}

prepare_x86_64()
{
	rm -r ../obj/local
}

x86_64()
{
	if [ $BUILD -eq 1 ]
	then
		echo -ne '\nBUILDING '$TARGET'.x86_64...\n\n'
		prepare_x86_64
		$MAKE NDK_DEBUG=0 \
				  -j$CPU_CORE \
				  -e APP_ABI=x86_64 \
				  -e APP_PLATFORM=android-$PLATFORM \
				  -e LINK_AGAINST=21-x86_64 \
				  -e APP_BUILD_SCRIPT=a-$TARGET.mk \
				  -e ARCH=atom \
				  -e NDK_APP_DST_DIR=libs/x86_64 \
				  -e NAME=$TARGET \
				  -e PROFILE=$PROFILE
	fi
}

# x86 (Atom)
x86()
{
	if [ $BUILD -eq 1 ]
	then
		echo -ne '\nBUILDING '$TARGET'.x86 Atom...\n\n'
		prepare_x86
		$MAKE NDK_DEBUG=0 \
				  -j$CPU_CORE \
				  -e APP_ABI=x86 \
				  -e APP_PLATFORM=android-$PLATFORM \
				  -e LINK_AGAINST=16-x86 \
				  -e APP_BUILD_SCRIPT=a-$TARGET.mk \
				  -e ARCH=atom \
				  -e NDK_APP_DST_DIR=libs/x86 \
				  -e NAME=$TARGET \
				  -e PROFILE=$PROFILE
	fi
}

armv8a()
{
	if [ $BUILD -eq 1 ]
	then
		echo -ne '\nBUILDING '$TARGET'.arm64...\n\n'
		prepare
		$MAKE NDK_DEBUG=0 \
				  -j$CPU_CORE \
				  -e APP_ABI=arm64-v8a \
				  -e APP_PLATFORM=android-$PLATFORM \
				  -e LINK_AGAINST=22-arm64 \
				  -e APP_BUILD_SCRIPT=a-$TARGET.mk \
				  -e ARCH=armv8-a \
				  -e VFP=neon \
				  -e V=1\
				  -e NDK_APP_DST_DIR=libs/arm64-v8a \
				  -e NAME=$TARGET \
				  -e PROFILE=$PROFILE
	fi

	if [ $COPY -eq 1 ]
	then
		cp libs/arm64-v8a/lib$TARGET.so libs/output/videoplayer/arm64-v8a/
	fi
}

armv7a()
{
	if [ $BUILD -eq 1 ]
	then
		echo -ne '\nBUILDING '$TARGET'.armv7a...\n\n'
		prepare
		$MAKE NDK_DEBUG=0 \
				  -j$CPU_CORE \
				  -e APP_ABI=armeabi-v7a \
				  -e APP_PLATFORM=android-$PLATFORM \
				  -e LINK_AGAINST=16-arm \
				  -e APP_BUILD_SCRIPT=a-$TARGET.mk \
				  -e ARCH=armv7-a \
				  -e VFP=neon \
				  -e NDK_APP_DST_DIR=libs/armeabi-v7a \
				  -e NAME=$TARGET \
				  -e PROFILE=$PROFILE
	fi

	if [ $COPY -eq 1 ]
	then
		cp libs/armeabi-v7a/neon/lib$TARGET.so libs/output/videoplayer/armeabi-v7a/
	fi
}

# @note 'then' should appear next line of [] block. 'do' for 'for' seems to be same.. i don't know why ..
#if [ $# -eq 1 ]
#then
#	COPY=1
#	BUILD=1
#	v7a
#else
	for p in $*
	do
		case "$p" in
			copy)
				COPY=1 ;;
			build)
				BUILD=1 ;;
			x86_64)
				x86_64 ;;
			x86)
				x86 ;;
			armv8a)
				armv8a;;
			armv7a)
				armv7a;;
			profile)
				PROFILE=1 ;;
			21)
				PLATFORM=21 ;;
			4.8)
				export NDK_TOOLCHAIN_VERSION=4.8 ;;
		esac
	done
#fi
