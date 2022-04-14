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
package com.link_intersystems.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@link SerializableTemplateObjectFactory} takes a {@link Serializable}
 * object as it's template to create new instance (clones) of it each time the
 * {@link #getObject()} method gets invoked.
 *
 * @param <T> the type of object that this
 *            {@link SerializableTemplateObjectFactory} creates.
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
public class SerializableTemplateObjectFactory<T extends Serializable> implements ObjectFactory<T>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3315022442675292743L;

    private final byte[] template;

    /**
     * Constructs a new {@link SerializableTemplateObjectFactory} based on the
     * template object. The template object's state is frozen at construction
     * time of this {@link SerializableTemplateObjectFactory} so that the
     * {@link #getObject()} method returns new instances of the template object
     * whose states reflect the template object's state when this
     * {@link SerializableTemplateObjectFactory} was constructed. So changes to
     * the template object after constructing this
     * {@link SerializableTemplateObjectFactory} will have no effect to this
     * {@link ObjectFactory}.
     *
     * @param template the template for this {@link ObjectFactory}.
     */
    public SerializableTemplateObjectFactory(T template) {
        this.template = Serialization.serialize(Objects.requireNonNull(template, "template"));
    }

    /**
     * @return a new instance of the template object reflecting the template
     * object's state at the time that this
     * {@link SerializableTemplateObjectFactory} was created.
     */
    @SuppressWarnings("unchecked")
    public T getObject() {
        return (T) Serialization.deserialize(template);
    }

}
