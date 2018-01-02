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
package org.terasology.computer.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.computer.machine.Computer;
import org.terasology.computer.machine.lua.LuaJMachine;
import org.terasology.computer.system.ModuleRegistrySystem;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.registry.In;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public abstract class BaseComputerModule extends BaseComponentSystem implements ComputerModule {

    @In
    protected ModuleRegistrySystem moduleRegistrySystem;

    @Override
    public void initialise() {
        super.initialise();
        this.moduleRegistrySystem.RegisterModule(this);
    }
}
