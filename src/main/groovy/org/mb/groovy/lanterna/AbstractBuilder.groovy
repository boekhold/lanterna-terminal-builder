package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.Component


abstract class AbstractBuilder {
    protected Component component

    protected Component getComponent() {
        return component
    }

    protected void runClosure(Closure cl, AbstractBuilder builder) {
        Closure code = cl.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}
