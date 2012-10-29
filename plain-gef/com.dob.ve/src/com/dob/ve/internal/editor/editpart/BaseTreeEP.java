package com.dob.ve.internal.editor.editpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.ve.abstractmodel.DobModelChangeListenerAdapter;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.abstractmodel.DobXmlModelEvent;
import com.dob.ve.abstractmodel.IDobModelChangeListener;
import com.dob.ve.internal.editor.DobEditDomain;
import com.dob.ve.internal.editor.VEUtil;

/**
 * @author idanilov
 *
 */
public abstract class BaseTreeEP extends AbstractTreeEditPart {

	protected IPropertySource propertySource;
	protected IDobModelChangeListener changeListener;

	public BaseTreeEP() {
		super();
		changeListener = new DobModelChangeListenerAdapter() {

			public void added(DobXmlModelEvent e) {
				refreshChildren();
			}

			public void removed(DobXmlModelEvent e) {
				refreshChildren();
			}

			public void changed(DobXmlModelEvent e) {
				refreshVisuals();
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

	protected DobXmlElement getElement() {
		return (DobXmlElement) getModel();
	}

	@Override
	protected List getModelChildren() {
		List result = Collections.EMPTY_LIST;
		if (getElement() != null) {
			result = getElement().getChildren();
		}
		return result;
	}

	@Override
	public void activate() {
		super.activate();
		DobXmlElement model = getElement();
		if (model != null) {
			model.addChangeListener(changeListener);
		}
	}

	@Override
	public void deactivate() {
		DobXmlElement model = getElement();
		if (model != null) {
			model.removeChangeListener(changeListener);
		}
		super.deactivate();
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
