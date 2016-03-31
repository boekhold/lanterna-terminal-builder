package org.mb.groovy.lanterna

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.Borders
import com.googlecode.lanterna.gui2.Component

abstract class AbstractBuilder {
    protected LanternaWindow window
    protected Component component

    protected Component getComponent() {
        return component
    }

    static Component addBorder(Component c, Map attr) {
        if (!attr?.borderStyle && !attr?.borderTitle)
            return c

        def borderStyle = attr.borderStyle ?: 'singleLine'
        def borderTitle = attr.borderTitle ?: ''

        def border
        switch (borderStyle) {
            case 'singleLine':
                border = Borders.singleLine(borderTitle)
                break
            case 'singleLineBevel':
                border = Borders.singleLineBevel(borderTitle)
                break
            case 'doubleLine':
                border = Borders.doubleLine(borderTitle)
                break
            case 'doubleLineBevel':
                border = Borders.doubleLineBevel(borderTitle)
                break
            default: border = Borders.singleLine()
        }
        return c.withBorder(border)
    }

    static TerminalSize getSize(Map attr) {
        TerminalSize size

        if (!attr?.size)
            return null

        if (attr.size instanceof List) {
            def sl = attr.size as List
            if (sl*.class == [Integer, Integer]) {
                size = new TerminalSize(sl[0] as int, sl[1] as int)
            } else {
                throw new LanternaBuilderException('size specification invalid: requires 2 list elements of type int')
            }
        } else if (attr.size instanceof Integer) {
            size = new TerminalSize((int)attr.size, 1)
        } else {
            throw new LanternaBuilderException('size specification invalid')
        }
        return size
    }

    void registerComponent(Map attr, Component c) {
        if (!attr?.id)
            return

        if (window.components.containsKey(attr.id as String))
            throw new LanternaBuilderException("component with id '${attr.id}' is already registered")

        window.components[attr.id as String] = c
    }

    /**
     * for shortcut access to the components of this underlying
     */
    Map<String, Component> getComponents() {
        return window.components
    }

    /**
     *
     */
    LanternaTerminal getTerminal() {
        return window.terminal
    }

    /**
     *
     */
    LanternaWindow getWindow() {
        return window
    }

    String messageDialog(Map attr, String text) {
        window.terminal.messageDialog(attr, text)
    }

    String textInputDialog(Map attr) {
        window.terminal.textInputDialog(attr)
    }

    protected void runClosure(Closure cl, AbstractBuilder builder) {
        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}
