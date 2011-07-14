package bourse.protocole;

import org.w3c.dom.*;
import bourse.sdd.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;

/** C'est le message envoyé à chaque étape d'enchère par la place de marché vers
 * les agents. */
public class PropositionEnchereP extends bourse.protocole.Protocole {
    
    private String nom;
    private int numero;
    private int temps;
    private float pas;
    private Livre livre;
    private float enchere;
    private String nomagent;
  
    
    public String getNom(){return this.nom;}
    public String getAgent(){return this.nomagent;}
    public int getNumeroEnchere(){return this.numero;}
    public int getTemps(){return this.temps;}
    public Livre getLivre(){return this.livre;}
    public float getValeurEnchere(){return this.enchere;}
    public float getPas(){return this.pas;}
    
    /** contructeur general*/
    public PropositionEnchereP(String nom,int numero, int temps,float pas,Livre livre,float enchere,String nomagent) {
        super(new TypeMessage(TypeMessage.TM_PROPOSITION_ENCHERE_P));
        this.nom=nom;
        this.numero=numero;
        this.pas=pas;
        this.livre=livre;
        this.temps=temps;
        this.enchere = enchere;
        this.nomagent=nomagent;

    }
    public PropositionEnchereP(String nom,int numero, int temps,float pas,Livre livre,float enchere) {
        super(new TypeMessage(TypeMessage.TM_PROPOSITION_ENCHERE_P));
        this.nom=nom;
        this.numero=numero;
        this.pas=pas;
        this.livre=livre;
        this.temps=temps;
        this.enchere = enchere;
        this.nomagent="";

    }
    /** A utiliser pour enchère un (à prendre ou à laisser) */
    public PropositionEnchereP(String nom,int numero, int temps,Livre livre,float enchere) {
        super(new TypeMessage(TypeMessage.TM_PROPOSITION_ENCHERE_P));
        this.nom=nom;
        this.numero=numero;
        this.pas = 0;
        this.livre=livre;
        this.temps=temps;
        this.enchere = enchere;
        this.nomagent="";

    }
    /**constructeur ne demandant pas le nom de l'agent et l'enchere*/
   public PropositionEnchereP(String nom,int numero, int temps,float pas,Livre livre){
        super(new TypeMessage(TypeMessage.TM_PROPOSITION_ENCHERE_P));
        this.nom=nom;
        this.numero=numero;
        this.pas=pas;
        this.livre=livre;
        this.temps=temps;
        this.enchere = (float)livre.getEtat() * livre.getPrix();
        this.nomagent="";
    
    }
    /**constructeur ne demandant pas le pas*/
   public PropositionEnchereP(String nom,int numero,int temps,Livre livre){
        super(new TypeMessage(TypeMessage.TM_PROPOSITION_ENCHERE_P));
        this.nom=nom;
        this.numero=numero;
        this.pas=0;
        this.livre=livre;
        this.temps=temps;
        this.enchere = 0;
        this.nomagent="";
    
    }
    /** constructeur à partir du DOM*/
    public PropositionEnchereP(Element type) {
        super(new TypeMessage(TypeMessage.TM_PROPOSITION_ENCHERE_P));
        this.toClass(type);
    }
    /**permet le passage du DOM à la classe*/
    protected void toClass(Element type) {
        this.nom = type.getAttribute("NOM");
        this.numero = Integer.parseInt(type.getAttribute("NUMERO"));
        this.temps =Integer.parseInt(type.getAttribute("TEMPS"));
        if (this.nom.equalsIgnoreCase("ENCHERETROIS")||(this.nom.equalsIgnoreCase("ENCHEREQUATRE")&&type.hasAttribute("PAS")))
            this.pas=Float.parseFloat(type.getAttribute("PAS"));
        else
            this.pas=0;
        NodeList noeuds = type.getChildNodes();
        Element livrend = (Element)noeuds.item(0);
        this.livre=new Livre(livrend);
        //test si enchere2
        System.out.println(noeuds.getLength());
        if (noeuds.getLength()==1)
        {
            /*Element enchere = (Element)noeuds.item(1);
            Text noeud=(Text)enchere.getFirstChild();
            this.enchere = Float.parseFloat(noeud.getNodeValue());*/
            this.enchere=0;
            this.nomagent ="";
            
        }
        else
        {
            if(noeuds.getLength()==2)
            {
                Element enchere = (Element)noeuds.item(1);
                Text noeud=(Text)enchere.getFirstChild();
                this.enchere = Float.parseFloat(noeud.getNodeValue());
                this.nomagent="";
            }
            else
            {
                Element enchere = (Element)noeuds.item(1);
                Text noeud=(Text)enchere.getFirstChild();
                this.enchere = Float.parseFloat(noeud.getNodeValue());
                Element agent = (Element)noeuds.item(2);
                this.nomagent=agent.getAttribute("NOM");;
                System.out.println( this.nomagent);
            }
        }
        
    }
    
    public org.w3c.dom.Document toDOM() {
         Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            Element type = document.createElement("PROPOSITIONENCHERE");
            root.appendChild(type);
            Attr nom = document.createAttribute("NOM");
            nom.setValue(this.nom);
            type.setAttributeNode(nom);
            Attr numero = document.createAttribute("NUMERO");
            numero.setValue(String.valueOf(this.numero));
            type.setAttributeNode(numero);
            Attr temps = document.createAttribute("TEMPS");
            temps.setValue(String.valueOf(this.temps));
            type.setAttributeNode(temps);
            if(this.nom.equalsIgnoreCase("ENCHERETROIS")||(this.nom.equalsIgnoreCase("ENCHEREQUATRE")&&this.pas!=0))
            {
            Attr pas = document.createAttribute("PAS");
            pas.setValue(String.valueOf(this.pas));
            type.setAttributeNode(pas);
            }
            livre.addElement(type, this.type);
            if(this.nom.equalsIgnoreCase("ENCHERETROIS"))
            {
                Element encherelm =document.createElement("ENCHERE");
                type.appendChild(encherelm);
                encherelm.appendChild(document.createTextNode(String.valueOf(enchere)));
            
                if(!this.nomagent.equals(""))
                {
               
                    Element agentlm =document.createElement("AGENT");
                    Attr nomag = document.createAttribute("NOM");
                    nomag.setValue(this.nomagent);
                    agentlm.setAttributeNode(nomag);
                    type.appendChild(agentlm);
                }
            }
            else
            {
                if(this.nom.equalsIgnoreCase("ENCHEREQUATRE")||this.nom.equalsIgnoreCase("ENCHEREUN"))
                {
                    Element encherelm =document.createElement("ENCHERE");
                    type.appendChild(encherelm);
                    encherelm.appendChild(document.createTextNode(String.valueOf(enchere)));
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
    
    public static void main(String args[]) {
        float enchere=1546;int num=50;int temps=60;
        float pas=0;
        int i=12;
        float pr=153;
        float et= (float)0.4;
        Livre livre=new Livre("lupin", "leblanc", new Categorie(), "poch", "belin", pr, et,"15/11/00","yetet",i,"kamel",(float)0.0); 
        
        String p=new PropositionEnchereP("encherequatre",num,temps,pas,livre,enchere,"mouloud").toXML();
        System.out.println(p);
        Protocole message = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // D'après le tutorial JAXP, ces variables fixées à true permettent à
        // l'application de se concentrer sur l'analyse sémantique.
        factory.setCoalescing(true);
        factory.setExpandEntityReferences(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        try {
            // factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // La définition de ErrorHandler est inspirée de
            // http://java.sun.com/j2ee/1.4/docs/tutorial/doc/JAXPDOM3.html#wp64106
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                // ignore fatal errors (an exception is guaranteed)
                public void fatalError(SAXParseException exception) throws SAXException { }
                // treat validation errors as fatal
                public void error(SAXParseException e) throws SAXParseException { throw e; }
                // dump warnings too
                public void warning(SAXParseException err) throws SAXParseException {
                    System.out.println("** Warning"
                    + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId());
                    System.out.println("   " + err.getMessage());
                }
            }
            );
            Document document = builder.parse(new ByteArrayInputStream(p.getBytes("UTF-8")), Protocole.BASE_DTD);
            Element root = document.getDocumentElement();
            NodeList noeuds = root.getChildNodes();
            Element typeDOM = (Element)noeuds.item(0);
            Element typeDOME = (Element)typeDOM;
            p= new PropositionEnchereP(typeDOME).toXML();
            System.out.println(p);   
        } catch (Exception e) {
            System.err.print("Protocole: ");
            e.printStackTrace(System.err);
        }
    }
      
}
