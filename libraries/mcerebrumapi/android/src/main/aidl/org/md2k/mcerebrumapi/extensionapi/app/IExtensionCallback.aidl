package org.md2k.mcerebrumapi.extensionapi.app;

import org.md2k.mcerebrumapi.data.MCValue;

interface IExtensionCallback{
    void onSuccess(in MCValue value);
    void onError(String message);
}
