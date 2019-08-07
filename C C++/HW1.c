/*

Coded By: Ryan Boudreaux 
	Date 2/12/2018		
		
HW1 is a C program that counts
the number of non white-space
characters in an input text
file, then based on the argument
flags (-s or -f) the result will
be displayed via screen (-s) or
be written to an output file (-f)

Usage: ./HW1 [-s | -f] [input-file] [output-file(if using -f option)]

*/	


#include <stdio.h>
#include <stdlib.h>

#ifdef _WIN32 || _WIN64
#define clear "CLS"		// DOS clear command
#else
#define clear "clear"	// UNIX clear command
#endif

/* Throw the instructions */
void instruct_error() {
	system(clear);    /* Clear the display screen */
	printf("Operating Systems Homework Code Assignment 1\n\nUsage: ./HW1 [-s | -f] [input-file] [output-file(if using -f option)]\n\n");
}


/* If the arguments are not valid, throw the instructions and exit */
/* argc needs to equal 3 or more, and argv[1] needs to be the -s or -f flag */
void validateArgs(int n, char *c) {
	if (n < 3 || (strcmp(c, "-s") != 0) && (strcmp(c, "-f") != 0)) { 
		instruct_error();											
#ifdef _WIN32 || _WIN64
		system("pause");
#endif

		exit(0);
	}
}


/* Cry 'Havoc!', and let slip the dogs of war */

int main(int argc, char *argv[]) {
	FILE *input, *output;
	char output_txt[] = "The number of non-whitespace characters: %d\n"; /* Set the output text for both screen and file */
	int c;																 /* Allocate the fgetc() result */
	unsigned int chars = 0; /* Allocate and set the character counter to 0, unsigned because we are not dealing with negatives */
	validateArgs(argc, argv[1]); /* arg validation */

								 /* Open the file as read only. If it does not exist, throw the instruct_error and return 0 */
	input = fopen(argv[2], "r");
	if (!input) {
		instruct_error();
		return 0;
	}

	/* Using a while loop to count. When EOF is reached, c will return -1 */
	while ((c = fgetc(input)) != -1) {
		if (!isspace(c)) {      /* isspace() function to find the  */
			chars++;      /* whitespace in the file. If */
		}                       /* a NOT whitespace, the counter   */
	}                           /* will be incrimented by 1 for    */
	fclose(input);              /* each pass in the loop           */


/* If the -s flag was used, display the output text on the console. If -f flag was used, print that the output was written to the output file and write the output text to the file. */

	if (strcmp(argv[1], "-s") == 0) {
		printf(output_txt, chars);
	}
	if (strcmp(argv[1], "-f") == 0) {
		printf("Output written to output.txt\n");
		output = fopen("output.txt", "w");
		fprintf(output, output_txt, chars);
		fclose(output);
	}
	return 0;
}