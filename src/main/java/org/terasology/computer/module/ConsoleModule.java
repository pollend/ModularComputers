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

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import org.terasology.computer.machine.Computer;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;

import java.util.Map;
import java.util.Queue;

@RegisterComputerModule(name = "console")
@RegisterSystem(RegisterMode.AUTHORITY)
public class ConsoleModule extends BaseComputerModule {
    public static int MAX_LINES = 100;

    private Map<Computer,ConsoleMeta> consoleMeta = Maps.newHashMap();

    @ComputerMethod(name = "println", description = "")
    public void println(Computer computer, String str){

    }

    @ComputerMethod(name = "print", description = "")
    public void print(Computer computer,String str){

    }

    public static class ConsoleMeta{
        public Queue<StringBuffer> lines = Queues.newArrayDeque();

    }

    @Override
    public void loadComputer(Computer computer) {
        consoleMeta.put(computer,new ConsoleMeta());
    }

    @Override
    public void unloadComputer(Computer computer) {
        consoleMeta.remove(computer);
    }

    @Override
    public void onViewer(Computer computer, EntityRef viewer) {
        if(computer.getActiveScreen() == "") {

        }
    }

}
