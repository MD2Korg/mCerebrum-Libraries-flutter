package org.md2k.mcerebrumapi.datakitapi.ipc.insert_data;

import android.util.SparseArray;

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
class _DataBuffer {
    private CircularBuffer circularBuffer;
    private static final int BUFFER_SIZE = 10;

    _DataBuffer() {
        circularBuffer = new CircularBuffer();
    }

    public void add(long timestamp) {
        circularBuffer.add(timestamp);
    }

    protected boolean isHighFrequency() {
        if (circularBuffer.getSize() < BUFFER_SIZE) return false;
        return circularBuffer.getTop() - circularBuffer.getBottom() <= 5000;
    }

    class CircularBuffer {
        private long[] buffer;
        private int tail;
        private int head;
        private int size;

        CircularBuffer() {
            buffer = new long[BUFFER_SIZE];
            tail = 0;
            head = -1;
            size = 0;
        }

        public int getSize() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void add(long toAdd) {
            if (size == BUFFER_SIZE)
                remove();
            size++;
            head = (head + 1) % buffer.length;
            buffer[head] = toAdd;
        }

        private void remove() {
            if (size == 0) return;
            size--;
            tail = (tail + 1) % buffer.length;
        }

        protected long getTop() {
            return buffer[head];
        }

        protected long getBottom() {
            return buffer[tail];
        }
    }
}
