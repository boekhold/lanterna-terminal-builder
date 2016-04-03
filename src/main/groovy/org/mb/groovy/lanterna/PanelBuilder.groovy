package org.mb.groovy.lanterna

import com.googlecode.lanterna.gui2.*

class PanelBuilder extends AbstractBuilder {
    final private Panel panel

    PanelBuilder(LanternaWindow window, Map attr) {
        this.window = window
        this.panel = new Panel()
        this.component = addBorder(panel, attr)
        registerComponent(attr, panel)
    }

    void gridLayout(int cols, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        final GridLayout layout = new GridLayout(cols)
        layout.leftMarginSize = 0
        layout.rightMarginSize = 0
        panel.layoutManager = layout
        ComponentBuilder builder = new ComponentBuilder(window, panel, null)
        runClosure(cl, builder)
    }

    void linearLayout(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        linearLayout(null, cl)
    }

    void linearLayout(Map attr, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        final LinearLayout layout
        if (attr?.direction) {
            final Direction direction = Direction.valueOf((attr.direction as String).toUpperCase())
            layout = new LinearLayout(direction)
        } else {
            layout = new LinearLayout()
        }

        final LayoutHelper layoutHelper = new LayoutHelper() {
            @Override
            LayoutData createLayoutData(Map attributes) {
                if (attributes?.align) {
                    // normalize specified alignment name
                    String align = (attributes.align as String).toUpperCase()
                    LinearLayout.Alignment alignment
                    switch (align) {
                        case "BEGINNING":
                            alignment = LinearLayout.Alignment.Beginning
                            break;
                        case "CENTER":
                            alignment = LinearLayout.Alignment.Center
                            break;
                        case "END":
                            alignment = LinearLayout.Alignment.End
                            break;
                        case "FILL":
                            alignment = LinearLayout.Alignment.Fill
                            break;
                        default:
                            throw new LanternaBuilderException("invalid alignment '${attributes.align}'")
                    }
                    return LinearLayout.createLayoutData(alignment)
                }
                return null
            }
        }
        panel.setLayoutManager(layout)
        ComponentBuilder builder = new ComponentBuilder(window, panel, layoutHelper)
        runClosure(cl, builder)
    }

    void borderLayout(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = BorderLayoutBuilder) Closure cl) {
        panel.setLayoutManager(new BorderLayout())
        BorderLayoutBuilder builder = new BorderLayoutBuilder(window, panel)
        runClosure(cl, builder)
    }

    void absoluteLayout(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        panel.setLayoutManager(new AbsoluteLayout())
        ComponentBuilder builder = new ComponentBuilder(window, panel, null)
        runClosure(cl, builder)
    }
}
