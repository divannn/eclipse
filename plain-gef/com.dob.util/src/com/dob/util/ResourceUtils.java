package com.dob.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

/**
 * Utilites for working with Eclipse resources API.
 * Subject to refactor - many methods are not used.
 * @author idanilov
 *
 */
public class ResourceUtils {

	private ResourceUtils() {
	}

	//	private static IEditorInput createEditorInput(final File file) {
	//	IFile workspaceFile = getWorkspaceFile(file);
	//	if (workspaceFile != null) {
	//		return new FileEditorInput(workspaceFile);
	//	}
	//	return null;
	//}

	public static CoreException createCoreException(final String pluginId, final String msg) {
		return createCoreException(pluginId, msg, null);
	}

	public static CoreException createCoreException(final String pluginId, final String msg,
			final Throwable t) {
		return new CoreException(new Status(IStatus.ERROR, pluginId, 0, msg, t));
	}

	public static void addNatureToProject(final IProject proj, final String natureId)
			throws CoreException {
		IProjectDescription description = proj.getDescription();
		String prevNatures[] = description.getNatureIds();
		String newNatures[] = new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length] = natureId;
		description.setNatureIds(newNatures);
		proj.setDescription(description, null);
	}

	public static IFile getWorkspaceFile(final File file) {
		if (file == null) {
			return null;
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = new Path(file.getAbsolutePath());
		IFile wsFile = workspace.getRoot().getFileForLocation(location);
		return wsFile;
	}

	public static IProject findImportedProject(IPath path) {
		if ("win32".equals(Platform.getOS()) && path.lastSegment().equalsIgnoreCase(".project")
				|| path.lastSegment().equalsIgnoreCase(".project"))
			path = path.removeLastSegments(1);
		File location = path.toFile();
		IProject aiproject[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		int i = 0;
		for (int j = aiproject.length; i < j; i++) {
			IProject prj = aiproject[i];
			if (location.equals(prj.getLocation().toFile()))
				return prj;
		}

		return null;
	}

	public static IProject convertToRealProject(IProject project) {
		IProject realProject = getProjectByName(project.getName());
		if (realProject != null)
			return realProject;
		else
			return project;
	}

	public static IProject getProjectByName(String projectName) {
		IProject aiproject[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		int i = 0;
		for (int j = aiproject.length; i < j; i++) {
			IProject prj = aiproject[i];
			if ("win32".equals(Platform.getOS()) && prj.getName().equalsIgnoreCase(projectName)
					|| prj.getName().equals(projectName))
				return prj;
		}

		return null;
	}

	public static String extractDotProjectPath(String path) {
		IPath p = new Path(path);
		if (p.segmentCount() == 1)
			return path;
		String convertedName = p.lastSegment();
		if (convertedName == null)
			return null;
		if ("win32".equals(Platform.getOS()))
			convertedName = convertedName.toLowerCase();
		if (!convertedName.equals(".project")) {
			File tdf = new File(path);
			File projectDir = tdf.getParentFile().getParentFile();
			File projectFile = new File(projectDir, ".project");
			if (projectFile.exists())
				return projectFile.getAbsolutePath();
		}
		return path;
	}

	public static URL toURL(IResource resource) {
		return resource != null ? FileUtils.toURL(resource.getLocation().makeAbsolute().toFile())
				: null;
	}

	public static File getPluginFile(Plugin plugin, String path) {
		try {
			if (plugin != null) {
				URL url = FileLocator.find(plugin.getBundle(), new Path(path), null);
				return new File(FileLocator.resolve(url).getFile());
			}
			return null;
		} catch (Exception e) {
			UtilPlugin.error(e);
			return null;
		}
	}

	public static List<IFile> getAllFiles(IContainer parent) throws CoreException {
		List<IFile> result = new ArrayList<IFile>();
		if (parent != null) {
			collectFiles(parent.members(), result);
		}
		return result;
	}

	private static void collectFiles(IResource members[], List<IFile> store) throws CoreException {
		for (int i = 0; i < members.length; i++) {
			IResource res = members[i];
			if (res instanceof IFolder) {
				collectFiles(((IFolder) res).members(), store);
			} else if (res instanceof IFile) {
				store.add((IFile) res);
			}
		}
	}

	public static IFile getProjectResourceByAbsolutePath(IProject p, IPath absolutePath)
			throws CoreException {
		List<IFile> files = getAllFiles(p);
		for (IFile n : files) {
			if (absolutePath.equals(n.getLocation().makeAbsolute())) {
				return n;
			}
		}
		return null;
	}

	public static String generateFolderName(final String start, final IFolder folder) {
		String result = start;
		List<String> existing = new ArrayList<String>();
		try {
			IResource[] children = folder.members();
			for (IResource n : children) {
				existing.add(n.getName());
			}
			int i = 1;
			do {
				result = start + i;
				i++;
			} while (existing.contains(result));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Creates full folder tree.
	 * @param folder
	 * @throws CoreException
	 */
	public static void createFolder(final IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
	}

}
