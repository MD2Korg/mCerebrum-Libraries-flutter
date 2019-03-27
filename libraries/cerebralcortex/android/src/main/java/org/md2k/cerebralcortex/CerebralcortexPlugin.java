package org.md2k.cerebralcortex;

import android.content.Context;
import android.util.Log;

import org.md2k.mcerebrumapi.core.exception.MCException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * CerebralcortexPlugin
 */
public class CerebralcortexPlugin implements MethodCallHandler {
    private static final String CHANNEL = "cerebralcortex";
    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String LOGIN = "LOGIN";
    private static final String LOGOUT = "LOGOUT";
    private static final String USER_ID = "USER_ID";
    private static final String SERVER_ADDRESS = "SERVER_ADDRESS";

    private static final String CONFIG_LIST = "CONFIG_LIST";
    private static final String CONFIG_CURRENT = "CONFIG_CURRENT";
    private static final String CONFIG_DOWNLOAD = "CONFIG_DOWNLOAD";
    private static final String CONFIG_SERVER = "CONFIG_SERVER";
    private static final String CONFIG_UPDATE = "CONFIG_UPDATE";

    private static Context context;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
        context = registrar.context();
        channel.setMethodCallHandler(new CerebralcortexPlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        switch (call.method) {
            case USER_ID:
                result.success(CerebralCortexManager.getInstance(context).getUserId());
                break;
            case SERVER_ADDRESS:
                result.success(CerebralCortexManager.getInstance(context).getServerAddress());
                break;
            case IS_LOGGED_IN:
                boolean res = CerebralCortexManager.getInstance(context).isLoggedIn();
                result.success(res);
                break;
            case LOGIN:
                String server = call.argument("server");
                String username = call.argument("username");
                String password = call.argument("password");
                Log.d("abc", "server=" + server + " username = " + username + " password=" + password);
                String passwordHash = "";

                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA-256");
                    md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
                    byte[] digest = md.digest();
                    passwordHash = String.format("%064x", new java.math.BigInteger(1, digest));

                } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
                }
                CerebralCortexManager.getInstance(context).login(server, username, passwordHash, new CerebralCortexCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        result.success("SUCCESS");
                    }

                    @Override
                    public void onError(MCException exception) {
                        result.error(exception.getMessage(), exception.getMessage(), null);
                    }
                });
                break;
            case LOGOUT:
                CerebralCortexManager.getInstance(context).logout();
                result.success(true);
                break;
            case CONFIG_LIST:
                CerebralCortexManager.getInstance(context).getConfigList(new CerebralCortexCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        ArrayList<FileInfo> res = (ArrayList<FileInfo>) obj;
                        result.success(res);
                    }

                    @Override
                    public void onError(MCException exception) {
                        result.error(exception.getMessage(), exception.getMessage(), null);
                    }
                });
                break;
            case CONFIG_CURRENT:
                result.success(CerebralCortexManager.getInstance(context).getCurrentConfig());
                break;
            case CONFIG_UPDATE:
                String configUpdate = ServerInfo.getFileInfo().getName();
                CerebralCortexManager.getInstance(context).downloadConfigurationFile(configUpdate, new CerebralCortexCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        result.success(obj);
                    }

                    @Override
                    public void onError(MCException exception) {
                        result.error(exception.getMessage(), exception.getMessage(), null);
                    }
                });
                break;

            case CONFIG_DOWNLOAD:
                String config = call.argument("config");
                CerebralCortexManager.getInstance(context).downloadConfigurationFile(config, new CerebralCortexCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        result.success(obj);
                    }

                    @Override
                    public void onError(MCException exception) {
                        result.error(exception.getMessage(), exception.getMessage(), null);
                    }
                });
                break;
            case CONFIG_SERVER:
                final FileInfo f = CerebralCortexManager.getInstance(context).getCurrentConfig();
                CerebralCortexManager.getInstance(context).getConfigList(new CerebralCortexCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        boolean flag = false;
                        ArrayList<FileInfo> res = (ArrayList<FileInfo>) obj;
                        for(int i=0;i<res.size();i++){
                            if(res.get(i).getName().equals(f.getName())) {
                                result.success(res.get(i));
                                flag = true;
                                break;
                            }
                        }
                        if(flag==false) result.success(f);
                    }

                    @Override
                    public void onError(MCException exception) {
                        result.error(exception.getMessage(), exception.getMessage(), null);
                    }
                });
                break;
        }
    }
}
