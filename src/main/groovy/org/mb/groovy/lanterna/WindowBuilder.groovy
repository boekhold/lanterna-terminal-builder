package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BasicWindow
import com.googlecode.lanterna.gui2.Component
import com.googlecode.lanterna.gui2.Window

class WindowBuilder {
    final private Map<String, Component> registry
    final private Window window

    WindowBuilder(Map<String, Component> registry, Map attr) {
        this.registry = registry
        window = new BasicWindow()

        if (attr?.title)
            window.title = attr.title
    }

    Window getWindow() {
        return window
    }

    void panel(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PanelBuilder) Closure cl) {
        panel(null, cl)
    }

    void panel(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PanelBuilder) Closure cl) {
        if (window.component)
            throw new LanternaBuilderException('window can only contain a single panel')

        PanelBuilder builder = new PanelBuilder(registry, attr)
        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()

        window.setComponent(builder.component)
    }
}
