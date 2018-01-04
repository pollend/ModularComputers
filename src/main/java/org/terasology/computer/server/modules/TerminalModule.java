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

import org.terasology.computer.server.ComputerCommand;
import org.terasology.computer.server.ComputerContext;
import org.terasology.computer.server.ComputerMethod;
import org.terasology.computer.server.RegisterComputerModule;
import org.terasology.entitySystem.systems.BaseComponentSystem;

@RegisterComputerModule(name = "terminal")
public class TerminalModule extends BaseComponentSystem {
    @ComputerMethod(name = "println", description = "")
    public void println(ComputerContext computer, String str) {

    }

    @ComputerMethod(name = "print", description = "")
    public void print(ComputerContext computer, String str) {

    }
//
//    @ComputerCommand(name = "ls", description = "")
//    public void list(Context computer, String[] args) {
//
//    }
//
//    @ComputerCommand(name = "cd", description = "")
//    public void changeDirectory(Context computer, String[] args) {
//
//    }
}
