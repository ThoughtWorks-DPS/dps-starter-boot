#!/usr/bin/env bash

# regex to validate in commit msg
commit_regex='(\[ThoughtWorks-DPS\/dps-multi-module-starterkit-java#[0-9]+])'
error_msg="Aborting commit. Your commit message is formatted incorrectly ex. [ThoughtWorks-DPS/dps-multi-module-starterkit-java#17] message"

if ! grep -iqE "$commit_regex" "$1"; then
    echo "$error_msg" >&2
    exit 1
fi
