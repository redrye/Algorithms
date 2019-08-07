/* Dijkastra - Ryan Boudreaux */

#include <cstdio>
#include <cstdlib>

#include <limits> // Numeric Limits
#include <iostream>
#include <list>
#include <utility> // Pair function
#include <vector>

using namespace std;


typedef pair<int, float> Unit;
typedef list<Unit> Invintory; 
typedef vector<Invintory> Pool;

static bool rankUnits(const Unit &x, const Unit &y) {
	if(x.second == y.second) {
		return (x.first < y.first);
	}
	return (x.second > y.second);
}

static int indexUnits(const Invintory &invintory, int i) {
	int j = 0;
	while(j < invintory.size()) {
		const Unit &unit = invintory[j];
		if(unit.first == i) {
			if(unit.second == 0) {
				return(invintory.size() - 1);
			}
			else if(invintory.second == 1) {
				return 0;
			}
			else {
				return j;
			}
		}	
	}
	return -1;
}


static int asymetricalROD




int main(int argn, char *argc[]) {


	return 0;
}
