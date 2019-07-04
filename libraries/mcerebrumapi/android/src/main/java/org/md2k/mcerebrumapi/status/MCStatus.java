package org.md2k.mcerebrumapi.status;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
\ * All rights reserved.
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

/**
 * This class defines various status that can occur in the system.
 */
public enum MCStatus {
    UNKNOWN_ERROR(100,"Unknown error"),
    SUCCESS(0,"Success"),
    MCEREBRUM_API_NOT_INITIALIZED(1, "MCerebrum API not initialized"),
    MCEREBRUM_APP_NOT_INSTALLED(2,"Main app not installed"),
    CONNECTION_ERROR(3, "Connection error"),
    INVALID_PARAMETER(4, "Invalid parameter"),
    INVALID_DATA_SOURCE(5, "invalid data source"),
    MISSING_DATA_SOURCE_TYPE(6, "Missing data source type"),
    MISSING_DATA_TYPE(7, "Missing data type"),
    DATA_SOURCE_NOT_REGISTERED(8, "Data source not registered"),
    INVALID_DATA(9, "Invalid data"),
    INCONSISTENT_DATA_TYPE(10, "Inconsistent data type"),
    INVALID_TIMESTAMP(11, "Invalid timestamp"),
    DATA_SIZE_TOO_LARGE(12, "Data too large"),
    INVALID_DATA_FORMAT(13, "Invalid data format"),
    TIMEOUT(14, "Timeout"),
    AUTHENTICATION_REQUIRED(15, "authentication required"),
    AUTHENTICATION_REFUSED(16, "authentication refused"),
    INVALID_OPERATION(17,"Invalid operation"),
    MCEREBRUM_BIND_ERROR(18, "Bind error"),
    DATAKIT_STOPPED(19, "Datakit stopped"),
    DATAKIT_CONNECTION_ERROR(20, "Datakit connection error"),
    NO_INTERNET_CONNECTION(21, "No internet connection"),
    SERVER_DOWN(22, "Server down"),
    NOT_LOGGED_IN(23, "Not logged in"),
    INVALID_CONFIG_FILE(24, "Invalid config file"),
    CONFIG_FILE_NOT_AVAILABLE(25, "Config file not available"),
    PERMISSION_DENIED(26, "Permission denied"),
    INVALID_LOGIN(27, "invalid login");

    private int id;
    private String message;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
    public static MCStatus valueOf(int id){
        for(MCStatus value: MCStatus.values()){
            if(value.id==id) return value;
        }
        return UNKNOWN_ERROR;
    }

    MCStatus(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
