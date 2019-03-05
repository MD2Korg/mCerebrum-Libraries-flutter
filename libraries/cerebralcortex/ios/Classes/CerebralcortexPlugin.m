#import "CerebralcortexPlugin.h"
#import <cerebralcortex/cerebralcortex-Swift.h>

@implementation CerebralcortexPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftCerebralcortexPlugin registerWithRegistrar:registrar];
}
@end
