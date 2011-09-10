package bourse.protocole;

import org.w3c.dom.*;
import bourse.sdd.*;

//import  javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.ListIterator;

 

public class Programme extends Protocole {
    
    /** Une liste chaînée remplie de bourse.sdd.ProgrammePro */
    private LinkedList listeProgramme;
    
    public LinkedList getListeProgramme() { return this.listeProgramme; }
    public void setListeProgramme(LinkedList listeProgramme) { this.listeProgramme = listeProgramme; }
    
    public Programme(LinkedList liste) {
        super(new TypeMessage(TypeMessage.TM_PROGRAMME));
        this.listeProgramme=liste;
    }
    
    public Programme(Element type) {
        super(new TypeMessage(TypeMessage.TM_PROGRAMME));
        this.toClass(type);
    }

    public int nbEncheresProposeesParAgents(String nomPdm) {
        int resultat = 0;
        java.util.ListIterator i = listeProgramme.listIterator();
        while (i.hasNext() && !((ProgrammePro)i.next()).getLivre().getProprietaire().equalsIgnoreCase(nomPdm))
            resultat++;
        return resultat;
    }

    /** Regarde si un agent peut proposer une vente (le livre n'est pas déjà en vente
     * dans le programme et la taille de programme est cohérente). */
    public boolean insertionPossible(String nomPdm, int idLivre) {
        boolean bonneTaille = nbEncheresProposeesParAgents(nomPdm) < listeProgramme.size();
        if (bonneTaille) {
            java.util.ListIterator i = listeProgramme.listIterator();
            boolean dejaPresent = false;
            while (i.hasNext() && !dejaPresent)
                dejaPresent = ((ProgrammePro)i.next()).getLivre().getId() == idLivre;
            return !dejaPresent;
        } else
            return false;
    }
    
    /** Insère une proposition de vente dans le programme.
     * @return true si l'ajout a réussit, false sinon
     */
    public boolean ajouterVente(Livre l, String nomPdm, int typeEnchere, float prixVente) {
        Object derniereEnchere = listeProgramme.removeLast();
        int index = 0;
        java.util.ListIterator i = listeProgramme.listIterator();
        while (i.hasNext() && !((ProgrammePro)i.next()).getLivre().getProprietaire().equalsIgnoreCase(nomPdm))
            index++;
        if (!i.hasNext()) { // Il n'y a plus de place dans le programme.
            listeProgramme.addLast(derniereEnchere);
            return false;
        } else {
            int emplacementInsertion = index;
            int numeroEnchereInsertion = ((ProgrammePro)i.next()).getNum();
            i.previous();
            while (i.hasNext())
                ((ProgrammePro)i.next()).incrementerNumEnchere();
            listeProgramme.add(emplacementInsertion, new ProgrammePro(numeroEnchereInsertion , l, typeEnchere, prixVente));
            return true;
        }
    }
    
    protected void toClass(Element type) {
        NodeList noeuds = type.getChildNodes();
        this.listeProgramme=new LinkedList();
        for(int i=0;i<noeuds.getLength();i++)
        {
            Element enchere = (Element)noeuds.item(i);
            NodeList noeud = enchere.getChildNodes();
            Element livrelm=(Element)noeud.item(0);
            ProgrammePro p=new ProgrammePro(Integer.parseInt(enchere.getAttribute("NUMERO")), new Livre(livrelm));
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
            Element type = document.createElement("PROGRAMME");
            root.appendChild(type);
            for(int i=0;i<this.listeProgramme.size();i++)
            {
            Element enchere = document.createElement("ENCHERE");
            type.appendChild(enchere);
            Attr num = document.createAttribute("NUMERO");
            num.setValue(String.valueOf(((ProgrammePro)this.listeProgramme.get(i)).getNum()));
            enchere.setAttributeNode(num);
            if (((ProgrammePro)this.listeProgramme.get(i)).getLivre() == null)
                System.err.println("Programme : c'est livre qui est null !");
            ((ProgrammePro)this.listeProgramme.get(i)).getLivre().addElement(enchere, this.type);
            
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
        
        LinkedList pdmliste=new LinkedList();
        int i=12;
        float pr=0;
        float et= (float)0.4;
        Livre livre=new Livre("lupin", "leblanc", new Categorie(), "poch", "belin", pr, et,"15/11/00","yetet",i,"",(float)0.0); 
        ProgrammePro plm=new ProgrammePro(1,livre);
        pdmliste.add(0, plm);
        Livre livre2=new Livre("peterpan", "disney", new Categorie(), "poch", "belin", pr, et,"15/11/00","yetet",i,"",(float)0.0); 
        ProgrammePro plm2=new ProgrammePro(2,livre2);
        pdmliste.add(1, plm2);
        Livre livre3=new Livre("roi lion", "disney", new Categorie(), "poch", "belin", pr, et,"15/11/00","yetet",i,"",(float)0.0); 
        ProgrammePro plm3=new ProgrammePro(3,livre3);
        pdmliste.add(2, plm3);
        
        String p=new Programme(pdmliste).toXML();
        System.out.println(p);
        Protocole message = Protocole.newInstance("<MSG><PROGRAMME><ENCHERE NUMERO=\"1\"><LIVRE TITRE=\"coucou\" CATEGORIE=\"Romans\" PRIX=\"5\" ETAT=\"0.8\"/></ENCHERE></PROGRAMME></MSG>");
        System.out.println(((Programme)message).toHtml());
    }
    
    public String toHtml() {
        ListIterator i = listeProgramme.listIterator();
        String sortie = "<ol>";
        while (i.hasNext())
            sortie += "<li>" + ((ProgrammePro)i.next()).getLivre().toHtml() + "</li>";
        sortie += "</ol>";
        return sortie;
    }
    
}
