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
package org.terasology.computer.system.common;

import com.gempukku.lang.ObjectDefinition;
import org.terasology.browser.data.ParagraphData;
import org.terasology.computer.system.server.lang.ComputerModule;

import java.util.Collection;
import java.util.Map;

public interface ComputerLanguageContext {
    public void addObject(String object, ObjectDefinition objectDefinition, String objectDescription,
                          Collection<ParagraphData> additionalParagraphs, Map<String, String> functionDescriptions,
                          Map<String, Map<String, String>> functionParametersDescriptions,
                          Map<String, String> functionReturnDescriptions, Map<String, Collection<ParagraphData>> functionAdditionalParagraphs);

    public void addComputerModule(ComputerModule computerModule, String description,
                                  Collection<ParagraphData> additionalParagraphs, Map<String, String> methodDescriptions,
                                  Map<String, Map<String, String>> methodParametersDescriptions,
                                  Map<String, String> methodReturnDescriptions, Map<String, Collection<ParagraphData>> methodAdditionalParagraphs);
}
