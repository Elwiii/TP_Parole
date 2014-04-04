/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tool;

/**
 *
 * @author Nikolai
 */
public class IterateurSignal {

    protected short[] signal_tab;
    protected int wsize_seconde;
    protected int stepsize_seconde;
    protected int frequence_echantillonage;
    protected int wsize_ech ;
    protected int stepsize_ech;
    
    public IterateurSignal(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage) {
        this.wsize_seconde = wsize_seconde;
        this.stepsize_seconde = stepsize_seconde;
        this.frequence_echantillonage = frequence_echantillonage;
        wsize_ech = wsize_seconde * frequence_echantillonage / 1000;
        stepsize_ech = stepsize_seconde * frequence_echantillonage / 1000;
    }
    
    
    public int iterate() {
        int debut_fenetre_ech = 0;
        int fin_fenetre_ech = wsize_ech;
        int indice_fenetre = 0;
        while (fin_fenetre_ech < signal_tab.length) {
            indice_fenetre++;
            System.out.println("debut ech : "+debut_fenetre_ech+ " / fin ech : "+fin_fenetre_ech);
//            for (int i = debut_fenetre_ech; i < fin_fenetre_ech; i++) {
                toIterate(debut_fenetre_ech, fin_fenetre_ech, indice_fenetre);
//            }
            debut_fenetre_ech += stepsize_ech;
            fin_fenetre_ech = debut_fenetre_ech + wsize_ech;
        }
        return indice_fenetre;
    }

    public void toIterate(int debut_fenetre_ech,int fin_fenetre_ech,int indice_fenetre){
        // à override
    }

}
