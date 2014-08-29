/*
 * Copyright (C) 2012 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.swift.codec.internal;

import com.facebook.swift.codec.ThriftCodec;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMap;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TSet;
import org.apache.thrift.protocol.TType;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

@NotThreadSafe
public class TProtocolReader
{
    private final TProtocol protocol;
    private TField currentField;

    public TProtocolReader(TProtocol protocol)
    {
        this.protocol = protocol;
    }

    public TProtocol getProtocol()
    {
        return protocol;
    }

    public void readStructBegin()
            throws TException
    {
        protocol.readStructBegin();
        currentField = null;
    }

    public void readStructEnd()
            throws TException
    {
        if (currentField == null || currentField.id != TType.STOP) {
            throw new IllegalStateException("Some fields have not been consumed");
        }

        currentField = null;
        protocol.readStructEnd();
    }

    public boolean nextField()
            throws TException
    {
        // if the current field is a stop record, the caller must call readStructEnd.
        if (currentField != null && currentField.id == TType.STOP) {
            throw new NoSuchElementException();
        }
        checkState(currentField == null, "Current field was not read");

        // advance to the next field
        currentField = protocol.readFieldBegin();

        return currentField.type != TType.STOP;
    }

    public short getFieldId()
    {
        checkState(currentField != null, "No current field");
        return currentField.id;
    }

    public byte getFieldType()
    {
        checkState(currentField != null, "No current field");
        return currentField.type;
    }

    public void skipFieldData()
            throws TException
    {
        TProtocolUtil.skip(protocol, currentField.type);
        protocol.readFieldEnd();
        currentField = null;
    }

    public Object readField(ThriftCodec<?> codec)
            throws Exception
    {
        if (!checkReadState(codec.getType().getProtocolType().getType())) {
            return null;
        }
        currentField = null;
        return codec.read(protocol);
    }

    public ByteBuffer readBinaryField()
            throws TException
    {
        if (!checkReadState(TType.STRING)) {
            return null;
        }

        ByteBuffer value = protocol.readBinary();
        currentField = null;
        return value;
    }

    public boolean readBoolField()
            throws TException
    {
        if (!checkReadState(TType.BOOL)) {
            return false;
        }
        currentField = null;
        return protocol.readBool();
    }

    public byte readByteField()
            throws TException
    {
        if (!checkReadState(TType.BYTE)) {
            return 0;
        }
        currentField = null;
        return protocol.readByte();
    }

    public double readDoubleField()
            throws TException
    {
        if (!checkReadState(TType.DOUBLE)) {
            return 0;
        }
        currentField = null;
        return protocol.readDouble();
    }

    public short readI16Field()
            throws TException
    {
        if (!checkReadState(TType.I16)) {
            return 0;
        }
        currentField = null;
        return protocol.readI16();
    }

    public int readI32Field()
            throws TException
    {
        if (!checkReadState(TType.I32)) {
            return 0;
        }
        currentField = null;
        return protocol.readI32();
    }

    public long readI64Field()
            throws TException
    {
        if (!checkReadState(TType.I64)) {
            return 0;
        }
        currentField = null;
        return protocol.readI64();
    }

    public String readStringField()
            throws TException
    {
        if (!checkReadState(TType.STRING)) {
            return null;
        }
        currentField = null;
        return protocol.readString();
    }

    public <T> T readStructField(ThriftCodec<T> codec)
            throws Exception
    {
        if (!checkReadState(TType.STRUCT)) {
            return null;
        }
        currentField = null;
        return codec.read(protocol);
    }

    public <E> Set<E> readSetField(ThriftCodec<Set<E>> setCodec)
            throws Exception
    {
        if (!checkReadState(TType.SET)) {
            return null;
        }
        currentField = null;
        return setCodec.read(protocol);
    }

    public <E> List<E> readListField(ThriftCodec<List<E>> listCodec)
            throws Exception
    {
        if (!checkReadState(TType.LIST)) {
            return null;
        }
        currentField = null;
        return listCodec.read(protocol);
    }

    public <K, V> Map<K, V> readMapField(ThriftCodec<Map<K, V>> mapCodec)
            throws Exception
    {
        if (!checkReadState(TType.MAP)) {
            return null;
        }
        currentField = null;
        return mapCodec.read(protocol);
    }

    public <T extends Enum<T>> T readEnumField(ThriftCodec<T> enumCodec)
            throws Exception
    {
        if (!checkReadState(TType.I32)) {
            return null;
        }
        currentField = null;
        return enumCodec.read(protocol);
    }

    public ByteBuffer readBinary()
            throws TException
    {
        return protocol.readBinary();
    }

    public boolean readBool()
            throws TException
    {
        return protocol.readBool();
    }

    public byte readByte()
            throws TException
    {
        return protocol.readByte();
    }

    public short readI16()
            throws TException
    {
        return protocol.readI16();
    }

    public int readI32()
            throws TException
    {
        return protocol.readI32();
    }

    public long readI64()
            throws TException
    {
        return protocol.readI64();
    }

    public double readDouble()
            throws TException
    {
        return protocol.readDouble();
    }

    public String readString()
            throws TException
    {
        return protocol.readString();
    }

    public <E> Set<E> readSet(ThriftCodec<E> elementCodec)
            throws Exception
    {
        TSet tSet = protocol.readSetBegin();
        Set<E> set = new HashSet<>();
        for (int i = 0; i < tSet.size; i++) {
            E element = elementCodec.read(protocol);
            set.add(element);
        }
        protocol.readSetEnd();
        return set;
    }

    public <E> List<E> readList(ThriftCodec<E> elementCodec)
            throws Exception
    {
        TList tList = protocol.readListBegin();
        List<E> list = new ArrayList<>();
        for (int i = 0; i < tList.size; i++) {
            E element = elementCodec.read(protocol);
            list.add(element);
        }
        protocol.readListEnd();
        return list;
    }


    public <K, V> Map<K, V> readMap(ThriftCodec<K> keyCodec, ThriftCodec<V> valueCodec)
            throws Exception
    {

        TMap tMap = protocol.readMapBegin();
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < tMap.size; i++) {
            K key = keyCodec.read(protocol);
            V value = valueCodec.read(protocol);
            map.put(key, value);
        }
        protocol.readMapEnd();
        return map;
    }

    private boolean checkReadState(byte expectedType)
            throws TException
    {
        checkState(currentField != null, "No current field");

        if (currentField.type != expectedType) {
            TProtocolUtil.skip(protocol, currentField.type);
            protocol.readFieldEnd();
            currentField = null;
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("TProtocolReader");
        sb.append("{currentField=").append(currentField);
        sb.append('}');
        return sb.toString();
    }
}
