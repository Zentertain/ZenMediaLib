export ARM_ROOT=$NDK
export PLATFORM=$NDK/platforms/android-16/arch-arm
export ARM_INC=$PLATFORM/usr/include
export ARM_LIB=$PLATFORM/usr/lib
export ARM_TOOL=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64
export ARM_LIBO=$ARM_TOOL/lib/gcc/arm-linux-androideabi/4.9
export PATH=$ARM_TOOL/bin:$PATH
export PREFIX=$PWD/build/x264/android
export STRIP=$ARM_TOOL/bin/arm-linux-androideabi-strip
./configure --disable-gpac \
--enable-pic --enable-strip \
--extra-cflags=" -I$ARM_INC -fPIC -DANDROID -fpic -mthumb-interwork -ffunction-sections -funwind-tables -fstack-protector -fno-short-enums -march=armv7-a -mtune=cortex-a8 -mfloat-abi=softfp -mfpu=neon -D__ARM_ARCH_7__ -D__ARM_ARCH_7A__  -Wno-psabi -msoft-float -mthumb -Os -fomit-frame-pointer -fno-strict-aliasing -finline-limit=64 -DANDROID  -Wa,--noexecstack -MMD -MP " \
--extra-ldflags="-Bdynamic -Wl,--no-undefined -Wl,-z,noexecstack -Wl,-z,nocopyreloc -lc -lm -ldl" \
--cross-prefix=${ARM_PRE}- \
--host=arm-linux \
--cross-prefix=$ARM_TOOL/bin/arm-linux-androideabi- \
--enable-strip \
--enable-static \
--disable-asm \
--prefix=$PREFIX \
--sysroot=$PLATFORM \
