#! /bin/bash


path="."
tl='io'
org='twdps'
pkg=""

function usage {
  local msg=$*
  echo "${msg}"
  echo "$0 [--path <path>] [--tl <toplevel>] [--org <organization>] [--pkg <package name>]"
  echo "  --path        path to create new package ($path)"
  echo "  --tl          top level package name ($tl)"
  echo "  --org         org level package name ($org)"
  echo "  --pkg         package name ($pkg)"
  echo "  --help        display this help"
}

function fail {
  local msg=$*
  echo "${msg}"
  exit 1
}

function create_lib {
  local path=$1
  local pkg=$2
  local tl=$3
  local org=$4

  local pwd
  pwd=$(pwd)
  cd "${path}" || fail "unable to change directory to [${path}]"
  mkdir -p "${pkg}"/src/{main,test}/java/"${tl}"/"${org}"/starter/boot
  mkdir -p "${pkg}"/src/{main,test}/resources
  cat << EOF > "${pkg}"/build.gradle
plugins {
    id 'starter.std.java.library-spring-conventions'
}

dependencies {
    annotationBom platform(project(':starter-bom'))
    checkstyleRules platform(project(':checkstyle-bom'))
}
EOF

  cd "${pwd}" || fail "unable to change directory to [${pwd}]"
}

while [ $# -gt 0 ]
do
  case $1 in
  --path) shift; path=$1;;
  --tl) shift; tl=$1;;
  --org) shift; org=$1;;
  --pkg) shift; pkg=$1;;
  --help) usage; exit 0;;
  *) usage "Unknown option(s) [$*]"; exit 1;;
  esac
  shift;
done

if [[ -z "${pkg}" ]]
then
  usage "Package name must be specified."
  exit 1
fi

pwd=$(pwd)

create_lib "${path}" "${pkg}" "${tl}" "${org}"

cd "${pwd}" || fail "unable to change directory to [${pwd}]"
