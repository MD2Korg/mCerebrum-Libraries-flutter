package org.md2k.core.cerebralcortex.scheduler;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;

public class JobUploadHighFrequency extends Job {

    public static final String TAG = "job_upload_high_frequency";

    JobUploadHighFrequency(){
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
