package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BorderLayout
import com.googlecode.lanterna.gui2.Panel

class BorderLayoutBuilder extends AbstractBuilder {
    final private Panel panel

    BorderLayoutBuilder(LanternaWindow window, Panel panel) {
        this.window = window
        this.panel = panel
        this.component = panel
    }

    private void applyBuilder(Closure cl, BorderLayout.Location location) {
        ComponentBuilder builder = new ComponentBuilder(window, panel, location)
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
