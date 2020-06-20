#! /usr/bin/env bash

cd /home/cji/poligon/jvm/zsh-merge-hist/

# if [[ -z $NOWAIT ]]; then
#     wait=$(( $RANDOM % 60 ))m
#     echo "Waiting for $wait minutes before continuing..."
#     sleep $wait 
# fi

set -e

gradle installDist

./build/install/zsh-merge-hist/bin/zsh-merge-hist

set +e

noti_cmd='source ~/mgmnt/commands.sh && noti -t "ZSH History Merge"'
ssh nightingale "zsh -c '$noti_cmd'"

