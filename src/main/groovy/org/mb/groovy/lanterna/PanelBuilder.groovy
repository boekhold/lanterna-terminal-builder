package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.*

class PanelBuilder extends AbstractBuilder {
    final private Panel panel

    PanelBuilder(Map<String, Component> registry, Map attr) {
        this.registry = registry
        this.panel = new Panel()
        this.component = this.panel // default
        registerComponent(attr, panel)
    }

    void border(Map attr) {
        if (!attr.style)
            throw new LanternaBuilderException("'style' attribute is mandatory for border")

        def title = attr.title ?: ''
        def border
        switch (attr.style) {
            case 'singleLine':
                border = Borders.singleLine(title)
                break
            case 'singleLineBevel':
                border = Borders.singleLineBevel(title)
                break
            case 'doubleLine':
                border = Borders.doubleLine(title)
                break
            case 'doubleLineBevel':
                border = Borders.doubleLineBevel(title)
                break
            default: border = Borders.singleLine()
        }
        component = panel.withBorder(border)
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
