package orka.test;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Test {

	public static void main(String[] args) {
		// String iban = "HR71 2500 0091 1020 0356 0";

		IBANCheckDigit icd = new IBANCheckDigit();

		String iban = "HR7125000091102003560";
		System.err.println("iban: " + iban);
		System.err.println(icd.isValid(iban));
		try {
			System.err.println(icd.calculate("HR0025000091102003560"));
		} catch (CheckDigitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generiranjeKontrolnogBroja() {
		// BigDecimal b = new BigDecimal("25000091102003560172700");
		// b = b.divide(new BigDecimal("97"), 32, RoundingMode.HALF_UP);
		//
		// System.err.println(b);
		// String s = b.toString().substring(0, b.toString().indexOf("."));
		//
		// System.err.println(s);
		//
		// BigDecimal bs = new BigDecimal(s);
		//
		// bs = bs.multiply(new BigDecimal("97"));
		//
		// BigDecimal bb = new
		// BigDecimal("25000091102003560172700").subtract(bs);
		// System.err.println(bb);
		//
		// System.err.println(new BigDecimal("98").subtract(bb));
	}

	private static void writeDodatniPodatak() {
		BigDecimal b = new BigDecimal("0.03");
		for (int i = 0; i < 42; i++) {
			b = b.add(b);
		}

		System.err.println("Milimetara: " + b);

		b = b.divide(new BigDecimal("1000"));

		System.err.println("Metara : " + b);

		b = b.divide(new BigDecimal("1000"));

		System.err.println("Kilometara : " + b);

	}

	private static void writeXml() {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("odvage");
			doc.appendChild(rootElement);

			Element staff = doc.createElement("odvaga");
			rootElement.appendChild(staff);

			Attr attr = doc.createAttribute("brojOdvage");
			attr.setValue("00000007");
			staff.setAttributeNode(attr);

			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			Element firstname = doc.createElement("datumKreiranjaBruto");
			firstname.appendChild(doc.createTextNode(sd.format(new Date())));
			staff.appendChild(firstname);

			// lastname elements
			Element lastname = doc.createElement("datumKreiranjaTara");
			lastname.appendChild(doc.createTextNode(sd.format(new Date())));
			staff.appendChild(lastname);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// Preety print
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"/home/tomislav/Desktop/01.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	private static void readXml() {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(
					"/home/tomislav/Desktop/a.xml"));
			doc.getDocumentElement().normalize();

			NodeList lista = doc.getElementsByTagName("Staff");
			for (int i = 0; i < lista.getLength(); i++) {
				Node n = lista.item(i);

				NodeList podLista = n.getChildNodes();
				for (int j = 0; j < podLista.getLength(); j++) {
					Node nn = podLista.item(j);
					if (nn.getNodeName().equals("firstname")) {
						System.err.println(nn.getFirstChild().getNodeValue());
					}

					if (nn.getNodeName().equals("lastname")) {
						System.err.println(nn.getFirstChild().getNodeValue());
					}

					if (nn.getNodeName().equals("nickname")) {
						System.err.println(nn.getFirstChild().getNodeValue());
					}

					if (nn.getNodeName().equals("salary")) {
						System.err.println(nn.getFirstChild().getNodeValue());
						nn.getFirstChild().setNodeValue("10000");
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"/home/tomislav/Desktop/a.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);
		} catch (Exception e2) {
			throw new RuntimeException(e2);
		}
	}

	private static BigDecimal razlikaSati(String s1, String s2) {
		String[] a = s1.split("\\.");

		BigDecimal dolazak = new BigDecimal(a[0]);

		if (a.length > 1) {
			dolazak = new BigDecimal(a[0] + ".5");
		}

		a = s2.split("\\.");

		BigDecimal odlazak = new BigDecimal(a[0]);

		if (a.length > 1) {
			odlazak = new BigDecimal(a[0] + ".5");
		}

		if (dolazak.compareTo(odlazak) > 0) {
			odlazak = odlazak.add(new BigDecimal("24"));
		}

		System.err.println(odlazak.subtract(dolazak));

		return odlazak.subtract(dolazak);
	}

	public static String calc(String digStr) {
		int len = digStr.length();
		int sum = 0, rem = 0;
		int[] digArr = new int[len];
		for (int k = 1; k <= len; k++)
			// compute weighted sum
			sum += (11 - k) * Character.getNumericValue(digStr.charAt(k - 1));
		if ((rem = sum % 11) == 0)
			return "0";
		else if (rem == 1)
			return "X";
		return (new Integer(11 - rem)).toString();
	}

	public static void metoda(BigDecimal b) {
		b = b.subtract(new BigDecimal("1"));

		System.err.println(b);
	}
}
