package id.popup.actions;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.ui.search.ElementQuerySpecification;

/**
 * Finds unused java classes in selected element (java project or package) in the enclosing project  
 * of the selected element search scope.
 * @author idanilov
 * @version Eclipse 3.2
 */
public class FindUnusedClassesInProjectAction extends FindUnusedClassesInWorkspaceAction {

	ElementQuerySpecification createQuery(IJavaElement element) throws JavaModelException {
		JavaSearchScopeFactory factory = JavaSearchScopeFactory.getInstance();
		boolean isInsideJRE = factory.isInsideJRE(element);
		IJavaSearchScope scope = factory.createJavaProjectSearchScope(element.getJavaProject(),
				isInsideJRE);
		String description = factory.getProjectScopeDescription(element.getJavaProject(),
				isInsideJRE);
		return new ElementQuerySpecification(element, getLimitTo(), scope, description);
	}

}
