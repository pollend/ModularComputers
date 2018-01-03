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
import org.terasology.computer.component.ComputerComponent;
import org.terasology.computer.component.ComputerTerminalComponent;
import org.terasology.computer.machine.Computer;
import org.terasology.computer.module.console.ConsoleModule;
import org.terasology.computer.module.server.EditorModule;
import org.terasology.computer.module.server.OsModule;
import org.terasology.computer.ui.ComputerTerminalWindow;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.world.block.BlockComponent;

import java.util.Map;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ComputerSystem extends BaseComponentSystem {
    @In
    private NUIManager nuiManager;

    @In
    private OsModule osModule;

    @In
    private ConsoleModule consoleModule;

    @In
    private EditorModule editorModule;


    private Map<EntityRef,Computer> computers = Maps.newHashMap();

    @ReceiveEvent
    public void computerLoadInWorld(OnActivatedComponent event, EntityRef entityRef, BlockComponent block, ComputerComponent computer) {
        Computer comp =  buildComputer(entityRef);
        computers.put(entityRef,comp);
    }

    @ReceiveEvent
    public void computerUnloadedFromWorld(BeforeDeactivateComponent event, EntityRef entityRef, BlockComponent block, ComputerComponent computer) {
        computers.remove(entityRef);

    }

    private Computer  buildComputer(EntityRef entityRef){
       Computer computer = new Computer(entityRef);
       computer.loadModule(OsModule.class,osModule);
       computer.loadModule(ConsoleModule.class,consoleModule);
       computer.loadModule(EditorModule.class,editorModule);
       return computer;
    }

    @ReceiveEvent
    public void terminalActivated(ActivateEvent event, EntityRef item, ComputerTerminalComponent component) {
        EntityRef target = event.getTarget();
        if (target.hasComponent(ComputerComponent.class)) {
            Computer computer = computers.get(target);
            computer.setViewer(target);
            event.consume();
        }
    }
}
