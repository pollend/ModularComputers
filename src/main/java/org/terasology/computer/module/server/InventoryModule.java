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
package org.terasology.computer.module.server;

import org.terasology.computer.machine.Computer;
import org.terasology.computer.module.BaseComputerModule;
import org.terasology.computer.module.ComputerMethod;
import org.terasology.computer.module.RegisterComputerModule;
import org.terasology.entitySystem.entity.EntityRef;

@RegisterComputerModule(name = "Inventory")
public class InventoryModule extends BaseComputerModule {

    @ComputerMethod(name = "dump", description = "This example moves all items from an inventory above the computer to inventory to the east of the computer. Please make sure this computer has a module of Inventory Manipulator type in any of its slots.")
    public void dump(){

    }

    @Override
    public void loadComputer(Computer computer) {

    }

    @Override
    public void unloadComputer(Computer computer) {

    }

    @Override
    public void onViewer(Computer computer, EntityRef viewer) {

    }

    @Override
    public void onStateChange(Computer computer, String state) {

    }
}
