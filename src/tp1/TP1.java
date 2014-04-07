/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import Tool.SoundSignal;
import Tool.Graphe;
import Tool.IterateurSignal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author nikolai
 */
public class TP1 extends IterateurSignal {

    private static SoundSignal signal;
    private static ArrayList<Double> energies;

    public static void main(String[] args) {

        signal = new SoundSignal();
        try {
            signal.setSignal("test_seg.wav");
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
        }
        TP1 tp1 = new TP1(30, 8, 22050, signal.getSignal());
        tp1.iterate();
        Graphe g1 = new Graphe(energies, 8, "Energie du signal en fonction du temps", "");
        System.out.println("energies : " + energies);
        System.out.println("Length of the signal (in sample) : " + signal.getSignalLength());
        System.out.println("Sampling frequency : " + signal.getSamplingFrequency());
        System.out.println("Length of the signal (in ms) : " + signal.getSignalLength() * 1000 / signal.getSamplingFrequency());
    }

    public TP1(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig) {
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig);
        energies = new ArrayList<>();
    }

    @Override
    public void toIterate(int debut_fenetre_ech, int fin_fenetre_ech, int indice_fenetre) {
//        System.out.println("indice_f : "+indice_fenetre);
        energies.add(computeEnergy(debut_fenetre_ech, wsize_ech));
    }

//    public static double[] signalEnergy(int m,int N){
//        int signalLenghtMs = signal.getSignalLength() * 1000 / signal.getSamplingFrequency();
//        int nb_fenetre = signalLenghtMs/N;
////        System.out.println("nb_fenetre : "+nb_fenetre);
//        double[] nrjs = new double[nb_fenetre];
//        int taille_fenetre_tab = N * signal_tab.length / signalLenghtMs;
//        int taille_decalage_tab = m * signal_tab.length / signalLenghtMs;
//        int i_fenetre = 0;
//        int j = 0 ;
//        while(i_fenetre < nb_fenetre){
////            System.out.println(""+i_fenetre);
////            System.out.println("j : "+j);
//            int bornesup = taille_decalage_tab * i_fenetre + taille_fenetre_tab;
//            while(j < bornesup){
//                // on calcul l'énergie
//                nrjs[i_fenetre]=computeEnergy(j,i_fenetre);
//                j++;
//            }
//            i_fenetre++;
//            j = taille_decalage_tab * i_fenetre;
//        }
//        return nrjs;
//    }
    private double computeEnergy(int instant_echt, int N) {
        double somme = 0.;
        for (int k = 0; k < N; k++) {
            somme += Math.pow(signal_tab[instant_echt + k], 2);
        }
        somme *= (1. / (N + 1.));

//        System.out.println("somme : "+somme);
        return somme;
    }
//    
//    private double[] signalZeroRate(int m, int N){
//        int signalLenghtMs = signal.getSignalLength() * 1000 / signal.getSamplingFrequency();
//        int nb_fenetre = signalLenghtMs/N;
////        System.out.println("nb_fenetre : "+nb_fenetre);
//        double[] nrjs = new double[nb_fenetre];
//        int taille_fenetre_tab = N * signal_tab.length / signalLenghtMs;
//        int taille_decalage_tab = m * signal_tab.length / signalLenghtMs;
//        int i_fenetre = 0;
//        int j = 0 ;
//        while(i_fenetre < nb_fenetre){
////            System.out.println(""+i_fenetre);
////            System.out.println("j : "+j);
//            int bornesup = taille_decalage_tab * i_fenetre + taille_fenetre_tab;
//            while(j < bornesup){
//                // on calcul l'énergie
//                nrjs[i_fenetre]=zeroRate(j,i_fenetre);
//                j++;
//            }
//            i_fenetre++;
//            j = taille_decalage_tab * i_fenetre;
//        }
//        return nrjs;        
//    }
//    private static double zeroRate(int n, int nw){
//        double somme = 0;
//        for (int k = -nw/2; k < nw/2; k++) {
//            somme += Math.abs(Math.signum(signal_tab[n+k]) - Math.signum(signal_tab[n+k-1]));
//        }
//        somme *= (1./(2*nw));
//        
//        return somme;
//        
//    }

}
