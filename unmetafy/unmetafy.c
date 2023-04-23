// -*- coding: utf-8 -*-
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>


#define Meta ((char) 0x83)


const unsigned char unmetafied[] = "ę"; // valid UTF-8
// the same string after metafying by ZSH, shows as 'ă' if printed as UTF-8
const unsigned char metafied[] = {196, Meta, 153 ^ 32, 0};


char *unmetafy(char *s) {
  char *p, *t;
  for (p = s; *p && *p != Meta; p++);
  for (t = p; (*t = *p++);)
    if (*t++ == Meta)
      t[-1] = *p++ ^ 32;
  return s;
}


int main(int _argc, char *_argv[]) {
  assert(sizeof(unmetafied) == 3 /* 2 for 'ę' + 1 for '\0' */);
  assert(unmetafied[0] == 196);
  assert(unmetafied[1] == 153);
  assert(strlen(unmetafied) == 2);

  printf("Metafied/unmetafied: '%s' vs '%s'\n", metafied, unmetafied);

  char* buf = malloc(sizeof(metafied));
  memcpy(buf, metafied, sizeof(metafied));

  unmetafy(buf);

  assert(strlen(buf) == 2);
  assert((strcmp(buf, unmetafied) == 0));

  free(buf);

  return EXIT_SUCCESS;
}
