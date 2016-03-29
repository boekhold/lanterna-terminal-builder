package org.mb.groovy.lanterna

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.Component
import com.googlecode.lanterna.gui2.DefaultWindowManager
import com.googlecode.lanterna.gui2.EmptySpace
import com.googlecode.lanterna.gui2.MultiWindowTextGUI
import com.googlecode.lanterna.gui2.Window
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame

class LanternaTerminal {
    private Screen screen
    private MultiWindowTextGUI gui
    private Map<String, Window> windows = [:]
    private Map<String, Map<String, Component>> registry = [:]

    LanternaTerminal() {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        if (terminal instanceof SwingTerminalFrame)
            ((SwingTerminalFrame)terminal).defaultCloseOperation = javax.swing.WindowConstants.DISPOSE_ON_CLOSE

        screen = new TerminalScreen(terminal);
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
    }

    void window(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = WindowBuilder) Closure cl) {
        window(null, cl)
    }

    void window(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = WindowBuilder) Closure cl) {
        if (!attr.id)
            throw new LanternaBuilderException("'id' attribute is mandatory for windows")

        registry[(String)attr.id] = [:]
        WindowBuilder builder = new WindowBuilder(registry[(String)attr.id], attr)

        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        windows[(String)attr.id] = builder.window
        gui.addWindow(builder.window);
    }

    Map<String, Window> getWindows() {
        return windows
    }

    Map<String, Map<String, Component>> getRegistry() {
        return registry
    }

    void waitFor(String id) {
        windows[id]?.waitUntilClosed()
    }
}
