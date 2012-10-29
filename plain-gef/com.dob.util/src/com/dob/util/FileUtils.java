package com.dob.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.internal.baseadaptor.AdaptorUtil;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.service.urlconversion.URLConverter;
import org.osgi.framework.Bundle;

/**
 * Numerous file utilites.
 * Subject to refactor - many methods are not used.
 * @author idanilov
 *
 */
public class FileUtils {

	public static final String DOT = ".";

	private FileUtils() {
	}

	public static String getNameWitoutExtension(final String s) {
		if (s == null) {
			throw new IllegalArgumentException("File name cannot be null");
		}
		int i = s.lastIndexOf(DOT);
		if (i == -1) {
			return s;
		} else {
			return s.substring(0, i);
		}
	}

	public static String getNameWitoutExtension(final File f) {
		if (f == null) {
			throw new IllegalArgumentException("File cannot be null");
		}
		return getNameWitoutExtension(f.getName());
	}

	public static boolean isAbsolutePath(String path) {
		if (!StringUtils.isEmpty(path))
			return (new File(path)).isAbsolute();
		else
			return false;
	}

	public static void copyFile(String sourceFileName, String destFileName) throws IOException {
		copyFile(new File(sourceFileName), new File(destFileName));
	}

	public static void copyFile(File src, File dst) throws IOException {
		copyFile(src, dst, null);
	}

	public static void copyFile(File src, File dst, IProgressMonitor monitor) throws IOException {
		FileInputStream in;
		FileOutputStream out;
		in = null;
		out = null;
		dst.getParentFile().mkdirs();
		in = new FileInputStream(src);
		out = new FileOutputStream(dst);
		copy(in, out, monitor);
		//[ID]        
		//        break MISSING_BLOCK_LABEL_56;
		//        Exception exception;
		//        exception;
		//        close(in);
		//        close(out);
		//        throw exception;
		close(in);
		close(out);
		return;
	}

	public static void copyFile(InputStream in, File file) throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		copy(in, out);
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		copy(in, out, null);
	}

	public static void copy(InputStream in, OutputStream out, IProgressMonitor monitor)
			throws IOException {
		byte buf[];
		int slice;
		buf = new byte[65535];
		int length = in.available();
		slice = Math.max(1, length / 65535);
		int val;
		while ((val = in.read(buf)) > 0) {
			out.write(buf, 0, val);
			out.flush();
			if (monitor != null)
				monitor.worked(slice);
		}
		//[ID]        
		//        break MISSING_BLOCK_LABEL_76;
		//        Exception exception;
		//        exception;
		//        close(in);
		//        close(out);
		//        throw exception;
		close(in);
		close(out);
		return;
	}

	public static boolean deleteDirectory(File dir) {
		return deleteDirectory(dir, null);
	}

	public static boolean deleteDirectory(File dir, IProgressMonitor monitor) {
		boolean result = false;
		if (dir.isDirectory()) {
			File files[] = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i], monitor);
				} else {
					files[i].delete();
					if (monitor != null) {
						monitor.worked(1);
					}
				}
			}
			result = dir.delete();
		}
		return result;
	}

	public static void cleanDirectory(File path) {
		if (path.isDirectory()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
	}

	public static String getExtension(final String filename, final boolean leaveDot) {
		if (filename != null) {
			int i = filename.lastIndexOf(DOT);
			if (i != -1 && i < filename.length()) {
				return filename.substring(leaveDot ? i : ++i);
			}
		}
		return "";
	}

	public static String getExtension(final File f, final boolean leaveDot) {
		return getExtension(f.getName(), leaveDot);
	}

	public static String generateUniqueFileName(String prefix, String extension) {
		String dir = getTemporaryDirectory();
		return (new StringBuilder(String.valueOf(dir))).append(File.separator).append(
				generateUniqueFileName(prefix, dir, extension)).toString();
	}

	public static String getTemporaryDirectory() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String generateUniqueFileName(String prefix, String directory, String ext) {
		return generateUniqueFileName(prefix, directory, ext, true);
	}

	public static String generateUniqueFileName(String prefix, String directory, String ext,
			boolean returnWithExtension) {
		prefix = prefix.replace('%', '_');
		int count = 0;
		String unique = prefix;
		for (File file = new File((new StringBuilder(String.valueOf(directory))).append(
				File.separator).append(unique).append(DOT).append(ext).toString()); file.exists(); file = new File(
				(new StringBuilder(String.valueOf(directory))).append(File.separator)
						.append(unique).append(DOT).append(ext).toString()))
			unique = (new StringBuilder(String.valueOf(prefix))).append("_").append(++count)
					.toString();

		return returnWithExtension ? (new StringBuilder(String.valueOf(unique))).append(DOT)
				.append(ext).toString() : unique;
	}

	public static String generateUniqueDirectory(String desiredName) {
		String uniqueDirName = generateUniqueDirectoryName(getTemporaryDirectory(), desiredName);
		File f = new File(uniqueDirName);
		if (!f.exists()) {
			boolean b = f.mkdir();
			if (b)
				return uniqueDirName;
			else
				return null;
		} else {
			return uniqueDirName;
		}
	}

	public static String generateUniqueDirectoryName(String baseDir, String preferredName) {
		StringBuilder fName;
		boolean status;
		int range;
		Random random;
		StringBuffer tmpfName;
		fName = new StringBuilder();
		fName.append(baseDir);
		fName.append(File.separator);
		fName.append(preferredName);
		File f = new File(fName.toString());
		status = f.exists();
		range = 0x55555554;
		int randId = 0;
		random = new Random();
		tmpfName = new StringBuffer(fName.toString());
		String tempFilePath;
		//[ID]
		//File f;
		for (; status; status = f.exists()) {
			/*[ID]int */randId = random.nextInt(range);
			tmpfName.delete(0, tmpfName.length());
			tmpfName.append(fName.toString());
			tmpfName.append(randId);
			f = new File(tmpfName.toString());
		}

		//[ID]		
		//      UtilsPlugin.error(e);
		//      return null;        
		//        Exception e;
		//        e;
		//        UtilsPlugin.error(e);
		//        return null;
		try {
			tempFilePath = (new File(tmpfName.toString())).getCanonicalPath();
			return tempFilePath;
		} catch (IOException e) {
			UtilPlugin.error(e);
			return null;
		}
	}

	public static String makeAbsolutePath(String baseDir, String file) {
		String absFilePath = null;
		File tfile = new File(file);
		if (tfile.exists())
			return file;
		try {
			boolean fileStatus = tfile.isAbsolute();
			if (fileStatus) {
				absFilePath = tfile.getCanonicalPath();
			} else {
				String filePath = (new StringBuilder(String.valueOf(baseDir))).append(
						File.separator).append(file).toString();
				absFilePath = (new File(filePath)).getCanonicalPath();
			}
		} catch (Exception e) {
			UtilPlugin.error(e);
		}
		return absFilePath;
	}

	public static boolean isFileValid(String fileName) {
		return fileName != null && isFileValid(new Path(fileName));
	}

	public static boolean isFileValid(File file) {
		return file != null && isFileValid(file.getAbsolutePath());
	}

	public static boolean isFileValid(Path path) {
		if (path == null)
			return false;
		String segments[] = path.segments();
		String as[] = segments;
		int i = 0;
		for (int j = as.length; i < j; i++) {
			String segment = as[i];
			IStatus status = ResourcesPlugin.getWorkspace().validateName(segment, 1);
			if (!status.isOK())
				return false;
		}

		return true;
	}

	public static boolean isFileValidAndAccessible(String fileName) {
		return fileName != null && fileName.length() < 512
				&& isFileValidAndAccessible(new File(fileName));
	}

	public static boolean isFileValidAndAccessible(Path path) {
		return path != null && isFileValidAndAccessible(path.toFile());
	}

	public static boolean isFileValidAndAccessible(File file) {
		if (isFileValid(file))
			return file.canRead();
		else
			return false;
	}

	public static void copyDir(File inDir, File outDir) throws IOException {
		copyDir(inDir, outDir, (String[]) null);
	}

	public static void copyDir(File inDir, File outDir, String exclude[]) throws IOException {
		copyDir(inDir, outDir, true, exclude);
	}

	public static void copyDir(File inDir, File outDir, boolean override, String exclude[])
			throws IOException {
		String files[] = inDir.list();
		if (files != null && files.length > 0) {
			outDir.mkdirs();
			for (int i = 0; i < files.length; i++) {
				File inFile = new File(inDir, files[i]);
				File outFile = new File(outDir, files[i]);
				boolean cont = false;
				if (exclude != null) {
					String as[] = exclude;
					int j = 0;
					for (int k = as.length; j < k; j++) {
						String excludeItem = as[j];
						if (!inFile.getName().equals(excludeItem))
							continue;
						cont = true;
						break;
					}

				}
				if (!cont)
					if (inFile.isDirectory())
						copyDir(inFile, outFile, exclude);
					else if (!outFile.exists() || override) {
						InputStream in = new FileInputStream(inFile);
						AdaptorUtil.readFile(in, outFile);
					}
			}

		}
	}

	public static void copyDir(File inDir, File outDir, FileFilter ff) throws IOException {
		copyDir(inDir, outDir, true, ff);
	}

	public static void copyDir(File inDir, File outDir, boolean override, FileFilter ff)
			throws IOException {
		File files[] = inDir.listFiles(ff);
		if (files != null && files.length > 0) {
			outDir.mkdirs();
			for (int i = 0; i < files.length; i++) {
				File inFile = files[i];
				File outFile = new File(outDir, files[i].getName());

				if (inFile.isDirectory()) {
					copyDir(inFile, outFile, ff);
				} else if (!outFile.exists() || override) {
					InputStream in = new FileInputStream(inFile);
					AdaptorUtil.readFile(in, outFile);
				}
			}
		}
	}

	public static URL toURL(String fileName) {
		return fileName != null ? toURL(new File(fileName)) : null;
	}

	public static URL toURL(File file) {
		//[ID]        
		//        return file != null ? new URL(file.toURI().toURL().toString().replace("+", "%2B")) : null;
		//        MalformedURLException e;
		//        e;
		//        UtilsPlugin.error(e);
		//        return null;
		try {
			return file != null ? new URL(file.toURI().toURL().toString().replace("+", "%2B"))
					: null;
		} catch (MalformedURLException e) {
			UtilPlugin.error(e);
			return null;
		}
	}

	public static byte[] readBytes(File file) throws IOException {
		InputStream in = null;
		byte abyte0[];
		in = new FileInputStream(file);
		abyte0 = readBytes(in);
		close(in);
		return abyte0;
		//[ID]        
		//        Exception exception;
		//        exception;
		//        close(in);
		//        throw exception;
	}

	public static byte[] readBytes(InputStream in) throws IOException {
		byte abyte0[];
		byte buf[] = new byte[65535];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read;
		while ((read = in.read(buf)) > 0)
			out.write(buf, 0, read);
		abyte0 = out.toByteArray();
		close(in);
		return abyte0;
		//[ID]        
		//        Exception exception;
		//        exception;
		//        close(in);
		//        throw exception;
	}

	public static List unzip(File archive, File destination) throws IOException {
		return unzip(archive, destination, null);
	}

	public static List unzip(File archive, File destination, IProgressMonitor monitor)
			throws IOException {
		List filesToRemove;
		ZipFile zf;
		filesToRemove = new ArrayList();
		zf = new ZipFile(archive);
		destination.mkdirs();
		for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (!entry.isDirectory()) {
				InputStream is = zf.getInputStream(entry);
				File destFile = new File(destination, entry.getName());
				if (destFile.isDirectory()) {
					destFile.mkdirs();
				} else {
					destFile.getParentFile().mkdirs();
					copyFile(is, destFile);
					if (monitor != null)
						monitor.worked(1);
				}
				filesToRemove.add(destFile);
			}
		}
		//[ID]
		//        break MISSING_BLOCK_LABEL_153;
		//        Exception exception;
		//        exception;
		//        zf.close();
		//        throw exception;
		zf.close();
		return filesToRemove;
	}

	public static URL getURL(IConfigurationElement element, String path) {
		if (path == null)
			return null;
		Bundle bundle = null;
		String relativePath = null;
		if (path.startsWith("/")) {
			int index = path.indexOf("/", 1);
			String bundleName = null;
			if (index > 0) {
				bundleName = path.substring(1, index);
				relativePath = path.substring(index + 1);
			} else {
				bundleName = path.substring(1);
				relativePath = "";
			}
			bundle = Platform.getBundle(bundleName);
		} else {
			relativePath = path;
		}
		if (bundle == null && element != null)
			bundle = Platform.getBundle(element.getNamespaceIdentifier());
		return getURL(bundle, relativePath);
	}

	public static URL getURL(Plugin plugin, String path) {
		if (plugin == null)
			return null;
		else
			return getURL(plugin.getBundle(), path);
	}

	public static URL getURL(Bundle bundle, String path) {
		if (bundle == null || path == null)
			return null;
		URL url;
		url = FileLocator.find(bundle, new Path(path), null);
		if (url == null) {
			url = FileLocator.find(bundle, new Path(""), null);
			if (url != null) {
				String base = url.toExternalForm();
				//[ID]+
				try {
					url = new URL((new StringBuilder(String.valueOf(base))).append("/")
							.append(path).toString());
				} catch (MalformedURLException e) {
					UtilPlugin.error(e);
					return null;
				}
				//-
			}
		}
		return url;
		//[ID]        
		//        Exception e;
		//        e;
		//        UtilsPlugin.error(e);
		//        return null;
	}

	public static File getFile(URL fileUrl) {
		URLConverter converter;
		converter = Activator.getURLConverter(fileUrl);
		//[ID]        
		//        if(converter != null)
		//            break MISSING_BLOCK_LABEL_34;
		//        return new File(fileUrl.toURI());
		//        JVM INSTR pop ;
		//        return new File(fileUrl.getFile());
		//        return new File(converter.resolve(fileUrl).getFile());
		//        Exception e;
		//        e;
		//        UtilsPlugin.error(e);
		//        return null;
		try {
			if (converter != null) {
				return new File(converter.resolve(fileUrl).getFile());
			} else {
				return new File(fileUrl.toURI());
			}
		} catch (Exception e) {
			return new File(fileUrl.getFile());
		}
	}

	public static String getBundlePath(URL fileUrl) {
		if (fileUrl == null) {
			return null;
		} else {
			String str = fileUrl.toExternalForm();
			return str.replaceAll("bundleentry://[^/]*/", "");
		}
	}

	public static File getConfigDir(Plugin plugin) {
		Location location = Platform.getConfigurationLocation();
		if (location != null) {
			URL configURL = location.getURL();
			return getFile(configURL);
		} else {
			return plugin.getStateLocation().toFile();
		}
	}

	public static String loadFully(String path) throws IOException {
		byte buf[] = readBytes(new File(path));
		return new String(buf);
	}

	public static String normalizePath(String path) {
		try {
			path = path.replace('/', '\\').replace("\\\\", "\\").replace("\\", File.separator)
					.trim();
		} catch (Throwable e) {
			UtilPlugin.error(e);
		}
		return path;
	}

	public static boolean isValidFileName(String fName, boolean strict, boolean alphanumericalOnly) {
		if (fName == null) {
			return false;
		} else {
			fName = fName.trim();
			return strict ? alphanumericalOnly ? Pattern.matches(
					"[a-zA-Z0-9_\\-\\.]*[a-zA-Z0-9_\\-]{1,1}", fName) : Pattern.matches(
					"[ a-zA-Z0-9_\\-\\.~]*[a-zA-Z0-9_\\-~]{1,1}", fName) : Pattern.matches(
					"[ a-zA-Z0-9_\\-\\.+~!@#$%\\^&\\(\\)]*[a-zA-Z0-9_\\-+~!@#$%\\^&\\(\\)]{1,1}",
					fName);
		}
	}

	public static List getAllContent(String directory) {
		List files = new ArrayList();
		Stack stack = new Stack();
		stack.push(new File(directory));
		while (!stack.isEmpty()) {
			File content[] = ((File) stack.pop()).listFiles();
			File afile[] = content;
			int i = 0;
			for (int j = afile.length; i < j; i++) {
				File f = afile[i];
				files.add(f);
				if (f.isDirectory())
					stack.push(f);
			}

		}
		return files;
	}

	public static String getFileName(URL url) {
		String file = url.getFile();
		int index = file.lastIndexOf("/");
		if (index >= 0)
			return file.substring(index + 1);
		else
			return file;
	}

	public static boolean resourceExists(URL url) {
		InputStream in = null;
		try {
			in = url.openStream();
		} catch (Exception exception) {
			close(in);
			return false;
		}
		close(in);
		return true;
	}

	public static void close(Object resource) {
		try {
			if (resource instanceof InputStream)
				((InputStream) resource).close();
			else if (resource instanceof OutputStream)
				((OutputStream) resource).close();
			else if (resource instanceof Reader)
				((Reader) resource).close();
			else if (resource instanceof Writer)
				((Writer) resource).close();
			else if (resource instanceof RandomAccessFile)
				((RandomAccessFile) resource).close();
			else if (resource != null)
				throw new IllegalArgumentException((new StringBuilder("Unknown resource: "))
						.append(resource).toString());
		} catch (IOException _ex) {
		}
	}

}
