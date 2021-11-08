package org.jdesktop.swingx.util;

import javax.swing.KeyStroke;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class EventUtils {

    /**
     * @return the accelerator for shortcuts
     */
    public static KeyStroke getMenuShortCut(int key) {
        int modifiers;
        if (GraphicsEnvironment.isHeadless()) {
            modifiers = OS.isMacOSX() ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK;
        } else {
            modifiers = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        }
        return KeyStroke.getKeyStroke(key, modifiers);
    }
}
