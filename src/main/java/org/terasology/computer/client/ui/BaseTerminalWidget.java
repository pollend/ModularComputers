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
package org.terasology.computer.client.ui;

import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.CoreWidget;
import org.terasology.utilities.Assets;

public abstract class BaseTerminalWidget extends CoreWidget {
    public static final Color BACKGROUND_COLOR = new Color(0x111111ff);
    public static final Color FRAME_COLOR = new Color(0xffffffff);

    public static final int CONSOLE_WIDTH = 87;
    public static final int CONSOLE_HEIGHT = 35;

    public static final String MONOSPACE_FONT = "ModularComputers:november";

    public static final int CHARACTER_WIDTH = 8;
    public static final int CHARACTER_HEIGHT = 16;

    public static final int PADDING_HOR = 5;
    public static final int PADDING_VER = 5;


    protected Font monospacedFont;


    @Override
    public void onDraw(Canvas canvas) {
        if (monospacedFont == null) {
            monospacedFont = Assets.getFont(MONOSPACE_FONT).get();
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

    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        int width = PADDING_HOR * 2 + CHARACTER_WIDTH * CONSOLE_WIDTH;
        int height = PADDING_VER * 2 + CHARACTER_HEIGHT * CONSOLE_HEIGHT;
        return new Vector2i(width, height);
    }

}
