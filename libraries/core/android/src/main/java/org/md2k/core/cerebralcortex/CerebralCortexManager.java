package org.md2k.core.cerebralcortex;
/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.md2k.core.ReceiveCallback;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.CCWebAPICalls;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioObjectStats;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserMetadata;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserSettings;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.utils.ApiUtils;
import org.md2k.core.cerebralcortex.scheduler.CCJobCreator;
import org.md2k.core.cerebralcortex.scheduler.JobUploadLowFrequency;
import org.md2k.core.cerebralcortex.scheduler.UploadHighFrequencyWorker;
import org.md2k.core.data.LoginInfo;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;

public class CerebralCortexManager {
    public CerebralCortexManager() {
    }
    public void start(Context context){
        PeriodicWorkRequest periodicHighFrequencyWorkRequest = new PeriodicWorkRequest.Builder(UploadHighFrequencyWorker.class, 15, TimeUnit.MINUTES).addTag(UploadHighFrequencyWorker.TAG)
                .build();
        WorkManager workManager = WorkManager.getInstance();
        workManager.cancelAllWorkByTag(UploadHighFrequencyWorker.TAG);
        workManager.enqueue(periodicHighFrequencyWorkRequest);
    }
    public void stop(){
        WorkManager workManager = WorkManager.getInstance();
        workManager.cancelAllWorkByTag(UploadHighFrequencyWorker.TAG);

    }
}
