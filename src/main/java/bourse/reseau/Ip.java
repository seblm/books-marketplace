package bourse.reseau;

/** Stocke une ip sous la forme d'un tableau de 4 entiers. */
public class Ip {
    
    /** Variables d'instance. */
    /** Le tableau d'entier représentant l'ip. */
    private int[] adresse = new int[4];
    /** Le port de communication. */
    private int port = 0;
    
    /** Constructeurs. */
    /** Constructeur par défaut, renvoie à HOME. */
    public Ip() { for (int i=0; i<4; i++) this.adresse[i] = 0; }
    /** Constructeur explicite. */
    public Ip(int a, int b, int c, int d, int port) {
        this.adresse[0] = a; this.adresse[1] = b; this.adresse[2] = c; this.adresse[3] = d;
        this.port = port;
    }
    /** Constructeur à partir d'une String. */
    public Ip(String ip) {
        java.util.StringTokenizer st;
        try {
            st = new java.util.StringTokenizer(ip, ":");
            String adresse = st.nextToken();
            this.port = Integer.parseInt(st.nextToken());
            st = new java.util.StringTokenizer(adresse, ".");
            for (int i=0; i<4; i++) this.adresse[i] = Integer.parseInt(st.nextToken());
        } catch (Exception e) { }
    }
    
    /** Méthodes. */
    /** L'ip en string. */
    public int[] getIp() { return this.adresse; }
    /** Retourne le port de communication. */
    public int getPort() { return this.port; }
    /** Retourne l'ip en String de la forme : "X.X.X.X". */
    public String ipToString() {
        String output;
        output = String.valueOf(this.adresse[0]);
        for (int i=1; i<4; i++) { output += "." + this.adresse[i]; }
        return output;
    }
    /** Méthode d'affichage brute de la forme : "X.X.X.X:YYYY". */
    public String toString() { return this.ipToString() + ":" + this.port; }
    /** Méthode d'affichage de la forme : "ip = X.X.X.X, port = YYYY". */
    public String toString(int decalage) { 
        String delta = "";
        for (int i=0; i<decalage; i++) delta += " ";
        String output = delta + "ip = " + this.ipToString();
        output += ", port = " + this.port;
        return output;
    }
    public static void main(String argc[]) {
        Ip i = new Ip(192, 168, 1, 1, 8080);
        System.out.println(i.toString(0));
        i = new Ip();
        System.out.println(i.toString(0));
        i = new Ip("HOME");
        System.out.println(i.toString(0));  
        i = new Ip("192.168.1.2:8080");
        System.out.println(i.toString(5)); 
        int[] adresse = i.getIp();
        for (int j=0; j<4; j++) System.out.println("adresse[" + j + "] = " + adresse[j]); 
    }
}

