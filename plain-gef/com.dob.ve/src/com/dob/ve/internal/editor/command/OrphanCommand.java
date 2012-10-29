package com.dob.ve.internal.editor.command;

/**
 * Same as delete.
 * @author idanilov
 *
 */
public class OrphanCommand extends DeleteCommand {

	public OrphanCommand() {
		setLabel("Orphan Child");
		setDebugLabel("DOB orphan child command");
	}

}
