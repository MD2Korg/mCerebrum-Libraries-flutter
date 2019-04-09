package org.md2k.core.datakit.converter;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
public class KryoConverter implements IByteConverter{
    private Kryo kryo;
    public KryoConverter(){
        kryo = new Kryo();
        kryo.register(MCDataSource.class);
        kryo.register(int[].class);
        kryo.register(long[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        kryo.register(byte[].class);
        kryo.register(boolean[].class);
        kryo.register(String.class);
        kryo.register(String[].class);
        kryo.register(HashMap.class);
        kryo.register(HashMap[].class);
        kryo.register(ArrayList.class);
    }
    /**
     * Reads a <code>DataSource</code> from a byte array that contains it.
     *
     * @param bytes Array to read.
     * @return The <code>DataSource</code> that was stored in the array.
     */

    @Override
    public Object fromBytes(byte[] bytes, Class c) {
        Input input = new Input(new ByteArrayInputStream(bytes));
        Object o = kryo.readObject(input,c);
        input.close();
        return o;
    }
    /*
     * Converts a <code>DataSource</code> to a byte array.
     *
     * @param MCDataSource <code>DataSource</code> to convert.
     * @return The resulting byte array.
     */

    @Override
    public <T> byte[] toBytes(T object) {
        byte[] bytes;
        Output output = new Output(new ByteArrayOutputStream());
        kryo.writeObject(output, object);
        bytes = output.toBytes();
        output.close();
        return bytes;
    }

}
