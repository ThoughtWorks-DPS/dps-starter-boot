#!/usr/bin/env bash

runpath=$(dirname "$0")
script=
file=""
tree=""
path=""
preserve=""

function usage {
  echo "$0 [--sed <script>] [--preserve <ext>] [--file <file>] [--path <path>] [--tree <tree>]"
  echo "  --sed         sed script to execute (${script})"
  echo "  --preserve    preserve translated files with this extension for debugging ($preserve)"
  echo "  --file        file to process (${file})"
  echo "  --path        process all files in path non-recursively (${path})"
  echo "  --tree        process all files in tree recursively (${tree})"
  echo "  --help        display this help"
  echo "  NOTE: files are processed in the order specified on the command line"
  echo "        The --sed script and --preserve flag must be specified before any files/paths"
}

function process_file {
  local sed=$1
  local file=$2

  if [[ $preserve != "" ]]
  then
    echo "processing [${file}]"
  fi
  [ -e "${file}" ] && sed -i "${preserve}" -f "${sed}" "${file}"
}

function find_script {
  local file=$1
  if [ ! -e "${file}" ]
  then
    # echo "no path to ${file}, searching in [${runpath}]"
    file="${runpath}"/"${file}"
    if [ ! -e "${file}" ]
    then
      echo "no path to sed script [${file}], exiting..."
      exit 1
    fi
  fi

  echo "${file}"
}

function process_tree {
  local path=$1

  if [ -d "${path}" ]
  then
    find "${path}" -type f  $* | while read -r i
    do
      process_file "${script}" "${i}"
    done
  fi
}

function process_path {
  local path=$1
  process_tree "${path}" -depth 1
}

while [ $# -gt 0 ]
do
  case $1 in
  --sed) shift; script=$(find_script "$1");;
  --file) shift; process_file "${script}" "$1";;
  --tree) shift; process_tree "${script}" "$1";;
  --path) shift; process_path "${script}" "$1";;
  --preserve) shift; preserve=$1;;
  --help) usage; exit 0;;
  *) usage; exit 1;;
  esac
  shift;
done

