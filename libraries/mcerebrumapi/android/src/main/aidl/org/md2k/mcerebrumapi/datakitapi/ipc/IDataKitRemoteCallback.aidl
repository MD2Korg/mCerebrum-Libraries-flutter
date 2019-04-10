package org.md2k.mcerebrumapi.datakitapi.ipc;

import org.md2k.mcerebrumapi.datakitapi.ipc._Session;

interface IDataKitRemoteCallback{
    void onReceived(in _Session _session);
}
