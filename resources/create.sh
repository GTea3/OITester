#!/bin/bash
if [[ $# -lt 4 ]] ; then
	exit
fi

mkdir $1
cd $1
mkdir in out limits
for ((i=0;i<$3;i++)); do
	touch in/$2$i.in
	touch out/$2$i.out
	echo $4 > limits/$2$i.limit
done
cd ..
