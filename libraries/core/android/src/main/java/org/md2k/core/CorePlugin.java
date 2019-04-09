package org.md2k.core;

import android.content.Context;

import org.md2k.core.plugin.ConfigInfo;
import org.md2k.core.plugin.ConfigInfoServer;
import org.md2k.core.plugin.ConfigListAsset;
import org.md2k.core.plugin.ConfigListServer;
import org.md2k.core.plugin.DataSourceInfo;
import org.md2k.core.plugin.DeleteData;
import org.md2k.core.plugin.ChangeConfig;
import org.md2k.core.plugin.IPluginExecute;
import org.md2k.core.plugin.IsRunning;
import org.md2k.core.plugin.Login;
import org.md2k.core.plugin.LoginInfo;
import org.md2k.core.plugin.Logout;
import org.md2k.core.plugin.SpaceInfo;
import org.md2k.core.plugin.Start;
import org.md2k.core.plugin.Stop;

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
        methods.put(ConfigInfo.METHOD_NAME, new ConfigInfo());
        methods.put(ConfigInfoServer.METHOD_NAME, new ConfigInfoServer());
        methods.put(ConfigListAsset.METHOD_NAME, new ConfigListAsset());
        methods.put(ConfigListServer.METHOD_NAME, new ConfigListServer());
        methods.put(DataSourceInfo.METHOD_NAME, new DataSourceInfo());
        methods.put(DeleteData.METHOD_NAME, new DeleteData());
        methods.put(ChangeConfig.METHOD_NAME, new ChangeConfig());
        methods.put(IsRunning.METHOD_NAME, new IsRunning());
        methods.put(Login.METHOD_NAME, new Login());
        methods.put(LoginInfo.METHOD_NAME, new LoginInfo());
        methods.put(Logout.METHOD_NAME, new Logout());
        methods.put(SpaceInfo.METHOD_NAME, new SpaceInfo());
        methods.put(Start.METHOD_NAME, new Start());
        methods.put(Stop.METHOD_NAME, new Stop());

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
