package org.mb.groovy.lanterna

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.DefaultWindowManager
import com.googlecode.lanterna.gui2.EmptySpace
import com.googlecode.lanterna.gui2.MultiWindowTextGUI
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal

import javax.swing.*
import java.awt.*

class LanternaTerminal {
    private Screen screen
    private MultiWindowTextGUI gui
    private Map<String, LanternaWindow> windows = [:]

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

        WindowBuilder builder = new WindowBuilder(this, attr)

        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        windows[attr.id as String] = builder.window
        gui.addWindow(builder.window.underlying);
    }

    Map<String, LanternaWindow> getWindows() {
        return windows
    }

    void waitFor(String id) {
        windows[id]?.underlying?.waitUntilClosed()
    }

    String messageDialog(Map attr, String text) {
        MessageDialogBuilder dialogBuilder = new MessageDialogBuilder()
        dialogBuilder.text = text

        if (attr?.title)
            dialogBuilder.setTitle(attr.title as String)
        if (attr?.button) {
            // will throw an IllegalArgumentException if the user of the DSL
            // specified an invalid button name!
            dialogBuilder.addButton(MessageDialogButton.valueOf(attr.button as String))
        }
        if (attr?.buttons instanceof Collection) {
            def buttons = attr.buttons as Collection
            if (buttons.every { it instanceof String} ) {
                buttons.each {
                    dialogBuilder.addButton(MessageDialogButton.valueOf( it as String))
                }
            }
        }

        return dialogBuilder.build().showDialog(gui).toString()
    }
}
