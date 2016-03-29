package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.*

class PanelBuilder extends AbstractBuilder {
    final private Panel panel

    PanelBuilder(Map<String, Component> registry, Map attr) {
        this.registry = registry
        this.panel = new Panel()
        this.component = addBorder(panel, attr)
        registerComponent(attr, panel)
    }

    void gridLayout(int cols, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        panel.setLayoutManager(new GridLayout(cols))
        ComponentBuilder builder = new ComponentBuilder(registry, panel)
        runClosure(cl, builder)
    }

    void linearLayout(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        panel.setLayoutManager(new LinearLayout())
        ComponentBuilder builder = new ComponentBuilder(registry, panel)
        runClosure(cl, builder)
    }

    void borderLayout(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = BorderLayoutBuilder) Closure cl) {
        panel.setLayoutManager(new BorderLayout())
        BorderLayoutBuilder builder = new BorderLayoutBuilder(registry, panel)
        runClosure(cl, builder)
    }

    void absoluteLayout(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        panel.setLayoutManager(new AbsoluteLayout())
        ComponentBuilder builder = new ComponentBuilder(registry, panel)
        runClosure(cl, builder)
    }
}
