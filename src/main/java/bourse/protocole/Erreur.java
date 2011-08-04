/*
 * Erreur.java
 *
 * Created on 15 janvier 2004, 18:48
 */
package bourse.protocole;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author pechot
 */
public class Erreur extends Protocole {

	private String nom;

	public String getNom() {
		return this.nom;
	}

	private String message;
	private String pdmnom;
	private String adresse;
	private String raison;

	public String getRaison() {
		return this.raison;
	}

	/**
	 * Creates a new instance of Erreur general
	 */
	public Erreur(String nom, String message, String pdmnom, String adresse,
			String raison) {
		super(new TypeMessage(TypeMessage.TM_ERREUR));
		this.nom = nom;
		this.message = message;
		this.pdmnom = pdmnom;
		this.adresse = adresse;
		this.raison = raison;
	}

	/**
	 * Creates a new instance of Erreur pour erreur ZEROVENTE
	 */
	public Erreur(String nom, String message, String raison) {
		super(new TypeMessage(TypeMessage.TM_ERREUR));
		this.nom = nom;
		this.message = message;
		this.raison = raison;
	}

	/**
	 * Creates a new instance of Erreur pour erreur DUPLICATION
	 */
	public Erreur(String nom, String message, String pdmnom, String adresse) {
		super(new TypeMessage(TypeMessage.TM_ERREUR));
		this.nom = nom;
		this.message = message;
		this.pdmnom = pdmnom;
		this.adresse = adresse;
		this.raison = "";
	}

	/**
	 * Creates a new instance of Erreur simple evitant le passage de chaine vide
	 */
	public Erreur(String nom, String message) {
		super(new TypeMessage(TypeMessage.TM_ERREUR));
		this.nom = nom;
		this.message = message;
		this.pdmnom = "";
		this.adresse = "";
		this.raison = "";
	}

	public Erreur(Element type) {
		super(new TypeMessage(TypeMessage.TM_ERREUR));
		this.toClass(type);
	}

	public String toXML() {
		return super.toXML(this.toDOM());
	}

	protected void toClass(Element type) {
		this.nom = type.getAttribute("NOM");
		try {
			this.message = ((Text) type.getChildNodes().item(0)).getNodeValue();

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		if (this.nom.equalsIgnoreCase("DUPLICATION")) {
			try {
				Element pdmElem = ((Element) type.getChildNodes().item(1));
				this.pdmnom = pdmElem.getAttribute("NOM");
				this.adresse = pdmElem.getAttribute("ADRESSE");
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
			this.raison = "";
		} else if (this.nom.equalsIgnoreCase("ZEROVENTE")) {
			try {
				Element pdmElem = ((Element) type.getChildNodes().item(1));
				this.raison = pdmElem.getAttribute("TYPE");

			} catch (Exception e) {
				e.printStackTrace(System.err);
			}

			this.pdmnom = "";
			this.adresse = "";
		} else {
			this.pdmnom = "";
			this.adresse = "";
			this.raison = "";
		}

	}

	public Document toDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			Element root = document.createElement("MSG");
			document.appendChild(root);
			Element type = document.createElement("ERREUR");
			root.appendChild(type);
			Attr nom = document.createAttribute("NOM");
			nom.setValue(this.nom);
			type.setAttributeNode(nom);
			type.appendChild(document.createTextNode(this.message));
			if (this.nom.equalsIgnoreCase("DUPLICATION")) {
				Element pdm = document.createElement("PDM");
				type.appendChild(pdm);
				Attr pdmnom = document.createAttribute("NOM");
				pdmnom.setValue(this.pdmnom);
				pdm.setAttributeNode(pdmnom);
				Attr adresse = document.createAttribute("ADRESSE");
				adresse.setValue(this.adresse);
				pdm.setAttributeNode(adresse);
			} else if (this.nom.equalsIgnoreCase("ZEROVENTE")) {
				Element pdm = document.createElement("RAISON");
				type.appendChild(pdm);
				Attr raison = document.createAttribute("TYPE");
				raison.setValue(this.raison);
				pdm.setAttributeNode(raison);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return document;
	}

}
