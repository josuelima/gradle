/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.changedetection.resources;

import java.nio.CharBuffer;

public class IndexedRelativePath extends AbstractPath {
    private final String absolutePath;
    private final int index;

    public IndexedRelativePath(String absolutePath, int index) {
        this.absolutePath = absolutePath;
        this.index = index;
    }

    @Override
    public CharSequence getPath() {
        return CharBuffer.wrap(absolutePath, index, absolutePath.length());
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public int getIndex() {
        return index;
    }
}
