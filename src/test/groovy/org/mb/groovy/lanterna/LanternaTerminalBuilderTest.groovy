package org.mb.groovy.lanterna;

import org.junit.Test;

import static org.junit.Assert.*;

public class LanternaTerminalBuilderTest {
    @Test
    public void testBasics() {
        def terminal = new LanternaTerminalBuilder().build() {
            window(title: 'My First Window') {
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

        terminal.waitFor()
    }
}