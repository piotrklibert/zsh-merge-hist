#! /usr/bin/env bash

noti_cmd='source ~/mgmnt/commands.sh && noti -t "ZSH History Merge"'
ssh nightingale "zsh -c '$noti_cmd'"

