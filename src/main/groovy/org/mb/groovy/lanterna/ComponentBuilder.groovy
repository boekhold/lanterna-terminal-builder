package org.mb.groovy.lanterna

import com.googlecode.lanterna.SGR
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*

import java.util.regex.Pattern

class ComponentBuilder extends AbstractBuilder {
    final private Panel panel
    BorderLayout.Location location = null
    LayoutHelper layoutHelper = null

    ComponentBuilder(LanternaWindow window, Panel panel, LayoutHelper layoutHelper, BorderLayout.Location location) {
        this.window = window
        this.panel = panel
        this.layoutHelper = layoutHelper
        this.component = panel
        this.location = location
    }

    ComponentBuilder(LanternaWindow window, Panel panel, LayoutHelper layoutHelper) {
        this(window, panel, layoutHelper, null)
    }

    private void addComponent(Map attr, Component c) {
        if (layoutHelper) {
            LayoutData layoutData = layoutHelper.createLayoutData(attr)
            c.layoutData = layoutData
        }

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
        PanelBuilder builder = new PanelBuilder(window, attr)
        runClosure(cl, builder)
        addComponent(attr, builder.component)
    }

    void label(String text) {
        label(null, text)
    }

    void label(Map attr, String text) {
        Label c = new Label(text)

        if (attr?.style) {
            if (attr.style instanceof Collection) {
                def styles = attr.style as Collection
                styles.each {
                    c.addStyle(SGR.valueOf(it.toString().toUpperCase()))
                }
            } else {
                c.addStyle(SGR.valueOf((attr.style as String).toUpperCase()))
            }
        }

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

        if (attr?.password)
            box.mask = '*'
        if (attr?.validationPattern) {
            def pattern = Pattern.compile(attr.validationPattern as String)
            box.validationPattern = pattern
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

        addComponent(attr, addBorder(box, attr))
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

        addComponent(attr, addBorder(box, attr))
    }

    public void actionListBox(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ActionListBoxBuilder) Closure cl) {
        actionListBox(null, cl)
    }

    public void actionListBox(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ActionListBoxBuilder) Closure cl) {
        ActionListBoxBuilder builder = new ActionListBoxBuilder(window, attr)
        runClosure(cl, builder)
        addComponent(attr, addBorder(builder.component, attr))
    }

    public void separator() {
        separator(null)
    }

    public void separator(Map attr) {
        final Direction dir = attr?.direction ? Direction.valueOf((attr.direction as String).toUpperCase()) : Direction.HORIZONTAL
        Separator sep = new Separator(dir)

        TerminalSize size = getSize(attr)
        if (dir == Direction.VERTICAL && attr?.size instanceof Number)
            size = new TerminalSize(size.rows, size.columns)

        if (size)
            sep.setPreferredSize(size)

        addComponent(attr, sep)
    }
}
