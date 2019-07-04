package org.md2k.mcerebrumapi.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

public class Utils {
    private static final String SERVER_ID = "org.md2k.datakit.server";

    public static boolean isLibrary(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(SERVER_ID);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, 0);
        if (resolveInfo.size() == 0) return false;
        for (int i = 0; i < resolveInfo.size(); i++) {
            if (resolveInfo.get(i).serviceInfo.packageName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    public static Intent findServer(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(SERVER_ID);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, 0);
        if (resolveInfo.size() == 0) return null;
        if (resolveInfo.size() == 1) {
            intent = new Intent();
            intent.setComponent(new ComponentName(resolveInfo.get(0).serviceInfo.packageName, resolveInfo.get(0).serviceInfo.name));
            return intent;
        }
        for (int i = 0; i < resolveInfo.size(); i++) {
            if (resolveInfo.get(i).serviceInfo.packageName.equals(context.getPackageName())) {
                intent = new Intent();
                intent.setComponent(new ComponentName(resolveInfo.get(i).serviceInfo.packageName, resolveInfo.get(i).serviceInfo.name));
                return intent;
            }
        }
        //TODO: select
        intent = new Intent();
        intent.setComponent(new ComponentName(resolveInfo.get(0).serviceInfo.packageName, resolveInfo.get(0).serviceInfo.name));
        return intent;
    }
}
