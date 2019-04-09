#import "PhonesensorPlugin.h"
#import <phonesensor/phonesensor-Swift.h>

@implementation PhonesensorPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPhonesensorPlugin registerWithRegistrar:registrar];
}
@end
