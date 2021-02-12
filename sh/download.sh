source env.rc

download() {
  local -r IPATH=/apps/datalake/uploaded.txt
  $CMD cat $IPATH
}

download

