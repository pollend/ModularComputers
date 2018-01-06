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
package org.terasology.computer.client.ui;

import org.lwjgl.input.Keyboard;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.events.NUIKeyEvent;

import java.util.LinkedList;
import java.util.List;

public class ComputerTerminalWindow extends CoreScreenLayer {
    public static final int CONSOLE_WIDTH = 87;
    public static final int CONSOLE_HEIGHT = 35;

    private ComputerTerminalWidget computerTerminalWidget;

    private List<String> commandHistory = new LinkedList<>();

    private StringBuilder currentCommand = new StringBuilder();
    private int cursorPositionInPlayerCommand = 0;
    private int historyIndex = 0;

    @Override
    public void initialise() {
        computerTerminalWidget = find("computerTerminal", ComputerTerminalWidget.class);
        requestFocusToTerminal();
    }

    @Override
    public void onGainFocus() {
        if (computerTerminalWidget != null) {
            requestFocusToTerminal();
        }
    }

    @Override
    public boolean onKeyEvent(NUIKeyEvent event) {
        int keyboardCharId = event.getKey().getId();
        if (event.isDown()) {
            char character = event.getKeyCharacter();

            if (character >= 32 && character < 127) {
                currentCommand.insert(cursorPositionInPlayerCommand, character);
                cursorPositionInPlayerCommand++;
            } else if (keyboardCharId == Keyboard.KEY_BACK && cursorPositionInPlayerCommand > 0) {
                currentCommand.delete(cursorPositionInPlayerCommand - 1, cursorPositionInPlayerCommand);
                cursorPositionInPlayerCommand--;
            } else if (keyboardCharId == Keyboard.KEY_DELETE && cursorPositionInPlayerCommand < currentCommand.length()) {
                currentCommand.delete(cursorPositionInPlayerCommand, cursorPositionInPlayerCommand + 1);
            } else if (keyboardCharId == Keyboard.KEY_LEFT && cursorPositionInPlayerCommand > 0) {
                cursorPositionInPlayerCommand--;
            } else if (keyboardCharId == Keyboard.KEY_RIGHT && cursorPositionInPlayerCommand < currentCommand.length()) {
                cursorPositionInPlayerCommand++;
            } else if (keyboardCharId == Keyboard.KEY_HOME) {
                cursorPositionInPlayerCommand = 0;
            } else if (keyboardCharId == Keyboard.KEY_END) {
                cursorPositionInPlayerCommand = currentCommand.length();
            } else if (keyboardCharId == Keyboard.KEY_RETURN) {
                String command = currentCommand.toString().trim();
                if (command.length() > 0) {
                    //add a command and remove if the terminal overflows
                    commandHistory.add(0, command);
                    historyIndex = 0;
                    if (commandHistory.size() > 20) {
                        commandHistory.remove(20);
                    }
                    this.appendToTerminal(">" + command);

                    //send to server
//                        computerTerminalWidget.executeCommand(command);
                    currentCommand = new StringBuilder();
                    cursorPositionInPlayerCommand = 0;
                }
            } else if (keyboardCharId == Keyboard.KEY_UP) {
                if (historyIndex < commandHistory.size()) {
                    currentCommand = new StringBuilder();
                    currentCommand.append(commandHistory.get(historyIndex));
                    cursorPositionInPlayerCommand = currentCommand.length();
                    historyIndex++;
                }
            } else if (keyboardCharId == Keyboard.KEY_DOWN) {
                if (historyIndex > 1) {
                    currentCommand = new StringBuilder();
                    currentCommand.append(commandHistory.get(historyIndex - 2));
                    cursorPositionInPlayerCommand = currentCommand.length();
                    historyIndex--;
                } else if (historyIndex == 1) {
                    currentCommand = new StringBuilder();
                    cursorPositionInPlayerCommand = 0;
                    historyIndex = 0;
                }
            }

        }
        computerTerminalWidget.setCommand(currentCommand.toString());
        computerTerminalWidget.setCursorIndex(cursorPositionInPlayerCommand);

        return super.onKeyEvent(event);
    }


    private void requestFocusToTerminal() {
        CoreRegistry.get(NUIManager.class).setFocus(computerTerminalWidget);
    }


    public void appendToTerminal(String toAppend) {
        this.computerTerminalWidget.appendToTerminal(toAppend);
    }
}
