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
import org.terasology.computer.server.ComputerCommand;
import org.terasology.computer.server.ComputerMethod;
import org.terasology.computer.server.ComputerServerSystem;
import org.terasology.computer.server.RegisterComputerModule;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.registry.In;

@RegisterComputerModule(name = "os")
public class OperatingSytemModule extends BaseComponentSystem {

    @In
    ComputerServerSystem computerServerSystem;

    @In
    GUIModule guiModule;
    @In
    InventoryModule inventoryModule;
    @In
    TerminalModule terminalModule;

    @Override
    public void initialise() {
        super.initialise();

        computerServerSystem.loadModule(this);
        computerServerSystem.loadModule(guiModule);
        computerServerSystem.loadModule(inventoryModule);
        computerServerSystem.loadModule(terminalModule);
    }

    @ComputerMethod(name = "dump", description = "")
    public void dump(ComputerContext computer){

    }

    @ComputerCommand(name = "run", description = "")
    public void run(ComputerContext computer, String[] args){

    }

//    public boolean command(ComputerContext computer,String op, String[] args, boolean printCommand) {
//        ComputerCommand.ComputerCommandInfo info = commandInfoMap.get(op);
//        TerminalModule consoleModule = (TerminalModule) this.moduleMap.get(TerminalModule.class);
//        if (printCommand) {
//            if (consoleModule != null) {
//                consoleModule.println(this, op + " " + String.join(" ", args));
//            }
//        }
//        if (info != null) {
//            try {
//                if (!info.invoke(this, args)) {
//                    if (consoleModule != null) {
//                        consoleModule.println(this, "Unknown Command: " + op);
//                        return false;
//                    }
//                }
//                return true;
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (consoleModule != null) {
//            consoleModule.println(this, "Unknown Command: " + op);
//        }
//        return false;
//
//    }

}
