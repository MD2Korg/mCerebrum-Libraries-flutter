/*
 * Copyright (c) 2018, The University of Memphis, MD2K Center of Excellence
 *
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

package org.md2k.mcerebrumapi.exception;


import org.md2k.mcerebrumapi.status.MCStatus;

/**
 * Generic <code>MCerebrumException</code>.
 */
public class MCException extends Exception {

    private MCStatus status;

    /**
     * Constructor with just a cause.
     *
     * @param cause What caused the exception to be thrown.
     */
    public MCException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message and cause
     *
     * @param message Message to display when the exception is triggered.
     * @param cause   What caused the exception to be thrown.
     */
    public MCException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with just a message.
     *
     * @param message Message to display when the exception is triggered.
     */
    public MCException(String message) {
        super(message);
    }

    /**
     * Constructor
     * This constructor take in a status.
     *
     * @param status <code>MCerebrumStatus</code> that triggered the exception.
     */
    public MCException(MCStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    /**
     * returns the status code of the <code>MCerebrumStatus</code>
     *
     * @return <code>MCerebrumStatus</code> as an integer.
     */
    public MCStatus getStatus() {
        return status;
    }
}
