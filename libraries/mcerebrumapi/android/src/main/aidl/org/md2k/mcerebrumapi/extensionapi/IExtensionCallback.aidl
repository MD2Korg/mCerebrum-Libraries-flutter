package org.md2k.mcerebrumapi.extensionapi;

interface IExtensionCallback{
    void onSuccess(in String value);
    void onError(String message);
}
