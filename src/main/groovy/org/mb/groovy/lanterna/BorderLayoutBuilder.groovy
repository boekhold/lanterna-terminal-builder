package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BorderLayout
import com.googlecode.lanterna.gui2.Component
import com.googlecode.lanterna.gui2.Panel

class BorderLayoutBuilder extends AbstractBuilder {
    final private Panel panel

    BorderLayoutBuilder(Map<String, Component> registry, Panel panel) {
        this.registry = registry
        this.panel = panel
        this.component = panel
    }

    private void applyBuilder(Closure cl, BorderLayout.Location location) {
        ComponentBuilder builder = new ComponentBuilder(registry, panel, location)
        runClosure(cl, builder)
    }

    void top(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        applyBuilder(cl, BorderLayout.Location.TOP)
    }

    void right(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        applyBuilder(cl, BorderLayout.Location.RIGHT)
    }

    void bottom(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        applyBuilder(cl, BorderLayout.Location.BOTTOM)
    }

    void left(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        applyBuilder(cl, BorderLayout.Location.LEFT)
    }

    void center(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        applyBuilder(cl, BorderLayout.Location.CENTER)
    }
}
