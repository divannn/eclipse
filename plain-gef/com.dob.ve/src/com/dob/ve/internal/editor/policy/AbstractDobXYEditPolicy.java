package com.dob.ve.internal.editor.policy;

import java.text.MessageFormat;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.dob.ve.abstractmodel.DobXmlElement;
import com.dob.ve.internal.editor.command.CreateCommand;
import com.dob.ve.internal.editor.command.MoveResizeCommand;
import com.dob.ve.internal.editor.gef.CursorHelper;
import com.dob.ve.internal.editor.gef.IRequestConstants2;
import com.dob.ve.internal.editor.util.IContainerRestriction;
import com.dob.ve.internal.editor.util.ID2FigureDefaults;
import com.dob.ve.internal.model.FooModelUtils;

/**
 * @author idanilov
 *
 */
public abstract class AbstractDobXYEditPolicy extends XYLayoutEditPolicy {

	private Label cursorLabel;
	protected IFigure cursorFigure;
	protected CursorHelper cursorHelper;
	//flag for feedback. If creation forbidden - feedback is not show.
	protected boolean canCreate;
	protected IContainerRestriction restriction;

	public AbstractDobXYEditPolicy(IContainerRestriction r) {
		restriction = r;
	}

	@Override
	public Command getCommand(Request request) {
		if (IRequestConstants2.REQ_PASTE.equals(request.getType())) {
			return getPasteCommand((CreateRequest) request);
		}
		return super.getCommand(request);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Command result = UnexecutableCommand.INSTANCE;
		canCreate = false;
		DobXmlElement parent = (DobXmlElement) getHost().getModel();
		DobXmlElement child = (DobXmlElement) request.getNewObject();

		if (restriction != null && restriction.canCreate(child)) {//allow only visuals.
			CreateCommand createCommand = new CreateCommand();
			createCommand.setParent(parent);
			createCommand.setChild(child);

			int ind = parent.getChildren().size();//add at the end.
			createCommand.setChildIndex(ind);

			Point loc = request.getLocation();// given in absolute coordinates.
			// System.err.println("req loc: " + loc);
			// System.err.println("req size: " + request.getSize());
			Rectangle constr = (Rectangle) getConstraintFor(request);
			// System.err.println(">> draq2D const: " + constr);

			// means creation is requested not through palette (for instance, from
			// tree EP's "New" context menu.
			// there impossible to calculate location).
			if (loc.x == Integer.MIN_VALUE) {
				constr.x = 0;
			}
			if (loc.y == Integer.MIN_VALUE) {
				constr.y = 0;
			}
			constr.width = constr.width <= 0 ? ID2FigureDefaults.DEFAULT_BOUNDS.width
					: constr.width;
			constr.height = constr.height <= 0 ? ID2FigureDefaults.DEFAULT_BOUNDS.height
					: constr.height;

			createCommand.setConstraint(constr);
			result = createCommand;
			canCreate = true;
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

			// set constraint as is.
			pasteCommand.setConstraint(FooModelUtils.getBounds(child));
			result = pasteCommand;
		}
		return result;
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart childEP, Object constraint) {
		Command result = UnexecutableCommand.INSTANCE;
		DobXmlElement child = (DobXmlElement) childEP.getModel();
		if (FooModelUtils.isResizable(child)) {
			//System.err.println(">>> rect " + constraint);
			MoveResizeCommand c = new MoveResizeCommand();
			c.setModel(child);
			c.setConstraint((Rectangle) constraint);
			result = c;
		}
		return result;
	}

	@Override
	//install drag role policy directly in child edit part.
	protected void decorateChild(EditPart child) {
	}

	@Override
	protected void eraseLayoutTargetFeedback(Request request) {
		super.eraseLayoutTargetFeedback(request);
		if (cursorLabel != null) {
			cursorLabel = null;
		}
		if (cursorFigure != null) {
			cursorFigure = null;
		}
		if (cursorHelper != null) {
			cursorHelper.dispose();
			cursorHelper = null;
		}
	}

	@Override
	protected void showLayoutTargetFeedback(Request request) {
		Point position = getLocationFromRequest(request).getCopy(); // This is absolute to the current displayed window, not to the figure.

		org.eclipse.swt.graphics.Point absolutePosition = getHost().getViewer().getControl()
				.toDisplay(position.x, position.y);
		absolutePosition.x += 13; // This point is used to display the little id box listing the coordinates to the user. It is in display coor so
		// that it can be placed correctly in a separate window.
		absolutePosition.y += 6;
		// Convert it to be relative to the host figure. Don't ask why this works this way. Just copied
		// from ConstrainedLayoutEditpolicy. It makes no sense why the final position.translate is needed.
		// I would of thought that the translateFromParent would of put it into relative to figure.
		IFigure figure = getLayoutContainer();
		figure.translateToRelative(position);
		figure.translateFromParent(position);
		position.translate(getLayoutOrigin().getNegated());
		position = (Point) convertModelToDraw2D(translateToModelConstraint(position));
		/**
		 * The cursor feedback consists of a PopupHelper that contains a standard Figure with a FlowLayout. The Figure contains two Labels, one that
		 * contains the X value representing the gridx value, and the other Label containts the Y value representing the gridY value.
		 */

		// First create the X and Y Labels
		if (cursorLabel == null) {
			cursorLabel = new Label();
			cursorLabel.setOpaque(true);
			cursorLabel.setBorder(new MarginBorder(new Insets(0, 2, 0, 0)));
			cursorLabel.setBackgroundColor(Display.getDefault().getSystemColor(
					SWT.COLOR_INFO_BACKGROUND));
			//cursorLabel.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.NORMAL)); //$NON-NLS-1$
		}
		cursorLabel.setText(MessageFormat.format("{0}, {1}", new Object[] {
				String.valueOf(position.x), String.valueOf(position.y) }));

		if (cursorFigure == null) {
			cursorFigure = new Figure();
			FlowLayout fl = new FlowLayout();
			fl.setMinorSpacing(1);
			cursorFigure.setLayoutManager(fl);
			cursorFigure.setBorder(new LineBorder());
			cursorFigure.setOpaque(true);
			cursorFigure.setBackgroundColor(ColorConstants.black);
		}
		cursorFigure.add(cursorLabel);
		// fCursorFigure.add(yCursorLabel);
		// Now create the PopupHelper to contain the overall figure so that the cursor
		// feedback will paint over the top of other views if necessary.
		if (cursorHelper == null) {
			cursorHelper = new CursorHelper(getHost().getViewer().getControl());
		}
		cursorHelper.showCursorFigure(cursorFigure, absolutePosition.x, absolutePosition.y);
	}

	protected Object convertModelToDraw2D(Object modelconstraint) {
		/*if (modelconstraint instanceof Rectangle) {
		 Rectangle rect = (Rectangle) modelconstraint;
		 return new org.eclipse.draw2d.geometry.Rectangle(rect.x, rect.y, rect.width,
		 rect.height); // Convert to CDM rect.
		 } else if (modelconstraint instanceof Point) {
		 Point point = (Point) modelconstraint;
		 return new org.eclipse.draw2d.geometry.Point(point.x, point.y); // Convert to CDM point.
		 } else*/
		return modelconstraint;
	}

	protected Point getLocationFromRequest(Request request) {
		if (request instanceof CreateRequest)
			return ((CreateRequest) request).getLocation();
		if (request instanceof ChangeBoundsRequest)
			return ((ChangeBoundsRequest) request).getLocation();
		return null;
	}

}
