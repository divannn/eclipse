package com.dob.ve.internal.editor;

import java.util.EventObject;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetEntry;

import com.dob.ve.internal.editor.property.BasePropertySource;

/**
 * Almost copy of UndoablePropertySheetEntry.
 * Fixed bug with multiple selection and undo/redo.
 * Changed 2 methods : 
 * 1) resetPropertyValue()
 * 2) valueChanged(..)
 * @author idanilov
 * @see UndoablePropertySheetEntry
 */
public class DobPropertySheetEntry extends PropertySheetEntry {

	private CommandStackListener commandStackListener;
	private CommandStack stack;
	/**
	 * Added to the command name to indicate that same operation has been performed on multiple objects.
	 */
	public static final String MULTI_SUFFIX = "<multiple>";

	private DobPropertySheetEntry() {
	}

	/**
	 * Constructs the root entry using the given command stack.
	 * @param stack the command stack
	 * @since 3.1
	 */
	public DobPropertySheetEntry(CommandStack stack) {
		setCommandStack(stack);
	}

	/**
	 * @see org.eclipse.ui.views.properties.PropertySheetEntry#createChildEntry()
	 */
	protected PropertySheetEntry createChildEntry() {
		return new DobPropertySheetEntry();
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySheetEntry#dispose()
	 */
	public void dispose() {
		if (stack != null) {
			stack.removeCommandStackListener(commandStackListener);
		}
		super.dispose();
	}

	protected CommandStack getCommandStack() {
		//only the root has, and is listening too, the command stack
		if (getParent() != null) {
			return ((DobPropertySheetEntry) getParent()).getCommandStack();
		}
		return stack;
	}

	void setCommandStack(CommandStack stack) {
		this.stack = stack;
		commandStackListener = new CommandStackListener() {

			public void commandStackChanged(EventObject e) {
				refreshFromRoot();
			}
		};
		stack.addCommandStackListener(commandStackListener);
	}

	//performs unset.
	public void resetPropertyValue() {
		CompoundCommand cc = new CompoundCommand();
		//UndoablePropertySheetEntry's code commented.
		/*ResetValueCommand restoreCmd;

		 if (getParent() == null)
		 // root does not have a default value
		 return;

		 //	Use our parent's values to reset our values.
		 boolean change = false;
		 Object[] objects = getParent().getValues();
		 for (int i = 0; i < objects.length; i++) {
		 IPropertySource source = getPropertySource(objects[i]);
		 if (source.isPropertySet(getDescriptor().getId())) {
		 //source.resetPropertyValue(getDescriptor()getId());
		 restoreCmd = new ResetValueCommand();
		 restoreCmd.setTarget(source);
		 restoreCmd.setPropertyId(getDescriptor().getId());
		 cc.add(restoreCmd);
		 change = true;
		 }
		 }
		 if (change) {
		 getCommandStack().execute(cc);
		 refreshFromRoot();
		 }*/

		if (getParent() == null) {
			// root does not have a default value
			return;
		}

		//	Use our parent's values to reset our values.
		boolean change = false;
		Object[] objects = getParent().getValues();
		for (int i = 0; i < objects.length; i++) {
			Object nextSelection = objects[i];
			//System.err.println("sel: " + nextSelection);
			BasePropertySource ps = (BasePropertySource) getPropertySource(nextSelection);
			Object model = ps.getElement();
			//if (ps.isPropertySet(getDescriptor().getId())) {//don't care as it is not so useful.
			Command nextCC = ps.resetValue(model, getDescriptor().getId());
			if (nextCC != null && nextCC.canExecute()) {
				cc.add(nextCC);
				change = true;
				//}
			}
		}
		if (change) {
			if (cc.size() > 1) {
				cc.setLabel(cc.getLabel() + MULTI_SUFFIX);
			}
			getCommandStack().execute(cc);
			refreshFromRoot();
		}
	}

	/**
	 * @see PropertySheetEntry#valueChanged(PropertySheetEntry)
	 */
	protected void valueChanged(PropertySheetEntry child) {
		valueChanged((DobPropertySheetEntry) child/*, new ForwardUndoCompoundCommand()*/);
	}

	//performs set. I will not use ForwardUndoCompoundCommand - seems it is useless.
	void valueChanged(DobPropertySheetEntry child/*, CompoundCommand command*/) {
		CompoundCommand cc = new CompoundCommand();//will contain commands per selection.

		//UndoablePropertySheetEntry's code commented.
		/*
		 command.add(cc);
		 SetValueCommand setCommand;
		 for (int i = 0; i < getValues().length; i++) {
		 setCommand = new SetValueCommand(child.getDisplayName());
		 setCommand.setTarget(getPropertySource(getValues()[i]));
		 setCommand.setPropertyId(child.getDescriptor().getId());
		 setCommand.setPropertyValue(child.getValues()[i]);
		 cc.add(setCommand);
		 }
		 */

		for (int i = 0; i < getValues().length; i++) {
			Object nextSelection = getValues()[i];
			//System.err.println("sel: " + nextSelection);
			Object key = child.getDescriptor().getId();
			//System.err.println("key: " + key);
			Object value = child.getValues()[i];
			//System.err.println("val: " + value);
			BasePropertySource ps = (BasePropertySource) getPropertySource(nextSelection);
			//System.err.println("PS " + ps);
			Object model = ps.getElement();
			//will contain commands for one selected object.
			Command nextCC = ps.setValue(model, key, value);
			if (nextCC != null && nextCC.canExecute()) {
				cc.add(nextCC);
			}
		}
		if (cc.size() > 0) {
			//command.add(cc);
			//System.err.println("curr command size: " + cc.getChildren().length);
			if (cc.size() > 1) {
				cc.setLabel(cc.getLabel() + MULTI_SUFFIX);
			}
		}

		//XXX: Unclear why to call parent.valueChanged()?
		//Seems in commandStackListener call to refreshFromRoot() does the job.
		//I will execute comand right away.
		// inform our parent
		//				if (getParent() != null) {
		//					((DobPropertySheetEntry) getParent()).valueChanged(this, command);
		//				} else {
		//					//I am the root entry
		//					stack.execute(command);
		//				}
		getCommandStack().execute(/*command*/cc);
	}

}
