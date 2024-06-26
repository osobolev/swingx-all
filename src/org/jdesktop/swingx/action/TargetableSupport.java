/*
 * $Id: TargetableSupport.java 3972 2011-03-17 20:31:58Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx.action;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;

/**
 * @author rbair
 */
public class TargetableSupport {

    private final JComponent component;

    /**
     * Creates a new instance of TargetableSupport
     */
    public TargetableSupport(JComponent component) {
        this.component = component;
    }

    public boolean doCommand(Object command, Object value) {
        // Look at the internal component first.
        ActionMap map = component.getActionMap();
        Action action = map.get(command);

        if (action != null) {
            if (value instanceof ActionEvent) {
                action.actionPerformed((ActionEvent) value);
            } else {
                // XXX should the value represent the event source?
                action.actionPerformed(new ActionEvent(value, 0, command.toString()));
            }
            return true;
        }
        return false;
    }

    public Object[] getCommands() {
        ActionMap map = component.getActionMap();
        return map.allKeys();
    }

    public boolean hasCommand(Object command) {
        Object[] commands = getCommands();
        for (Object cmd : commands) {
            if (cmd.equals(command)) {
                return true;
            }
        }
        return false;
    }
}
