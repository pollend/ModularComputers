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
package org.terasology.computer.client;

import org.terasology.computer.component.ComputerComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.clipboard.ClipboardManager;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

@RegisterSystem(RegisterMode.CLIENT)
public class ComputerClientSystem extends BaseComponentSystem {
    private static final String COMPUTER_TERMINAL_UI = "ModularComputers:ComputerTerminal";

    @In
    private NUIManager nuiManager;
    @In
    private ClipboardManager clipboardManager;
    @ReceiveEvent
    public void terminalActivated(ActivateEvent event, EntityRef item, ComputerComponent component) {
        EntityRef target = event.getTarget();
        if (target.hasComponent(ComputerComponent.class)) {
            ComputerComponent computerComponent = target.getComponent(ComputerComponent.class);

            EntityRef client = event.getInstigator();

            nuiManager.pushScreen(COMPUTER_TERMINAL_UI);
            event.consume();
        }
    }


}
