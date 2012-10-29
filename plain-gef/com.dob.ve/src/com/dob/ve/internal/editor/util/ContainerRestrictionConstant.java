package com.dob.ve.internal.editor.util;

/**
 * @author idanilov
 *
 */
public final class ContainerRestrictionConstant {

	private ContainerRestrictionConstant() {
	}

	public static final IContainerRestriction ALLOW_NOTHING = new IContainerRestriction() {

		public boolean canCreate(Object child) {
			return false;
		}

		public boolean canPaste(Object child) {
			return false;
		}

	};

	public static final IContainerRestriction ALLOW_ALL = new IContainerRestriction() {

		public boolean canCreate(Object child) {
			return true;
		}

		public boolean canPaste(Object child) {
			return canCreate(child);
		}

	};

}
