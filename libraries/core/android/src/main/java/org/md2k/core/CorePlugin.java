package org.md2k.core;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * CorePlugin
 */
public class CorePlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    private static final String CHANNEL = "org.md2k.core";

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
        channel.setMethodCallHandler(new CorePlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        result.notImplemented();
    }
}
