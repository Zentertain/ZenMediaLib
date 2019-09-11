#!/bin/bash                                                                                                                                                                                                                                 
ROOT=$(cd "$(dirname "$0")"; pwd)
source ${ROOT}/util.sh

if test -z ${NDK}
then
    die "ndk not found.Please set NDK environment variable properly."
fi

FLAVOR=$1

export NDK_PROJECT_PATH=${ROOT}
./build-x264.sh $FLAVOR
./build-fdk-aac.sh $FLAVOR
./build-ffmpeg.sh $FLAVOR
./build.sh zenffmpeg build $FLAVOR 
