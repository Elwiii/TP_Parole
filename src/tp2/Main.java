/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2;

import Tool.SoundSignal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Nikolai
 */
public class Main {

    public static void main(String[] args) {

        final int[] alphas = {2, 4};
        final int[] betas = {1, 10, 30};
        final int[] gammas = {0, 1, 3};
        final String[] ss = {"signal_avec_bruit_0dB", "test_seg", "test_seg_bruit_0dB", "test_seg_bruit_10dB"};
        
        for (String s : ss) {
            for (int alpha : alphas) {
                for (int beta : betas) {
                    for (int gamma : gammas) {
                        System.out.println("processing : " + s + " with parameters alpha " + alpha + " beta " + beta + " gamma " + gamma);
                        SoundSignal ssignal = new SoundSignal();
                        try {
                            ssignal.setSignal("sons/" + s + ".wav");
                        } catch (UnsupportedAudioFileException | IOException ex) {
                            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        short[] sig = ssignal.getSignal();

                        Hamming hamming = new Hamming(32, 8, 22050, sig);

                        short[] modifHamming = hamming.computeHammingSignal();

                        Fourier fourier = new Fourier(32, 8, 22050, modifHamming, false, alpha, beta, gamma);

                        short[] modifFFT = fourier.compute();

                        ssignal.setSignal(modifFFT, 22050);

                        try {
                            ssignal.exportSignal("sons/results/main/" + "soustraction_" + s + "_" + alpha + "_" + beta + "_" + gamma + ".wav", true);
                        } catch (IOException ex) {
                            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        
        System.out.println("2 30 et 0 semble être de bonnes valeurs pour alpha beta et gamma");
    }
}
