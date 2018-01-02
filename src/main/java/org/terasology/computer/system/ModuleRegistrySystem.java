/*
 * Copyright 2017 MovingBlocks
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
package org.terasology.computer.system;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.terasology.computer.module.BaseComputerModule;
import org.terasology.computer.module.ComputerCommand;
import org.terasology.computer.module.ComputerMethod;
import org.terasology.computer.module.RegisterComputerModule;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ModuleRegistrySystem extends BaseComponentSystem {
    private Map<String, BaseComputerModule> computerModules = Maps.newHashMap();

    public boolean RegisterModule(BaseComputerModule module) {
        RegisterComputerModule computerModule = module.getClass().getAnnotation(RegisterComputerModule.class);
        if (computerModule != null) {
            computerModules.put(computerModule.name(), module);
            return true;
        }
        return false;
    }

    public BaseComputerModule getModule(String module) {
        return this.computerModules.get(module);
    }

    public static RegisterComputerModule getModuleInfo(BaseComputerModule module) {
        if (module != null) {
            return module.getClass().getAnnotation(RegisterComputerModule.class);
        }
        return null;
    }

    public static ComputerMethod getMethodInfo(Method method) {
        return method.getAnnotation(ComputerMethod.class);
    }

    public static ComputerCommand getCommandInfo(Method method) {
        return method.getAnnotation(ComputerCommand.class);
    }

    public static Set<ComputerMethod> getMethodInfo(Iterable<Method> methods) {
        Set<ComputerMethod> result = Sets.newLinkedHashSet();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ComputerMethod.class))
                result.add(method.getAnnotation(ComputerMethod.class));
        }
        return result;
    }

    public static Set<Method> getMethods(BaseComputerModule module) {
        Method[] methods = module.getClass().getMethods();
        Set<Method> result = Sets.newLinkedHashSet();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ComputerMethod.class)) {
                result.add(method);
            }
        }
        return result;
    }
}
