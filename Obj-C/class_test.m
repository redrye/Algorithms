#import <Foundation/Foundation.h>

@interface CommonClass : NSObject {}
+(CommonClass *)sharedObject;
@property NSString *commonString;
@end

@implementation CommonClass
+(CommonClass *)sharedObject {
	static CommonClass *sharedClass = nil;
	static dispatch_once_t onceToken;
	dispatch_once(&onceToken, ^{
		sharedClass = [[self alloc] init];
		});
	return sharedClass;
}
- (id)init {
	if(self = [super init]) {
		self.commonString = @"this is NSString";
	}
	return self;
}
@end

int main(int argc, char *argv[]) {
	@autoreleasepool {
		NSLog(@"%@",[CommonClass sharedObject].commonString);
	}
}