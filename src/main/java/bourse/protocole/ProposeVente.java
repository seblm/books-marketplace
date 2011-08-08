package bourse.protocole;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ProposeVente extends Protocole {

	private String nom;
	private float prix;
	private int id;

	public int getNom() {
		String typeEnchere = nom.toUpperCase();
		if (typeEnchere.equals("ENCHEREUN")) {
			return 1;
		}
		if (typeEnchere.equals("ENCHEREDEUX")) {
			return 2;
		}
		if (typeEnchere.equals("ENCHERETROIS")) {
			return 3;
		}
		if (typeEnchere.equals("ENCHEREQUATRE")) {
			return 4;
		}
		if (typeEnchere.equals("ENCHERECINQ")) {
			return 5;
		}
		return 0;
	}

	public float getPrix() {
		return prix;
	}

	public int getId() {
		return id;
	}

	public ProposeVente(String nom, float prix, int id) {
		super(new TypeMessage(TypeMessage.TM_PROPOSE_VENTE));
		if (nom == null) {
			this.nom = "";
		} else {
			this.nom = nom;
		}
		this.prix = prix;
		this.id = id;
	}

	public ProposeVente(Element type) {
		super(new TypeMessage(TypeMessage.TM_PROPOSE_VENTE));
		this.toClass(type);
	}

	protected void toClass(Element type) {
		this.nom = type.getAttribute("NOM");
		NodeList noeuds = type.getChildNodes();
		Element livre = (Element) noeuds.item(0);
		this.id = Integer.valueOf(livre.getAttribute("ID")).intValue();
		Element enchere = (Element) noeuds.item(1);
		Text noeud = (Text) enchere.getFirstChild();
		this.prix = Float.valueOf(noeud.getNodeValue()).floatValue();

	}

	public Document toDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			Element root = document.createElement("MSG");
			document.appendChild(root);
			Element type = document.createElement("PROPOSEVENTE");
			root.appendChild(type);
			Attr nom = document.createAttribute("NOM");
			nom.setValue(this.nom);
			type.setAttributeNode(nom);
			Element livre = document.createElement("LIVRE");
			type.appendChild(livre);
			Attr idelm = document.createAttribute("ID");
			idelm.setValue(String.valueOf(id));
			livre.setAttributeNode(idelm);
			Element encherelm = document.createElement("ENCHERE");
			type.appendChild(encherelm);
			encherelm.appendChild(document.createTextNode(String.valueOf(prix)));
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return document;
	}

	public String toXML() {
		return super.toXML(this.toDOM());
	}

}
