package org.mb.groovy.lanterna

import org.junit.Ignore
import org.junit.Test

public class LanternaTerminalBuilderTest {
    @Ignore @Test
    public void testBasics() {
        def terminal = new LanternaTerminalBuilder().terminal() {
            window(id: 'mainWindow', title: 'My First Window') {
                panel {
                    gridLayout(2) {
                        label('My First Little Label')
                        panel {
                            border(style: 'singleLine', title: 'BorderLayout')
                            borderLayout {
                                top {
                                    label 'TOP'
                                }
                                right {
                                    label 'RIGHT'
                                }
                                bottom {
                                    label 'BOTTOM'
                                }
                                left {
                                    label 'LEFT'
                                }
                                center {
                                    label 'CENTER'
                                }
                            }
                        }
                    }
                }
            }
        }

        terminal.waitFor('mainWindow')
    }

    @Ignore @Test
    public void testBasicAlternative() {
        def terminal = new LanternaTerminalBuilder().terminal() {
            window(id: 'mainWindow', title: 'My First Window') {
                panel {
                    gridLayout(2) {
                        label('My First Little Label')
                        label('And a second one')
                    }
                }
            }
        }

        terminal.windows['mainWindow'].waitUntilClosed()
    }

    @Ignore @Test
    public void testComponentRegistration() {
        def terminal = new LanternaTerminalBuilder().terminal() {
            window(id: 'mainWindow', title: 'My First Window') {
                panel {
                    gridLayout(2) {
                        label(id: 'label1', 'My First Little Label')
                        label(id: 'label2', 'And a second one')
                    }
                }
            }
            window(id: 'subWindow', title: 'Sub Window', visible: false) {
                panel {
                    linearLayout {
                        label(id: 'label1', 'Label 1')
                        label(id: 'label2', 'Label 2')
                    }
                }
            }
        }

        assert terminal.registry['mainWindow'].collect { k, v -> k } == ['label1', 'label2']
        assert terminal.registry['subWindow'].collect { k, v -> k } == ['label1', 'label2']
    }
}