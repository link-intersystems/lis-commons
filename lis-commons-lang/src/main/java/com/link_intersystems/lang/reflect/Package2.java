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

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.ref.SerializableReference;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides more features when working with {@link Package} objects.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0.0
 */
public class Package2 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4421507426199204063L;

    private static final Map<Package, Package2> PACKAGE_TO_PACAKGE2 = new HashMap<Package, Package2>();

    private final SerializableReference<Package> packageRef;

    public static Package2 get(Package packageObj) {
        if (packageObj == null) {
            return null;
        }
        Package2 package2 = PACKAGE_TO_PACAKGE2.get(packageObj);
        if (package2 == null) {
            package2 = new Package2(packageObj);
            PACKAGE_TO_PACAKGE2.put(packageObj, package2);
        }
        return package2;
    }

    public static Package2 get(String packageName) {
        Assert.notNull("packageName", packageName);
        return get(Package.getPackage(packageName));
    }

    Package2(Package packageObj) {
        this.packageRef = new SerializablePackage(packageObj);
    }

    /**
     * Returns the java {@link Package} this {@link Package2} is based on.
     *
     * @return the java {@link Package} this {@link Package2} is based on.
     * @since 1.2.0.0
     */
    public Package getPackage() {
        return packageRef.get();
    }

    /**
     * @return the parent {@link Package2} of this {@link Package2} if any or
     * null.
     * @since 1.2.0.0
     */
    public Package2 getParent() {
        String packageName = getName();
        String simpleName = getSimpleName();
        String parentPackageName = packageName.substring(0, packageName.length() - simpleName.length() - 1);
        return Package2.get(parentPackageName);
    }

    @Override
    public String toString() {
        return getPackage().toString();
    }

    /**
     * Returns the full package name like the {@link Package #getName()} does.
     *
     * @return the full package name like the {@link Package #getName()} does.
     * @since 1.2.0.0
     */
    public String getName() {
        return getPackage().getName();
    }

    /**
     * Returns the name of only this package, not the full package name.
     *
     * @return the name of only this package, not the full package name.
     * @since 1.2.0.0
     */
    public String getSimpleName() {
        String packageName = getName();
        List<String> packageFragmentNames = Arrays.asList(packageName.split("\\."));
        return packageFragmentNames.stream().reduce((prev, next) -> next).orElse(null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + packageRef.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Package2 other = (Package2) obj;
        if (!packageRef.equals(other.packageRef)) {
            return false;
        }
        return true;
    }
}
