package com.dob.util;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

/**
 * @author idanilov
 *
 */
public class ConsoleUtil {

	public static MessageConsole getConsole(String name) {
		MessageConsole result = null;
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conManager = plugin.getConsoleManager();
		IConsole[] existing = conManager.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			if (name.equals(existing[i].getName())) {
				result = (MessageConsole) existing[i];
				break;
			}
		}
		if (result == null) {
			result = new MessageConsole(name, null);
			conManager.addConsoles(new IConsole[] { result });
		}

		return result;
	}

	//	showConsole() {
	//	 IConsole myConsole = ...;//your console instance
	//	   IWorkbenchPage page = ...;//obtain the active page
	//	   String id = IConsoleConstants.ID_CONSOLE_VIEW;
	//	   IConsoleView view = (IConsoleView) page.showView(id);
	//	   view.display(myConsole);
	//	}
}
