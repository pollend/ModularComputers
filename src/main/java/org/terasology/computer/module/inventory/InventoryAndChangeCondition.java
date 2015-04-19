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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryAndChangeCondition implements ModuleFunctionExecutable {
    private InventoryModuleConditionsRegister inventoryModuleConditionsRegister;

    public InventoryAndChangeCondition(InventoryModuleConditionsRegister inventoryModuleConditionsRegister) {
        this.inventoryModuleConditionsRegister = inventoryModuleConditionsRegister;
    }

    @Override
    public int getDuration() {
        return 200;
    }

    @Override
    public int getMinimumExecutionTicks() {
        return 0;
    }

    @Override
    public String[] getParameterNames() {
        return new String[] {"inventoryBinding"};
    }

    @Override
    public Object executeFunction(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        Variable inventoryBinding = parameters.get("inventoryBinding");
        if (inventoryBinding.getType() != Variable.Type.CUSTOM_OBJECT
                || !((CustomObject) inventoryBinding.getValue()).getType().equals("INVENTORY_BINDING"))
            throw new ExecutionException(line, "Invalid inventoryBinding in getInventoryAndChangeCondition()");

        InventoryBinding binding = (InventoryBinding) inventoryBinding.getValue();
        EntityRef inventoryEntity = binding.getInventoryEntity(line, computer);
        InventoryComponent inventory = inventoryEntity.getComponent(InventoryComponent.class);

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> inventoryResult = new ArrayList<>();

        for (EntityRef item : inventory.itemSlots) {
            Map<String, Object> itemMap = new HashMap<>();

            int itemCount = InventoryModuleUtils.getItemCount(item);
            String itemName = InventoryModuleUtils.getItemName(item);

            itemMap.put("name", itemName);
            itemMap.put("count", itemCount);

            inventoryResult.add(itemMap);
        }

        result.put("inventory", inventoryResult);

        result.put("condition", inventoryModuleConditionsRegister.registerInventoryChangeListener(inventoryEntity));

        return result;
    }
}