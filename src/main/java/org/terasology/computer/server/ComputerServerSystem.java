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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.terasology.computer.component.ComputerComponent;
import org.terasology.computer.component.ComputerTerminalComponent;
import org.terasology.computer.server.event.OnComputerLoaded;
import org.terasology.computer.server.event.OnComputerUnload;
import org.terasology.computer.server.event.OnDisplayActivated;
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

import java.util.Map;
import java.util.Set;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ComputerServerSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    private Map<EntityRef, ComputerContext> contexts = Maps.newHashMap();
    private Set<BaseComponentSystem> componentSystems = Sets.newLinkedHashSet();

    public boolean loadModule(BaseComponentSystem componentSystem) {
        if (!componentSystem.getClass().isAnnotationPresent(RegisterComputerModule.class)) {
            return false;
        }
        componentSystems.add(componentSystem);
        return true;
    }

    public Iterable<BaseComponentSystem> getComputerModules() {
        return componentSystems;
    }

    public ComputerContext getComputerContext(EntityRef entityRef) {
        return this.contexts.get(entityRef);
    }

    @ReceiveEvent
    public void computerLoadInWorld(OnActivatedComponent event, EntityRef entityRef, BlockComponent block, ComputerComponent computer) {
        ComputerContext context = new ComputerContext(entityRef, this);
        contexts.put(entityRef, context);
        entityRef.send(new OnComputerLoaded(context));
    }

    @ReceiveEvent
    public void computerUnloadedFromWorld(BeforeDeactivateComponent event, EntityRef entityRef, BlockComponent block, ComputerComponent computer) {
        ComputerContext computerContext = contexts.remove(entityRef);
        entityRef.send(new OnComputerUnload(computerContext));
    }

    @ReceiveEvent
    public void terminalActivated(ActivateEvent event, EntityRef playerEntity, ComputerTerminalComponent component) {
        EntityRef target = event.getTarget();
        if (target.hasComponent(ComputerComponent.class)) {
            ComputerContext computerContext = getComputerContext(target);
            if (computerContext != null) {
                computerContext.setViewer(event.getInstigator());
                target.send(new OnDisplayActivated(computerContext, event.getInstigator()));
            }
            event.consume();
        }
    }

    @Override
    public void update(float delta) {
        for (ComputerContext context : contexts.values()) {

        }
    }
}
