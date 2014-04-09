/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2;

import Tool.IterateurSignal;
import Tool.SoundSignal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Thomas
 */
public class Hamming extends IterateurSignal {

    private final double[] w_hamming;
    private final double[] signal_hamming;

    public Hamming(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig) {
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig);
        w_hamming = fenetreHamming(wsize_ech);
        signal_hamming = new double[signal_tab.length];
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
            double d = signal_tab[debut_fenetre_ech + i] * w_hamming[i];
            signal_hamming[debut_fenetre_ech + i] += d;
        }
    }

    /**
     *
     * @return le signal d'entrée fenêtré avec Hamming
     */
    public short[] computeHammingSignal() {
        iterate();
        short[] new_sig = new short[signal_hamming.length];
        for (int i = 0; i < new_sig.length; i++) {
            new_sig[i] = (short) Math.round(signal_hamming[i] / 2.16);
        }
        return new_sig;
    }

    public static void main(String[] args) {
        SoundSignal ssignal = new SoundSignal();
        try {
            ssignal.setSignal("test_seg.wav");
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
        }
        short[] sig = ssignal.getSignal();

        Hamming tp2 = new Hamming(32, 8, 22050, sig);

        short[] modifHamming = tp2.computeHammingSignal();
        
//        for (int i = 0; i < sig.length; i++) {
//            if (sig[i] != modifHamming[i]) {
//                System.out.println("sig[" + i + "] + " + sig[i]);
//                System.out.println("modifHamming[" + i + "] + " + modifHamming[i]);
//                System.exit(1);
//            }
//        }
        
        ssignal.setSignal(modifHamming, 22050);

        try {
            ssignal.exportSignal("test_hamming.wav", true);
        } catch (IOException ex) {
            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
