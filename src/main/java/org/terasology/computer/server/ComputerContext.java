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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.entitySystem.systems.BaseComponentSystem;

public class ComputerContext {
    private EntityRef entityRef;
    private ComputerServerSystem computerServerSystem;
    private EntityRef viewer = null;

    public ComputerContext(EntityRef entityRef,ComputerServerSystem computerServerSystem) {
        this.entityRef = entityRef;
        this.computerServerSystem = computerServerSystem;
    }

    public EntityRef getEntityRef() {
        return entityRef;
    }

    public void setViewer(EntityRef viewer) {
        this.viewer = viewer;
    }

    public EntityRef getViewer() {
        return viewer;
    }

    public <T extends BaseComponentSystem> T getComputerModule(Class<T> classz){
        return this.computerServerSystem.getComputerModule(classz);
    }

}
