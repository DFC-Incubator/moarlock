FROM java:8-jdk-alpine
LABEL organization="RENCI"
LABEL maintainer="michael_conway@unc.edu"
LABEL description="iRODS MoarLock."

ADD runit.sh /
ADD target/moarlock-4.2.1.0-SNAPSHOT-jar-with-dependencies.jar /
CMD ["/runit.sh"]
#CMD ["top"]


# build: docker build -t diceunc/moarlock:1.0 .

# run:  docker run -i  -p 8080:8080 -v /etc/irods-ext:/etc/irods-ext   -v /home/mcc/webdavcert:/tmp/cert --add-host irods420.irodslocal:172.16.250.101 diceunc/moarlock:1.0

#  docker run -i -v /home/mcc/temp/moarlock:/var/input  -v /home/mcc/webdavcert:/tmp/cert --add-host irods420.irodslocal:172.16.250.101 -e "host=irods420.irodslocal" -e "port=1247" -e "zone=zone1" -e "user=test1" -e "passwd=test" -e "irodsout=/zone1/home/test1/moarlock2" -e "parm1=xxx" -e "parm2=xxx" -e "parm3=xxx" -e "parm4=xxx" -e "guid=xxx"  diceunc/moarlock:1.0 file:///home/mcc/Documents/workspace-dev/moarlock/misc/mdmanifest.json 
