#!/bin/sh

echo "Starting the application"
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.helmes.consent.provider.ProviderApplication"  "$@"
