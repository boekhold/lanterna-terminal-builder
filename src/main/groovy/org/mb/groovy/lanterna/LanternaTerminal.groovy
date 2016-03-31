package org.mb.groovy.lanterna

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal

import javax.swing.*
import java.awt.*

class LanternaTerminal {
    private Screen screen
    private MultiWindowTextGUI gui
    private Map<String, Window> windows = [:]
    private Map<String, Map<String, Component>> registry = [:]

    LanternaTerminal(Map attr) {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        if (terminal instanceof Frame) {
            Frame frame = terminal as Frame

            if (attr?.title)
                frame.title = attr.title
        }

        if (terminal instanceof JFrame)
            (terminal as JFrame).defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        screen = new TerminalScreen(terminal);
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
    }

    void window(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = WindowBuilder) Closure cl) {
        if (!attr.id)
            throw new LanternaBuilderException("'id' attribute is mandatory for windows")

        registry[attr.id as String] = [:]
        WindowBuilder builder = new WindowBuilder(registry[attr.id as String], attr)

        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        windows[attr.id as String] = builder.window
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
