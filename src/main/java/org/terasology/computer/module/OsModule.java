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

import org.terasology.computer.machine.Computer;
import org.terasology.entitySystem.entity.EntityRef;

@RegisterComputerModule(name = "os")
public class OsModule extends BaseComputerModule{

    @ComputerMethod(name = "dump", description = "")
    public void dump(Computer computer){

    }

    @ComputerCommand(name = "run", description = "")
    public void run(Computer computer, String[] args){

    }

    @ComputerCommand(name = "edit", description = "")
    public void touch(Computer computer, String[] args){

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
}
