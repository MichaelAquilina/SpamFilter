#!/bin/bash
for i in `seq 0.0 0.001 0.02`; do
    for j in `seq 0.5 0.1 1`; do
        echo $i $j
        java -Xmx5g -jar target/spamfilter-learning_2.10-0.1.0-jar-with-dependencies.jar ../../traindata/ runlogs/classifier-$i-$j $i $j | tee runlogs/training-$i-$j
    done
done
