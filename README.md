# moarlock
post-analysis staging and provenance (demo)


## input parameters

* mounted volume reflecting physical path (should there be multiples allowed?)
* GUID representing analysis
* list of params as kvps?


## actions

* stage mounted output dir back to iRODS given output parent path, all files in output volume added
* if a metadatamanifest.json file is available, add these avus to spedified output dir. AVUs are given relative to the iRODS output directory
* add fixed AVUS reflecting GUID, parameters, other provenance
	
	* attr | val | iRODS:Analysis:GUID
	* parmName | parmVal | iRODS:Analsys:Param
	...etc
	



## target docker run

docker build -t diceunc/moarlock:1.0 .

docker run -rm -v irodsabspathtoparent:/var/input-e "host=xxx" -e "port=xxx" -e "zone=xxx" -e "user=xxx" -e "passwd=xxxx" -e "irodsout=yyy" -e "parm1=xxx" -e "parm2=xxx" -e "parm3=xxx" -e "parm4=xxx" -e "guid=xxx"  diceunc/moarlock:1.0 








## sample run

docker run -i -v /home/mcc/temp/moarlock:/var/input  -v /home/mcc/webdavcert:/tmp/cert --add-host irods420.irodslocal:172.16.250.101 -e "host=irods420.irodslocal" -e "port=1247" -e "zone=zone1" -e "user=test1" -e "passwd=test" -e "irodsout=/zone1/home/test1/moarlock1" -e "parm1=xxx" -e "parm2=xxx" -e "parm3=xxx" -e "parm4=xxx" -e "guid=xxx"  diceunc/moarlock:1.0 

	
	

