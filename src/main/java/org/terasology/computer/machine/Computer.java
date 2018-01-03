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
package org.terasology.computer.machine;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.terasology.computer.machine.event.ComputerStateEvent;
import org.terasology.computer.module.BaseComputerModule;
import org.terasology.computer.module.ComputerCommand;
import org.terasology.computer.module.console.ConsoleModule;
import org.terasology.computer.module.RegisterComputerModule;
import org.terasology.entitySystem.entity.EntityRef;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class Computer {


    private Set<Machine> machines = Sets.newLinkedHashSet();
    private Map<Class<? extends BaseComputerModule>, BaseComputerModule> moduleMap = Maps.newHashMap();
    private Map<String, BaseComputerModule> moduleInfoMap = Maps.newHashMap();
    private Map<String, ComputerCommand.ComputerCommandInfo> commandInfoMap = Maps.newHashMap();
    private Set<Task> tasks = Sets.newLinkedHashSet();
    private EntityRef entityRef;
    private ComputerFileSystem fileSystem;
    private String state = "";
    private boolean exist = true;
    // players looking at the monitor
    private EntityRef viewer = null;

    public Computer(EntityRef ref) {
        this.entityRef = ref;
        this.fileSystem = new ComputerFileSystem(ref);

    }

    public <T extends BaseComputerModule> boolean loadModule(Class<T> moduleClass, T module) {
        RegisterComputerModule info = module.getClass().getAnnotation(RegisterComputerModule.class);
        if (info == null)
            return false;

        for (Method method : module.getClass().getMethods()) {
            ComputerCommand computerCommand = method.getAnnotation(ComputerCommand.class);
            if (computerCommand != null) {
                commandInfoMap.put(computerCommand.name(), new ComputerCommand.ComputerCommandInfo(module, method));
            }
        }

        this.moduleMap.put(moduleClass, module);
        this.moduleInfoMap.put(info.name(), module);
        module.loadComputer(this);
        return true;
    }

    public <T extends BaseComputerModule> T getModule(Class<T> moduleClass) {
        return (T) this.moduleMap.get(moduleClass);
    }

    public <T extends BaseComputerModule> T getModule(String moduleName) {
        return (T) this.moduleInfoMap.get(moduleName);
    }


    public boolean command(String op, String[] args, boolean printCommand) {
        ComputerCommand.ComputerCommandInfo info = commandInfoMap.get(op);
        ConsoleModule consoleModule = (ConsoleModule) this.moduleMap.get(ConsoleModule.class);
        if (printCommand) {
            if (consoleModule != null) {
                consoleModule.println(this, op + " " + String.join(" ", args));
            }
        }
        if (info != null) {
            try {
                if (!info.invoke(this, args)) {
                    if (consoleModule != null) {
                        consoleModule.println(this, "Unknown Command: " + op);
                        return false;
                    }
                }
                return true;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (consoleModule != null) {
            consoleModule.println(this, "Unknown Command: " + op);
        }
        return false;

    }

    public void start(Task task) {
        tasks.add(task);
    }

    public boolean kill(Task task) {
        return false;
    }

    public Task[] tasks() {
        return null;
    }

    public EntityRef getEntityRef() {
        return entityRef;
    }

    public ComputerFileSystem getFileSystem() {
        return fileSystem;
    }

    public void setActiveState(String state) {
        if(viewer != null)
            this.viewer.send(new ComputerStateEvent(state,moduleInfoMap.keySet()));
        this.state = state;

    }

    public String getActiveState(){
        return this.state;
    }

    public void destroy() {
        this.exist = false;
        for (BaseComputerModule m : this.moduleMap.values()) {
            m.unloadComputer(this);
        }
    }

    public boolean exist() {
        return exist;
    }

    public void setViewer(EntityRef viewer) {
        if (viewer != null) {
            for (BaseComputerModule module : moduleMap.values()) {
                module.onViewer(this, viewer);
            }
            this.viewer.send(new ComputerStateEvent(state,moduleInfoMap.keySet()));

        }
        this.viewer = viewer;
    }

    public EntityRef getViewer() {
        return viewer;
    }


}
