/*
Coded by Ryan Boudreaux
3/13/2018

This program demonstrates pthread_mutex_lock(),
pthread_mutex_unlock(), and pthreads

*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

double balance = 600;	// Create Global variable 'balance'
			// initalized at 600

/* Create pthread_mutex_lock() variable 'lock' */
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;


void *deposit(void *ptr) {
    /*Lock function until completion*/
    pthread_mutex_lock(&lock);

    /* Initalize i with the value of int of ptr */
	double i;
	i = *((double *)ptr);
	printf("Deposit: $%0.2f --> ",i); // Print Deposit Amount, presision set to 2.
	while(i != 0) { // Count down while loop
		balance++;
		i--;
	}
	/* Print results, presision set to 2 */
	printf("Result: $%0.2f\n", balance);
    /*Unlock function upon completion*/
    pthread_mutex_unlock(&lock);
}
void* withdraw(void *ptr) {
    /* Initalize i with the value of int of ptr */
	double i;
	i = *((double *)ptr);
	printf("Withdrawl: $%0.2f --> ",i); // Print Deposit Amount, presision set to 2.
	while(i != 0) { // Count down while loop
		balance--;
		i--;
	}
	/* Print results, presision set to 2*/
	printf("Result: $%0.2f\n", balance);
    /*Unlock function upon completion*/
    pthread_mutex_unlock(&lock);
}

int main(int argc, char *argv[]) {
    double i = 20; // Test variable 1
    double j = 50; // Test variable 2
    pthread_t t1,t2;
    pthread_create(&t1, NULL, deposit, &i);
    pthread_create(&t2, NULL, withdraw, &j);
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    return 0;
}
