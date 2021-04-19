#!/usr/bin/env bash

path="."
origTl='io'
origOrg='twdps'
tl='com'
org='thoughtworks'

function usage {
  echo "$0 [--path <path>] [--tl <toplevel>] [--org <organization>] [--orig-tl <original toplevel>] [--orig-org <original org>]"
  echo "  --path        path to process ($path)"
  echo "  --tl          top level package name ($tl)}"
  echo "  --org         org level package name ($org)"
  echo "  --orig-tl     top level package name ($origTl)}"
  echo "  --orig-org    org level package name ($origOrg)"
  echo "  --help        display this help"
}

function fail {
  local msg=$*
  echo "${msg}"
  exit 1
}

function rename_dir {
  local from=$1
  local to=$2
  if [ -d "${from}" ]
  then
    mv "${from}" "${to}"
  fi
}

function process_dir {
  local path=$1

  local pwd
  pwd=$(pwd)
  cd "${path}" || fail "unable to change directory to [${path}]"

  find . -type d -depth 1 | while read -r i
  do
    local name
    name=$(basename "$i")
    process_dir "${name}"
    [[ "${name}" = "${origTl}" ]] && rename_dir "${name}" "${tl}"
    [[ "${name}" = "${origOrg}" ]] && rename_dir "${name}" "${org}"
  done
  cd "${pwd}" || fail "unable to change directory to [${pwd}]"
}

while [ $# -gt 0 ]
do
  case $1 in
  --path) shift; path=$1;;
  --tl) shift; tl=$1;;
  --org) shift; org=$1;;
  --orig-tl) shift; origTl=$1;;
  --orig-org) shift; origOrg=$1;;
  --help) usage; exit 0;;
  *) usage; exit 1;;
  esac
  shift;
done

pwd=$(pwd)

process_dir "${path}"

cd "${pwd}" || fail "unable to change directory to [${pwd}]"
