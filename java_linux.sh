#!/bin/sh
cc edflib.c -g -c -D_LARGEFILE64_SOURCE -D_LARGEFILE_SOURCE
ld -G -o libEDFlib.so edflib.o
rm edflib.o
java -jar jnaerator.jar edflib.h
