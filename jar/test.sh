HADOOP_CLIENT=/usr/hdp/current/hadoop-client

list() {
   java -cp HdfsClient.jar:$HADOOP_CLIENT/*:$HADOOP_CLIENT/client/*  Main list /tmp
}

cattxt() {
  local -r TMP=`mktemp`
  local -r IPATH=test.txt
  hdfs dfs -rm $IPATH
  echo "Hello, I was just created from command line" >$TMP
  echo "What about you?" >>$TMP
  hdfs dfs -copyFromLocal $TMP $IPATH
  echo "File has been created by hdfs command line"
  echo "Now test how it is read by Hadoop client"
  echo
  rm $TMP
  java -cp HdfsClient.jar:$HADOOP_CLIENT/*:$HADOOP_CLIENT/client/*  Main cat $IPATH
}

puttxt() {
  local -r OPATH=out.txt
  hdfs dfs -rm $OPATH
  java -cp HdfsClient.jar:$HADOOP_CLIENT/*:$HADOOP_CLIENT/client/*  Main put $OPATH
  echo 
  echo "Now I'm reading using hdfs command line"
  echo
  hdfs dfs -cat $OPATH
}

#list
#cattxt
puttxt
