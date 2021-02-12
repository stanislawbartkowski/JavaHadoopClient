source env.rc

upload() {
  local -r OPATH=/datalake/uploaded.txt
  hdfs dfs -rm -skipTrash $OPATH
  $CMD -a put -f $OPATH
  echo
  echo "Now I'm reading using hdfs command line"
  echo
  hdfs dfs -cat $OPATH
}

upload

