/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2;

import Tool.IterateurSignal;
import Tool.SoundSignal;
import fft.FFT;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Thomas
 */
public class Fourier extends IterateurSignal {

//    private final double[] w_hamming;
    private final double[] signal_fft;
    private final int fft_order;
    
    
    public Fourier(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig){
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig);
//        w_hamming = fenetreHamming(wsize_ech);
        signal_fft = new double[signal_tab.length];
        fft_order = -666;
    }


    @Override
    public void toIterate(int debut_fenetre_ech, int fin_fenetre_ech, int indice_fenetre) {
//        for (int i = 0; i < wsize_ech; i++) {
//            double d = signal_tab[debut_fenetre_ech + i] * w_hamming[i];
//            signal_hamming[debut_fenetre_ech + i] += d;
//        }
        FFT fft = new FFT(fft_order);
        
    }

    /**
     * 
     * @return le signal d'entrée fenêtré avec Hamming 
     */
    public short[] computeFFTSignal(){
        iterate();
        short[] new_sig = new short[signal_fft.length];
//        for (int i = 0; i < new_sig.length; i++) {
//            new_sig[i] = (short) Math.round(signal_hamming[i] / 2.16);
//        }
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
        
        ssignal.setSignal(tp2.computeHammingSignal(), 22050);

        try {
            ssignal.exportSignal("test_toto.wav", true);
        } catch (IOException ex) {
            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
