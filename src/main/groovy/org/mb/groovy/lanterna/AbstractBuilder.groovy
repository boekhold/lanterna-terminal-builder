package org.mb.groovy.lanterna

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.Borders
import com.googlecode.lanterna.gui2.Component

import java.util.regex.Pattern

abstract class AbstractBuilder {
    private static final Pattern STYLE_FORMAT = Pattern.compile("([a-zA-Z]+)(\\[([a-zA-Z0-9-_]+)\\])?");
    private static final Pattern INDEXED_COLOR = Pattern.compile("#[0-9]{1,3}");
    private static final Pattern RGB_COLOR = Pattern.compile("#[0-9a-fA-F]{6}");

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

    static TextColor parseColor(String value) {
        value = value.trim();
        if(RGB_COLOR.matcher(value).matches()) {
            int r = Integer.parseInt(value.substring(1, 3), 16);
            int g = Integer.parseInt(value.substring(3, 5), 16);
            int b = Integer.parseInt(value.substring(5, 7), 16);
            return new TextColor.RGB(r, g, b);
        }
        else if(INDEXED_COLOR.matcher(value).matches()) {
            int index = Integer.parseInt(value.substring(1));
            return new TextColor.Indexed(index);
        }
        try {
            return TextColor.ANSI.valueOf(value.toUpperCase());
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown color definition \"" + value + "\"", e);
        }
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

    protected void runClosure(Closure cl, AbstractBuilder builder) {
        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}
