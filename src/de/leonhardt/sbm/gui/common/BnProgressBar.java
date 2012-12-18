package de.leonhardt.sbm.gui.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.IIntegerPM;

/**
 * The <code>BnProgressBar</code> is a {@link JProgressBar} that can subscribe to an
 * {@link IIntegerPM}.
 * 
 * @author Frederik Leonhardt
 * @beaninfo
 */
public class BnProgressBar extends JProgressBar implements View<IIntegerPM>, ModelSubscriber {

	private static final long serialVersionUID = -6160089539320338875L;

	private final Link link = new Link(this);
	
	private transient final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };
    
    private IIntegerPM pModel;

    /**
     * Constructs a <code>BnProgressBar</code>.
     */
    public BnProgressBar() {
    }
    
    /**
     * Constructs a <code>BnProgressBar</code> and binds it to the specified model.
     * 
     * @param pModel the model
     */
    public BnProgressBar(IIntegerPM pModel) {
    	this();
    	setPresentationModel(pModel);
    }
    
    /**
     * Constructs a <code>BnProgressBar</code> and subscribes it for the model at the
     * specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnProgressBar(ModelProvider provider, Path path) {
    	this.setModelProvider(provider);
    	this.setPath(path);
    }
    
    /**
     * Constructs a <code>BnProgressBar</code> and subscribes it for the model at the
     * root level provided by the given provider.
     * 
     * @param provider
     */
    public BnProgressBar(ModelProvider provider) {
    	this.setModelProvider(provider);
    	this.setPath(new Path());
    }
    
    /**
     * Returns whether this component is connected to the target
     * {@link AbstractPM} to synchronize with.
     * 
     * @return <code>true</code> when this component is connected, else
     *         <code>false</code>
     */
    boolean isConnected() {
        return this.pModel != null;
    }
    
    /**
     * Configures this component depending on the target {@link AbstractPM} s
     * attributes.
     */
    private void refresh() {
        refreshProgressBar();
    }
    
    /**
     * Configures the progress of this component on the depending on the target
     * {@link IIntegerPM}s attributes.
     */
    private void refreshProgressBar() {
    	if (this.pModel == null) {
    		this.setValue(0);
    	} else {
    		this.setValue(pModel.getInteger());
    		if (this.getValue() > 0) {
    			this.setVisible(true);
    		}
    	}
    }
    
	@Override
	public IIntegerPM getPresentationModel() {
		return pModel;
	}

	@Override
	public void setPresentationModel(IIntegerPM newModel) {
	     IIntegerPM oldModel = this.pModel;
	     if (this.pModel != null) {
	    	 this.pModel.removePropertyChangeListener(listener);
	     }
	     this.pModel = newModel;
	     if (this.pModel != null) {
	    	 this.pModel.addPropertyChangeListener(listener);
	     }
	     refresh();
	     this.firePropertyChange("presentationModel", oldModel, newModel);
	    }


	@Override
	public IModelProvider getModelProvider() {
		return this.link.getModelProvider();
	}

	@Override
	public Path getPath() {
		return this.link.getPath();
	}

	@Override
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	@Override
	public void setPath(Path path) {
		this.link.setPath(path);
	}

}
