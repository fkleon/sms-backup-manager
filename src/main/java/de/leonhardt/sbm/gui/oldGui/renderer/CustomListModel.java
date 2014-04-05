package de.leonhardt.sbm.gui.oldGui.renderer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A list model based on a linked list for efficient add, addAll and remove.
 * 
 * @author Frederik Leonhardt
 *
 */
public class CustomListModel extends AbstractListModel {

	private static final long serialVersionUID = 8502920390418488566L;
	
	// the underlying list
	private List<Object> delegate = new LinkedList<Object>();
	
	@Override
	public Object getElementAt(int index) {
		return delegate.get(index);
	}

	@Override
	public int getSize() {
		return delegate.size();
	}
	
	/**
	 * Adds an element to the model.
	 * @param obj
	 */
	public void addElement(Object obj) {
		delegate.add(obj);
		fireIntervalAdded(this, getSize(), getSize());
	}
	
	/**
	 * Adds a collection of elements to the model.
	 * @param objects
	 */
	public void addElements(Collection<? extends Object> objects) {
		if (objects.size() < 1) {
			return;
		}
		
		int curSize = delegate.size();
		delegate.addAll(curSize, objects);
		fireIntervalAdded(this, curSize, getSize()-1);
	}
	
	/**
	 * Removes the element at the given index of the model.
	 * @param index
	 */
	public void remove(int index) {
		delegate.remove(index);
		fireIntervalRemoved(this, index, getSize());
	}
	
	/**
	 * Removes the given element from the model.
	 * @param obj
	 */
	public void remove(Object obj) {
		if (delegate.remove(obj)) {
			fireIntervalRemoved(this, 0, getSize()+1);
		}
	}
	
	/**
	 * Removes all elements from the model.
	 */
	public void clear() {
		int oldSize = getSize();
		delegate.clear();
		fireIntervalRemoved(this, 0, oldSize);
	}
	
	/**
	 * Checks, if the given element is in the model.
	 * @param obj
	 * @return
	 */
	public boolean contains(Object obj) {
		return delegate.contains(obj);
	}
	
	/**
	 * Replaces the element at the given position of the model.
	 * @param index
	 * @param element
	 */
	public void setElementAt(int index, Object element) {
		delegate.set(index, element);
		fireContentsChanged(this, index, index);
	}
}
