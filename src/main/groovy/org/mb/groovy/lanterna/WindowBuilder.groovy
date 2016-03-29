package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.BasicWindow
import com.googlecode.lanterna.gui2.Window

class WindowBuilder extends AbstractBuilder {
    final private Window window

    WindowBuilder(Map attr) {
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

        PanelBuilder builder = new PanelBuilder(attr)
        runClosure(cl, builder)
        window.setComponent(builder.component)
    }
}
