#!/bin/sh
OPID=`date +%s`
LEVELS=$1
DIRSPERLEVEL=$2
FILESPERLEVEL=$3
FILELENGTH=$4
BLOCKSIZE=$5
PASSNUM=$6
export ETCDCTL_API=3
echo "{\"opID\":\"$OPID\",\"cmd\":\"generate\",\"levels\":\"$LEVELS\",\"dirsPerLevel\":\"$DIRSPERLEVEL\",\"filesPerLevel\":\"$FILESPERLEVEL\",\"fileLength\":\"$FILELENGTH\",\"blockSize\":\"$BLOCKSIZE\",\"passNum\":\"$PASSNUM\"}" | etcdctl put /kibishii/control --endpoints=http://etcd-client:2379
STATUS="running"
while [ "$STATUS" = 'running' ]
do
	sleep 10
	STATUS=`etcdctl get /kibishii/ops/$OPID --endpoints=http://etcd-client:2379 --print-value-only | jq ".status" | sed -e 's/"//g'`
done
echo $STATUS
if [ "$STATUS" = 'success' ]
then
	exit 0
fi
exit 1

