#! /usr/bin/env bash

set -e

cd /home/cji/priv/zsh-merge-hist/

/home/cji/portless/gradle/bin/gradle installDist

./build/install/zsh-merge-hist/bin/zsh-merge-hist
