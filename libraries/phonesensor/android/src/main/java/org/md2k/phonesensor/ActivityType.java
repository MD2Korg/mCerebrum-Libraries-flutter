package org.md2k.phonesensor;

import android.content.Context;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.HashMap;

import io.flutter.plugin.common.EventChannel;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation2.ReactiveLocationProviderConfiguration;

public class ActivityType extends ISensor{
    private Disposable activityDisposable;
    private static final double STILL=0;
    private static final double ON_FOOT=1;
    private static final double WALKING=2;
    private static final double RUNNING=3;
    private static final double ON_BICYCLE=4;
    private static final double IN_VEHICLE=5;
    private static final double TILTING=6;
    private static final double UNKNOWN=7;
    ActivityType(Context context){
        super(context);
    }

    void start(final EventChannel.EventSink events){
        final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context, ReactiveLocationProviderConfiguration
                .builder()
                .setRetryOnConnectionSuspended(true)
                .build()
        );

        activityDisposable = locationProvider
                .getDetectedActivity(10000)

                .subscribe(new Consumer<ActivityRecognitionResult>() {
                    @Override
                    public void accept(ActivityRecognitionResult activityRecognitionResult) {
                        double a;
                        switch (activityRecognitionResult.getMostProbableActivity().getType()) {
                            case DetectedActivity.IN_VEHICLE:
                                a = IN_VEHICLE;
                                break;
                            case DetectedActivity.ON_BICYCLE:
                                a = ON_BICYCLE;
                                break;
                            case DetectedActivity.ON_FOOT:
                                a = ON_FOOT;
                                break;
                            case DetectedActivity.RUNNING:
                                a = RUNNING;
                                break;
                            case DetectedActivity.STILL:
                                a = STILL;
                                break;
                            case DetectedActivity.TILTING:
                                a = TILTING;
                                break;
                            case DetectedActivity.WALKING:
                                a = WALKING;
                                break;
                            case DetectedActivity.UNKNOWN:
                            default:
                                a = UNKNOWN;
                                break;
                        }
                        events.success(createData(activityRecognitionResult.getTime(),new double[]{a, activityRecognitionResult.getMostProbableActivity().getConfidence()}));
                    }
                });

    }

    void stop(){
        if (activityDisposable != null && !activityDisposable.isDisposed()) {
            activityDisposable.dispose();
        }

    }
}
