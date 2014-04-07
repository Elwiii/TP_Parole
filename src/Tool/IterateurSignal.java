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
    protected int wsize_ech;
    protected int stepsize_ech;
    
//    protected int size_ech;
//    protected int size_seconde;

    public IterateurSignal(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage,short[] signal_tab/*,int size_seconde*/) {
//        this.size_seconde = size_seconde;
        this.signal_tab = signal_tab;
        this.wsize_seconde = wsize_seconde;
        this.stepsize_seconde = stepsize_seconde;
        this.frequence_echantillonage = frequence_echantillonage;
        wsize_ech = wsize_seconde * frequence_echantillonage / 1000;
        stepsize_ech = stepsize_seconde * frequence_echantillonage / 1000;
//        size_ech = frequence_echantillonage * size_seconde;
//        size_ech = signal_tab.length;
    }

    
    /**
     * appelle la fonction toIterate à chaque fenêtre du signal
     * renvoi le nombre de fenêtre du signal
     * @return 
     */
    public int iterate() {
        int debut_fenetre_ech = 0;
        int fin_fenetre_ech = wsize_ech;
        int indice_fenetre = 0;
        while (fin_fenetre_ech < signal_tab.length) {
            indice_fenetre++;
            toIterate(debut_fenetre_ech, fin_fenetre_ech, indice_fenetre);
            debut_fenetre_ech += stepsize_ech;
            fin_fenetre_ech = debut_fenetre_ech + wsize_ech;
        }
        return indice_fenetre;
    }

    /**
     * fonction à override fonction appellée au parcour de chaque fênetre du
     * signal
     *
     * @param debut_fenetre_ech
     * @param fin_fenetre_ech
     * @param indice_fenetre
     */
    public void toIterate(int debut_fenetre_ech, int fin_fenetre_ech, int indice_fenetre) {
        // à override
    }

}
