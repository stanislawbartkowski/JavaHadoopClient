source env.rc

upload() {
  local -r OPATH=/apps/datalake/uploaded.txt
  hdfs dfs -rm -skipTrash $OPATH
  $CMD put $OPATH
  echo
  echo "Now I'm reading using hdfs command line"
  echo
  hdfs dfs -cat $OPATH
}

upload

