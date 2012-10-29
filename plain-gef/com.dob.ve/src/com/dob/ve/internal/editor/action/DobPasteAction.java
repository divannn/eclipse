package com.dob.ve.internal.editor.action;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.dob.ve.abstractmodel.Copier;
import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.factory.PasteModelFactory;
import com.dob.ve.internal.editor.gef.IRequestConstants2;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public class DobPasteAction extends SelectionAction {

	private Command pasteCommand;

	//private EditDomain editDomain;

	public DobPasteAction(IWorkbenchPart part) {
		super(part);
		setId(ActionFactory.PASTE.getId());
		setText("Paste");
	}

	@Override
	protected boolean calculateEnabled() {
		List selection = getSelectedObjects();
		if (selection.size() == 1) {
			Object first = selection.get(0);
			if (first instanceof EditPart) {
				//				EditPart selectedEditPart = (EditPart) first;
				//				//editDomain = DobEditDomain.getEditDomain(selectedEditPart);
				//				Request pasteRequest = new Request(PasteCommand.REQ_PASTE);
				//				pasteCommand = selectedEditPart.getCommand(pasteRequest);

				pasteCommand = createPasteCommand();
				return pasteCommand != null && pasteCommand.canExecute();
			}
		}
		return false;
	}

	@Override
	public void run() {
		execute(pasteCommand);
	}

	protected Command createPasteCommand() {
		Command result = null;
		List selection = getSelectedObjects();
		Object obj = selection.get(0);
		if (obj instanceof EditPart) {
			EditPart ep = (EditPart) obj;
			Object template = getClipboardContents();
			//System.err.println("clipboard contents: " + template);
			if (template instanceof DobXmlElement) {
				DobXmlElement copy = new Copier((DobXmlElement) template).copy();
				CreationFactory factory = getFactory(copy);
				if (factory != null) {
					CreateRequest request = new CreateRequest(IRequestConstants2.REQ_PASTE);
					request.setFactory(factory);
					Rectangle copyBounds = FooModelUtils.getBounds(copy);
					request.setLocation(/*getPasteLocation(ep)*/copyBounds.getLocation());
					request.setSize(/*getPasteSize(ep)*/copyBounds.getSize());
					result = ep.getCommand(request);
				}
			}
		}
		return result;
	}

	protected Point getPasteLocation(EditPart ep) {
		return new Point(10, 10);
	}

	protected Dimension getPasteSize(EditPart ep) {
		return new Dimension(10, 10);
	}

	protected Object getClipboardContents() {
		return Clipboard.getDefault().getContents();
	}

	private CreationFactory getFactory(Object template) {
		CreationFactory result = null;
		if (template instanceof DobXmlElement) {
			result = new PasteModelFactory((DobXmlElement) template);
		}
		return result;
	}

}
