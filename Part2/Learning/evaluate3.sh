#!/bin/bash
for k in `seq 2 1 10`; do
    i=0.017
    #for i in `seq 0.01 0.02 0.16`; do
        j=0.19
        #for j in `seq 0.1 0.01 0.2`; do
            echo $i $j $k
            time java -Xmx5g -jar target/spamfilter-learning_2.10-0.1.0-jar-with-dependencies.jar ../../traindata/ runlogs/classifier-$i-$j-$k $i $j $k | tee runlogs/j48-training-$i-$j-$k
        #done
    #done
done
