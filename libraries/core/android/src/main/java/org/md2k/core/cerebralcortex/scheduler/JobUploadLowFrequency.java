package org.md2k.core.cerebralcortex.scheduler;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;

import org.md2k.core.datakit.DataKitManager;

public class JobUploadLowFrequency extends Job {

    public static final String TAG = "job_upload_low_frequency";

    JobUploadLowFrequency(){
    }

    @Override
    @NonNull
    protected Result onRunJob(@NonNull final Params params) {
        boolean success = uploadData();
        return success ? Result.SUCCESS : Result.FAILURE;
    }
    private boolean uploadData(){
        return true;
    }

}
