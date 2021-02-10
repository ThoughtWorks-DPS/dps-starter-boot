#!/usr/bin/env bash

runpath=$(dirname $0)
script=
file=""
tree=""
path="."
preserve=""

function usage {
  echo "$0 [--sed <script>] [--file <file>] [--path <path>] [--tree <tree>] [--preserve]"
  echo "  --sed         sed script to execute (${script})"
  echo "  --file        file to process (${file})"
  echo "  --tree        process all files in tree recursively (${tree})"
  echo "  --preserve    preserve translated files with this extension for debugging ($preserve)"
  echo "  --help        display this help"
}

function process_file {
  local sed=$1
  local file=$2

  if [[ $preserve != "" ]]
  then
    echo "processing [${file}]"
  fi
  sed -i "${preserve}" -f "${sed}" "${file}"
}

while [ $# -gt 0 ]
do
  case $1 in
  --sed) shift; script=$1;;
  --file) shift; file=$1;;
  --tree) shift; tree=$1;;
  --path) shift; path=$1;;
  --preserve) shift; preserve=$1;;
  --help) usage; exit 0;;
  *) usage; exit -1;;
  esac
  shift;
done

if [ ! -e "${script}" ]
then
  echo "no path to ${script}, searching in [${runpath}]"
  script="${runpath}"/"${script}"
  if [ ! -e "${script}" ]
  then
    echo "no path to ${script}, exiting..."
    exit -1
  fi
fi

if [ -e "${file}" ]
then
  process_file "${script}" "${file}"
  exit
fi

if [ -d "${path}" ]
then
  for i in "${path}"/*
  do
    if [ -f "${i}" ]
    then
      process_file "${script}" "${i}"
    fi
  done
fi

if [ -d "${tree}" ]
then
  find "${tree}" -type f -exec sed -i "${preserve}" -f "${script}" {} \;
fi
