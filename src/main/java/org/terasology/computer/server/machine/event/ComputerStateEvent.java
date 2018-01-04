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
package org.terasology.computer.server.machine.event;

import org.terasology.entitySystem.event.Event;
import org.terasology.network.ServerEvent;

import java.util.Set;

@ServerEvent
public class ComputerStateEvent implements Event {
    private  String state;
    private Set<String> modules;

    public ComputerStateEvent(String state, Set<String> modules){
        this.state = state;
        this.modules = modules;
    }

    public String getState() {
        return state;
    }

    public Set<String> getModules() {
        return modules;
    }
}
