/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.lang.reflect;

import com.link_intersystems.lang.ref.AbstractSerializableReference;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

class SerializablePackage extends AbstractSerializableReference<Package> {

    /**
     *
     */
    private static final long serialVersionUID = 7408462485150280625L;

    public SerializablePackage(Package packageObject) {
        super(requireNonNull(packageObject));
    }

    @Override
    protected Serializable serialize(Package nonSerializableObject2) {
        return nonSerializableObject2.getName();
    }

    @Override
    protected Package deserialize(Serializable restoreInfo) {
        String packageName = (String) restoreInfo;
        Package packageObject = Package.getPackage(packageName);
        if (packageObject == null) {
            throw new IllegalStateException("Unable to restore package "
                    + packageName);
        }
        return packageObject;
    }

}