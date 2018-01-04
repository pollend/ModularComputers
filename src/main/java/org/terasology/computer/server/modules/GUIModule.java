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
package org.terasology.computer.server.modules;

import org.terasology.computer.server.ComputerContext;
import org.terasology.computer.server.ComputerMethod;
import org.terasology.computer.server.ComputerServerSystem;
import org.terasology.computer.server.RegisterComputerModule;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;

@RegisterComputerModule(name = "gui")
@RegisterSystem(RegisterMode.AUTHORITY)
public class GUIModule extends BaseComponentSystem {
    @In
    ComputerServerSystem computerServerSystem;

    @ComputerMethod(name = "circle", description = "")
    public void drawCircle(ComputerContext entityRef, float x, float y,float radius ){

    }

    @ComputerMethod(name = "square", description = "")
    public void drawSquare(ComputerContext entityRef, float x, float y, float radius ){

    }

    @ComputerMethod(name = "text", description = "")
    public void drawText(ComputerContext entityRef, float x, float y,String text){

    }

    @ComputerMethod(name = "bind", description = "")
    public void bindGui(ComputerContext entityRef, float x, float y,String text){

    }
}
