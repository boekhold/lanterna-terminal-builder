package org.mb.groovy.lanterna

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.ActionListBox

class ActionListBoxBuilder extends AbstractBuilder {
    private ActionListBox listBox

    public ActionListBoxBuilder(LanternaWindow window, Map attr) {
        this.window = window

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
