package bourse.protocole;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bourse.sdd.PDMPro;

public class ResultBye extends Protocole {
  
   private LinkedList<PDMPro> listepdm;
   
   	/**
     * Retourne une liste remplie de PDMPro.
	 */
	public List<PDMPro> getListepdm() {
		 return this.listepdm;
	 }
    
    /**
     * Instancie un nouveau message de fin de session en envoyant la liste des
     * places de marché présentes sur la base de données.
     * @param LinkedListe liste est une liste chaînée remplie de PDMPro.
     */
   public ResultBye(LinkedList<PDMPro> liste) {
        super(TypeMessage.RESULTBYE);
        if (liste == null) {
            listepdm = new LinkedList<PDMPro>();
        } else {
            listepdm=liste;
        }
    }
    
    public ResultBye(Element type) {
        super(TypeMessage.RESULTBYE);
        this.toClass(type);
    }
    
    protected void toClass(Element type) {
        NodeList noeuds = type.getChildNodes();
        this.listepdm=new LinkedList<PDMPro>();
        for(int i=0;i<noeuds.getLength();i++) {
            Element pdm = (Element)noeuds.item(i);
            PDMPro p=new PDMPro(pdm.getAttribute("NOM"), pdm.getAttribute("ADRESSE"));
            this.listepdm.add(p);
        }
    }
    
    public Document toDOM() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("RESULTBYE");
            root.appendChild(type);
            for(int i=0;i<this.listepdm.size();i++) {
				Element pdm = document.createElement("PDM");
				type.appendChild(pdm);
				Attr nomAttr = document.createAttribute("NOM");
				nomAttr.setValue(((PDMPro) this.listepdm.get(i)).getNom());
				pdm.setAttributeNode(nomAttr);
				Attr adresseAttr = document.createAttribute("ADRESSE");
				adresseAttr.setValue(((PDMPro) this.listepdm.get(i))
						.getAdresse());
				pdm.setAttributeNode(adresseAttr);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;

    }
    
    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
    public static void main(String args[]) {
        
        LinkedList<PDMPro> pdmliste=new LinkedList<PDMPro>();
        
        PDMPro plm=new PDMPro("Prem", "192.12.26.56");
        pdmliste.add(0, plm);
        PDMPro plm2=new PDMPro("Deux", "192.12.26.57");
        pdmliste.add(1, plm2);
        PDMPro plm3=new PDMPro("Prem", "192.12.26.56");
        pdmliste.add(2, plm3);
        
        String p=new ResultBye(pdmliste).toXML();
        System.out.println(p);
        
        ResultBye r = (bourse.protocole.ResultBye)bourse.protocole.Protocole.newInstance("<MSG><RESULTBYE><PDM NOM=\"jhlfdhsl\" ADRESSE=\"192.168.1.1:24\"/><PDM NOM=\"coucou\" ADRESSE=\"1.1.1.1:1\"/></RESULTBYE></MSG>");
        System.out.println(r.toXML());
    }

}
