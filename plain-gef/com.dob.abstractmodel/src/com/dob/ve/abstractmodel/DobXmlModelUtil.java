package com.dob.ve.abstractmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idanilov
 *
 */
public final class DobXmlModelUtil {

	private DobXmlModelUtil() {
	}

	public static boolean isTopLevel(final DobXmlElement bean) {
		return (bean.getParent() instanceof DobXmlModel);
	}

	//	XXX: may be remake via extra storage - map<id,bean>.
	//	public static DobXmlElement getBeanById(DobXmlElement root, String id) {
	//		DobXmlElement result = null;
	//		if (id != null) {
	//			List<DobXmlElement> allChildren = getAllChildren(root, false);
	//			for (DobXmlElement n : allChildren) {
	//				if (id.equals(DobComponentUtils.getId(n))) {
	//					result = n;
	//					break;
	//				}
	//			}
	//		}
	//		return result;
	//	}

	/**
	 * Collects all parent beans hierarchy including <code>bean</code>.
	 * Final list has root as first element.
	 * @param bean
	 * @param list storage
	 */
	public static List<DobXmlElement> collectSuperTree(final DobXmlElement bean) {
		List<DobXmlElement> result = new ArrayList<DobXmlElement>();
		collectSuperTree(bean, result);
		return result;
	}

	private static void collectSuperTree(final DobXmlElement bean, final List<DobXmlElement> list) {
		if (bean != null) {
			DobXmlElement parent = bean.getParent();
			if (parent != null) {
				collectSuperTree(parent, list);//root - first.
			}
			list.add(bean);
		}
	}

	public static List<DobXmlElement> getAllChildren(DobXmlElement root, boolean includeRoot) {
		List<DobXmlElement> result = new ArrayList<DobXmlElement>();
		collectAllChildren(root, includeRoot, result);
		return result;
	}

	private static void collectAllChildren(DobXmlElement root, boolean includeRoot,
			List<DobXmlElement> result) {
		if (root != null) {
			if (includeRoot) {
				result.add(root);
			}
			List<DobXmlElement> children = root.getChildren();
			for (DobXmlElement n : children) {
				collectAllChildren(n, true, result);
			}
		}
	}

}
