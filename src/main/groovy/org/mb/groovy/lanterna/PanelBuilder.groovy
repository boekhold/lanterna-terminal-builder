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
        gridLayout(null, cols, cl)
    }

    void gridLayout(Map attr, int cols, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ComponentBuilder) Closure cl) {
        final GridLayout layout = new GridLayout(cols)
        
        layout.leftMarginSize = attr?.leftMargin ? attr.leftMargin as int : 0
        layout.rightMarginSize = attr?.leftMargin ? attr.leftMargin as int : 0
        layout.topMarginSize = attr?.topMargin ? attr.topMargin as int : 0
        layout.bottomMarginSize = attr?.bottomMargin ? attr.bottomMargin as int : 0
        panel.layoutManager = layout

        final LayoutHelper layoutHelper = new LayoutHelper() {
            @Override
            LayoutData createLayoutData(Map a) {
                def getAlignment = {
                    it ? GridLayout.Alignment.valueOf((it as String).toUpperCase()) : GridLayout.Alignment.BEGINNING
                }
                final GridLayout.Alignment hAlign = getAlignment(a?.hAlign)
                final GridLayout.Alignment vAlign = getAlignment(a?.vAlign)

                def getBoolean = {
                    it ? it as boolean : false
                }
                final boolean grabExtraHorizontalSpace = getBoolean(a?.hGrabExtraSpace)
                final boolean grabExtraVerticalSpace = getBoolean(a?.vGrabExtraSpace)

                final int hSpan = a?.hSpan ? a.hSpan as int : 1
                final int vSpan = a?.vSpan ? a.vSpan as int : 1

                return GridLayout.createLayoutData(hAlign, vAlign, grabExtraHorizontalSpace, grabExtraVerticalSpace, hSpan, vSpan)
            }
        }
        ComponentBuilder builder = new ComponentBuilder(window, panel, layoutHelper)
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
