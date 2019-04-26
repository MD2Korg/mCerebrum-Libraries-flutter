package org.md2k.core;

import android.content.Context;

import org.md2k.core.plugin.IPluginExecute;
import org.md2k.core.plugin.PChangeConfig;
import org.md2k.core.plugin.PConfigInfo;
import org.md2k.core.plugin.PConfigInfoServer;
import org.md2k.core.plugin.PConfigList;
import org.md2k.core.plugin.PDataSourceInfo;
import org.md2k.core.plugin.PDeleteData;
import org.md2k.core.plugin.PIsRunning;
import org.md2k.core.plugin.PLogin;
import org.md2k.core.plugin.PLoginInfo;
import org.md2k.core.plugin.PLogout;
import org.md2k.core.plugin.PSpaceInfo;
import org.md2k.core.plugin.PStart;
import org.md2k.core.plugin.PStop;

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
        Core.init(context);
        methods = new HashMap<>();
        methods.put(PConfigInfo.METHOD_NAME, new PConfigInfo());
        methods.put(PConfigInfoServer.METHOD_NAME, new PConfigInfoServer());
        methods.put(PConfigList.METHOD_NAME, new PConfigList());
        methods.put(PDataSourceInfo.METHOD_NAME, new PDataSourceInfo());
        methods.put(PDeleteData.METHOD_NAME, new PDeleteData());
        methods.put(PChangeConfig.METHOD_NAME, new PChangeConfig());
        methods.put(PIsRunning.METHOD_NAME, new PIsRunning());
        methods.put(PLogin.METHOD_NAME, new PLogin());
        methods.put(PLogout.METHOD_NAME, new PLogout());
        methods.put(PLoginInfo.METHOD_NAME, new PLoginInfo());
        methods.put(PSpaceInfo.METHOD_NAME, new PSpaceInfo());
        methods.put(PStart.METHOD_NAME, new PStart());
        methods.put(PStop.METHOD_NAME, new PStop());

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
