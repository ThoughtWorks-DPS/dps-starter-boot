#!/usr/bin/env bash

path="."
origTl='io'
origOrg='twdps'
tl='com'
org='thoughtworks'
nukeGit=n
dst=
clear=n

function usage {
  echo "$0 [--path <path>] [--dst <dest path> ] [--tl <toplevel>] [--org <organization>] [--orig-tl <original toplevel>] [--orig-org <original org>] [--clear] [--test]"
  echo "  --path        path to process ($path)"
  echo "  --dest        copy to destination (no in-place mods) ($dst)"
  echo "  --tl          top level package name ($tl)}"
  echo "  --org         org level package name ($org)"
  echo "  --orig-tl     top level package name ($origTl)}"
  echo "  --orig-org    org level package name ($origOrg)"
  echo "  --nuke-git    remove .git repository folder ($nukeGit)"
  echo "  --clear       clear destination directory of current contents"
  echo "  --test        copy to temporary directory to test"
  echo "  --help        display this help"
}

while [ $# -gt 0 ]
do
  case $1 in
  --path) shift; path=$1;;
  --dst) shift; dst=$1;;
  --test) dst=`mktemp -d /tmp/rebrand.XXXXXX`;;
  --tl) shift; tl=$1;;
  --org) shift; org=$1;;
  --orig-tl) shift; origTl=$1;;
  --orig-org) shift; origOrg=$1;;
  --clear) clear="y";;
  --help) usage; exit 0;;
  *) usage; exit -1;;
  esac
  shift;
done

if [ ! -z "${dst}" ]
then
  if [ ! -d "${dst}" ]
  then
    mkdir -p "${dst}" || exit 1
  else
    [[ "${clear}" == "y" ]] && rm -rf "${dst}"/{*,.*}
  fi
  cp -r "${path}" "${dst}"
  path="${dst}"
fi

sedFile=`mktemp /tmp/sed.XXXXXX` || exit 1
echo "s:${origTl}\.${origOrg}:${tl}.${org}:g" >> "${sedFile}"
echo "s:${origTl}/${origOrg}:${tl}/${org}:g" >> "${sedFile}"
echo "s:${origOrg}/di:${org}/di:g" >> "${sedFile}"
echo "s:\"${origTl}\":\"${tl}\":g" >> "${sedFile}"
echo "s:\"${origOrg}\":\"${org}\":g" >> "${sedFile}"
echo "s:\"${origGroup}\":\"${group}\":g" >> "${sedFile}"

pwd=$(pwd)
# assume other scripts are in same dir
binDir=$(dirname "$0")
"${binDir}"/alter-path.sh --path "${path}" \
  --tl "${tl}" \
  --org "${org}" \
  --orig-tl "${origTl}" \
  --orig-org "${origOrg}"
"${binDir}"/apply-sed.sh --tree "${path}" --sed "${sedFile}"

[ -e .git -a $nukeGit = "n" ] && echo "Local .git repository still exists, consider deleting..."

cd "${pwd}"
rm "${sedFile}"
echo "new repository is in ${path}"
