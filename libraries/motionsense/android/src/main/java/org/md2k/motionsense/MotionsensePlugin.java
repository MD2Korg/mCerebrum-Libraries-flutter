package org.md2k.motionsense;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MotionsensePlugin
 */
public class MotionsensePlugin implements MethodCallHandler {

    /**
     * Plugin registration.
     */

    public static void registerWith(Registrar registrar) {
        MotionsensePlugin motionsensePlugin = new MotionsensePlugin();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "org.md2k.motionsense.channel");
        channel.setMethodCallHandler(motionsensePlugin);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
                result.notImplemented();
    }

}
