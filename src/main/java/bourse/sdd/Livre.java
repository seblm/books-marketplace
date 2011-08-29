package bourse.sdd;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import bourse.protocole.Categorie;
import bourse.protocole.TypeMessage;

public class Livre {
    
    protected String titre;
    protected String auteur;
    protected Categorie categorie;
    protected String format;
    protected String editeur;
    protected float prix;
    protected float etat;
    protected String dateParu;
    protected String isbn;
    protected int id;
    protected String proprietaire;
    protected float prixAchat;

    /** Constructeur par recopie */
    public Livre(bourse.sdd.Livre livre){
        this.titre =livre.getTitre();
        this.auteur =livre.getAuteur();
        this.categorie =livre.getCategorie();
        this.format =livre.getFormat();
        this.editeur =livre.getEditeur();
        this.prix=livre.getPrix();
        this.etat =livre.getEtat();
        this.dateParu =livre.getDateParu();
        this.isbn =livre.getIsbn();
        this.id =livre.getId();
        this.proprietaire=livre.getProprietaire();
        this.prixAchat = livre.getPrixAchat();
    }
    
    /**
     * Constructeur par défaut.
     * @deprecated Ce constructeur fixe toutes les valeurs d'un livre à des valeurs arbitraires.
     * Elles ne reflètent pas l'existance d'un livre propre et l'instanciation d'un livre par
     * ce constructeur est par conséquent vivement déconseillé. */
    public Livre() {
        this("", "", new Categorie(), "", "", (float)0.0, (float)0.0, "", "", 0, "", (float)0.0);
    }
    
    /** Constructeur initialisant tous les paramètres d'un livre. */
    public Livre(String titre,String auteur,Categorie categorie,String format,String editeur,float prix,float etat,String dateParu,String isbn,int id, String proprietaire, float prixAchat) {
        this.titre =titre;
        this.auteur =auteur;
        this.categorie =categorie;
        this.format =format;
        this.editeur =editeur;
        this.prix=prix;
        this.etat =etat;
        this.dateParu =dateParu;
        this.isbn =isbn;
        this.id =id;
        this.proprietaire =proprietaire;
        this.prixAchat =prixAchat;
    }
    
    /** Constructeur initialisant livre à partir de l'élément DOM livre. */
    public Livre(Element livre) { setLivre(livre); }
    
    /** Rempli l'objet livre à l'aide du pointeur vers la strucuture du document
     * xml représentant l'élément livre. */
    private void setLivre(Element livre){
        this.titre=livre.getAttribute("TITRE");
        this.auteur=livre.getAttribute("AUTEUR");
        this.categorie=new Categorie(livre.getAttribute("CATEGORIE"));
        this.format=livre.getAttribute("FORMAT");
        this.editeur=livre.getAttribute("EDITEUR");
        this.isbn=livre.getAttribute("ISBN");
        try { this.prix=Float.parseFloat(livre.getAttribute("PRIX")); }
            catch (NumberFormatException e) { this.prix = (float)0.0; }
        try { this.etat=Float.parseFloat(livre.getAttribute("ETAT")); }
            catch (NumberFormatException e) { this.etat = (float)0.0; }
        this.dateParu=livre.getAttribute("DATEPAR");
        try { this.id=Integer.parseInt(livre.getAttribute("ID")); }
            catch (NumberFormatException e) { this.id = 0; }
        this.proprietaire=livre.getAttribute("PROPRIETAIRE");
        try { prixAchat = Float.parseFloat(livre.getAttribute("PRIXACHAT")); }
            catch (NumberFormatException e) { prixAchat = (float)0.0; }
    }
    
    /** Ajoute à la structure du document xml la balise livre et ses attributs,
     * conformément au type de message à envoyer.
     * @param typeMessage suivant ce type, la balise livre ne possèdera pas les
     * mêmes attributs. Ce type peut-être bourse.protocole.TypeMessage.TM_PROGRAMME,
     * TM_PROPOSITION_ENCHERE_P, TM_RESULTAT, TM_PROPOSE_VENTE ou TM_RESULT_PROPOSE_VENTE.
     */
    public void addElement(Node root, TypeMessage typeMessage){
        Document document=root.getOwnerDocument();
        try {
            Element livre = document.createElement("LIVRE");
            switch (typeMessage) {
                case PROPOSEVENTE :
                case RESULTPROPOSEVENTE :
                    // Attribut nécessaire : ID.
                    livre.setAttribute("ID", String.valueOf(id));
                    break;
                case PROPOSITIONENCHEREP :
                    // Attribut nécessaire supplémentaire : PROPRIETAIRE
                    
                case RESULTAT :
                    // Attributs nécessaires supplémentaires : AUTEUR, FORMAT, EDITEUR, DATEPAR, ID, ISBN
                    livre.setAttribute("PROPRIETAIRE", proprietaire);
                    livre.setAttribute("AUTEUR", auteur);
                    livre.setAttribute("FORMAT", format);
                    livre.setAttribute("EDITEUR", editeur);
                    livre.setAttribute("DATEPAR", dateParu);
                    livre.setAttribute("ID", String.valueOf(id));
                    livre.setAttribute("ISBN", isbn);
                case PROGRAMME :
                    // Attributs nécessaires : TITRE, CATEGORIE, PRIX, ETAT
                    livre.setAttribute("TITRE",  titre);
                    livre.setAttribute("CATEGORIE", categorie.toString());
                    livre.setAttribute("PRIX", String.valueOf(prix));
                    livre.setAttribute("ETAT", String.valueOf(etat));
                default :
                    // Si on ne vise aucun message particulier, on ne rend aucun attribut.
            }
            root.appendChild(livre);
            //changer en donnant le node pour rajouter à un document
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    public String getTitre(){
        return titre;}
    
    public String getAuteur(){
        return auteur;}
    
    public Categorie getCategorie(){
        return categorie;}
    
    public String getFormat(){
        return format;}
    
    public String getEditeur(){
        return editeur;}
    
    public float getPrix(){
        return this.prix;}
    
    public float getEtat(){
        return this.etat;}
    
    public String getDateParu(){
        return this.dateParu;}
    
     public String getIsbn(){
        return this.isbn;}
     
      public int getId(){
        return this.id;}

    public String getProprietaire() {
        return proprietaire; }
    
    public float getPrixAchat() {
        return prixAchat; }
    
    public void setTitre(String t){
        this.titre=t;}
    
    public void setAuteur(String a){
        this.auteur=a;}
    
    public void setCategorie(Categorie c){
        this.categorie=c;}
    
    public void setFormat(String f){
        this.format=f;}
    
    public void setEditeur(String e){
        this.editeur=e;}
    
    public void setIsbn(String i){
        this.isbn=i;}
    
    public void setDateParu(String d){
        this.dateParu=d;}
    
    public void setPrix(float p){
        this.prix=p;}
    
    public void setEtat(float e){
        this.etat=e;}
    
    public void setId(int id){
        this.id=id;}
    
    public void setProprietaire(String p) {
        this.proprietaire = p; }
    
    public void setPrixAchat(float p) {
        this.prixAchat = p; }
    
    /** Méthode d'affichage. le décalage sert à "indenter" l'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + ((auteur.length() == 0)?"":"Auteur = ") + this.auteur
           + ((categorie == null || categorie.getCode() == new Categorie(Categorie.AUCUNE).getCode())?"":", Catégorie = ") + this.categorie
           + ((dateParu.length() == 0)?"":", Date Parution = ") + this.dateParu
           + ((editeur.length() == 0)?"":", Editeur = ") + this.editeur
           + ((etat == (float)0.0)?"":", Etat = " + this.etat)
           + ((format.length() == 0)?"":", Format = ") + this.format
           + ((id == 0)?"":", Id = " + this.id)
           + ((isbn.length() == 0)?"":", Isbn = ") + this.isbn
           + ((prix == (float)0.0)?"":", Prix = " + this.prix)
           + ((titre.length() == 0)?"":", Titre = ") + this.titre
           + (proprietaire.equals("")?"":", Proprietaire = ") + proprietaire
           + ((prixAchat == (float)0.0)?"":", PrixAchat = " + prixAchat);
    }
    
    public String toString() {
        String affichage = "Livre n°" + id + " : " + titre + " d'une valeur de <b>" + Math.round(prix*etat) + " Euros</b>";
        if (proprietaire.length() != 0)
            affichage += ", détenu par " + proprietaire + "";
        if (prixAchat != (float)0.0)
            affichage += ", acheté " + prixAchat + "Euros";
        affichage += ".";
        return affichage;
    }
    public String toHtml() {
        String affichage = "Livre n°" + id + " : <i>" + titre + "</i> d'une valeur de <b>" + Math.round(prix*etat*100)/100 + " Euros</b>";
        if (proprietaire.length() != 0)
            affichage += ", d&eacute;tenu par <b>" + proprietaire + "</b>";
        if (prixAchat != (float)0.0)
            affichage += ", achet&eacute; <b>" + prixAchat + " Euros</b>";
        affichage += ".";
        return affichage;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == null || !(obj instanceof Livre)) {
    		return false;
    	}
    	final Livre livre = (Livre) obj;
    	return isbn == livre.isbn && id == livre.id;
    }
    
    @Override
    public int hashCode() {
    	int hash = 7;
    	hash = 31 * hash + (null == titre ? 0 : titre.hashCode());
    	hash = 31 * hash + (null == auteur ? 0 : auteur.hashCode());
    	hash = 31 * hash + (null == categorie ? 0 : categorie.hashCode());
    	hash = 31 * hash + (null == format ? 0 : format.hashCode());
    	hash = 31 * hash + (null == editeur ? 0 : editeur.hashCode());
    	hash = 31 * hash + new Float(prix).hashCode();
    	hash = 31 * hash + new Float(etat).hashCode();
    	hash = 31 * hash + (null == dateParu ? 0 : dateParu.hashCode());
    	hash = 31 * hash + (null == isbn ? 0 : isbn.hashCode());
    	hash = 31 * hash + id;
    	hash = 31 * hash + (null == proprietaire ? 0 : proprietaire.hashCode());
    	hash = 31 * hash + new Float(prixAchat).hashCode();
    	return hash;
    }

    public static void main(String args[]) {
        int i=12;
        float pr=153;
        float et= (float)0.4;
        Livre livr=new Livre("lupin", "leblanc", new Categorie(), "poch", "belin", pr, et,"15/11/00","yetet",i, "protocoleman", (float)50.95); 
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("MSG");
            document.appendChild(root);
            
          
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.out.println(livr.toString(0));
    }
}