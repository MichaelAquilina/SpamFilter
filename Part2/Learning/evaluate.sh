#!/bin/bash
for i in `seq 0.0 0.001 0.01`; do
    for j in `seq 0.90 0.01 1.0`; do
        echo $i $j
        java -Xmx5g -jar target/spamfilter-learning_2.10-0.1.0-jar-with-dependencies.jar ../../traindata/ runlogs/classifier-$i-$j | tee runlogs/training-$i-$j
   done
done
