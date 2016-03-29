package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BorderLayout
import com.googlecode.lanterna.gui2.LayoutData
import com.googlecode.lanterna.gui2.Panel

import javax.swing.border.Border


class BorderLayoutBuilder extends AbstractBuilder {
    private Panel panel

    BorderLayoutBuilder(Panel panel) {
        this.panel = panel
        this.component = panel
    }

    private void applyBuilder(Closure cl, BorderLayout.Location location) {
        ComponentBuilder builder = new ComponentBuilder(panel, location)
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
