package org.mb.groovy.lanterna

import org.junit.Ignore
import org.junit.Test

public class LanternaTerminalBuilderTest {
    @Ignore @Test
    public void testBasics() {
        def terminal = new LanternaTerminalBuilder().terminal(title: 'Main Frame') {
            window(id: 'mainWindow', title: 'My First Window') {
                panel {
                    gridLayout(2) {
                        label('My First Little Label')
                        panel(borderStyle: 'singleLine', borderTitle: 'BorderLayout') {
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

    @Ignore @Test
    public void testTextBox() {
        def terminal = new LanternaTerminalBuilder().terminal() {
            window(id: 'mainWindow', title: 'My First Window') {
                panel {
                    linearLayout() {
                        textBox(id: 'tb1', size: 20, 'Hello World')
                        textBox(size: 20, registry.tb1.text)
                        textBox(size: [20,2], 'Hello\n  World')
                        textBox(size: 20, validationPattern: '[0-9]+', '12345A')
                        // the above should throw an exception as per the Lanterna docs,
                        // but it doesn't
                    }
                }
            }
        }

        terminal.waitFor('mainWindow')
    }

    @Ignore @Test
    public void testButton() {
        def terminal = new LanternaTerminalBuilder().terminal() {
            window(id: 'mainWindow', title: 'My First Window') {
                panel {
                    linearLayout() {
                        textBox(id: 'tb1', size: 20, 'Value to print')
                        label('Click below')
                        button('Click Me') {
                            println registry.tb1.text
                        }
                    }
                }
            }
        }

        terminal.waitFor('mainWindow')
    }
}