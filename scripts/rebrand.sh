#!/usr/bin/env bash

path="."
tlOrig='io'
orgOrig='twdps'
githubOrgOrig='ThoughtWorks-DPS'
repoNameOrig='dps-starter-boot'
githubOrg='thoughtworks'
tl='com'
org='thoughtworks'
repoName='starter-boot'
nukeGit=n
dst=
clear=n

function usage {
  echo "$0 [--path <path>] [--dst <dest path> ] [--repo <reponame>] [--gh-org <github Org name>] [--tl <toplevel>] [--org <organization>] [--orig-repo <reponame>] [--orig-gh-org <github Org name>] [--orig-tl <original toplevel>] [--orig-org <original org>] [--nuke-git] [--clear] [--test]"
  echo "  --path        path to process ($path)"
  echo "  --dst         copy to destination (no in-place mods) ($dst)"
  echo "  --repo        repository name ($repoName)"
  echo "  --gh-org      github organization name [or username] ($githubOrg)"
  echo "  --tl          top level package name ($tl)"
  echo "  --org         org level package name ($org)"
  echo "  --orig-repo   original repository name ($repoNameOrig)"
  echo "  --orig-gh-org original github organization name [or username] ($githubOrgOrig)"
  echo "  --orig-tl     original top level package name ($tlOrig)"
  echo "  --orig-org    original org level package name ($orgOrig)"
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
  --gh-org) shift; githubOrg=$1;;
  --orig-gh-org) shift; githubOrgOrig=$1;;
  --tl) shift; tl=$1;;
  --org) shift; org=$1;;
  --orig-tl) shift; tlOrig=$1;;
  --orig-org) shift; orgOrig=$1;;
  --nuke-git) nukeGit="y";;
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

githubOrgOrigLower=$(echo "${githubOrgOrig}" | tr '[:upper:]' '[:lower:]')
sedFile=`mktemp /tmp/sed.XXXXXX` || exit 1
echo "s:${orgOrig}\.${tlOrig}:${org}.${tl}:g" >> "${sedFile}"
echo "s:${tlOrig}\.${orgOrig}:${tl}.${org}:g" >> "${sedFile}"
echo "s:${tlOrig}/${orgOrig}:${tl}/${org}:g" >> "${sedFile}"
echo "s:${githubOrgOrig}:${githubOrg}:g" >> "${sedFile}"
echo "s:${githubOrgOrigLower}:${githubOrg}:g" >> "${sedFile}"
echo "s:${repoNameOrig}:${repoName}:g" >> "${sedFile}"
echo "s:${orgOrig}/di:${org}/di:g" >> "${sedFile}"
echo "s:${orgOrig}-di:${org}-di:g" >> "${sedFile}"
echo "s:\"${tlOrig}\":\"${tl}\":g" >> "${sedFile}"
echo "s:\"${orgOrig}\":\"${org}\":g" >> "${sedFile}"
echo "s:\"${origGroup}\":\"${group}\":g" >> "${sedFile}"

pwd=$(pwd)
# assume other scripts are in same dir
binDir=$(dirname "$0")
"${binDir}"/alter-path.sh --path "${path}" \
  --tl "${tl}" \
  --org "${org}" \
  --orig-tl "${tlOrig}" \
  --orig-org "${orgOrig}"
"${binDir}"/apply-sed.sh --tree "${path}" --sed "${sedFile}"

[ -e "${path}"/.git -a "${nukeGit}" = "y" ] && rm -rf "${path}"/.git
[ -e "${path}"/.git -a "${nukeGit}" = "n" ] && echo "Local .git repository still exists, consider deleting..."

cd "${pwd}"
rm "${sedFile}"
echo "new repository is in ${path}"
