#!/bin/sh

if [ -f /tmp/cert/server.crt ];
then
   echo "Cert will be imported"
   keytool -delete -noprompt -alias mycert -keystore /usr/lib/jvm/default-jvm/jre/lib/security/cacerts -storepass changeit
   keytool -import -trustcacerts -keystore /usr/lib/jvm/default-jvm/jre/lib/security/cacerts -storepass changeit -noprompt -alias mycert -file /tmp/cert/server.crt
else
   echo "No cert to import"
fi

echo "running moarlock"

echo $irodsout
echo $host
echo $port
echo $zone
echo $user

java -jar moarlock-4.2.1.0-SNAPSHOT-jar-with-dependencies.jar "${irodsout}" "${host}" "${port}" "${zone}" "${user}" "${passwd}"  

