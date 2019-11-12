#! /usr/bin/env bash

set -e

scp shinigami:~/mgmnt/.zsh_history ./shin_hist
cat ~/mgmnt/zsh_history ~/mgmnt/.zsh_history shin_hist | unmetafy | mergehist  > ./hist
rm -v ./shin_hist
# scp hist shinigami:~/mgmnt/.zsh_history
# mv -v hist ~/mgmnt/zsh_history
