//  main.m
//
//  Created by Christopher Wright on 2007.06.12.

#import <Foundation/Foundation.h>

NSLock *lock;

/* Thread class deceration */
@interface Thread : NSObject
+(void)run:(id)param;	// + is a class method
						// - is an instance method
@end

/* Thread class implementation */

@implementation Thread
+(void)run:(id)param{
	int x;
	for(x=0;x<50;++x)
	{
		[lock lock];
		printf("Object Thread says x is %i\n",x);
		usleep(1);
		[lock unlock];
	}
}
@end

void threadrun() {
	[NSThread detachNewThreadSelector:@selector(run:) toTarget:[Thread class] withObject:nil];
}

int main(int argc, char *argv[]) {
	int x;
	lock = [[NSLock alloc] init];
	threadrun();

	for(x=0;x<50;++x) {
		[lock lock];
		printf("Main thread says x is %i\n",x);
		usleep(1);
		printf("Main thread lets go\n",x);
		[lock unlock];
		usleep(1);
	}

	return 0;
}