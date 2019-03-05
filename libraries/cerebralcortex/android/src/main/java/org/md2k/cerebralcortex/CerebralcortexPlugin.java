package org.md2k.cerebralcortex;

import android.content.Context;
import android.util.Log;

import org.md2k.mcerebrumapi.core.exception.MCException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** CerebralcortexPlugin */
public class CerebralcortexPlugin implements MethodCallHandler {
  private static final String CHANNEL = "cerebralcortex";
  private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
  private static final String LOGIN = "LOGIN";
  private static final String LOGOUT = "LOGOUT";
  private static final String DOWNLOAD_CONFIG ="DOWNLOAD_CONFIG";
  private static final String CHECK_UPDATE = "CHECK_UPDATE";
  private static Context context;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
    context = registrar.context();
    channel.setMethodCallHandler(new CerebralcortexPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, final Result result) {
    switch(call.method){
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
        break;
      case DOWNLOAD_CONFIG:
        CerebralCortexManager.getInstance(context).downloadConfigurationFile("decisions.zip", new CerebralCortexCallback() {
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
    }

    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }
}
