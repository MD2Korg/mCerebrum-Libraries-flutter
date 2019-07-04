package org.md2k.mcerebrumapi.extensionapi;


import org.md2k.mcerebrumapi.extensionapi.MCExtensionAPI;
import org.md2k.mcerebrumapi.extensionapi.IExtensionCallback;

interface IExtensionRemoteService{
    MCExtensionAPI getExtensionInfo();
    void execute(String type, String id, IExtensionCallback callback);
}
