package org.mb.groovy.lanterna

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

import java.util.regex.Pattern

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
        Component c = new Label(text)
        addComponent(attr, addBorder(c, attr))
    }

    void textBox(Map attr) {
        textBox(attr, null)
    }

    void textBox(Map attr, String defaultText) {
        TextBox box
        TerminalSize size = getSize(attr)

        if (!size)
            throw new LanternaBuilderException('size specification is required')

        if (defaultText) {
            box = new TextBox(size, defaultText)
        } else {
            box = new TextBox(size)
        }

        if (attr.validationPattern) {
            def pattern = Pattern.compile(attr.validationPattern as String)
            box.setValidationPattern(pattern)
        }

        addComponent(attr, addBorder(box, attr))
    }

    public void button(String text) {
        button(null, text, null)
    }

    public void button(Map attr, String text) {
        button(attr, text, null)
    }

    public void button(String text, Closure cl) {
        button(null, text, cl)
    }

    public void button(Map attr, String text, Closure cl) {
        Component button

        if (cl == null) {
            button = new Button(text)
        } else {
            button = new Button(text, cl.clone())
        }

        addComponent(attr, button)
    }

    public void comboBox(Collection c) {
        comboBox(null, c, null)
    }

    public void comboBox(Map attr, Collection c) {
        comboBox(attr, c, null)
    }

    public void comboBox(Collection c, Closure cl) {
        comboBox(null, c, cl)
    }

    public void comboBox(Map attr, Collection c, Closure cl) {
        TerminalSize size = getSize(attr)
        ComboBox cb = new ComboBox(c)

        if (size)
            cb.preferredSize = size

        // Not sure what this is supposed to do, the documentation mentions it
        // but it doesn't appear to do anything
        if (attr?.writeable)
            cb.readOnly = !attr.writeable

        if (cl != null) {
            cb.addListener(cl.clone())
        }

        addComponent(attr, cb)
    }

    public void emptySpace() {
        addComponent(null, new EmptySpace(new TerminalSize(0,0)))
    }

    public void checkBox(String label) {
        checkBox(null, label, null)
    }

    public void checkBox(Map attr, String label) {
        checkBox(attr, label, null)
    }

    public void checkBox(String label, Closure cl) {
        checkBox(null, label, cl)
    }

    public void checkBox(Map attr, String label, Closure cl) {
        TerminalSize size = getSize(attr)
        CheckBox box = new CheckBox(label)

        if (size)
            box.preferredSize = size

        if (cl)
            box.addListener(cl)

        if (attr?.checked == true)
            box.checked = attr.checked

        addComponent(attr, box)
    }

    public void checkBoxList(Collection coll) {
        checkBoxList(null, coll, null)
    }

    public void checkBoxList(Map attr, Collection coll) {
        checkBoxList(attr, coll, null)
    }

    public void checkBoxList(Collection coll, Closure cl) {
        checkBoxList(null, coll, cl)
    }

    public void checkBoxList(Map attr, Collection coll, Closure cl) {
        TerminalSize size = getSize(attr)
        CheckBoxList box = new CheckBoxList()

        coll.each { box.addItem(it) }

        if (size)
            box.preferredSize = size

        if (cl)
            box.addListener(cl)

        if (attr?.checked instanceof Collection) {
            Collection checkedList = attr.checked as Collection
            checkedList.each {
                box.setChecked(it, true)
            }
        }

        addComponent(attr, box)
    }

    public void radioBoxList(Collection coll) {
        radioBoxList(null, coll, null)
    }

    public void radioBoxList(Map attr, Collection coll) {
        radioBoxList(attr, coll, null)
    }

    public void radioBoxList(Collection coll, Closure cl) {
        radioBoxList(null, coll, cl)
    }

    public void radioBoxList(Map attr, Collection coll, Closure cl) {
        TerminalSize size = getSize(attr)
        RadioBoxList box = new RadioBoxList()

        coll.each { box.addItem(it) }

        if (size)
            box.preferredSize = size

        // Doesn't seem to work at the moment (3.0.0-beta2)
        if (cl)
            box.addListener(cl)

        if (attr?.checked)
            box.setCheckedItem(attr.checked)

        if (attr?.checkedByIndex instanceof Number)
            box.setCheckedItemIndex(attr.checkedByIndex as int)

        addComponent(attr, box)
    }
}
