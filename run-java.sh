#!/bin/bash

if [[ "${SERVER:=tomcat}" =~ ^(jetty|netty|tomcat|undertow)$ ]]; then
  RUN_JAVA_OPTIONS="${JAVA_OPTIONS:-} -Dserver.port=${PORT:-9966} -Dspring.profiles.active=${PROFILE:=webmvc-jdbc} -XX:+PrintFlagsFinal --enable-preview -XX:StartFlightRecording:filename=/usr/local/app/dump/$SERVER-$PROFILE-recording.jfr,dumponexit=true -XX:+ExitOnOutOfMemoryError"

  echo exec java $RUN_JAVA_OPTIONS -jar $SERVER.jar "$@"
  exec java $RUN_JAVA_OPTIONS -jar $SERVER.jar "$@"
else
  echo "SERVER environment variable: '$SERVER' is not in the list: jetty|netty|tomcat|undertow"
fi
