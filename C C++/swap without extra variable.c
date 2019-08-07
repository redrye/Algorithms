#include <stdio.h>

int main(int argc, char *argv[]) {
	
	int a = 1050;
	int b = 10;
	a ^= b;
	b ^= a;
	a ^= b;
	printf("a = %d\n", a);
	printf("b = %d\n", b);
}