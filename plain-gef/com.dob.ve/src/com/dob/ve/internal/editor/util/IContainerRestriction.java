package com.dob.ve.internal.editor.util;

/**
 * @author idanilov
 *
 */
public interface IContainerRestriction {

	boolean canCreate(Object child);

	boolean canPaste(Object child);

}
