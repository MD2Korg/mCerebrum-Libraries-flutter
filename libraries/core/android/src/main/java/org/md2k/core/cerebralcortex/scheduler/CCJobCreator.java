package org.md2k.core.cerebralcortex.scheduler;

import android.content.Context;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

public class CCJobCreator implements JobCreator {

    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case JobUploadLowFrequency.TAG:
                return new JobUploadLowFrequency();
            case JobUploadHighFrequency.TAG:
                return new JobUploadHighFrequency();
            default:
                return null;
        }
    }

    public static final class AddReceiver extends AddJobCreatorReceiver {
        @Override
        protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
            // manager.addJobCreator(new DemoJobCreator());
        }
    }
}
