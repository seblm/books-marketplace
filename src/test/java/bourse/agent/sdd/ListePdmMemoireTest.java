package bourse.agent.sdd;

import org.junit.Before;
import org.junit.Test;

import bourse.agent.Visualisation;

public class ListePdmMemoireTest {
	
	private PdmMemoire[] pdmMemoires;
	
	private ListePdmMemoire listePdmMemoire;
	
	@Before
	public void initialisation() {
		pdmMemoires = new PdmMemoire[] {
			new PdmMemoire("p1", "HOME", false, false, new ListeProgramme(), 1),
		    new PdmMemoire("p2", "192.168.1.2:80", true, true, new ListeProgramme(), 2),
		    new PdmMemoire("p3", "0.0.0.0:0", false, false, new ListeProgramme(), 2),
		};
		Visualisation visu = new Visualisation();
		visu.setVisible(true);
		listePdmMemoire = new ListePdmMemoire(visu);
	}

	@Test
	public void testListePdmMemoire() {
         
        System.out.println("initialisation : ");
        listePdmMemoire.ajouter(pdmMemoires[0]);
        listePdmMemoire.ajouter(pdmMemoires[1]);
        listePdmMemoire.ajouter(pdmMemoires[2]);
        System.out.println(listePdmMemoire.toString(3));
        
        System.out.println("Aucune active ? " + listePdmMemoire.aucuneActive());
        
        if (listePdmMemoire.premiereActiveNonVisitee() != null)
            System.out.println("Première active non visitée : " + listePdmMemoire.premiereActiveNonVisitee().toString(4));
        if (listePdmMemoire.premiereActiveVisitee() != null)
            System.out.println("Première active visitée : " + listePdmMemoire.premiereActiveVisitee().toString(4));
        
        System.out.println("mises à jour : ");
        Pdm pdm1 = new Pdm("p1", "192.168.1.1:8080"); // p1 toujours active
        Pdm pdm2 = new Pdm("p2", "192.168.1.3:80"); // p2 a changé d'adresse
        // p3 déconnectée.
        Pdm pdm4 = new Pdm("p4", "192.168.1.5:2726"); // p4 nouvelle arrivée.
        ListePdm p = new ListePdm(); p.ajouter(pdm1); p.ajouter(pdm2); p.ajouter(pdm4);
        listePdmMemoire.miseAJour(p);
        System.out.println(listePdmMemoire.toString(5));
        
        System.out.println("mises à jour : ");
        pdm1 = new Pdm("p1", "192.168.1.52:8080"); // p1 changé d'adresse
        // p2 déconnectée.
        pdm4 = new Pdm("p4", "192.168.1.5:2726"); // p4 nouvelle arrivée.
        p = new ListePdm(); p.ajouter(pdm1); p.ajouter(pdm4);
        listePdmMemoire.miseAJour(p);        
	}

}
