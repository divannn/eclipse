package com.dob.ve.internal.editor.factory;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.EditorImages;
import com.dob.ve.internal.model.FooModelUtils;
import com.dob.ve.internal.model.IFooModelProperty;

/**
 * @author idanilov
 *
 */
public final class LabelProviderFactory {

	private static LabelProviderFactory instance = new LabelProviderFactory();

	public static LabelProviderFactory getInstance() {
		return instance;
	}

	private LabelProviderFactory() {
	}

	public ImageDescriptor getImageDescriptor(final String type) {
		return EditorImages.getDescriptor(getFileName(type));
	}

	public Image getImage(final String type) {
		return EditorImages.getImage(getFileName(type));
	}

	public ILabelProvider getLabelProvider(final Object model) {
		//TODO: consider possibility to create (load class) label provider per model.
		return new DefaultLabelProvider();
	}

	private static String getFileName(String nameWithoutExtension) {
		return nameWithoutExtension + EditorImages.GIF_EXT1;
	}

	private static class DefaultLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			Image result = super.getImage(element);
			if (element instanceof DobXmlElement) {
				DobXmlElement e = (DobXmlElement) element;
				String type = FooModelUtils.getType(e);
				String elemFileName = getFileName(type);
				result = EditorImages.getImage(elemFileName);
			}
			return result;
		}

		@Override
		public String getText(Object element) {
			String result = super.getText(element);
			if (element instanceof DobXmlElement) {
				DobXmlElement e = (DobXmlElement) element;
				result = (String) e.getOProperty(IFooModelProperty.DISPLAY_NAME);
				String id = FooModelUtils.getId(e);
				if (id != null && id.length() > 0) {
					result += " - " + id;
				}
			}
			return result;
		}

	}

}
