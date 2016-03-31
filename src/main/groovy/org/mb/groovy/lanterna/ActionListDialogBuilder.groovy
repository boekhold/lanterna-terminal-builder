package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder


class ActionListDialogBuilderImpl {
    private ActionListDialogBuilder builder

    ActionListDialogBuilderImpl(ActionListDialogBuilder builder) {
        this.builder = builder
    }

    public void action(Closure cl) {
        builder.addAction(cl)
    }

    public void action(String label, Closure cl) {
        builder.addAction(label, cl)
    }
}
