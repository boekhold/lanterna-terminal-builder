package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BorderLayout
import com.googlecode.lanterna.gui2.Component
import com.googlecode.lanterna.gui2.Label
import com.googlecode.lanterna.gui2.Panel

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
        addComponent(attr, new Label(text))
    }
}
