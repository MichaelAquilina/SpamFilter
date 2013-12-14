#!/bin/bash
grep Precision j48-training-0.017-0.19-* | awk -F '-' '{print $5}' | awk -F ':' '{print $1" "$2}'|awk '{print "("$1", "$4")"}'
