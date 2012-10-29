package ide.custom;

import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

/**
 * @author idanilov
 *
 */
public class ForcePerspectiveOnStart
		implements IStartup {

	public void earlyStartup() {
		openPerspective(CustomPerspective.ID);
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			public void run() {
				MenuCustomizer.initializeMenus();
				PerspectiveHackManager.initializePerspective();
			}

		});
	}

	/**
	 * Called after workbench initilizes.
	 * 
	 * @param perspectiveId
	 */
	private static void openPerspective(final String perspectiveId) {
		//needs to be called in UI theread.
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			public void run() {
				//XXX kludge: re-open Intro part after opening perspective.
				//this is needed because when we open perspective in a such unfair way for the first time - is shifts Intro Part.
				IIntroManager im = PlatformUI.getWorkbench().getIntroManager();
				//System.err.println(">has intro: " + im.hasIntro());//TODO: trace
				IIntroPart introPart = null;
				boolean isStandby = false;
				if (im.hasIntro()) {
					introPart = im.getIntro();
					if (introPart != null) {
						isStandby = im.isIntroStandby(introPart);
						//System.err.println(">Intro standby: " + isStandby);//TODO: trace
						//im.closeIntro(introPart);
					}
				}

				//open perspective.
				if (shouldSwitchperspective()) {
					try {
						PlatformUI.getWorkbench().showPerspective(perspectiveId,
								getActiveWorkbenchWindow());
					} catch (WorkbenchException wbe) {
						//DOBIdeCustomizerPlugin.error("Perspective could not be opened", wbe);
					}
				}

				//restore Intro part.
				if (introPart != null) {
					//System.err.println(">restore intro");//TODO: trace
					im.showIntro(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), isStandby);
				}

				//remove after perspective is opened.
				ViewRemover.removeViews();
				NewWizardRemover.removeNewWizards();
			}

		});

	}

	//	TODO: remove???.
	private static boolean shouldSwitchperspective() {
		//		String str = S60WorkspacePlugin.getDefault().getPreferenceStore().getString(
		//				"com.nokia.tools.s60.ide.activate.perspective");
		//		if (str.equals("always"))
		//			return true;
		//		if (str.equals("never")) {
		//			return false;
		//		} else {
		//			MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(
		//					getActiveWorkbenchWindow().getShell(),
		//					S60IDEMessages.ConfirmPerspectiveSwitchTitle,
		//					S60IDEMessages.InfoWhyToLounchPerspective, null, false, S60WorkspacePlugin
		//							.getDefault().getPreferenceStore(),
		//					"com.nokia.tools.s60.ide.activate.perspective");
		//			boolean bret = dialog.getReturnCode() == 2;
		//			return bret;
		//		}
		return true;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			int cnt = workbench.getWorkbenchWindowCount();
			if (cnt > 0)
				return workbench.getWorkbenchWindows()[0];
		}
		return activeWorkbenchWindow;
	}
}
