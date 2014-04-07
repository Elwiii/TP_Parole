/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2;

import Tool.IterateurSignal;
import Tool.SoundSignal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import tp1.TP1;

/**
 *
 * @author Thomas
 */
public class TP2 extends IterateurSignal {

    private static SoundSignal ssignal;
//    private static short[] signal_tab;
    private static ArrayList<Double> signal_fenetre;

    private final double[] hamming;
    private static double[] signal_modif;

    public TP2(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig){
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig);
        signal_fenetre = new ArrayList<>();
        hamming = fenetreHamming(wsize_ech);
        signal_modif = new double[signal_tab.length];
    }

    private static double[] fenetreHamming(int size) {
        double c1 = (double) 0.54;
        double c2 = (double) 0.46;
        double[] hamming = new double[size];
        for (int i = 0; i < size; i++) {
            hamming[i] = c1 - (double) (c2 * Math.cos((double) 2 * Math.PI * i / (size - 1)));
        }
        return hamming;
    }

    @Override
    public void toIterate(int debut_fenetre_ech, int fin_fenetre_ech, int indice_fenetre) {
        for (int i = 0; i < wsize_ech; i++) {
            double d = signal_tab[debut_fenetre_ech + i] * hamming[i];
            signal_modif[debut_fenetre_ech + i] += d;
        }
    }

    public static void main(String[] args) {
        ssignal = new SoundSignal();
        try {
            ssignal.setSignal("test_seg.wav");
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(TP2.class.getName()).log(Level.SEVERE, null, ex);
        }
        short[] sig = ssignal.getSignal();

        TP2 tp2 = new TP2(32, 8, 22050, sig);
        tp2.iterate();

        short[] new_sig = new short[signal_modif.length];
        for (int i = 0; i < new_sig.length; i++) {
            new_sig[i] = (short) Math.round(signal_modif[i] / 2.16);
        }
        ssignal.setSignal(new_sig, 22050);
        try {
            ssignal.exportSignal("test_toto.wav", true);
        } catch (IOException ex) {
            Logger.getLogger(TP2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
