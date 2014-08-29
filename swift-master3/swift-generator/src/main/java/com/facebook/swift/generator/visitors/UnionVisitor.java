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
package com.facebook.swift.generator.visitors;

import com.facebook.swift.generator.SwiftDocumentContext;
import com.facebook.swift.generator.SwiftGeneratorConfig;
import com.facebook.swift.generator.template.StructContext;
import com.facebook.swift.generator.util.TemplateLoader;
import com.facebook.swift.parser.model.ThriftField;
import com.facebook.swift.parser.model.Union;
import com.facebook.swift.parser.visitor.Visitable;

import java.io.File;
import java.io.IOException;

public class UnionVisitor extends AbstractTemplateVisitor
{
    public UnionVisitor(final TemplateLoader templateLoader,
                         final SwiftDocumentContext context,
                         final SwiftGeneratorConfig config,
                         final File outputFolder)
    {
        super(templateLoader, context, config, outputFolder);
    }

    @Override
    public boolean accept(final Visitable visitable)
    {
        return visitable.getClass() == Union.class;
    }

    @Override
    public void visit(final Visitable visitable) throws IOException
    {
        final Union union = Union.class.cast(visitable);
        final StructContext unionContext = contextGenerator.structFromThrift(union);

        for (final ThriftField field : union.getFields()) {
            unionContext.addField(contextGenerator.fieldFromThrift(field));
        }

        render(unionContext, "union");
    }
}
