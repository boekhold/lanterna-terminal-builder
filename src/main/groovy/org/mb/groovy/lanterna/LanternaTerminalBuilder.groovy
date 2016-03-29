package org.mb.groovy.lanterna

class LanternaTerminalBuilder {
    LanternaTerminal build(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = LanternaTerminal) Closure cl) {
        LanternaTerminal terminal = new LanternaTerminal()
        Closure code = cl.rehydrate(terminal, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
        return terminal
    }
}
