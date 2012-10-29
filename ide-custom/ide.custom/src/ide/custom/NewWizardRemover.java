package ide.custom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry;
import org.eclipse.ui.internal.wizards.NewWizardRegistry;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * Removes unwanted wizards from {@link NewWizardRegistry}.
 * Uses private Eclipse UI API to hack default behaviour.
 * Leavs just new file and new folder creation wizards. 
 * @author idanilov
 *
 */
public class NewWizardRemover {

	private static FakeExtension fakeExtension;
	static {
		IExtensionPoint newWizardPoint = Platform.getExtensionRegistry().getExtensionPoint(
				PlatformUI.PLUGIN_ID, IWorkbenchRegistryConstants.PL_NEW);
		fakeExtension = new FakeExtension(newWizardPoint.getUniqueIdentifier());
	}

	private static final Set<String> WIZARD_IDS = new HashSet<String>();
	static {
		WIZARD_IDS.add("org.eclipse.emf.codegen.ecore.ui.EmptyProjectWizard");
		WIZARD_IDS.add("org.eclipse.emf.importer.ui.EMFModelWizard");
		WIZARD_IDS.add("org.eclipse.emf.importer.ui.EMFProjectWizard");
		WIZARD_IDS.add("org.eclipse.emf.codegen.ui.ConvertToJETProjectWizard");
		WIZARD_IDS.add("org.eclipse.emf.ecore.presentation.EcoreModelWizardID");
		WIZARD_IDS.add("org.eclipse.emf.ecore.sdo.presentation.SDOModelWizardID");
		WIZARD_IDS.add("org.eclipse.emf.mapping.ecore2ecore.presentation.Ecore2EcoreModelWizardID");
		WIZARD_IDS.add("org.eclipse.emf.mapping.ecore2xml.presentation.Ecore2XMLModelWizardID");
		WIZARD_IDS.add("org.eclipse.ant.ui.wizards.JavaProjectWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.JavaProjectWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard");
		WIZARD_IDS.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.debug.ui.snippetEditor.NewSnippetFileCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.junit.wizards.NewTestCaseCreationWizard");
		WIZARD_IDS.add("org.eclipse.jdt.junit.wizards.NewTestSuiteCreationWizard");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Frame");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Dialog");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Panel");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Applet");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Editor");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.View");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.JFrame");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.JDialog");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.JPanel");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.JApplet");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Shell");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.NewVisualClassCreationWizard.Composite");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.AwtComponents");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.SimpleTextEditor");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.SliderGame");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.ToDoList");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.BasicSwingComponents");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.MoreSwingComponents");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.SimpleSWTBrowser");
		WIZARD_IDS
				.add("org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard.SimpleSWTTextEditor");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewProjectWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewLibraryPluginProjectWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewFragmentWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewSchemaFileWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewFeatureProjectWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewFeaturePatchWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewSiteProjectWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewProductConfigurationWizard");
		WIZARD_IDS.add("org.eclipse.pde.ui.NewProfileWizard");
		WIZARD_IDS.add("org.eclipse.team.cvs.ui.newProjectCheckout");
		WIZARD_IDS.add("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");
		WIZARD_IDS.add("org.eclipse.ui.wizards.new.project");
		//WIZARD_IDS.add("org.eclipse.ui.wizards.new.folder");
		//WIZARD_IDS.add("org.eclipse.ui.wizards.new.file");
	}

	static void removeNewWizards() {
		NewWizardRegistry nwr = (NewWizardRegistry) PlatformUI.getWorkbench()
				.getNewWizardRegistry();
		remove(nwr, nwr.getRootCategory());
	}

	private static void remove(NewWizardRegistry nwr, IWizardCategory wizardCategory) {
		//remove wizards from category.
		List toRemove = new ArrayList();
		for (IWizardDescriptor n : wizardCategory.getWizards()) {
			//System.err.println(">>> " + n.getId());
			if (WIZARD_IDS.contains(n.getId())) {
				toRemove.add(n);
			}
		}
		if (toRemove.size() > 0) {
			nwr.removeExtension(fakeExtension, toRemove.toArray());
		}
		//process children category.
		for (IWizardCategory nextCat : wizardCategory.getCategories()) {
			remove(nwr, nextCat);
		}
	}

	/**
	 * Needed for {@link AbstractExtensionWizardRegistry#removeExtension(IExtension, Object[])} check passed. 
	 * @author idanilov
	 *
	 */
	private static class FakeExtension
			implements IExtension {

		private String extensionPointId;

		public FakeExtension(final String id) {
			extensionPointId = id;
		}

		public IConfigurationElement[] getConfigurationElements()
				throws InvalidRegistryObjectException {
			return null;
		}

		public IContributor getContributor() throws InvalidRegistryObjectException {
			return null;
		}

		public IPluginDescriptor getDeclaringPluginDescriptor()
				throws InvalidRegistryObjectException {
			return null;
		}

		public String getExtensionPointUniqueIdentifier() throws InvalidRegistryObjectException {
			return extensionPointId;
		}

		public String getLabel() throws InvalidRegistryObjectException {
			return null;
		}

		public String getNamespace() throws InvalidRegistryObjectException {
			return null;
		}

		public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
			return null;
		}

		public String getSimpleIdentifier() throws InvalidRegistryObjectException {
			return null;
		}

		public String getUniqueIdentifier() throws InvalidRegistryObjectException {
			return null;
		}

		public boolean isValid() {
			return false;
		}

	}
}
