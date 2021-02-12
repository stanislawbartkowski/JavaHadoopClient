source env.rc

download() {
  local -r IPATH=/datalake/uploaded.txt
  $CMD -a cat -f $IPATH
}

download

