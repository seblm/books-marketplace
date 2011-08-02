package bourse.protocole;

public class TypeMessage {
                
    public static final int TM_INCONNU = 0;
    public static final int TM_WELCOME = 1;
    public static final int TM_RESULT_WELCOME = 2;
    public static final int TM_BYE = 3;
    public static final int TM_ERREUR =4 ;
    public static final int TM_REQUETE_PROGRAMME =5 ;
    public static final int TM_RESULT_PROPOSE_VENTE = 6;
    public static final int TM_PROPOSE_VENTE =7;
    public static final int TM_PROPOSITION_ENCHERE_A =8;
    public static final int TM_RESULTAT = 9;
    public static final int TM_RESULT_BYE = 10;
    public static final int TM_WELCOME_AGENT = 11;
    public static final int TM_PROPOSITION_ENCHERE_P =12;
    public static final int TM_PROPOSITION_ENCHERE =13;
    public static final int TM_PROGRAMME =14 ;
    public static final int TM_REQUETE_AGENTS =15 ;
    public static final int TM_RESULT_AGENTS =16 ;
    public static final int TM_ADMIN =17;
    private static final String[] message = {"INCONNU", "WELCOME", "RESULTWELCOME", "BYE","ERREUR","REQUETEPROGRAMME","RESULTPROPOSEVENTE","PROPOSEVENTE","PROPOSITIONENCHEREA","RESULTAT","RESULTBYE","WELCOMEAGENT","PROPOSITIONENCHEREP","PROPOSITIONENCHERE","PROGRAMME","REQUETEAGENTS","RESULTAGENTS","ADMIN"};

    private int type;

    public TypeMessage(int type) { this.type = type; }
    
    public TypeMessage(String type) {
        boolean trouve = false;
        int i = 0;
        while (!trouve && i < message.length) {
            i++;
            trouve = message[i].equals(type);
        }
        this.type = i;
    }
    
    public int getValue() {
    	return this.type;
    }
    
    @Override
    public String toString() {
    	return message[this.type];
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == null) {
    		return type < 1 || type > 17;
    	}
    	if (obj instanceof Integer) {
    		return type == (Integer) obj;
    	}
    	if (obj instanceof TypeMessage) {
    		return type == ((TypeMessage) obj).type;
    	}
    	return false;
    }
    
}