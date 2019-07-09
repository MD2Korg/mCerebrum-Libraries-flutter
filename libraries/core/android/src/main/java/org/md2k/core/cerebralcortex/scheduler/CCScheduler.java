package org.md2k.core.cerebralcortex.scheduler;

import android.content.Context;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class CCScheduler {
    CCScheduler(Context context){
    }

    private void schedulePeriodicJob() {
/*
        int jobId = new JobRequest.Builder(DemoSyncJob.TAG)
                .setBackoffCriteria(5000, JobRequest.BackoffPolicy.LINEAR)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .build()
                .schedule();
*/
    }

}
