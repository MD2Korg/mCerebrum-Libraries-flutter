package org.md2k.core.plugin;

import android.content.Context;

import java.util.HashMap;

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
    private static final String CHANNEL = "core";
    private Context context;
    private HashMap<String, IPluginExecute> methods;

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
        channel.setMethodCallHandler(new CorePlugin(registrar.context()));
    }

    private CorePlugin(Context context) {
        this.context = context;
        methods = new HashMap<>();
        methods.put(PConfig.METHOD_NAME, new PConfig());
        methods.put(PDataSourceInfo.METHOD_NAME, new PDataSourceInfo());
        methods.put(PDeleteData.METHOD_NAME, new PDeleteData());
        methods.put(PChangeConfigCC.METHOD_NAME, new PChangeConfigCC());
        methods.put(PIsRunning.METHOD_NAME, new PIsRunning());
        methods.put(PLogin.METHOD_NAME, new PLogin());
        methods.put(PLogout.METHOD_NAME, new PLogout());
        methods.put(PSpaceInfo.METHOD_NAME, new PSpaceInfo());
        methods.put(PStart.METHOD_NAME, new PStart());
        methods.put(PStop.METHOD_NAME, new PStop());
        methods.put(PCheckUpdateConfig.METHOD_NAME, new PCheckUpdateConfig());
        methods.put(PUpdateConfig.METHOD_NAME, new PUpdateConfig());

    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        IPluginExecute i = methods.get(call.method);
        if (i != null)
            i.execute(context, call, result);
        else
            result.notImplemented();
    }
}
