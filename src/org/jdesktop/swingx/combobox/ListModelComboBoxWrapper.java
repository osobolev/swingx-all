package org.jdesktop.swingx.combobox;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ListModelComboBoxWrapper<E> extends AbstractListModel<E> implements ComboBoxModel<E> {

    private final ListModel<E> delegate;

    private Object selectedItem;

    public ListModelComboBoxWrapper(ListModel<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public E getElementAt(int index) {
        return delegate.getElementAt(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        super.addListDataListener(l);
        delegate.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        delegate.removeListDataListener(l);
        super.removeListDataListener(l);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (selectedItem != null && !selectedItem.equals(anItem) || selectedItem == null && anItem != null) {
            selectedItem = anItem;

            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
}
