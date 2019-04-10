#import "McerebrumapiPlugin.h"
#import <mcerebrumapi/mcerebrumapi-Swift.h>

@implementation McerebrumapiPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMcerebrumapiPlugin registerWithRegistrar:registrar];
}
@end
