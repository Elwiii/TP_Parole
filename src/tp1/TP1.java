/*

* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;

import Graphe.*;
/**
 *
 * @author nikolai
 */
public class TP1 {

    private static short[] signal_tab;
    private static SoundSignal signal;
    
    public static void main(String[] args){
        try {
            
            signal = new SoundSignal();
            signal.setSignal("test_seg.wav");
            signal_tab = signal.getSignal();
            double[] energies = signalEnergy(8, 30);
            for (int i = 0; i < energies.length; i++) {
                System.out.println(i+" : "+energies[i]);
                
            }
            
            Graphe g1 = new Graphe(energies, 8, "Energies", "");
            System.out.println("energies : "+energies);
            System.out.println("Length of the signal (in sample) : " + signal.getSignalLength());
            System.out.println("Sampling frequency : " + signal.getSamplingFrequency());
            System.out.println("Length of the signal (in ms) : " + signal.getSignalLength() * 1000 / signal.getSamplingFrequency());        
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static double[] signalEnergy(int m,int N){
        int signalLenghtMs = signal.getSignalLength() * 1000 / signal.getSamplingFrequency();
        int nb_fenetre = signalLenghtMs/N;
//        System.out.println("nb_fenetre : "+nb_fenetre);
        double[] nrjs = new double[nb_fenetre];
        int taille_fenetre_tab = N * signal_tab.length / signalLenghtMs;
        int taille_decalage_tab = m * signal_tab.length / signalLenghtMs;
        int i_fenetre = 0;
        int j = 0 ;
        while(i_fenetre < nb_fenetre){
//            System.out.println(""+i_fenetre);
//            System.out.println("j : "+j);
            int bornesup = taille_decalage_tab * i_fenetre + taille_fenetre_tab;
            while(j < bornesup){
                // on calcul l'énergie
                nrjs[i_fenetre]=computeEnergy(j,i_fenetre);
                j++;
            }
            i_fenetre++;
            j = taille_decalage_tab * i_fenetre;
        }
        return nrjs;
    }


    private static double computeEnergy(int n, int nw){
        double somme = 0.;
        for (int k = -nw/2; k < nw/2; k++) {
            somme += Math.pow(signal_tab[n+k],2);
        }
        somme *= (1./(nw+1.));
        
//        System.out.println("somme : "+somme);
        return somme;
    }
}
