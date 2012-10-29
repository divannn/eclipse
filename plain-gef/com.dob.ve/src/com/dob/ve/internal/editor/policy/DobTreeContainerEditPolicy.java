package com.dob.ve.internal.editor.policy;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.command.AddCommand;
import com.dob.ve.internal.editor.command.CreateCommand;
import com.dob.ve.internal.editor.command.OrphanCommand;
import com.dob.ve.internal.editor.command.ReorderCommand;
import com.dob.ve.internal.editor.gef.IRequestConstants2;
import com.dob.ve.internal.editor.util.IContainerRestriction;
import com.dob.ve.internal.editor.util.ID2FigureDefaults;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public class DobTreeContainerEditPolicy extends BaseTreeContainerEditPolicy {

	public DobTreeContainerEditPolicy(IContainerRestriction r) {
		restriction = r;
	}

	@Override
	public Command getCommand(Request request) {
		if (REQ_ORPHAN_CHILDREN.equals(request.getType())) {
			return getOrphanChildrenCommand((GroupRequest) request);
		}
		if (IRequestConstants2.REQ_PASTE.equals(request.getType())) {
			return getPasteCommand((CreateRequest) request);
		}
		return super.getCommand(request);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Command result = UnexecutableCommand.INSTANCE;
		DobXmlElement parent = (DobXmlElement) getHost().getModel();
		DobXmlElement child = (DobXmlElement) request.getNewObject();
		if (restriction != null && restriction.canCreate(child)) {
			CreateCommand createCommand = new CreateCommand();
			createCommand.setParent(parent);
			createCommand.setChild(child);

			//System.err.println("loc: " + request.getLocation());
			//TreeItem item = findTreeItemAt(request.getLocation());
			//System.err.println("treeItem: " + (item == null ? "" : item.getText()));
			int ind = findIndexOfTreeItemAt(request.getLocation());
			//System.err.println("create ind: " + ind);
			if (ind == -1) {//item iself or tree itself - so insert in the end.
				ind = parent.getChildren().size();
			}
			createCommand.setChildIndex(ind);

			Rectangle constr = ID2FigureDefaults.DEFAULT_BOUNDS;
			createCommand.setConstraint(constr);
			result = createCommand;
		}
		return result;
	}

	protected Command getPasteCommand(CreateRequest request) {
		Command result = UnexecutableCommand.INSTANCE;
		DobXmlElement parent = (DobXmlElement) getHost().getModel();
		DobXmlElement child = (DobXmlElement) request.getNewObject();
		if (restriction != null && restriction.canPaste(child)) {
			CreateCommand pasteCommand = new CreateCommand();
			pasteCommand.setParent(parent);
			pasteCommand.setChild(child);

			int ind = parent.getChildren().size();//paste at the end.
			pasteCommand.setChildIndex(ind);

			pasteCommand.setConstraint(FooModelUtils.getBounds(child));
			result = pasteCommand;
		}
		return result;
	}

	//works in pair with getAddCommand().
	//Flow:
	//1.Created compound command in TreeViewerTransferDropListener#getCommand(). 
	//2.Added orphan children command (if not reorder). 
	//3.Added children command.
	protected Command getOrphanChildrenCommand(GroupRequest request) {
		Command result = UnexecutableCommand.INSTANCE;
		//System.err.println("ORPHAN children");
		List editParts = request.getEditParts();
		//System.err.println("children size: " + editParts.size());
		if (editParts != null && editParts.size() == 1) {
			OrphanCommand orphanCommand = new OrphanCommand();
			//this orphan children comman will be first command in final compound, so set appropriate name.
			orphanCommand.setLabel("Reparent Child");
			DobXmlElement parent = (DobXmlElement) getHost().getModel();
			orphanCommand.setParent(parent);
			TreeEditPart firstEP = (TreeEditPart) editParts.get(0);
			DobXmlElement child = (DobXmlElement) firstEP.getModel();
			orphanCommand.setChild(child);
			result = orphanCommand;
		}
		return result;
	}

	//works in pair with getOrphanCommand().
	protected Command getAddCommand(ChangeBoundsRequest request) {
		//System.err.println("ADD children");
		Command result = UnexecutableCommand.INSTANCE;
		DobXmlElement parent = (DobXmlElement) getHost().getModel();

		List editParts = request.getEditParts();
		//System.err.println("children size: " + editParts.size());
		if (editParts != null && editParts.size() == 1) {
			TreeEditPart firstEP = (TreeEditPart) editParts.get(0);
			DobXmlElement child = (DobXmlElement) firstEP.getModel();
			if (restriction != null && restriction.canCreate(child)) {
				AddCommand addCommand = new AddCommand();
				addCommand.setParent(parent);
				addCommand.setChild(child);
				int ind = findIndexOfTreeItemAt(request.getLocation());
				//System.err.println("add ind: " + ind);
				if (ind == -1) {//item iself or tree itself - so insert in the end.
					ind = parent.getChildren().size();
				}
				addCommand.setChildIndex(ind);
				result = addCommand;
			}
		}
		return result;
	}

	@Override
	//reorder child inside parent - only one child can be reordered at time.
	protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
		//System.err.println("MOVE (REORDER) children");
		Command result = UnexecutableCommand.INSTANCE;
		List editParts = request.getEditParts();
		//System.err.println("children size: " + editParts.size());
		if (editParts != null && editParts.size() == 1) {
			ReorderCommand reorderCommand = new ReorderCommand();
			TreeEditPart firstEP = (TreeEditPart) request.getEditParts().get(0);
			DobXmlElement child = (DobXmlElement) firstEP.getModel();
			reorderCommand.setChild(child);
			DobXmlElement parent = (DobXmlElement) getHost().getModel();
			reorderCommand.setParent(parent);
			int curChildInd = parent.childIndex(child);
			//System.err.println("old ind: " + curChildInd);
			reorderCommand.setOldChildIndex(curChildInd);

			int newInd = findIndexOfTreeItemAt(request.getLocation());
			//System.err.println("new ind: " + newInd);
			if (newInd != -1) {
				reorderCommand.setNewChildIndex(newInd);
				result = reorderCommand;
			}
		}
		return result;
	}

}
