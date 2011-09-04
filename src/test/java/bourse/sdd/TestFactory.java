package bourse.sdd;

import bourse.protocole.Categorie;

public class TestFactory {

    public static Livre createLivre() {
        return new Livre("lupin", "leblanc", new Categorie(Categorie.SCIENCE), "poch", "belin", 153f, 0.4f, "15/11/00",
                "yetet", 12, "protocoleman", 50.95f);
    }

}
