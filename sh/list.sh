source env.rc

list() {
  local -r IPATH=/datalake
  $CMD -a list -f $IPATH
}

list
