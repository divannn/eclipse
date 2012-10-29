package com.dob.ve.internal.editor.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.abstractmodel.DobModelChangeListenerAdapter;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.abstractmodel.DobXmlModelEvent;
import com.dob.ve.abstractmodel.IDobModelChangeListener;
import com.dob.ve.internal.editor.DobEditDomain;
import com.dob.ve.internal.editor.VEUtil;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilovO
 *
 */
public abstract class BaseGraphicalEP extends AbstractGraphicalEditPart {

	protected IPropertySource propertySource;
	private IDobModelChangeListener changeListener;
	/**
	 * Used to track "visible" property of children to hide/show it (by removing child from list of model children).
	 */
	private IDobModelChangeListener childChangeListener;

	public BaseGraphicalEP() {
		super();
		changeListener = new DobModelChangeListenerAdapter() {

			public void added(DobXmlModelEvent e) {
				updateChildren();
			}

			public void removed(DobXmlModelEvent e) {
				updateChildren();
			}

			@Override
			public void changed(DobXmlModelEvent e) {
				refreshVisuals(e);
			}

		};

		childChangeListener = new DobModelChangeListenerAdapter() {

			public void changed(DobXmlModelEvent e) {
				if (IFooModelProperty.VISIBLE.equals(e.getPropertyName())) {
					//System.err.println("set visible: " + e.getSource().isVisible());
					refreshChildren();
				}
			}
		};
	}

	@Override
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class) {
			if (propertySource == null) {
				propertySource = createPropertySource();
			}
			return propertySource;
		}
		return super.getAdapter(key);
	}

	protected IPropertySource createPropertySource() {
		return null;
	}

	private void updateChildren() {
		hookChildren(false);
		refreshChildren();
		hookChildren(true);
	}

	@Override
	//refreshes all visuals.
	protected void refreshVisuals() {
	}

	//refreshes granularly depending on property.
	protected void refreshVisuals(DobXmlModelEvent e) {

	}

	public DobXmlElement getElement() {
		return (DobXmlElement) getModel();
	}

	@Override
	protected List getModelChildren() {
		List<DobXmlElement> result = new ArrayList<DobXmlElement>(10);
		if (getElement() != null) {
			List<DobXmlElement> childs = getElement().getChildren();
			for (DobXmlElement nextChild : childs) {
				if (FooModelUtils.isVisible(nextChild).booleanValue()) {
					result.add(nextChild);
				}
			}
		}
		return result;
	}

	@Override
	public void activate() {
		super.activate();
		DobXmlElement model = getElement();
		if (model != null) {
			model.addChangeListener(changeListener);
			hookChildren(true);
		}
	}

	@Override
	public void deactivate() {
		DobXmlElement model = getElement();
		if (model != null) {
			model.removeChangeListener(changeListener);
			hookChildren(false);
		}
		super.deactivate();
	}

	private void hookChildren(final boolean add) {
		List<DobXmlElement> childModels = getElement().getChildren();
		for (DobXmlElement n : childModels) {
			if (add) {
				n.addChangeListener(childChangeListener);
			} else {
				n.removeChangeListener(childChangeListener);
			}
		}
	}

	protected DobEditDomain getEditDomain() {
		return DobEditDomain.getEditDomain(this);
	}

	protected Display getDisplay() {
		return getViewer().getControl().getDisplay();
	}

	protected IProject getProject() {
		DobEditDomain ed = getEditDomain();
		IProject result = VEUtil.getProject(ed);
		return result;
	}

}
