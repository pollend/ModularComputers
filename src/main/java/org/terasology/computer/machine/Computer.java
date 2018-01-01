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
package org.terasology.computer.machine;

import com.google.common.collect.Sets;
import org.terasology.computer.command.Command;
import org.terasology.computer.module.BaseComputerModule;
import org.terasology.entitySystem.entity.EntityRef;

import java.util.Set;

public class Computer {
    private Terminal terminal;
    private Set<Machine> machines = Sets.newLinkedHashSet();
    private Set<BaseComputerModule> modules = Sets.newLinkedHashSet();
    private Set<Task> tasks = Sets.newLinkedHashSet();
    private EntityRef entityRef;
    private ComputerFileSystem fileSystem;


    public Computer(EntityRef ref) {
        this.entityRef = ref;
        this.fileSystem = new ComputerFileSystem(ref);
        this.terminal = new Terminal(25);
    }

    public void loadModule(BaseComputerModule module) {
        modules.add(module);
    }

    public void loadCommand(Command command) {

    }

    public void command(String op, String[] args) {

    }

    public void start(Task task) {
        tasks.add(task);
    }

    public boolean kill(Task task) {
        return false;
    }

    public Task[] tasks() {
        return null;
    }

    public EntityRef getEntityRef() {
        return entityRef;
    }
}
