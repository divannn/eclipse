package com.dob.ve.internal.editor.property;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.dob.property.AbstractPropertySource;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.command.SetPropertyCommand;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public abstract class BasePropertySource extends AbstractPropertySource {

	protected DobXmlElement elem;
	protected IPropertyDescriptor[] propertyDescriptors;
	protected boolean cacheDescriptors;

	public BasePropertySource(DobXmlElement e) {
		this(e, true);
	}

	public BasePropertySource(DobXmlElement e, boolean cashe) {
		elem = e;
		cacheDescriptors = cashe;
	}

	public DobXmlElement getElement() {
		return elem;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null || !cacheDescriptors) {
			propertyDescriptors = createPropertyDescriptors().toArray(new IPropertyDescriptor[0]);
		}
		return propertyDescriptors;
	}

	protected List<IPropertyDescriptor> createPropertyDescriptors() {
		return Collections.emptyList();
	}

	@Override
	//actual set performed via command that is created in setValue(...).
	//@see setValue(Object model, Object id, Object value)
	public void setPropertyValue(Object id, Object value) {

	}

	@Override
	//	actual set performed via command that is created in resetValue(...).
	//@see resetValue(Object model, Object id)
	public void resetPropertyValue(Object id) {
	}

	/**
	 * Can be used to contribute property setting by adding into result additional command.
	 * @param model
	 * @param id
	 * @param value
	 * @param cc
	 */
	public CompoundCommand setValue(Object model, Object id, Object value) {
		CompoundCommand result = new CompoundCommand();
		Command primCommand = createSetPropertyCommand("Set Property", model, id, value);
		result.add(primCommand);
		return result;
	}

	public CompoundCommand resetValue(Object model, Object id, Object defaultValue) {
		CompoundCommand result = new CompoundCommand();
		Command primCommand = createSetPropertyCommand("Reset Property", model, id, defaultValue);
		result.add(primCommand);
		return result;
	}

	public CompoundCommand resetValue(Object model, Object id) {
		Object defaultValue = FooModelUtils.getDefaultValue((DobXmlElement) model, id.toString());
		return resetValue(model, id, defaultValue);
	}

	protected Command createSetPropertyCommand(final String commandName, Object model, Object id,
			Object value) {
		SetPropertyCommand result = new SetPropertyCommand(commandName);
		result.setModel(model);
		result.setKey(id);
		result.setValue(value);
		return result;
	}

	//TODO: move to utils.
	public static IPropertyDescriptor getDescriptorForID(IPropertySource source, Object propertyID) {
		IPropertyDescriptor result = null;
		if (source != null && propertyID != null) {
			IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
			for (int i = 0; i < descriptors.length; i++) {
				IPropertyDescriptor next = descriptors[i];
				if (next != null && propertyID.equals(next.getId())) {
					result = descriptors[i];
					break;
				}
			}
		}
		return result;
	}

}
