package com.dob.ide.internal.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.IntroPart;

import com.dob.ide.internal.DobIdePlugin;

/**
 * @author idanilov
 *
 */
public class DobIntroPart extends IntroPart {

	private Image logoImage;

	public void createPartControl(Composite parent) {
		Color whiteColor = new Color(parent.getDisplay(), new RGB(255, 255, 255));
		parent.setBackground(whiteColor);
		parent.setLayout(new GridLayout(2, false));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		Control text = createWelcomeHeader(parent);
		Control logo = createLogo(parent);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		logo.setLayoutData(gd);
		text.setBackground(whiteColor);
		Label line = createLine(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		line.setLayoutData(gd);
		Text welcomeText = createWelcomeText(parent);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		welcomeText.setLayoutData(gd);
	}

	private Control createLogo(final Composite parent) {
		Label imageLogo = new Label(parent, SWT.NONE);
		ImageDescriptor id = DobIdePlugin.getImageDescriptorForPath("icons/about.gif");
		logoImage = id.createImage();
		imageLogo.setImage(logoImage);
		return imageLogo;
	}

	private Control createWelcomeHeader(final Composite parent) {
		Label result = new Label(parent, SWT.NONE);
		result.setText("Welcome to DOB Studio");
		FontData fd = result.getFont().getFontData()[0];
		int fontHeight = fd.getHeight() * 2;
		fd.setHeight(fontHeight);
		result.setFont(new Font(null, fd));
		return result;
	}

	private Label createLine(final Composite parent) {
		Label result = new Label(parent, SWT.NONE);
		result.setBackground(new Color(null, new RGB(255, 128, 0)));
		FontData fd = result.getFont().getFontData()[0];
		int fontHeight = fd.getHeight() / 8;
		fd.setHeight(fontHeight);
		result.setFont(new Font(null, fd));
		return result;
	}

	private Text createWelcomeText(final Composite parent) {
		Text result = new Text(parent, SWT.NONE);
		result.setText("....................");
		return result;
	}

	public void dispose() {
		if (logoImage != null) {
			logoImage.dispose();
			logoImage = null;
		}
		super.dispose();
	}

	public void setFocus() {
	}

	public void standbyStateChanged(boolean standby) {
	}

}
