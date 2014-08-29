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
package com.facebook.swift.codec.generics;

import com.facebook.swift.codec.ThriftField;

import java.util.Objects;

public class GenericThriftStructBeanBase<T>
{
    private T genericProperty;

    @ThriftField(1)
    public T getGenericProperty()
    {
        return genericProperty;
    }

    @ThriftField(1)
    public void setGenericProperty(T value)
    {
        this.genericProperty = value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GenericThriftStructBeanBase<?> other = (GenericThriftStructBeanBase<?>) obj;
        return Objects.equals(this.genericProperty, other.genericProperty);
    }
}
