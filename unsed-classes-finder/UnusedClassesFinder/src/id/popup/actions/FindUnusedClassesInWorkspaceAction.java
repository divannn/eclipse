package id.popup.actions;

import id.Activator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.ui.search.JavaSearchQuery;
import org.eclipse.jdt.internal.ui.search.JavaSearchResult;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.internal.ui.search.SearchMessages;
import org.eclipse.jdt.ui.search.ElementQuerySpecification;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.internal.ui.SearchPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
 * Finds unused java classes in selected element (java project or package) in the workspace search scope.
 * @author idanilov
 * @version Eclipse 3.2
 */
public class FindUnusedClassesInWorkspaceAction
		implements IObjectActionDelegate {

	private IWorkbenchPart part;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
	}

	public void run(IAction action) {
		Shell shell = new Shell();
		//		if (!ActionUtil.isProcessable(getShell(), element))
		//			return;
		IStructuredSelection selection = (IStructuredSelection) part.getSite()
				.getSelectionProvider().getSelection();
		if (selection != null) {
			List<IType> unusedClasses = new ArrayList<IType>();
			Object o = selection.getFirstElement();
			try {
				//d System.err.println("o " + o.getClass());
				if (o instanceof IJavaProject) {
					IJavaProject javaProject = (IJavaProject) o;
					Activator.getDefault().getLog().log(
							new Status(IStatus.INFO, Activator.PLUGIN_ID, 0, "Find unused java classes from : "
									+ javaProject.getElementName(), null));
					for (IPackageFragment nextPackageFragment : javaProject.getPackageFragments()) {//all packages.
						if (nextPackageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
							//d System.err.println(">pack:" + nextPackageFragment.getElementName());
							nextPackageFragment.getCompilationUnits();
							for (ICompilationUnit nextCompUnit : nextPackageFragment
									.getCompilationUnits()) {
								//d System.err.println(">>unit: " + nextCompUnit.getElementName());
								for (IType nextType : nextCompUnit.getAllTypes()) {
									//System.err.println(">>>type: "
									//	+ nextType.getFullyQualifiedName());
									if (performNewSearch(nextType)) {
										unusedClasses.add(nextType);
									}
									//d System.err.println("-----------------------------------------------");
								}
							}
						}
					}
				} else if (o instanceof IPackageFragment) {
					Activator.getDefault().getLog().log(
							new Status(IStatus.INFO, Activator.PLUGIN_ID, 0, "Find unused java classes from : "
									+ ((IPackageFragment) o).getElementName(), null));
					IPackageFragment pack = (IPackageFragment) o;
					if (pack.getKind() == IPackageFragmentRoot.K_SOURCE) {
						for (ICompilationUnit nextCompUnit : pack.getCompilationUnits()) {
							//d System.err.println(">>unit: " + nextCompUnit.getElementName());
							for (IType nextType : nextCompUnit.getAllTypes()) {
								//d System.err.println(">>>type: " + nextType.getFullyQualifiedName());
								if (performNewSearch(nextType)) {
									unusedClasses.add(nextType);
								}
								//d System.err.println("-----------------------------------------------");
							}
						}
					}
				} else {
					throw new IllegalArgumentException("Invalid selection. It must be java project or package.");
				}
				if (unusedClasses.size() > 0) {
					MessageDialog.openWarning(shell, "Unused Classes", "Found "
							+ unusedClasses.size() + " unused classes in the scope: "
							+ ((IJavaElement) o).getElementName());
					writeResults((IJavaElement) o, unusedClasses);
				} else {
					MessageDialog.openInformation(shell, "Unused Classes",
							"No unused classes java in : "
									+ ((IJavaElement) o).getElementName());
				}
			} catch (JavaModelException ex) {
				ex.printStackTrace();
				MessageDialog.openError(shell,
						SearchMessages.Search_Error_search_notsuccessful_title,
						SearchMessages.Search_Error_search_notsuccessful_message);
				//ExceptionHandler.handle(ex, getShell(), SearchMessages.Search_Error_search_notsuccessful_title, SearchMessages.Search_Error_search_notsuccessful_message); 
			} catch (IOException ioe) {
				ioe.printStackTrace();
				MessageDialog.openError(shell, "Error", "Failed to save results to file");
			}
		}
	}

	private boolean performNewSearch(IJavaElement element) throws JavaModelException {
		JavaSearchQuery query = new JavaSearchQuery(createQuery(element));
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		IStatus r = doRunSearchInForeground(query, progressService);
		//System.err.println("STATUS: " + r);

		boolean result = false;

		JavaSearchResult res = (JavaSearchResult) query.getSearchResult();
		if (res.getMatchCount() > 0) {
			//d System.err.println(">>> results for: " + element.getElementName() + ": "
			//+ res.getMatchCount());
			IFile elementFile = res.getFile(element);
			//d System.err.println(">>> file: " + elementFile);
			result = hasSelfReferencesOnly(res, element/*elementFile*/);
		} else {
			result = true;
		}

		//		if (query.canRunInBackground()) {
		//			SearchUtil.runQueryInBackground(query);
		//		} else {
		//			//IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		//			IStatus status = SearchUtil.runQueryInForeground(progressService, query);
		//
		//			//			if (status.matches(IStatus.ERROR | IStatus.INFO | IStatus.WARNING)) {
		//			//				ErrorDialog.openError(getShell(), SearchMessages.Search_Error_search_title,
		//			//						SearchMessages.Search_Error_search_message, status);
		//			//			}
		//		}
		return result;
	}

	//returns true if there are self references.
	private boolean hasSelfReferencesOnly(final JavaSearchResult res, final IJavaElement elementFile) {
		boolean result = false;
		for (Object n : res.getElements()) {
			if (n instanceof IJavaElement) {
				IJavaElement nextJ = (IJavaElement) n;
				IJavaElement nextEnclosedType = nextJ.getAncestor(IJavaElement.TYPE);
				//								System.err.println("    >>> E: " + nextJ.getElementName());
				//								if (nextEnclosedUnit != null) {
				//									System.err.println("    >>> U: " + nextEnclosedUnit.getResource());
				//								}
				result = nextEnclosedType != null && nextEnclosedType.equals(elementFile);

			} else {
				System.err.println("unknown elem: " + n);
				result = false;
			}
			if (!result) {
				break;
			}
		}
		return result;
	}

	/*private boolean hasSelfReferencesOnly(final JavaSearchResult res, final IFile elementFile) {
	 boolean result = false;
	 for (Object n : res.getElements()) {
	 if (n instanceof IJavaElement) {
	 IJavaElement nextJ = (IJavaElement) n;
	 IJavaElement nextEnclosedUnit = nextJ.getAncestor(IJavaElement.COMPILATION_UNIT);
	 //								System.err.println("    >>> E: " + nextJ.getElementName());
	 //								if (nextEnclosedUnit != null) {
	 //									System.err.println("    >>> U: " + nextEnclosedUnit.getResource());
	 //								}
	 result = nextEnclosedUnit != null
	 && nextEnclosedUnit.getResource().equals(elementFile);

	 } else {
	 System.err.println("unknown elem: " + n);
	 result = false;
	 }
	 if (!result) {
	 break;
	 }
	 }
	 return result;
	 }*/

	private void writeResults(final IJavaElement selection, final List<IType> r) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("C:\\unsed.txt"));
			writer.write("Selection: " + selection.getElementName());
			writer.newLine();
			writer.write("-----------------------------------------------------");
			writer.newLine();
			for (IType next : r) {
				writer.write(next.getFullyQualifiedName());
				writer.newLine();
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private IStatus doRunSearchInForeground(final JavaSearchQuery query, IRunnableContext context) {
		try {
			context.run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					//searchJobStarted(rec);
					try {
						IStatus status = query.run(monitor);
						if (status.matches(IStatus.CANCEL)) {
							throw new InterruptedException();
						}
						if (!status.isOK()) {
							throw new InvocationTargetException(new CoreException(status));
						}
					} catch (OperationCanceledException e) {
						throw new InterruptedException();
					} finally {
						//searchJobFinished(rec);
					}
				}
			});
		} catch (InvocationTargetException e) {
			Throwable innerException = e.getTargetException();
			if (innerException instanceof CoreException) {
				return ((CoreException) innerException).getStatus();
			}
			return new Status(
					IStatus.ERROR,
					SearchPlugin.getID(),
					0,
					org.eclipse.search2.internal.ui.SearchMessages.InternalSearchUI_error_unexpected,
					innerException);
		} catch (InterruptedException e) {
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	ElementQuerySpecification createQuery(IJavaElement element) throws JavaModelException {
		JavaSearchScopeFactory factory = JavaSearchScopeFactory.getInstance();
		boolean isInsideJRE = factory.isInsideJRE(element);

		IJavaSearchScope scope = factory.createWorkspaceScope(isInsideJRE);
		String description = factory.getWorkspaceScopeDescription(isInsideJRE);
		return new ElementQuerySpecification(element, getLimitTo(), scope, description);
	}

	/*Class[] getValidTypes() {
	 return new Class[] { ICompilationUnit.class, IType.class, IMethod.class, IField.class,
	 IPackageDeclaration.class, IImportDeclaration.class, IPackageFragment.class,
	 ILocalVariable.class, ITypeParameter.class };
	 }*/

	int getLimitTo() {
		return IJavaSearchConstants.REFERENCES;
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
