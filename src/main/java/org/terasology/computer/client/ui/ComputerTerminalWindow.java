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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.clipboard.ClipboardManager;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.layouts.CardLayout;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserHyperlinkListener;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ComputerTerminalWindow extends CoreScreenLayer {
    private ComputerTerminalWidget computerTerminalWidget;

    private Deque<String> browserHistory = new LinkedList<>();
    private Deque<String> browserFuture = new LinkedList<>();
    private UIButton backButton;
    private UIButton forwardButton;

    private BrowserWidget browser;
    private CardLayout tabs;

    private UIButton playerConsoleTabButton;
    private UIButton computerConsoleTabButton;
    private UIButton documentationTabButton;

    private Set<String> expandedPageIds = new HashSet<>();

    @Override
    public void initialise() {
        computerTerminalWidget = find("computerTerminal", ComputerTerminalWidget.class);

        playerConsoleTabButton = find("playerConsole", UIButton.class);
        computerConsoleTabButton = find("computerConsole", UIButton.class);
        documentationTabButton = find("documentation", UIButton.class);
     //   tocList = find("tableOfContents", UIList.class);

        tabs = find("tabs", CardLayout.class);
        browser = find("browser", BrowserWidget.class);
        browser.addBrowserHyperlinkListener(
                new BrowserHyperlinkListener() {
                    @Override
                    public void hyperlinkClicked(String hyperlink) {
                        if (hyperlink.startsWith("navigate:")) {
                         //   navigateTo(hyperlink.substring(9), true);
                        } else if (hyperlink.startsWith("saveAs:")) {
                            String[] split = hyperlink.substring(7).split(":", 2);
                            saveAs(HTMLLikeParser.unencodeHTMLLike(split[0]), HTMLLikeParser.unencodeHTMLLike(split[1]));
                        }
                    }
                });

        playerConsoleTabButton.subscribe(
                new ActivateEventListener() {
                    @Override
                    public void onActivated(UIWidget widget) {
                        setTabButtonsState(false, true, true);
                        tabs.setDisplayedCard("computerTerminal");
                        computerTerminalWidget.setMode(ComputerTerminalWidget.TerminalMode.PLAYER_CONSOLE);
                        requestFocusToTerminal();
                    }
                });
        computerConsoleTabButton.subscribe(
                new ActivateEventListener() {
                    @Override
                    public void onActivated(UIWidget widget) {
                        setTabButtonsState(true, false, true);
                        tabs.setDisplayedCard("computerTerminal");
                        computerTerminalWidget.setMode(ComputerTerminalWidget.TerminalMode.COMPUTER_CONSOLE);
                        requestFocusToTerminal();
                    }
                });
        documentationTabButton.subscribe(
                new ActivateEventListener() {
                    @Override
                    public void onActivated(UIWidget widget) {
                        setTabButtonsState(true, true, false);
                        tabs.setDisplayedCard("browserTab");
                    }
                });

        playerConsoleTabButton.setEnabled(false);

        UIButton homeButton = find("homeButton", UIButton.class);
        backButton = find("backButton", UIButton.class);
        forwardButton = find("forwardButton", UIButton.class);

//        homeButton.subscribe(
//                new ActivateEventListener() {
//                    @Override
//                    public void onActivated(UIWidget widget) {
//                        navigateTo("introduction", true);
//                    }
//                }
//        );
//        backButton.subscribe(
//                new ActivateEventListener() {
//                    @Override
//                    public void onActivated(UIWidget widget) {
//                        String currentPage = browserHistory.removeLast();
//                        browserFuture.addFirst(currentPage);
//                        String previousPage = browserHistory.peekLast();
//                        navigateTo(previousPage, false);
//                    }
//                });
//        forwardButton.subscribe(
//                new ActivateEventListener() {
//                    @Override
//                    public void onActivated(UIWidget widget) {
//                        String nextPage = browserFuture.removeFirst();
//                        browserHistory.add(nextPage);
//                        navigateTo(nextPage, false);
//                    }
//                });
    }

    @Override
    public void onGainFocus() {
        if (computerTerminalWidget != null) {
            requestFocusToTerminal();
        }
    }

    private void setTabButtonsState(boolean playerConsole, boolean computerConsole, boolean documentation) {
        playerConsoleTabButton.setEnabled(playerConsole);
        computerConsoleTabButton.setEnabled(computerConsole);
        documentationTabButton.setEnabled(documentation);
    }



    private void saveAs(String programName, String code) {
        computerTerminalWidget.saveProgram(programName, code);
    }

    private void updateHistoryButtons() {
        backButton.setEnabled(browserHistory.size() > 1);
        forwardButton.setEnabled(browserFuture.size() > 0);
    }

    private void requestFocusToTerminal() {
        CoreRegistry.get(NUIManager.class).setFocus(computerTerminalWidget);
    }

    public void initializeTerminal(ClipboardManager clipboardManager, EntityRef client, int computerId) {

        computerTerminalWidget.setup(clipboardManager,
                new Runnable() {
                    public void run() {
                        CoreRegistry.get(NUIManager.class).closeScreen(ComputerTerminalWindow.this);
                    }
                }, client, computerId);
        requestFocusToTerminal();
    }

    public ComputerTerminalWidget getComputerTerminalWidget() {
        return computerTerminalWidget;
    }

    @Override
    public void onClosed() {
        computerTerminalWidget.onClosed();
    }
}
