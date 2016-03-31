package org.mb.groovy.lanterna

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.ActionListBox
import com.googlecode.lanterna.gui2.Component


class ActionListBoxBuilder extends AbstractBuilder {
    private ActionListBox listBox

    public ActionListBoxBuilder(Map<String, Component> registry, Map attr) {
        this.registry = registry

        TerminalSize size = getSize(attr)
        if (size)
            this.listBox = new ActionListBox(size)
        else
            this.listBox = new ActionListBox()

        this.component = this.listBox
    }

    public void action(String label, Closure cl) {
        listBox.addItem(label, cl)
    }
}
