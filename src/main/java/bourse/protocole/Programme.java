package bourse.protocole;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bourse.sdd.Enchere;
import bourse.sdd.Livre;
import bourse.sdd.ProgrammePro;

public class Programme extends Protocole {
    
    /**
     * Une liste chaînée remplie de bourse.sdd.ProgrammePro
     */
    private LinkedList<ProgrammePro> listeProgramme;
    
    public List<ProgrammePro> getListeProgramme() {
    	return this.listeProgramme;
    }
    
    public void setListeProgramme(List<ProgrammePro> listeProgramme) {
    	this.listeProgramme = new LinkedList<ProgrammePro>(listeProgramme);
    }
    
    public Programme(List<ProgrammePro> liste) {
        super(new TypeMessage(TypeMessage.TM_PROGRAMME));
    	this.listeProgramme = new LinkedList<ProgrammePro>(liste);
    }
    
    public Programme(Element type) {
        super(new TypeMessage(TypeMessage.TM_PROGRAMME));
        this.toClass(type);
    }

    public int nbEncheresProposeesParAgents(String nomPdm) {
        int resultat = 0;
        ListIterator<ProgrammePro> i = listeProgramme.listIterator();
        while (i.hasNext() && !i.next().getLivre().getProprietaire().equalsIgnoreCase(nomPdm)) {
            resultat++;
        }
        return resultat;
    }

    /**
     * Regarde si un agent peut proposer une vente (le livre n'est pas déjà en vente
     * dans le programme et la taille de programme est cohérente).
     */
    public boolean insertionPossible(String nomPdm, int idLivre) {
        boolean bonneTaille = nbEncheresProposeesParAgents(nomPdm) < listeProgramme.size();
        if (bonneTaille) {
            ListIterator<ProgrammePro> i = listeProgramme.listIterator();
            boolean dejaPresent = false;
            while (i.hasNext() && !dejaPresent) {
                dejaPresent = i.next().getLivre().getId() == idLivre;
            }
            return !dejaPresent;
        } else {
            return false;
        }
    }
    
    /**
     * Insère une proposition de vente dans le programme.
     * @return true si l'ajout a réussit, false sinon
     */
    public boolean ajouterVente(Livre l, String nomPdm, int typeEnchere, float prixVente) {
        ProgrammePro derniereEnchere = listeProgramme.removeLast();
        int index = 0;
        ListIterator<ProgrammePro> i = listeProgramme.listIterator();
        while (i.hasNext() && !i.next().getLivre().getProprietaire().equalsIgnoreCase(nomPdm)) {
            index++;
        }
        if (!i.hasNext()) {
        	// Il n'y a plus de place dans le programme.
            listeProgramme.addLast(derniereEnchere);
            return false;
        } else {
            int emplacementInsertion = index;
            int numeroEnchereInsertion = i.next().getNum();
            i.previous();
            while (i.hasNext()) {
                i.next().incrementerNumEnchere();
            }
            listeProgramme.add(emplacementInsertion, new ProgrammePro(numeroEnchereInsertion, l, typeEnchere, prixVente));
            return true;
        }
    }
    
    protected void toClass(Element msg) {
        NodeList programmes = msg.getChildNodes();
        listeProgramme = new LinkedList<ProgrammePro>();
        for (int i = 0; i < programmes.getLength(); i++) {
            Element programme = (Element)programmes.item(i);
            Element enchere = (Element)programme.getFirstChild();
            ProgrammePro p = new ProgrammePro(Integer.parseInt(enchere.getAttribute("NUMERO")), new Livre((Element) enchere.getFirstChild()));
            this.listeProgramme.add(i, p);
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
            Element programme = document.createElement("PROGRAMME");
            root.appendChild(programme);
            for (final ProgrammePro programmePro : listeProgramme) {
	            if (programmePro.getLivre() == null) {
	                System.err.println("Programme : c'est livre qui est null !");
	            } else {
	            	Enchere enchere = new Enchere(programmePro.getNum(), programmePro.getLivre());
	            	enchere.addElement(programme);
	            }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return document;
    }
    
    public String toXML() {
        return super.toXML(this.toDOM());
    }
    
    public String toHtml() {
        StringBuilder sortie = new StringBuilder();
        sortie.append("<ol>");
        for (final ProgrammePro programmePro : listeProgramme) {
            if (programmePro.getLivre() != null) {
                sortie.append("<li>" + programmePro.getLivre().toHtml() + "</li>");
            }
        }
        sortie.append("</ol>");
        return sortie.toString();
    }

	public ProgrammePro removeFirstEnchere() {
		return listeProgramme.removeFirst();
	}

	public ProgrammePro getLast() {
		return listeProgramme.getLast();
	}
    
}
