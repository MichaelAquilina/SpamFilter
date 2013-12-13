#!/bin/bash
grep '^Recall' training-0.0*-0.19-1 | awk -F '-' '{print $2 " " $4}' | awk '{print "("$1", "$4")"}'
