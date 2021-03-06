/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.computer.system.common;

import com.gempukku.lang.ExecutionContext;
import com.gempukku.lang.Variable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapObjectDefinition implements DocumentedObjectDefinition {
    private Map<String, Variable> variableMap = new HashMap<>();
    private Map<String, DocumentedFunctionExecutable> methods = new HashMap<>();

    public void addMember(String name, DocumentedFunctionExecutable value) {
        variableMap.put(name, new Variable(value));
        methods.put(name, value);
    }

    @Override
    public Collection<String> getMethodNames() {
        return Collections.unmodifiableCollection(methods.keySet());
    }

    @Override
    public DocumentedFunctionExecutable getMethod(String methodName) {
        return methods.get(methodName);
    }

    @Override
    public Variable getMember(ExecutionContext context, String name) {
        Variable variable = variableMap.get(name);
        if (variable != null) {
            return variable;
        }
        return new Variable(null);
    }
}
