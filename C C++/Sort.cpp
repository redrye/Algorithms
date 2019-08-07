#include <iostream>
using namespace std;

typedef unsigned int u64;

/* Swap function, without needing a temp variable */

void swap(u64 *a, u64 *b) {
	*b = *a + *b;
	*a = *b - *a;
	*b = *b - *a;
}

/* 	partition function, returns an unsigned int.
	This function checks the variables from start
	to end and swaps them.
*/

u64 partition(u64 *a, u64 start, u64 end) {
	int i; //count variable
	for(i = start; i < end; i++) {
		
		/* For the swap, increment the start variable first */
		if(a[i] <= a[end]) swap(a[i], a[start++]);
	}
	swap(a[i], a[start]);
	return start;
}

/* sort function, recursive */

void qsort(u64 *a, u64 begin, u64 end) {
	if(begin < end) {
		qsort(a, begin, partition(a, begin, end) - 1);
		qsort(a, partition(a, begin, end) + 1, end);
	}
}

int main(int argc, char *argv[]) {
	u64 arr[] = {2, 6, 15, 4, 8, 9, 14, 11, 45};
	qsort(arr, 0, sizeof(arr)/sizeof(u64));	
	for(u64 i = 0; i < sizeof(arr)/sizeof(u64); i++) cout << arr[i] << endl;
	return 0;
}