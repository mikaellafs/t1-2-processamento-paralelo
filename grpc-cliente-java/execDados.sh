#!/bin/bash

cd target/

for i  in  1 2 4 8; do
	CONTADOR=0
	TEMPO_MEDIO=0.0
	while [  $CONTADOR -lt 10 ]; do
		result=`java -jar grpc-cliente-java-1.0-SNAPSHOT-jar-with-dependencies.jar $i $1` 
		tempo=`echo "${result}" | grep -oP '[0-9]+\.[0-9]+'`
		#echo ${tempo}

		TEMPO_MEDIO=$(bc -l <<<"${tempo}/10 + ${TEMPO_MEDIO}");
		
		let CONTADOR=CONTADOR+1; 
	done
	echo "i=$i : $TEMPO_MEDIO s"
done
