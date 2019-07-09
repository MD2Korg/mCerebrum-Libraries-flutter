package org.md2k.core.cerebralcortex.scheduler;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadHighFrequencyWorker extends Worker {
    public static final String TAG = UploadHighFrequencyWorker.class.getSimpleName();
    public UploadHighFrequencyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    @NonNull
    public Result doWork() {
        // Do the work here--in this case, upload the images.

//        uploadImages();

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }
}
