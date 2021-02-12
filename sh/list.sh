source env.rc

list() {
  local -r IPATH=/apps/datalake
  $CMD list $IPATH
}

list
