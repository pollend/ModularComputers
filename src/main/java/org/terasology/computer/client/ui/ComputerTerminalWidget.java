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
import org.terasology.computer.utility.ComputerStringUtility;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.clipboard.ClipboardManager;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.CoreWidget;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.LayoutConfig;
import org.terasology.rendering.nui.VerticalAlign;
import org.terasology.rendering.nui.events.NUIKeyEvent;
import org.terasology.utilities.Assets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ComputerTerminalWidget extends CoreWidget {
    public static final int CONSOLE_WIDTH = 87;
    public static final int CONSOLE_HEIGHT = 35;

    public static final Color BACKGROUND_COLOR = new Color(0x111111ff);
    public static final Color FRAME_COLOR = new Color(0xffffffff);

    public static final Color COMPUTER_CONSOLE_TEXT_COLOR = new Color(0xffffffff);

    private static final int PADDING_HOR = 5;
    private static final int PADDING_VER = 5;

    private static final int BLINK_LENGTH = 20;

    @LayoutConfig
    private String monospaceFont;

    @LayoutConfig
    private int characterWidth;

    @LayoutConfig
    private int fontHeight;

    private Font monospacedFont;

    private char[][] chars = new char[CONSOLE_HEIGHT][CONSOLE_WIDTH];
    private int blinkDrawTick;

    private int cursorPositionInPlayerCommand = 0;
    private String command = "";


    @Override
    public void onDraw(Canvas canvas) {
        if (monospacedFont == null) {
            monospacedFont = Assets.getFont(monospaceFont).get();
        }

        Rect2i region = canvas.getRegion();

        int screenWidth = region.width();
        int screenHeight = region.height();

        // Fill background with solid dark-grey
        canvas.drawFilledRectangle(Rect2i.createFromMinAndSize(0, 0, screenWidth, screenHeight), BACKGROUND_COLOR);

        // Draw white rectangle around the screen
        canvas.drawLine(0, 0, 0, screenHeight, FRAME_COLOR);
        canvas.drawLine(screenWidth, 0, screenWidth, screenHeight, FRAME_COLOR);
        canvas.drawLine(0, 0, screenWidth, 0, FRAME_COLOR);
        canvas.drawLine(0, screenHeight, screenWidth, screenHeight, FRAME_COLOR);

        for (int y = 0; y < CONSOLE_HEIGHT; y++) {
            StringBuilder result = new StringBuilder();
            for (int x = 0; x < CONSOLE_WIDTH; x++) {
                if (chars[y][x] == 0) {
                    result.append((char) 32);
                } else {
                    result.append(chars[y][x]);
                }
            }
            String text = result.toString();
            canvas.drawTextRaw(text, monospacedFont, COMPUTER_CONSOLE_TEXT_COLOR, Rect2i.createFromMinAndSize(PADDING_HOR, PADDING_VER + (y - 1) * fontHeight, characterWidth * text.length(), fontHeight), HorizontalAlign.CENTER, VerticalAlign.TOP);
        }

        int currentCommandDisplayStartIndex = 0;
        int lineLength = command.length() + 1;
        if (lineLength <= CONSOLE_WIDTH)
            currentCommandDisplayStartIndex = 0;
        else {
            int cursorPositionInCommand = 1 + cursorPositionInPlayerCommand;
            if (currentCommandDisplayStartIndex + CONSOLE_WIDTH > lineLength)
                currentCommandDisplayStartIndex = lineLength - CONSOLE_WIDTH;
            else if (cursorPositionInCommand > CONSOLE_WIDTH + currentCommandDisplayStartIndex)
                currentCommandDisplayStartIndex = cursorPositionInCommand - CONSOLE_WIDTH;
            else if (cursorPositionInCommand - 1 < currentCommandDisplayStartIndex)
                currentCommandDisplayStartIndex = cursorPositionInCommand - 1;
        }

        String wholeCommandLine = ">" + command;
        String commandLine = wholeCommandLine.substring(currentCommandDisplayStartIndex, Math.min(currentCommandDisplayStartIndex + CONSOLE_WIDTH, wholeCommandLine.length()));
        int cursorPositionInDisplayedCommandLine = 1 + cursorPositionInPlayerCommand - currentCommandDisplayStartIndex;

        final int lastLineY = PADDING_VER + fontHeight * (CONSOLE_HEIGHT - 1);
        canvas.drawTextRaw(commandLine, monospacedFont, COMPUTER_CONSOLE_TEXT_COLOR, Rect2i.createFromMinAndSize(PADDING_HOR, PADDING_VER + lastLineY, characterWidth * commandLine.length(), fontHeight), HorizontalAlign.CENTER, VerticalAlign.TOP);

        blinkDrawTick = ((++blinkDrawTick) % BLINK_LENGTH);
        if (blinkDrawTick * 2 > BLINK_LENGTH)
            canvas.drawLine(PADDING_HOR + cursorPositionInDisplayedCommandLine * characterWidth, PADDING_VER + lastLineY, PADDING_HOR + cursorPositionInDisplayedCommandLine * characterWidth, PADDING_VER + lastLineY + fontHeight, COMPUTER_CONSOLE_TEXT_COLOR);

    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        int width = PADDING_HOR * 2 + characterWidth * CONSOLE_WIDTH;
        int height = PADDING_VER * 2 + fontHeight * CONSOLE_HEIGHT;
        return new Vector2i(width, height);
    }


    public void appendToTerminal(String toAppend) {
        final String[] lines = toAppend.split("\n");
        List<String> realLinesToAppend = new ArrayList<>();
        for (String line : lines) {
            final String printableLine = ComputerStringUtility.stripInvalidCharacters(line);
            for (int i = 0; i < printableLine.length(); i += CONSOLE_WIDTH) {
                realLinesToAppend.add(printableLine.substring(i, Math.min(i + CONSOLE_WIDTH, printableLine.length())));
            }
        }
        // Strip all the lines that are overflowing the screen
        if (realLinesToAppend.size() > CONSOLE_HEIGHT) {
            realLinesToAppend = realLinesToAppend.subList(realLinesToAppend.size() - CONSOLE_HEIGHT, realLinesToAppend.size());
        }

        String[] realLines = realLinesToAppend.toArray(new String[realLinesToAppend.size()]);

        // Move all existing lines up, unless we need to replace all lines
        if (realLines.length < CONSOLE_HEIGHT) {
            System.arraycopy(chars, realLines.length, chars, 0, CONSOLE_HEIGHT - realLines.length);
        }

        // Replace the lines (at the end) with the contents of realLines
        int startIndex = CONSOLE_HEIGHT - realLines.length;
        for (int i = startIndex; i < CONSOLE_HEIGHT; i++) {
            setLine(i, realLines[i - startIndex]);
        }
    }

    private void setLine(int lineIndex, String text) {
        chars[lineIndex] = new char[CONSOLE_WIDTH];
        System.arraycopy(text.toCharArray(), 0, chars[lineIndex], 0, text.length());
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCursorIndex(int index) {
        this.cursorPositionInPlayerCommand = index;
    }



}

