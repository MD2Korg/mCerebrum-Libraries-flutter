package org.md2k.mcerebrumapi.status;

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

/**
 * This class defines various status that can occur in the system.
 */
public final class MCStatus {
    public static final int UNKNOWN_ERROR = 100;
    public static final int SUCCESS = 0;
    public static final int MCEREBRUM_API_NOT_INITIALIZED = 1;
    public static final int MCEREBRUM_APP_NOT_INSTALLED = 2;
    public static final int CONNECTION_ERROR = 3;
    public static final int INVALID_PARAMETER = 4;
    public static final int INVALID_DATA_SOURCE = 5;
    public static final int MISSING_DATA_SOURCE_TYPE = 6;
    public static final int MISSING_DATA_TYPE = 7;
    public static final int DATA_SOURCE_NOT_REGISTERED = 8;
    public static final int INVALID_DATA = 9;
    public static final int INCONSISTENT_DATA_TYPE = 10;
    public static final int INVALID_TIMESTAMP = 11;
    public static final int DATA_SIZE_TOO_LARGE = 12;
    public static final int INVALID_DATA_FORMAT = 13;
    public static final int TIMEOUT = 14;
    public static final int AUTHENTICATION_REQUIRED = 15;
    public static final int AUTHENTICATION_REFUSED = 16;
    public static final int INVALID_OPERATION = 17;
    public static final int MCEREBRUM_BIND_ERROR = 18;
    public static final int DATAKIT_STOPPED = 19;
    public static final int DATAKIT_CONNECTION_ERROR = 20;
    public static final int NO_INTERNET_CONNECTION = 21;
    public static final int SERVER_DOWN = 22;
    public static final int NOT_LOGGED_IN = 23;
    public static final int INVALID_CONFIG_FILE = 24;
    public static final int CONFIG_FILE_NOT_AVAILABLE = 25;
    public static final int PERMISSION_DENIED = 26;
    public static final int INVALID_LOGIN = 27;

    private MCStatus() {
    }

    public static boolean isSuccessful(int status) {
        return status == SUCCESS;
    }

    /**
     * Returns the meaning of the integer status value as a string.
     *
     * @param status Status value to translate.
     * @return The meaning of the status as a string.
     */
    public static String getMessage(int status) {
        switch (status) {
            case UNKNOWN_ERROR:
                return "UNKNOWN_ERROR";
            case SUCCESS:
                return "SUCCESS";
            case MCEREBRUM_API_NOT_INITIALIZED:
                return "MCEREBRUM API is not initialized";
            case MCEREBRUM_APP_NOT_INSTALLED:
                return "MCEREBRUM_APP_NOT_INSTALLED";
            case CONNECTION_ERROR:
                return "CONNECTION_ERROR";
            case INVALID_PARAMETER:
                return "INVALID_PARAMETER";
            case INVALID_DATA_SOURCE:
                return "INVALID_DATA_SOURCE";
            case MISSING_DATA_SOURCE_TYPE:
                return "MISSING_DATA_SOURCE_TYPE";
            case MISSING_DATA_TYPE:
                return "MISSING_DATA_TYPE";
            case DATA_SOURCE_NOT_REGISTERED:
                return "DATA_SOURCE_NOT_REGISTERED";
            case INVALID_DATA:
                return "INVALID_DATA";
            case INCONSISTENT_DATA_TYPE:
                return "INCONSISTENT_DATA_TYPE";
            case INVALID_TIMESTAMP:
                return "INVALID_TIMESTAMP";
            case DATA_SIZE_TOO_LARGE:
                return "DATA_SIZE_TOO_LARGE";
            case INVALID_DATA_FORMAT:
                return "INVALID_DATA_FORMAT";
            case TIMEOUT:
                return "TIMEOUT";
            case AUTHENTICATION_REFUSED:
                return "AUTHENTICATION_REFUSED";
            case AUTHENTICATION_REQUIRED:
                return "AUTHENTICATION_REQUIRED";
            case INVALID_OPERATION:
                return "INVALID_OPERATION";
            case MCEREBRUM_BIND_ERROR:
                return "BIND_ERROR";
            case DATAKIT_STOPPED:
                return "DATAKIT_STOPPED";
            case DATAKIT_CONNECTION_ERROR:
                return "DATAKIT_CONNECTION_ERROR";
            case NO_INTERNET_CONNECTION:
                return "No Internet Connection";
            case NOT_LOGGED_IN:
                return "Not Logged In";
            case INVALID_CONFIG_FILE:
                return "INVALID_CONFIG_FILE";
            case SERVER_DOWN:
                return "Server is down";
            case CONFIG_FILE_NOT_AVAILABLE:
                return "CONFIG_FILE_NOT_AVAILABLE";
            case PERMISSION_DENIED:
                return "PERMISSION_DENIED";
            case INVALID_LOGIN:
                return "Invalid Username/Password";
            default:
                return "UNKNOWN_ERROR";
        }
    }
}
