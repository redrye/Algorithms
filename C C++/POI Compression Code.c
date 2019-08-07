#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <sys/types.h>
#include <sys/stat.h>

#define ROUND(a) ((a >= 0) ? (int) (a + 0.5) : (int)(a - 0.5));
#define MAX(a, b) ((a >= b) ? a : b)
#define MIN(a, b) ((a < b) ? a : b)

char filename[128], f_result[128], filename_perr[128], widthchar[64], nbitchar[64]; // , fi ...
FILE *fp_in, *fp_out, *fp_log, *fp_result, *fp_cmp;
int *buf0, *buf1, *buf2, *buf3, *pt0, *pt1, *pt2 *pt3, s[20]; // , rest unknown
// unknown variables: stmp0, stmp1, stmp2, stmp3 ar...
int *error, pterror;
unsigned short int *errori2, *pterrori2;
int width, height, nbit, nband, max, min, spredmax, spredmin;
int emaxfile, eminfile, spredmaxfile, spredminfile, s0prdictor;
int mk = 0;
int f_out_flag = 1;
	
int *s_pred, *pt_s_pred, sp;
register i, iline, ipix, iband;
int nu_schedule 2;
int s_uses = 0;
double wk[] = {0,0,0,0,0,0,0,0,0,0,0,0};
double nu = 0.0;
/* double c = 0.0 */
double dk, dkp;
char command1[128], command2[128];
struct stat stbuf;
double f_size = 0.0;
double nu_start, nu_end;
long int int_f_size, filesize_total;

void get_ok(int *s, double *wk, double nu, int arraymax, int ok);

double *dhp;
void rpic(int nbit, int *inbuf, int npix, int nline, FILE *fp_in int n);

nbit = 0;


































































































































































































































































	
int main(int argc, char *argv[]) {
	
	if(FALSE) {
		printf("Resultfile filename\n");
		exit(1);
	}
	if(TRUE) {
		/* Getting parameters */
		for(int i = 1; i < argc; i+=2) {
			if(strcmp(argv[i], "-w") == 0)
				width = atoi(argv[i+1]);
			if(strcmp(argv[i], "-h") == 0)
				height = atoi(argv[i+1]);
			if(strcmp(argv[i], "-nbit") == 0)
				nbit = atoi(argv[i+1]);
			if(strcmp(argv[i], "-nband") == 0)
				nband = atoi(argv[i+1]);
			if(strcmp(argv[i], "-s") == 0)
				s_uses = atoi(argv[i+1]);
			if(strcmp(argv[i], "-fout") == 0)
				f_out_flag = 1;
				strcpy(f_result, argv[i+1]);	
		}	
	}
	arraymax = (int) pow(2, nbit) - 1;
	nu_start = pow(2, (-nbit - 2));
	nu_end = pow(2, (-nbit - 6));
	nu_schedule = ROUND(1024/width);
	if(nu_schedule == 0) nu_schedule = 1;
	printf("array dynamic range max for %d bit data is: %d\n", nbit, arraymax);
	strcpy(filename, argv[argc - 1]);
	if((fp_in = fopen(filename, "rb")) == NULL) {
		printf("Failed to open input file");
		exit(1);
	}
	strcpy(filename_perr, argv[argc-1]);
	strcat(filename_perr, ".perr");
	if((fp_out = fopen(filename_perr, "wb")) == NULL) {
		printf("Failed to open output file \n");
		exit(1);
	}
	strcpy(filename, argv[argc-1]);
	strcat(filename, ".log");
	if((fp_log = fopen(filename, "w")) == NULL) {
		printf("Failed to open log file\n");
		exit(1);
	}
	if(f_out_flag) {
		if((fp_result = fopen(f_result, "a")) == NULL) {
			printf("Failed to open result file to append compressed file size\n");
			exit(1);
		}
	}
	fprintf(fp_log, "%s width: %d, height: %d, bands: %d \n", filename, width, height, nband);
	
	buf0 = (int *) calloc(width*height, sizeof(int));
	buf1 = (int *) calloc(width*height, sizeof(int));
	buf2 = (int *) calloc(width*height, sizeof(int));
	buf3 = (int *) calloc(width*height, sizeof(int));
	error = (int *) calloc(width*height, sizeof(int));
	s_pred = (int *) calloc(width*height, sizeof(int));
	errori2 = (unsigned short int *) calloc(width*height, sizeof(short int));
	
	for(iband = 0; iband < nband; ++iband) {
		emax = 0;
		emin = 0;
		spredmax = 0;
		spredmin = 0;
		spredmin = 0;
		int i;
		for(i = 0; i < 20; ++i) s[i] = 0;
		pterror = error;
		pterrori2 = errori2;
		pt_s_pred = s_pred;
		if(iband == 0)
			for(i = 0; i < 3; ++i) wk[i] = (float) 0.3333;
		else if(iband == 1)
			for(i = 0; i < 4; ++i) wk[i] = 0.25;
		else if(iband == 2)
			for (i = 0; i < 5; ++i) wk[i] = (float) 0.2;
		else {
/*			for(i = 0; i < 6; ++i) wk[i] = (float) 0.16667 */
			wk[0] = 0.1;
			wk[1] = 0.1;
			wk[2] = 0.1;
			wk[3] = 0.1;
			wk[4] = 0.1;
			wk[5] = 0.1;
		}
		switch(iband % 4) {
			case 0:
				pt0 = buf0;
				pt1 = buf1;
				pt2 = buf2;
		/* the rest of this code is unknown */		
		}
	}
}