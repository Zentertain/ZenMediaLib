#! /usr/bin/env bash

echo "---------- [x264] config ------------"
sh ./configure_x264_android.sh

echo "---------- [x264] make ------------"
make

echo "---------- [x264] make install ------------"
make install

echo "---------- [x264] Completed ------------"