package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.Borders
import com.googlecode.lanterna.gui2.Component

abstract class AbstractBuilder {
    protected Map<String, Component> registry
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

    void registerComponent(Map attr, Component c) {
        if (!attr?.id)
            return

        if (registry.containsKey(attr.id))
            throw new LanternaBuilderException("component with id '${attr.id}' is already registered")

        registry[(String)attr.id] = c
    }

    protected void runClosure(Closure cl, AbstractBuilder builder) {
        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}
