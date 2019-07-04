package org.md2k.mcerebrumapi.extensionapi;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import org.md2k.mcerebrumapi.extensionapi.action.MCAction;
import org.md2k.mcerebrumapi.extensionapi.user_interface.MCUserInterface;


/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center

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
public interface IMCExtensionBuilder {
    interface IIType {
        IIdLib asLibrary();
        IIDescriptionApp asApp(Context content);
    }

    interface IIDescriptionApp {
        IIPermissionApp setDescription(@NonNull String description);
    }
    interface IIPermissionApp {
        IIConfigureApp setCustomPermissionInterface(@NonNull IPermission.IPermissionInterface iPermission);
        IIConfigureApp noPermissionRequired();
    }
    interface IIConfigureApp {
        IIOperationApp setConfiguration(@NonNull IConfigure.IConfigureBasic iConfigure);
        IIOperationApp setConfigurationUI(@NonNull Activity activity, @NonNull IConfigure.IConfigureWithUI iConfigureWithUI1);
        IIOperationApp noConfiguration();
    }
    interface IIOperationApp {
        IIOperationApp setBackgroundExecutionInterface(@NonNull IBackgroundProcess iBackgroundProcess);

        IIOperationApp addUserInterface(MCUserInterface mcUserInterface);

        IIOperationApp addAction(MCAction mcAction);

        MCExtensionAPI build();
    }

    interface IIdLib {
        IINameLib setId(@NonNull String id);
    }
    interface IINameLib {
        IIDescriptionLib setName(@NonNull String name);
    }
    interface IIDescriptionLib {
        IIVersionLib setDescription(@NonNull String description);
    }
    interface IIVersionLib {
        IIPermissionLib setVersion(int versionCode,@NonNull  String versionName);
    }


    interface IIPermissionLib {
        IIConfigureLib setPermissionList(@NonNull String[] permissionList);
        IIConfigureLib noPermissionRequired();
    }
    interface IIConfigureLib {
        IIOperationLib setConfiguration(IConfigure.IConfigureBasic iConfigure);
        IIOperationLib setConfigurationUI(Activity activity, IConfigure.IConfigureWithUI iConfigureWithUI1);
        IIOperationLib noConfiguration();
    }

    interface IIOperationLib {
        IIOperationLib setBackgroundExecutionInterface(IBackgroundProcess iBackgroundProcess);

        IIOperationLib addUserInterface(MCUserInterface mcUserInterface);

        IIOperationLib addAction(MCAction mcAction);

        MCExtensionAPI build();
    }
}