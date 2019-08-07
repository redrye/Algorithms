/* serverapi.h */

#ifndef _SERVERAPI_H_
#define _SERVERAPI_H_

/*
#if defined(WIN32) || defined(WIN64)
#if defined(WIN32)
#include <winsock2.h>
#include <win32.h> */
/* defined(WIN32) OR defined(WIN64)*/
#else
#include <sys/socket>
#include <netinet/in>
#include <netdb.h>
#include <pthread>
#include <unistd>
#endif /* defined(LINUX) || defined(SOLARIS) */


#include <cstdio>
#include <sys/ctypes>

typedef short appnum;
typedef long computer;
typedef int	 connection;

class Server {
	#if defined(WIN32) || defined(WIN64)
	HANDLE await_contact_mutex;
	HANDLE cname_mutex;
	HANDLE appname_mutex;
	#else
	pthread_mutex_t await_contact_mutex = PTHREAD_MUTEX_INITIALIZER;
	pthread_mutex_t cname_mutex = PTHREAD_MUTEX_INITIALIZER;
	pthread_mutex_t appname_mutex = PTHREAD_MUTEX_INITIALIZER;
	#endif
	int init = 0;
	Server() {
		if(init != 0) return;
		#if defined(WIN32) || defined(WIN64)
		wsavers = MAKEWORD(1, 1);
		if(WSAStartup(wsavers, &wsadata) != 0) {
			cout << stderr << " WSAStartup failed" << endl;
			exit(1);
		}
		await_contact_mutex = CreateMutex(NULL, FALSE, NULL);
		cname_mutex = CreateMutex(NULL, FALSE, NULL);
		appname_mutex = CreateMutex(NULL, FALSE, NULL);
		#endif
		init = 1;
	}
	appname_to_appnum
	
};



class port2sock {
	short port;
	int sock;
};

#define P2S_SIZE 64 /* number of entries in port to socket map table	*/
#define LISTEN_Q_LEN 5

appnum		appname_to_appnum(char *appname);
computer	cname_to_comp(char *cname);
connection	await_contact(appnum a);
connection	make_contact(computer c, appnum a);
int		send_eof(connection c);
void    cnaiapi_init(void);

#if defined(LINUX) || defined(SOLARIS)
extern pthread_mutex_t await_contact_mutex, cname_mutex, appname_mutex;
#elif defined(WIN32)
extern HANDLE await_contact_mutex, cname_mutex, appname_mutex;
#endif

#endif /* !defined(_SERVERAPI_H_) */
