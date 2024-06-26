/*
 * $Id: DatePickerCellEditor.java 3927 2011-02-22 16:34:11Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.table;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A CellEditor using a JXDatePicker as editor component.<p>
 * <p>
 * NOTE: this class will be moved!
 *
 * @author Richard Osbald
 * @author Jeanette Winzenburg
 */
public class DatePickerCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {

    private static final Logger LOG = Logger.getLogger(DatePickerCellEditor.class.getName());

    protected JXDatePicker datePicker;

    protected DateFormat dateFormat;

    protected int clickCountToStart = 2;

    private ActionListener pickerActionListener;

    protected boolean ignoreAction;

    private static final long serialVersionUID = -1L;

    /**
     * Instantiates a editor with the default dateFormat.
     * <p>
     * PENDING: always override default from DatePicker?
     */
    public DatePickerCellEditor() {
        this(null);
    }

    /**
     * Instantiates an editor with the given dateFormat. If
     * null, the datePickers default is used.
     *
     * @param dateFormat
     */
    public DatePickerCellEditor(DateFormat dateFormat) {
        // JW: the copy is used to synchronize .. can 
        // we use something else?
        this.dateFormat = dateFormat != null ? dateFormat : DateFormat.getDateInstance();
        datePicker = new JXDatePicker();
        // default border crushes the editor/combo
        datePicker.getEditor().setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        // should be fixed by j2se 6.0
        datePicker.setFont(UIManager.getDefaults().getFont("TextField.font"));
        if (dateFormat != null) {
            datePicker.setFormats(dateFormat);
        }
        datePicker.addActionListener(getPickerActionListener());
    }

//-------------------- CellEditor

    /**
     * Returns the pickers date.
     * <p>
     * Note: the date is only meaningful after a stopEditing and
     * before the next call to getTableCellEditorComponent.
     */
    @Override
    public Date getCellEditorValue() {
        return datePicker.getDate();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= getClickCountToStart();
        }
        return super.isCellEditable(anEvent);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * Overridden to commit pending edits. If commit successful, returns super,
     * else returns false.
     */
    @Override
    public boolean stopCellEditing() {
        ignoreAction = true;
        boolean canCommit = commitChange();
        ignoreAction = false;
        if (canCommit) {
            return super.stopCellEditing();
        }
        return false;
    }

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count an int specifying the number of clicks needed to start
     *              editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    /**
     * Returns the number of clicks needed to start editing.
     *
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    }

//------------------------ TableCellEditor

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // PENDING JW: can remove the ignore flags here?
        // the picker learnde to behave ...
        ignoreAction = true;
        datePicker.setDate(getValueAsDate(value));
        // todo how to..
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // datePicker.getEditor().selectAll();
        // }
        // });
        ignoreAction = false;
        return datePicker;
    }

    //-------------------------  TreeCellEditor

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        // PENDING JW: can remove the ignore flags here?
        // the picker learnde to behave ...
        ignoreAction = true;
        datePicker.setDate(getValueAsDate(value));
        // todo how to..
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // datePicker.getEditor().selectAll();
        // }
        // });
        ignoreAction = false;
        return datePicker;
    }

//-------------------- helpers    

    /**
     * Returns the given value as Date.
     * <p>
     * PENDING: abstract into something pluggable (like StringValue
     * in ComponentProvider?)
     *
     * @param value the value to map as Date
     * @return the value as Date or null, if not successful.
     */
    protected Date getValueAsDate(Object value) {
        if (isEmpty(value))
            return null;
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Long) {
            return new Date(((Long) value).longValue());
        }
        if (value instanceof String) {
            try {
                return dateFormat.parse((String) value);
            } catch (ParseException e) {
                handleParseException(e);
            }
        }
        if (value instanceof DefaultMutableTreeNode) {
            return getValueAsDate(((DefaultMutableTreeNode) value).getUserObject());
        }
        if (value instanceof AbstractMutableTreeTableNode) {
            return getValueAsDate(((AbstractMutableTreeTableNode) value).getUserObject());
        }
        return null;
    }

    /**
     * @param e
     */
    protected void handleParseException(ParseException e) {
        LOG.log(Level.SEVERE, e.getMessage(), e.getMessage());
    }

    protected boolean isEmpty(Object value) {
        return value == null || value instanceof String && ((String) value).length() == 0;
    }

//--------------- picker specifics    

    /**
     * Commits any pending edits and returns a boolean indicating whether the
     * commit was successful.
     *
     * @return true if the edit was valid, false otherwise.
     */
    protected boolean commitChange() {
        try {
            datePicker.commitEdit();
            return true;
        } catch (ParseException e) {
            // ignore
        }
        return false;
    }

    /**
     * @return the DatePicker's formats.
     * @see JXDatePicker#getFormats().
     */
    public DateFormat[] getFormats() {
        return datePicker.getFormats();
    }

    /**
     * @param formats the formats to use in the datepicker.
     * @see JXDatePicker#setFormats(DateFormat...)
     */
    public void setFormats(DateFormat... formats) {
        datePicker.setFormats(formats);
    }

    /**
     * Returns the ActionListener to add to the datePicker.
     *
     * @return the action listener to listen for datePicker's
     * action events.
     */
    protected ActionListener getPickerActionListener() {
        if (pickerActionListener == null) {
            pickerActionListener = createPickerActionListener();
        }
        return pickerActionListener;
    }

    /**
     * Creates and returns the ActionListener for the Picker.
     *
     * @return the ActionListener to listen for Picker's action events.
     */
    protected ActionListener createPickerActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // avoid duplicate trigger from
                // commit in stopCellEditing
                if (ignoreAction)
                    return;
                // still need to invoke .. hmm
                // no ... with the table cooperating the
                // invoke is contra-productive!
                terminateEdit(e);
            }

            /**
             * @param e
             */
            private void terminateEdit(ActionEvent e) {
                if (e != null && JXDatePicker.COMMIT_KEY.equals(e.getActionCommand())) {
                    stopCellEditing();
                } else {
                    cancelCellEditing();
                }
            }
        };
    }
}
