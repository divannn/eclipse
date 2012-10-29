package com.dob.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Utilites for working with XML.
 * @author idanilov
 *
 */
public class XMLUtils {

	public static final String XML_TYPE = "xml";
	public static final String XML_ENCODING = "UTF-8";

	//public static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private XMLUtils() {
	}

	public static Document createEmptyDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		Document result = builder.newDocument();
		return result;
	}

	public static Element getOwnFirstChild(final Element source) {
		Element result = null;
		NodeList children = source.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node next = children.item(i);
			if (next instanceof Element) {
				result = (Element) next;
				break;
			}
		}
		return result;
	}

	public static Element getFirstChild(final Element source, final String tag) {
		/*		Element result = null;
		 NodeList children = source.getElementsByTagName(tag);
		 if (children != null && children.getLength() > 0) {
		 result = (Element) children.item(0);
		 }
		 return result;*/
		return getFirstOwnChild(source, tag);
	}

	public static Element getFirstOwnChild(final Element source, final String tag) {
		Element result = null;
		NodeList children = source.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node next = children.item(i);
			if (next instanceof Element) {
				Element nextE = (Element) next;
				if (nextE.getTagName().equals(tag)) {
					result = nextE;
					break;
				}
			}
		}
		return result;
	}

	public static String getTextContent(final Element e) {
		String result = "";
		if (e != null) {
			String text = e.getTextContent();
			if (text != null) {
				result = text.trim();
			}
		}
		return result;
	}

	public static List<Element> getTopLevelChildren(final Element rootNode, final String tag) {
		NodeList allElementsList = rootNode.getElementsByTagName(tag);
		List<Element> result = new ArrayList<Element>();
		for (int i = 0; i < allElementsList.getLength(); i++) {
			Element nextE = (Element) allElementsList.item(i);
			if (rootNode.equals(nextE.getParentNode())) {
				result.add(nextE);
			}
		}
		return result;
	}

	public static Document readXML2(final URL fileUrl) throws IOException,
			ParserConfigurationException, SAXException {
		Document result = null;
		if (fileUrl != null) {
			InputStream is = null;
			try {
				is = fileUrl.openStream();
				InputSource inputSource = new InputSource(is);
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				result = docBuilder.parse(inputSource);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	public static Element readXML(final URL fileUrl) throws IOException,
			ParserConfigurationException, SAXException {
		Element rootXmlElement = null;
		if (fileUrl != null) {
			InputStream is = null;
			try {
				is = fileUrl.openStream();
				InputSource inputSource = new InputSource(is);
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = docBuilder.parse(inputSource);
				rootXmlElement = doc.getDocumentElement();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException ioe) {
				}
			}
		}
		return rootXmlElement;
	}

	public static Collection getAllElements(final Element parent, final String name) {
		Collection result = new HashSet();
		if (parent != null && (name != null)) {
			NodeList nodeList = parent.getElementsByTagName(name);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nextNode = nodeList.item(i);
				//        result.add(curKeywordNode.getTextContent());//NOT supported in jdk1.4
				Node nextFirstChild = nextNode.getFirstChild();
				if (nextFirstChild != null) {
					result.add(nextFirstChild.getNodeValue());
				}
			}
		}
		return result;
	}

	/**
	 * Saves DOM to XML. 
	 * @param doc
	 * @param os
	 * @throws IOException
	 * NOTE: PI serialized at the end of document's root node. See BaseMarkupSerializer#serializePreRoot().
	 */
	//1st way to save.
	public static void writeXML(final Document doc, final OutputStream os, final boolean indenting,
			final int lineWidth) throws IOException {
		XMLSerializer serializer = null;
		OutputFormat outputFormat = new OutputFormat(XML_TYPE, XML_ENCODING, false);
		if (indenting) {
			outputFormat.setIndenting(indenting);
			outputFormat.setLineWidth(lineWidth);
		}
		serializer = new XMLSerializer(os, outputFormat);
		serializer.serialize(doc);
	}

	public static void writeXML(final Document doc, final Writer w, final boolean indenting,
			final int lineWidth) throws IOException {
		XMLSerializer serializer = null;
		OutputFormat outputFormat = new OutputFormat(XML_TYPE, XML_ENCODING, false);
		if (indenting) {
			outputFormat.setIndenting(indenting);
			outputFormat.setLineWidth(lineWidth);
		}
		serializer = new XMLSerializer(w, outputFormat);
		serializer.serialize(doc);
	}

	public static void writeXML(final Document doc, final File f, final boolean indenting,
			final int lineWidth) throws IOException {
		writeXML(doc, new FileWriter(f), indenting, lineWidth);
	}

	//2nd way to save.
	public static void writeXML(final File file, final Document document) throws IOException,
			TransformerException {
		if (file == null || document == null) {
			return;
		}
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, XML_TYPE); //$NON-NLS-1$            
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.ENCODING, XML_ENCODING); //$NON-NLS-1$
		Writer writer = new FileWriter(file);
		Result streamResult = new StreamResult(writer);
		transformer.transform(new DOMSource(document), streamResult);
		writer.close();
	}

	/* 
	 //3d way to save- using template. 	
	 public static void writeXML2(final File file, final Document document) 
	 throws IOException, TransformerException {
	 if (file == null || document == null) {
	 return;
	 }
	 StreamSource source = new StreamSource(new StringReader(XSL));
	 Templates templates = TransformerFactory.newInstance().newTemplates(source);
	 Transformer transformer = templates.newTransformer();
	 OutputStream os = new FileOutputStream(file);
	 Result streamResult = new StreamResult(os);        
	 transformer.transform(new DOMSource(document), streamResult);
	 os.close();
	 }

	 private static final String XSL =
	 "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<xsl:stylesheet version=\"1.0\" " +
	 "xmlns:xalan=\"http://xml.apache.org/xslt\" " +
	 "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
	 "<xsl:output method=\"xml\" indent=\"yes\" " + "xalan:indent-amount=\"4\" />" +
	 "<xsl:template match=\"/\">" + "<xsl:copy-of select=\".\"/>" + "</xsl:template>" +
	 "</xsl:stylesheet>";*/

}
