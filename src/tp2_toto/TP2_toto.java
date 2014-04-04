/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp2_toto;

import Tool.IterateurSignal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import tp1.SoundSignal;
import tp1.TP1;

/**
 *
 * @author Thomas
 */
public class TP2_toto extends IterateurSignal {
    
    public static SoundSignal ssignal;
    private static short[] signal_tab;
    private static ArrayList<Double> fenetreHamming;
    
    public TP2_toto(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage) {
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage);
        ssignal = new Tool.SoundSignal();
        ssignal.setSignal("test_seg.wav");
        signal_tab = ssignal.getSignal();
    }
    
    private static double[] fenetreHamming(int size){
        double c1 = (double) 0.54 ; 
        double c2 = (double) 0.46 ; 
        double[] hamming = new double[size] ; 
        for (int i = 0 ; i < size ; i++){ 
            hamming[i] = c1 - (double) (c2 * Math.cos((double) 2 * Math.PI * i / (size-1))) ; 
        }
        return hamming;
    }
    
    private static double[] fenetrageHamming(int start, int size){
        double[] hamming = fenetreHamming(size);
        double[] signal_fen = new double[size];
        for (int i = 0; i < size; i++) {
            signal_fen[i]= signal_tab[start + i]*hamming[i];
        }
        return signal_fen;
    }
    
    @Override
    public void toIterate(int debut_fenetre_ech, int fin_fenetre_ech, int indice_fenetre) {
        //nrjs[i_fenetre]=computeEnergy(j,i_fenetre);
        fenetreHamming.add(fenetrageHamming(debut_fenetre_ech,wsize_ech ));
    }
    public static void main(String[] args){
        TP2_toto tp2 = new TP2_toto(30, 8, 22050);
        tp2.iterate();
        try {
            
            ssignal = new SoundSignal();
            ssignal.setSignal("test_seg.wav");
            signal_tab = ssignal.getSignal();
            double[] dsignal_modif = fenetrageHamming(176,780);
            /* Cast en short */
            short[] signal_modif = new short[dsignal_modif.length];
            for (int i = 0; i < signal_modif.length; i++) {
                signal_modif[i] = (short)dsignal_modif[i];
            }
            ssignal.setSignal(signal_modif, 22080);
            ssignal.exportSignal("test_toto.wav",true);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TP1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
