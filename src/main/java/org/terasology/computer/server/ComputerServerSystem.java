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
package org.terasology.computer.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.computer.component.ComputerComponent;
import org.terasology.computer.component.ComputerTerminalComponent;
import org.terasology.computer.event.OnComputerLoaded;
import org.terasology.computer.event.OnComputerUnload;
import org.terasology.computer.event.OnDisplayActivated;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.world.block.BlockComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ComputerServerSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    private Map<EntityRef, ComputerContext> contexts = Maps.newHashMap();
    private Map<Class<? extends BaseComponentSystem>, ComputerModuleInfo> computerModuleInfoMap = Maps.newHashMap();
    private Map<Class<? extends Machine>, Machine> computerMachines = Maps.newHashMap();


    public <T extends BaseComponentSystem> boolean loadModule(T componentSystem) {
        if (!componentSystem.getClass().isAnnotationPresent(RegisterComputerModule.class)) {
            return false;
        }
        computerModuleInfoMap.put(componentSystem.getClass(), new ComputerModuleInfo<T>(componentSystem));
        return true;
    }

    public Iterable<ComputerModuleInfo> getComputerModules() {
        return computerModuleInfoMap.values();
    }

    public <T extends BaseComponentSystem> T getComputerModule(Class<T> classz) {
        ComputerModuleInfo moduleInfo = computerModuleInfoMap.get(classz);
        if (moduleInfo == null)
            return null;
        return (T) moduleInfo.getModule();
    }

    public ComputerContext getComputerContext(EntityRef entityRef) {
        return this.contexts.get(entityRef);
    }

    @ReceiveEvent
    public void computerLoadInWorld(OnActivatedComponent event, EntityRef entityRef, BlockComponent block, ComputerComponent computer) {
        ComputerContext context = new ComputerContext(entityRef, this);
        contexts.put(entityRef, context);
        entityRef.send(new OnComputerLoaded());
    }

    @ReceiveEvent
    public void computerUnloadedFromWorld(BeforeDeactivateComponent event, EntityRef entityRef, BlockComponent block, ComputerComponent computer) {
        ComputerContext computerContext = contexts.remove(entityRef);
        entityRef.send(new OnComputerUnload());
    }

    @ReceiveEvent
    public void terminalActivated(ActivateEvent event, EntityRef playerEntity, ComputerTerminalComponent component) {
        EntityRef target = event.getTarget();
        if (target.hasComponent(ComputerComponent.class)) {
            ComputerContext computerContext = getComputerContext(target);
            if (computerContext != null) {
                computerContext.setViewer(event.getInstigator());
                target.send(new OnDisplayActivated(event.getInstigator()));
            }
            event.consume();
        }
    }

    @Override
    public void update(float delta) {
        for (ComputerContext context : contexts.values()) {

        }
    }

    public static class ComputerModuleInfo<T extends BaseComponentSystem> {
        private T module;
        private String name;
        private Map<String, LinkedList<Method>> methoMap = Maps.newHashMap();
        private Map<String, Method> commandMap = Maps.newHashMap();

        public ComputerModuleInfo(T module) {
            name = module.getClass().getAnnotation(RegisterComputerModule.class).name();
            this.module = module;
            for (Method method : module.getClass().getMethods()) {
                ComputerMethod computerMethod = method.getAnnotation(ComputerMethod.class);
                if (computerMethod != null) {
                    LinkedList<Method> methods = methoMap.putIfAbsent(computerMethod.name(), Lists.newLinkedList());
                    methods.add(method);
                }
                ComputerCommand computerCommand = method.getAnnotation(ComputerCommand.class);
                if (computerCommand != null) {
                    commandMap.put(computerCommand.name(), method);
                }
            }
        }

        public String getName() {
            return name;
        }

        public T getModule() {
            return module;
        }

        public boolean hasMethod(String methodName) {
            return methoMap.containsKey(methodName);
        }

        public boolean hasCommand(String commandName) {
            return commandMap.containsKey(commandName);
        }

        public boolean invokeMethod(ComputerContext context, String methodName, Object[] objects) {
            //TODO: implement a handler for methodinfo
            return false;
        }

        public boolean invokeCommand(ComputerContext computer, String commandName, String[] args) throws InvocationTargetException, IllegalAccessException {
            Method method = commandMap.get(commandName);
            if (method == null)
                return false ;
            Object[] result = new Object[args.length + 1];
            result[0] = computer;
            for (int x = 1; x < args.length + 1; x++)
                result[x] = args[x - 1];
             return (boolean) method.invoke(module, result);
        }
    }
}
