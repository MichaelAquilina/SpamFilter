#!/bin/bash
grep '^Negative Recall' training-0.0*-1-1 | awk -F '-' '{print $2 " " $4}' | awk '{print "("$1", "$5")"}'
