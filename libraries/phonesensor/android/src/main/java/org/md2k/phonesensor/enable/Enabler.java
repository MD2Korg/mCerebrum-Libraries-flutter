/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.md2k.phonesensor.enable;

import android.app.Activity;
import android.content.Context;

public final class Enabler {

  private static EnablerInstance instance;


  public Enabler(Context context, SensorEnabler sensorType, EnableCallback enableCallback) {
    initialize(context, sensorType, enableCallback);
  }
  public void requestEnable() {
      instance.requestEnable();
  }
  public void requestEnableSilent() {
    instance.requestEnable();
  }


  private static void initialize(Context context, SensorEnabler sensorType, EnableCallback enableCallback) {
    if (instance == null) {
      instance = new EnablerInstance(context, sensorType, enableCallback);
    } else {
      instance.setContext(context);
    }
  }

  /**
   * Method called whenever the DexterActivity has been created or recreated and is ready to be
   * used.
   */
  protected static void onActivityReady(Activity activity) {
    /* Check against null values because sometimes the DexterActivity can call these internal
       methods when the DexterInstance has been cleaned up.
       Refer to this commit message for a more detailed explanation of the issue.
     */
    if (instance != null) {
      instance.onActivityReady(activity);
    }
  }

  /**
   * Method called whenever the DexterActivity has been destroyed.
   */
  protected static void onActivityDestroyed() {
    /* Check against null values because sometimes the DexterActivity can call these internal
       methods when the DexterInstance has been cleaned up.
       Refer to this commit message for a more detailed explanation of the issue.
     */
    if (instance != null) {
      instance.onActivityDestroyed();
    }
  }

  protected static void onPermissionsRequested(boolean success) {
    /* Check against null values because sometimes the DexterActivity can call these internal
       methods when the DexterInstance has been cleaned up.
       Refer to this commit message for a more detailed explanation of the issue.
     */
    if (instance != null) {
      instance.onPermissionsChecked(success);
    }
  }
}