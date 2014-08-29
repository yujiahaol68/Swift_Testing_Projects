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
package com.facebook.swift.codec.internal.compiler.byteCode;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public class ParameterizedType
{
    public static ParameterizedType type(String className)
    {
        return new ParameterizedType(className);
    }

    public static ParameterizedType type(Class<?> type)
    {
        return new ParameterizedType(type);
    }

    public static ParameterizedType type(Class<?> type, Class<?>... parameters)
    {
        return new ParameterizedType(type, parameters);
    }

    public static ParameterizedType type(Class<?> type, ParameterizedType... parameters)
    {
        return new ParameterizedType(type, parameters);
    }

    private final String type;
    private final String className;
    private final List<String> parameters;

    public ParameterizedType(String className)
    {
        this.className = className;
        this.type = "L" + className + ";";
        this.parameters = ImmutableList.of();
    }

    private ParameterizedType(Class<?> type)
    {
        this.type = toInternalIdentifier(type);
        this.className = getPathName(type);
        this.parameters = ImmutableList.of();
    }

    private ParameterizedType(Class<?> type, Class<?>... parameters)
    {
        this.type = toInternalIdentifier(type);
        this.className = getPathName(type);

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (Class<?> parameter : parameters) {
            builder.add(toInternalIdentifier(parameter));
        }
        this.parameters = builder.build();
    }

    private ParameterizedType(Class<?> type, ParameterizedType... parameters)
    {
        this.type = toInternalIdentifier(type);
        this.className = getPathName(type);

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (ParameterizedType parameter : parameters) {
            builder.add(parameter.toString());
        }
        this.parameters = builder.build();
    }

    public String getClassName()
    {
        return className;
    }

    public String getType()
    {
        return type;
    }

    public String getGenericSignature()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('L').append(className);
        if (!parameters.isEmpty()) {
            sb.append("<");
            for (String parameterType : parameters) {
                sb.append(parameterType);
            }
            sb.append(">");
        }
        sb.append(";");
        return sb.toString();
    }

    public boolean isGeneric()
    {
        return !parameters.isEmpty();
    }

    @Override
    public String toString()
    {
        return getGenericSignature();
    }

    public static String getPathName(Class<?> n)
    {
        return n.getName().replace('.', '/');
    }

    private static String toInternalIdentifier(Class<?> n)
    {
        if (n.isArray()) {
            n = n.getComponentType();
            if (n.isPrimitive()) {
                if (n == Byte.TYPE) {
                    return "[B";
                }
                else if (n == Boolean.TYPE) {
                    return "[Z";
                }
                else if (n == Short.TYPE) {
                    return "[S";
                }
                else if (n == Character.TYPE) {
                    return "[C";
                }
                else if (n == Integer.TYPE) {
                    return "[I";
                }
                else if (n == Float.TYPE) {
                    return "[F";
                }
                else if (n == Double.TYPE) {
                    return "[D";
                }
                else if (n == Long.TYPE) {
                    return "[J";
                }
                else {
                    throw new RuntimeException("Unrecognized type in compiler: " + n.getName());
                }
            }
            else {
                return "[" + toInternalIdentifier(n);
            }
        }
        else {
            if (n.isPrimitive()) {
                if (n == Byte.TYPE) {
                    return "B";
                }
                else if (n == Boolean.TYPE) {
                    return "Z";
                }
                else if (n == Short.TYPE) {
                    return "S";
                }
                else if (n == Character.TYPE) {
                    return "C";
                }
                else if (n == Integer.TYPE) {
                    return "I";
                }
                else if (n == Float.TYPE) {
                    return "F";
                }
                else if (n == Double.TYPE) {
                    return "D";
                }
                else if (n == Long.TYPE) {
                    return "J";
                }
                else if (n == Void.TYPE) {
                    return "V";
                }
                else {
                    throw new RuntimeException("Unrecognized type in compiler: " + n.getName());
                }
            }
            else {
                return "L" + getPathName(n) + ";";
            }
        }
    }

    public static Predicate<ParameterizedType> isGenericType()
    {
        return new Predicate<ParameterizedType>()
        {
            @Override
            public boolean apply(ParameterizedType input)
            {
                return input.isGeneric();
            }
        };
    }

    public static Function<ParameterizedType, String> getParameterType()
    {
        return new Function<ParameterizedType, String>()
        {
            @Override
            public String apply(ParameterizedType input)
            {
                return input.getType();
            }
        };
    }

    public static Function<Class<?>, ParameterizedType> toParameterizedType()
    {
        return new Function<Class<?>, ParameterizedType>()
        {
            @Override
            public ParameterizedType apply(@Nullable Class<?> input)
            {
                return new ParameterizedType(input);
            }
        };
    }
}
