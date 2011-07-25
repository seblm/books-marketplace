package bourse.protocole;

/** Répertorie toutes les catégories du protocole. */
public class Categorie {
    
    // Variables d'instance

    // Constantes utilisées pour l'échange et le stockage des catégories.
    public static final String SF = "Science fiction";
    public static final String BD = "Bandes Dessinées";
    public static final String SCIENCE = "Science";
    public static final String ROMAN = "Romans Policiers";
    public static final String INFO = "Informatique";
    public static final String AUCUNE = "Aucune";
    // Constantes utilisées pour le stockage dans la Base de données.
    private static final String _SF = "Science fiction";
    private static final String _SCIENCE = "Science";
    private static final String _ROMAN = "Romans Policiers";
    private static final String _BD = "Bandes dessinées";
    private static final String _INFO = "Informatique";
    /** Classe utilisée pour génerer des catégories aléatoires */
    private static final java.util.Random generateurAleatoire = new java.util.Random();
    
    /** La catégorie courante */
    private int categorie;
    
    // Constructeurs.
    /** Construit une catégorie au hasard. */
    public Categorie() { this.categorie = generateurAleatoire.nextInt(5); }
    /** Construit une catégorie à partir de son code. */
    public Categorie(int c) { this.categorie = c; }
    /** Construit une catégorie à partir d'une string. */
    public Categorie(String c) {
        if ((c.startsWith("s") || c.startsWith("S")) && c.length() > 11) this.categorie = 0;
        else if (c.startsWith("b") || c.startsWith("B")) this.categorie = 1;
        else if ((c.startsWith("s") || c.startsWith("S")) && c.length() < 11) this.categorie = 2;
        else if (c.startsWith("r") || c.startsWith("R")) this.categorie = 3;
        else if (c.startsWith("i") || c.startsWith("I")) this.categorie = 4;
        else if (c.equalsIgnoreCase(AUCUNE)) this.categorie = 5;
        else this.categorie = 5;
    }
    /** Construit une catégorie à partir d'une autre catégorie (constructeur par
     * recopie. */
    public Categorie(Categorie c) {
        this.categorie = c.getCode();
    }
    /** Cette méthode génère une catégorie à partir d'une chaîne stocquée dans la
     * base de données, donc libérée de tout caractère folklorique et autre URLEncode. */
    public static Categorie newCategorieFromBd(String c) {
        if (c.equalsIgnoreCase(_SF)) return new Categorie(SF);
        else if (c.equalsIgnoreCase(_SCIENCE)) return new Categorie(SCIENCE);
        else if (c.equalsIgnoreCase(_ROMAN)) return new Categorie(ROMAN);
        else if (c.equalsIgnoreCase(_BD)) return new Categorie(BD);
        else if (c.equalsIgnoreCase(_INFO)) return new Categorie(INFO);
        else return new Categorie(AUCUNE);
    }
        
    /** Méthodes. */
    /** Accéder à la catégorie. */
    public String getCategorie() {
        String output;
        switch (this.categorie) {
            case 0: output = SF; break;
            case 1: output = BD; break;
            case 2: output = SCIENCE; break;
            case 3: output = ROMAN; break;
            case 4: output = INFO; break;
            case 5: output = AUCUNE; break;
            default: output = AUCUNE; break;
        } return output;
    }
    /** Modifier la catégorie en donnant une string. */
    public void setCategorie(String categorie) { this.categorie = new Categorie(categorie).getCode();} 
    /** Accéder au code de la catégorie. */
    public int getCode() { return this.categorie; }
    /** Modifier la catégorie en donnant son code. */
    public void setCategorie(int categorie) { this.categorie = categorie; }
    /** Méthode de comparaison standard */
    public boolean equals(Object o) {
        if (o instanceof Categorie)
            return ((Categorie)o).getCode() == this.categorie;
        else
            return false;
    }
    /** Méthode d'affichage. */
    public String toString(int decalage) {
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        return delta + this.getCode() + " (" + this.getCategorie() + ")"; }
    /** Méthode d'affichage par défaut. */
    public String toString() {
        return getCategorie();
    }
    /** Méthode publique. */
    public static void main(String argc[]) {
        Categorie c = new Categorie();
        System.out.println(c.getCategorie());
        c.setCategorie(INFO);
        System.out.println(c.toString(0));
        System.out.println(c.getCategorie());
        c.setCategorie(2);
        System.out.println(c.toString(0));
        c = Categorie.newCategorieFromBd("Science fiction");
        System.out.println(c.toString(0));
        c = Categorie.newCategorieFromBd("Bandes dessinées");
        System.out.println(c.toString(0));
        c = Categorie.newCategorieFromBd("Science");
        System.out.println(c.toString(0));
        c = Categorie.newCategorieFromBd("Romans Policiers");
        System.out.println(c.toString(0));
        c = Categorie.newCategorieFromBd("Informatique");
        System.out.println(c.toString(0));
    }
}
