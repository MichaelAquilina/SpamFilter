#!/bin/bash
grep ^Recall training-0.0-*-1 | awk -F '-' '{print $3 " " $4}' | awk '{print "("$1", "$4")"}'
