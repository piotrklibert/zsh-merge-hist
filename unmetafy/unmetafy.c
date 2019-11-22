#define Meta ((char) 0x83)

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>

/*
char *unmetafy(char *s, int *len)
{
  char *p, *t;
>>>> ziă siă'

1. t= -- p= | t=-125 <> p=-89
2. t[-1]= t= -- p= | t[-1]=-125 t=-89 <> p=-89
3. t[-1]= t= -- p=  | t[-1]=-121 t=-89 <> p=32
1. t=  -- p=s | t=32 <> p=115
1. t=s -- p=i | t=115 <> p=105
1. t=i -- p= | t=105 <> p=-60
1. t= -- p= | t=-60 <> p=-125
1. t= -- p= | t=-125 <> p=-71
2. t[-1]= t= -- p= | t[-1]=-125 t=-125 <> p=-71
3. t[-1]= t= -- p=' | t[-1]=-103 t=-125 <> p=39
1. t=' -- p='\n' | t=39 <> p=10
1. t='\n' -- p= | t=10 <> p=0
: 1573579678:0;dict 'obrazić się'

  for (p = s; *p && *p != Meta; p++);
  printf(">>>> %s\n", p-3);
  for (t = p; (*t = *p++);){
    printf("1. t=%c -- p=%c | t=%i <> p=%i\n", *t, *p, *t, *p);
    if (*t++ == Meta){
      printf("2. t[-1]=%c t=%c -- p=%c | t[-1]=%i t=%i <> p=%i\n", t[-1], *t, *p, t[-1], *t, *p);
      t[-1] = *p++ ^ 32;
      printf("3. t[-1]=%c t=%c -- p=%c | t[-1]=%i t=%i <> p=%i\n", t[-1], *t, *p, t[-1], *t, *p);
    }
  }

  if (len)
    *len = t - s;

  return s;
}
*/


char *unmetafy(char*,int*);


int main(int _argc, char *_argv[]) {
  char *line = NULL;
  size_t size;

  while (getline(&line, &size, stdin) != -1) {
    unmetafy(line, NULL);
    printf("%s", line);
  }

  if (line) free(line);
  return EXIT_SUCCESS;
}


/* from zsh utils.c */
#define Meta ((char) 0x83)

char *unmetafy(char *s, int *len)
{
  char *p, *t;
  for (p = s; *p && *p != Meta; p++);
  for (t = p; (*t = *p++);)
    if (*t++ == Meta)
      t[-1] = *p++ ^ 32;
  if (len)
    *len = t - s;
  return s;
}
