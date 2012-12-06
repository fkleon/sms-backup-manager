package de.leonhardt.sbm.gui.renderer;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class CustomListModel extends AbstractListModel {

	private static final long serialVersionUID = 8502920390418488566L;
	
	private List<Object> delegate = new LinkedList<Object>();
	
	@Override
	public Object getElementAt(int index) {
		return delegate.get(index);
	}

	@Override
	public int getSize() {
		return delegate.size();
	}
	
	public void addElement(Object obj) {
		delegate.add(obj);
		fireIntervalAdded(this, getSize(), getSize());
	}
	
	public void addElements(Collection<? extends Object> objects) {
		int curSize = delegate.size();
		delegate.addAll(curSize, objects);
		fireIntervalAdded(this, curSize, getSize()-1);
	}
	
	public void remove(int index) {
		delegate.remove(index);
		fireIntervalRemoved(this, index, getSize());
	}
	
	public void remove(Object obj) {
		if (delegate.remove(obj)) {
			fireIntervalRemoved(this, 0, getSize()+1);
		}
	}
	
	public void clear() {
		int oldSize = getSize();
		delegate.clear();
		fireIntervalRemoved(this, 0, oldSize);
	}
	
	public boolean contains(Object obj) {
		return delegate.contains(obj);
	}
	
	public void setElementAt(int index, Object element) {
		delegate.set(index, element);
		fireContentsChanged(this, index, index);
	}
}
