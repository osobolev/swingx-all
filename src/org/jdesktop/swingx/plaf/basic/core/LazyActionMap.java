/*
 * $Id$
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.plaf.basic.core;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ActionMapUIResource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An ActionMap that populates its contents as necessary. The
 * contents are populated by invoking the <code>loadActionMap</code>
 * method on the passed in Object.
 *
 * @author Scott Violet
 * @version 1.6, 11/17/05
 */
public class LazyActionMap extends ActionMapUIResource {

    /**
     * Object to invoke <code>loadActionMap</code> on. This may be
     * a Class object.
     */
    private Object loader;

    /**
     * Installs an ActionMap that will be populated by invoking the
     * <code>loadActionMap</code> method on the specified Class
     * when necessary.
     * <p>
     * This should be used if the ActionMap can be shared.
     *
     * @param c           JComponent to install the ActionMap on.
     * @param loaderClass Class object that gets loadActionMap invoked
     *                    on.
     * @param defaultsKey Key to use to defaults table to check for
     *                    existing map and what resulting Map will be registered on.
     */
    public static void installLazyActionMap(JComponent c, Class<?> loaderClass, String defaultsKey) {
        ActionMap map = (ActionMap) UIManager.get(defaultsKey);
        if (map == null) {
            map = new LazyActionMap(loaderClass);
            UIManager.getLookAndFeelDefaults().put(defaultsKey, map);
        }
        SwingUtilities.replaceUIActionMap(c, map);
    }

    private LazyActionMap(Class<?> loader) {
        this.loader = loader;
    }

    public void put(Action action) {
        put(action.getValue(Action.NAME), action);
    }

    public void put(Object key, Action action) {
        loadIfNecessary();
        super.put(key, action);
    }

    public Action get(Object key) {
        loadIfNecessary();
        return super.get(key);
    }

    public void remove(Object key) {
        loadIfNecessary();
        super.remove(key);
    }

    public void clear() {
        loadIfNecessary();
        super.clear();
    }

    public Object[] keys() {
        loadIfNecessary();
        return super.keys();
    }

    public int size() {
        loadIfNecessary();
        return super.size();
    }

    public Object[] allKeys() {
        loadIfNecessary();
        return super.allKeys();
    }

    public void setParent(ActionMap map) {
        loadIfNecessary();
        super.setParent(map);
    }

    private void loadIfNecessary() {
        if (loader != null) {
            Object loader = this.loader;

            this.loader = null;
            Class<?> klass = (Class<?>) loader;
            try {
                Method method = klass.getDeclaredMethod("loadActionMap", LazyActionMap.class);
                method.invoke(klass, this);
            } catch (NoSuchMethodException nsme) {
                assert false : "LazyActionMap unable to load actions " + klass;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException iae) {
                assert false : "LazyActionMap unable to load actions " + iae;
            }
        }
    }
}
