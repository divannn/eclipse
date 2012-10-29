package com.dob.ve.internal.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

/**
 * @author idanilov
 * 
 */
public class VEUtil {

	private VEUtil() {
	}

	/**
	 * Domain and ediable file accosiation is 1:1 (editor per file).
	 * @param ed
	 * @return VE file that is associated with passed domain
	 */
	public static IFile getVeFile(final DobEditDomain ed) {
		return (IFile) ed.getData(DobVisualEditor.DXF_FILE);
	}

	/**
	 * Domain and project accosiation is n:1.
	 * @param ed
	 * @return VE projet that is associated with passed domain
	 */
	public static IProject getProject(final DobEditDomain ed) {
		IProject result = null;
		IFile f = getVeFile(ed);
		if (f != null) {
			result = f.getProject();
		}
		return result;
	}

}
