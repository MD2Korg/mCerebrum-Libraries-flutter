package org.md2k.core.cerebralcortex.scheduler;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class UploadWifiWorker extends Worker {
    public static final String TAG = UploadWifiWorker.class.getSimpleName();
    public UploadWifiWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }
    @Override
    @NonNull
    public Result doWork() {
//        if(!isWifiEnabled()) return Result.failure();

        // Do the work here--in this case, upload the images.
//        uploadImages();
        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }


}
