package bourse.sdd;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import bourse.protocole.TypeMessage;

public class Enchere {

	private Integer numero;
	
	private Livre livre;
	
	public Enchere(Element enchere) {
		numero = Integer.parseInt(enchere.getAttribute("NUMERO"));
		livre = new Livre((Element) enchere.getFirstChild());
	}

	public Enchere(Integer numero, Livre livre) {
		this.numero = numero;
		this.livre = livre;
	}
	
	public void addElement(final Node root) {
		Document document=root.getOwnerDocument();
        Element enchere = document.createElement("ENCHERE");
        enchere.setAttribute("NUMERO", numero.toString());
        livre.addElement(enchere, new TypeMessage(TypeMessage.TM_PROGRAMME));
        root.appendChild(enchere);
	}
}
