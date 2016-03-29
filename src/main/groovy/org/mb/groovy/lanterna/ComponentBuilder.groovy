package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BorderLayout
import com.googlecode.lanterna.gui2.Component
import com.googlecode.lanterna.gui2.Label
import com.googlecode.lanterna.gui2.LayoutData
import com.googlecode.lanterna.gui2.Panel


class ComponentBuilder extends AbstractBuilder {
    private Panel panel
    BorderLayout.Location location = null

    ComponentBuilder(Panel panel, BorderLayout.Location location) {
        this.panel = panel
        this.component = panel
        this.location = location
    }

    ComponentBuilder(Panel panel) {
        this(panel, null)
    }

    private void addComponent(Component c) {
        if (location) {
            if (panel.children?.any { ((BorderLayout.Location)it.layoutData) == location })
                throw new LanternaBuilderException(location.toString() + ' location already contains a component')

            panel.addComponent(c, location)
        } else {
            panel.addComponent(c)
        }
    }

    void panel(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PanelBuilder) Closure cl) {
        panel(null, cl)
    }

    void panel(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PanelBuilder) Closure cl) {
        PanelBuilder builder = new PanelBuilder(attr)
        runClosure(cl, builder)
        addComponent(builder.component)
    }

    void label(String text) {
        addComponent(new Label(text))
    }

    void label(String text, Map attr) {

    }
}
