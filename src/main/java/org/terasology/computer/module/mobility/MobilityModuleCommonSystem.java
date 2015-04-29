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
package org.terasology.computer.module.mobility;

import org.terasology.browser.data.ParagraphData;
import org.terasology.computer.system.common.ComputerModuleRegistry;
import org.terasology.computer.ui.documentation.DocumentationBuilder;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.mobileBlocks.server.BlockMoveManager;
import org.terasology.registry.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@RegisterSystem(RegisterMode.ALWAYS)
public class MobilityModuleCommonSystem extends BaseComponentSystem {
    public static final String MOBILITY_MODULE_TYPE = "Mobility";

    @In
    private ComputerModuleRegistry computerModuleRegistry;
    @In
    private BlockMoveManager blockMoveManager;

    @Override
    public void preBegin() {
        computerModuleRegistry.registerComputerModule(
                MOBILITY_MODULE_TYPE,
                new MobilityComputerModule(blockMoveManager, MOBILITY_MODULE_TYPE, "Mobility"),
                "This module allows computer to move within the world.",
                null,
                new TreeMap<String, String>() {{
                    put("move", "Moves the computer in the specified direction (if able).");
                }},
                new HashMap<String, Map<String, String>>() {{
                    put("move",
                            new LinkedHashMap<String, String>() {{
                                put("direction", "[String] Specifies the direction in which the computer should move. For more information " +
                                        "about <h navigate:object-type-Direction>Direction</h> - read the link.");
                            }});
                }},
                new HashMap<String, String>() {{
                    put("move", "[Boolean] If the movement was successful.");
                }},
                new HashMap<String, Collection<ParagraphData>>() {{
                    put("move", DocumentationBuilder.createExampleParagraphs(
                            "This example makes the computer move up one block. Please make sure " +
                                    "this computer has a module of Mobility type in any of its slots.",
                            "var mobilityMod = computer.bindModuleOfType(\"" + MOBILITY_MODULE_TYPE + "\");\n" +
                                    "mobilityMod.move(\"up\");"));
                }});
    }
}