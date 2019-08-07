#include <iostream>
#include <utility>
using namespace std;

typedef pair<uint, uint> u64;

/* Swap function, without needing a temp variable */

void swap(uint *a, uint *b) {
	*b = *a + *b;
	*a = *b - *a;
	*b = *b - *a;
}

/* Overloaded swap fuction to swap values of pairs */
void swap(u64 *a, u64 *b) {
	swap(a->first, b->first);
	swap(a->second, b->second);
}

/* 	
	partition function, returns an unsigned int.
	This function checks the variables from start
	to end and swaps them.
*/

uint partition(u64 *a, uint start, uint end) {
	int i;
	for(i = start; i < end; i++) {
		
		/* For the swap, increment the start variable first */
		if(a[i].first <= a[end].first) swap(a[i], a[start++]);
		if((a[i].first == a[i+1].first) && (a[i].second > a[i+1].second)) swap(a[i], a[i+1]);
	}
	swap(a[i], a[start]);
	return start;
}

/* sort function, recursive */

void qsort(u64 *a, uint begin, uint end) {
	if(begin < end) {
		qsort(a, begin, partition(a, begin, end) - 1);
		qsort(a, partition(a, begin, end) + 1, end);
	}
}

int main(int argc, char *argv[]) {
	uint array[2][6] = {1, 7, 1, 8, 5, 4, 9, 9, 4, 9, 8, 4};
	int n = sizeof(array)/sizeof(uint)/2;
	cout << n << endl;
	u64 *arr = new u64[n];
	for(int j = 0; j < n; j++) {
		arr[j] = make_pair(array[0][j], array[1][j]);
		cout << array[0][j] << " " << array[1][j] << endl;
	}
	cout << endl;
	
	//	 {2, 6, 15, 4, 8, 9, 14, 11, 45};
	qsort(arr, 0, n-1);	
	for(int i = 0; i < n; i++) cout << arr[i].first << " " << arr[i].second << endl;
	return 0;
}