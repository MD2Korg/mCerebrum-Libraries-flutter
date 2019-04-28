package org.md2k.mcerebrumapi.datakitapi.ipc;

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
public final class OperationType {
    public static final int AUTHENTICATE = 0;
    public static final int INSERT_DATASOURCE = 100;
    public static final int QUERY_DATASOURCE = 101;
    public static final int SUBSCRIBE_DATASOURCE = 102;
    public static final int UNSUBSCRIBE_DATASOURCE = 103;
    public static final int INSERT_DATA = 104;
    public static final int INSERT_DATA_IF_NEW = 105;
    public static final int QUERY_DATA_BY_NUMBER = 106;
    public static final int QUERY_DATA_BY_TIME = 107;
    public static final int QUERY_DATA_COUNT = 108;
    public static final int SUBSCRIBE_DATA = 109;
    public static final int UNSUBSCRIBE_DATA = 110;

    public static final int GET_CONFIGURATION = 200;
    public static final int SET_CONFIGURATION = 201;

    private OperationType() {
    }
}
