# ZSH-MERGE-HIST

### What?

It's a simple command-line utility for merging extended ZSH history files from
different sources, while keeping the entries properly ordered by date.

### Why?

In my home LAN I have a few servers (made out of old laptops and Raspberries)
with which I interact via `SSH` and `ZSH`. I use `fzf` to pimp my `Ctrl + R`
(along with setting `HISTSIZE` to a very large value - currently one million),
which makes it very easy to find the command I need even if I only remember
small parts of the command. More precisely, it's easy to find the command if it
was executed on the machine I'm currently logged into, which resulted in a lot
of irritating copy&paste in cases where I needed to execute the same command on
more than 2-3 servers.

### How?

This utility is meant to be run from CRON. When invoked, the app will download
history files from all the configured servers via SCP, then parse them all
taking into account the peculiar "metafying" that ZSH does. Then the parsed
entries are sorted by date and dumped back into ZSH (extended) history file
format. Finally, the merged history file is uploaded back to all the configured
servers.

### (Un)metafying?

One of the peculiarities of the history file format is that it encodes
characters outside of the `latin1` encoding with a special character called
`Meta` with the value of `0x83` (`134` in decimal). See
[the C implementation](/piotrklibert/zsh-merge-hist/blob/master/unmetafy/unmetafy.c)
of the escaping logic (taken from ZSH source). Unfortunately, ignoring the issue
and copying the `Meta` char along with the command text doesn't work, because it
makes for invalid UTF8, which is problematic when dumping the data back to file.
So I had to reimplement the escaping and unescaping logic as part of the app,
[in Scala](/piotrklibert/zsh-merge-hist/blob/master/src/scala/unmetafy.scala).
I believe that implementation is easier to understand due to pattern matching,
but anyway - it works, so it's all good! ☺
