/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.computer.module.harvest;

import org.terasology.computer.system.server.lang.ComputerModule;
import org.terasology.computer.system.server.lang.ModuleFunctionExecutable;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;

import java.util.Collection;

public class HarvestComputerModule implements ComputerModule {
    private WorldProvider worldProvider;
    private BlockEntityRegistry blockEntityRegistry;
    private String moduleType;
    private String moduleName;

    public HarvestComputerModule(WorldProvider worldProvider, BlockEntityRegistry blockEntityRegistry, String moduleType, String moduleName) {
        this.worldProvider = worldProvider;
        this.blockEntityRegistry = blockEntityRegistry;
        this.moduleType = moduleType;
        this.moduleName = moduleName;
    }

    @Override
    public String getModuleType() {
        return moduleType;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public boolean canBePlacedInComputer(Collection<ComputerModule> computerModulesInstalled) {
        return true;
    }

    @Override
    public boolean acceptsNewModule(ComputerModule computerModule) {
        return true;
    }

    @Override
    public ModuleFunctionExecutable getFunctionByName(String name) {
        if (name.equals("harvest")) {
            return new HarvestFunction(worldProvider, blockEntityRegistry);
        } else if (name.equals("harvestToInventory")) {
            return new HarvestToInventoryFunction(worldProvider, blockEntityRegistry);
        }
        return null;
    }
}