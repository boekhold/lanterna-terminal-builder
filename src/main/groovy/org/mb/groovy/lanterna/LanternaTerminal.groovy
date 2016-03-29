package org.mb.groovy.lanterna

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.BasicWindow
import com.googlecode.lanterna.gui2.DefaultWindowManager
import com.googlecode.lanterna.gui2.EmptySpace
import com.googlecode.lanterna.gui2.MultiWindowTextGUI
import com.googlecode.lanterna.gui2.Window
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import com.googlecode.lanterna.terminal.swing.SwingTerminal
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame


class LanternaTerminal extends AbstractBuilder {
    private Screen screen
    private Window window

    LanternaTerminal() {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        if (terminal instanceof SwingTerminalFrame)
            ((SwingTerminalFrame)terminal).defaultCloseOperation = javax.swing.WindowConstants.DISPOSE_ON_CLOSE

        screen = new TerminalScreen(terminal);
        screen.startScreen();
    }

    void window(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = WindowBuilder) Closure cl) {
        window(null, cl)
    }

    void window(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = WindowBuilder) Closure cl) {
        WindowBuilder builder = new WindowBuilder(attr)
        runClosure(cl, builder)
        window = builder.window
    }

    void waitFor() {
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
        window.close()
    }
}
