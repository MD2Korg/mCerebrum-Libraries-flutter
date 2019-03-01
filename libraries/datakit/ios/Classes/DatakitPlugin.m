#import "DatakitPlugin.h"
#import <datakit/datakit-Swift.h>

@implementation DatakitPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftDatakitPlugin registerWithRegistrar:registrar];
}
@end
