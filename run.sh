#! /usr/bin/env bash

set -e

gradle installDist

./build/install/zsh-merge-hist/bin/zsh-merge-hist
