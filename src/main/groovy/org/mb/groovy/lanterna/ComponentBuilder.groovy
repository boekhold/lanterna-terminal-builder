package org.mb.groovy.lanterna

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

import java.util.regex.Pattern

class ComponentBuilder extends AbstractBuilder {
    final private Panel panel
    BorderLayout.Location location = null

    ComponentBuilder(Map<String, Component> registry, Panel panel, BorderLayout.Location location) {
        this.registry = registry
        this.panel = panel
        this.component = panel
        this.location = location
    }

    ComponentBuilder(Map<String, Component> registry, Panel panel) {
        this(registry, panel, null)
    }

    private void addComponent(Map attr, Component c) {
        if (location) {
            if (panel.children?.any { ((BorderLayout.Location)it.layoutData) == location })
                throw new LanternaBuilderException(location.toString() + ' location already contains a component')

            panel.addComponent(c, location)
        } else {
            panel.addComponent(c)
        }
        registerComponent(attr, c)
    }

    void panel(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PanelBuilder) Closure cl) {
        panel(null, cl)
    }

    void panel(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PanelBuilder) Closure cl) {
        PanelBuilder builder = new PanelBuilder(registry, attr)
        runClosure(cl, builder)
        addComponent(attr, builder.component)
    }

    void label(String text) {
        label(null, text)
    }

    void label(Map attr, String text) {
        Component c = new Label(text)
        addComponent(attr, addBorder(c, attr))
    }

    void textBox(Map attr) {
        textBox(attr, null)
    }

    void textBox(Map attr, String defaultText) {
        TerminalSize size

        if (!attr.size)
            throw new LanternaBuilderException('size specification is required')

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

        TextBox box
        if (defaultText) {
            box = new TextBox(size, defaultText)
        } else {
            box = new TextBox(size)
        }

        if (attr.validationPattern) {
            def pattern = Pattern.compile(attr.validationPattern as String)
            box.setValidationPattern(pattern)
        }

        addComponent(attr, addBorder(box, attr))
    }
}
