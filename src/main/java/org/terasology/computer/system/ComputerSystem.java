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
import org.terasology.computer.ui.ComputerTerminalWindow;
import org.terasology.entitySystem.entity.EntityRef;
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
    private static final String COMPUTER_TERMINAL_UI = "ModularComputers:ComputerTerminal";

    @In
    private NUIManager nuiManager;


    private Map<EntityRef,Computer> computers = Maps.newHashMap();

    public void computerLoadInWorld(OnActivatedComponent event, EntityRef computerEntity, BlockComponent block, ComputerComponent computer) {

    }

    @ReceiveEvent
    public void terminalActivated(ActivateEvent event, EntityRef item, ComputerTerminalComponent component) {
        EntityRef target = event.getTarget();
        if (target.hasComponent(ComputerComponent.class)) {
            ComputerComponent computerComponent = target.getComponent(ComputerComponent.class);

            EntityRef client = event.getInstigator();

            nuiManager.pushScreen(COMPUTER_TERMINAL_UI);

            //ComputerTerminalWindow window = (ComputerTerminalWindow) nuiManager.getScreen(COMPUTER_TERMINAL_UI);
            //window.initializeTerminal(computerLanguageContextInitializer, clipboardManager, client, computerComponent.computerId);

            event.consume();
        }
    }
}
