#!/bin/bash
for k in `seq 1 1 1`; do
    for i in `seq 0.001 0.001 0.1`; do
        j=1
        #for j in `seq 0.5 0.1 1`; do
            echo $i $j
            java -Xmx5g -jar target/spamfilter-learning_2.10-0.1.0-jar-with-dependencies.jar ../../traindata/ runlogs/classifier-$i-$j-$k $i $j | tee runlogs/training-$i-$j-$k
        #done
    done
done
