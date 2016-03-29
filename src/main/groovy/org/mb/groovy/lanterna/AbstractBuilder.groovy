package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.Component


abstract class AbstractBuilder {
    protected Map<String, Component> registry
    protected Component component

    protected Component getComponent() {
        return component
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
