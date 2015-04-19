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
package org.terasology.computer.module.inventory;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.ModuleFunctionExecutable;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.inventory.InventoryComponent;

import java.util.Map;

public class ItemNameFunction implements ModuleFunctionExecutable {
    @Override
    public int getDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTicks() {
        return 0;
    }

    @Override
    public String[] getParameterNames() {
        return new String[] {"inventoryBinding", "slot"};
    }

    @Override
    public Object executeFunction(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        Variable inventoryBinding = parameters.get("inventoryBinding");
        if (inventoryBinding.getType() != Variable.Type.CUSTOM_OBJECT
                || !((CustomObject) inventoryBinding.getValue()).getType().equals("INVENTORY_BINDING"))
            throw new ExecutionException(line, "Invalid inventoryBinding in getItemName()");

        Variable slot = parameters.get("slot");
        if (slot.getType() != Variable.Type.NUMBER)
            throw new ExecutionException(line, "Invalid slot in getItemName()");

        int slotNo = ((Number) slot.getValue()).intValue();

        InventoryBinding binding = (InventoryBinding) inventoryBinding.getValue();
        EntityRef inventoryEntity = binding.getInventoryEntity(line, computer);
        InventoryComponent inventory = inventoryEntity.getComponent(InventoryComponent.class);

        int slotCount = inventory.itemSlots.size();

        if (slotNo<0 || slotCount<=slotNo)
            throw new ExecutionException(line, "Slot number out of range in getItemName()");

        EntityRef itemEntity = inventory.itemSlots.get(slotNo);
        return InventoryModuleUtils.getItemName(itemEntity);
    }

}