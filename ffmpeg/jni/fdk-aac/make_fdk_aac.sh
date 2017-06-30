#! /usr/bin/env bash

echo "---------- [AAC] Config ------------"
sh ./configure_aac_android.sh

echo "---------- [AAC] make ------------"
make

echo "---------- [AAC] make install ------------"
make install

echo "---------- [AAC] Completed ------------"
