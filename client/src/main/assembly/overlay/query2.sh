#!/bin/bash

PATH_TO_CODE_BASE=`pwd`

#JAVA_OPTS="-Djava.rmi.server.codebase=file://$PATH_TO_CODE_BASE/lib/jars/rmi-params-client-1.0-SNAPSHOT.jar"

MAIN_CLASS="ar.edu.itba.pod.queries.query2.Query2"

java -DtimeLogFile="time2.txt" $* -cp 'lib/jars/*' $MAIN_CLASS