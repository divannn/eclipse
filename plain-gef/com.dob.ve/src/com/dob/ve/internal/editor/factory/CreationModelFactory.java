package com.dob.ve.internal.editor.factory;

import java.util.Map;
import java.util.Set;

import org.eclipse.gef.requests.CreationFactory;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public class CreationModelFactory
		implements CreationFactory {

	private CreationInfo creationInfo;

	public CreationModelFactory(CreationInfo ci) {
		creationInfo = ci;
		if (ci == null) {
			throw new IllegalArgumentException("Provide non null creation info");
		}
		if (ci.type == null) {
			throw new IllegalArgumentException("Provide non null type");
		}
	}

	public Object getObjectType() {
		return creationInfo;
	}

	public Object getNewObject() {
		return createDefaultObject(creationInfo);
	}

	public static DobXmlElement createDefaultObject(CreationInfo ci) {
		DobXmlElement result = new DobXmlElement();
		result.setOProperty(IFooModelProperty.TYPE, ci.type);
		result.setOProperty(IFooModelProperty.DISPLAY_NAME, ci.type);
		setDefaultProps(result);
		return result;
	}

	private static void setDefaultProps(final DobXmlElement bean) {
		String type = FooModelUtils.getType(bean);
		Map<String, Object> defaultProps = FooModelUtils.getDefaultValues(type);
		if (defaultProps != null) {
			Set<String> propNames = defaultProps.keySet();
			for (String next : propNames) {
				bean.setOProperty(next, defaultProps.get(next));
			}
		}
	}

}
