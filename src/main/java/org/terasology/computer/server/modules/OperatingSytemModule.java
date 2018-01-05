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

import org.terasology.computer.component.FilesystemComponent;
import org.terasology.computer.event.OnComputerLoaded;
import org.terasology.computer.event.OnTerminalMessageReceived;
import org.terasology.computer.event.request.SendCommandRequest;
import org.terasology.computer.server.ComputerContext;
import org.terasology.computer.server.ComputerCommand;
import org.terasology.computer.server.ComputerMethod;
import org.terasology.computer.server.ComputerServerSystem;
import org.terasology.computer.server.RegisterComputerModule;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.registry.In;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

@RegisterComputerModule(name = "os")
public class OperatingSytemModule extends BaseComponentSystem {

    @In
    private ComputerServerSystem computerServerSystem;

    @In
    private GUIModule guiModule;
    @In
    private InventoryModule inventoryModule;
    @In
    private TerminalModule terminalModule;

    @Override
    public void initialise() {
        super.initialise();

        computerServerSystem.loadModule(this);
        computerServerSystem.loadModule(guiModule);
        computerServerSystem.loadModule(inventoryModule);
        computerServerSystem.loadModule(terminalModule);
    }

    @ComputerCommand(name = "run", description = "")
    public void run(ComputerContext computer, String[] args){
    }

    @ComputerCommand(name = "cd", description = "")
    public void changeDirectory(ComputerContext computer, String[] args){

    }

    @ComputerCommand(name = "ls", description = "")
    public void listDirectory(ComputerContext computer, String[] args){

    }

    @ComputerMethod(name = "save", description = "")
    public void save(ComputerContext computer,String path,String content, boolean overwrite){

    }

    @ComputerMethod(name = "read", description = "")
    public String read(ComputerContext computer,String path){
        return "";
    }


    @ReceiveEvent
    public void onComputerLoaded(OnComputerLoaded computerLoaded, EntityRef entityRef){
        ComputerContext context = computerServerSystem.getComputerContext(entityRef);
        if(!context.getEntityRef().hasComponent(FilesystemComponent.class))
            context.getEntityRef().addComponent(new FilesystemComponent());

    }
    @ReceiveEvent
    public void onCommandRequest(SendCommandRequest sendCommandRequest,EntityRef player){
        String message = sendCommandRequest.getMessage();
        String[] entries = message.split(" ");
        String[] args = new String[entries.length-1];
        for(int  x = 0; x < args.length;x++){
            args[x] = entries[x+1];
        }
        ComputerContext context = computerServerSystem.getComputerContext(sendCommandRequest.getEntityRef());
        this.command(context,entries[0],args);

    }


    public boolean command(ComputerContext computer,String op, String[] args) {
        for(ComputerServerSystem.ComputerModuleInfo info: computerServerSystem.getComputerModules()){
            if(info.hasCommand(op))
            {
                terminalModule.println(computer,op + " " + String.join(" ",args));
                try {
                    if(info.invokeCommand(computer,op,args)) {
                        return true;
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;

            }
        }
        terminalModule.println(computer,"Unknown Command: " + op);
        return false;
    }

}
