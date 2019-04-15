package org.md2k.core.plugin;

import android.content.Context;

import com.google.gson.Gson;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.core.configuration.ConfigId;

import java.util.ArrayList;
import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

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
public class ConfigInfoServer implements IPluginExecute {
    public static final String METHOD_NAME = "CONFIG_INFO_SERVER";

    @Override
    public void execute(final Context context, final MethodCall call, final MethodChannel.Result result) {
        Core.cerebralCortex.getConfigurationList(new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                ArrayList<HashMap<String, Object>> res = (ArrayList<HashMap<String, Object>>) obj;
                for (int i = 0; i < res.size(); i++) {
                    if (res.get(i).get(ConfigId.core_config_filename).equals(Core.configuration.getValue(ConfigId.core_config_filename))) {
                        Gson gson = new Gson();
                        result.success(gson.toJson(res.get(i)));
                        return;
                    }
                }
                result.error("not found", null, null);
            }
            @Override
            public void onError(Exception e) {
                result.error(e.getMessage(), null, null);
            }
        });
    }
}
