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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Thomas
 */
public class Fourier extends IterateurSignal {

    private double[] x;
    private final int fftOrder;
    private final boolean testing_fft;
    private final double[] tmp;

    public Fourier(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig, boolean testing) {
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig);
        fftOrder = 1024;
        testing_fft = testing;
        tmp = new double[sig.length];
    }

    @Override
    public void toIterate(int debut_fenetre_ech, int fin_fenetre_ech, int indice_fenetre) {
        FFT fft = new FFT(fftOrder);

        x = new double[fftOrder * 2];

        for (int i = 0; i < fftOrder; i++) {
            if ((debut_fenetre_ech + i) < signal_tab.length) {
                x[i * 2] = signal_tab[debut_fenetre_ech + i];
            } else {
                // nothing to do, on laisse à zero
            }
        }

        double[] y = null;
        if (testing_fft) {
            y = Arrays.copyOf(x, x.length);
        }

        fft.transform(x, false);

        if (!testing_fft) {
            // transformer le spectre
        } else {
            //nothing to do
        }

        fft.transform(x, true);

        // on reconstruit le signal en temporel
        for (int i = 0; i < wsize_ech; i++) {
            if (testing_fft) {
                if (Math.abs(x[i * 2] - y[i * 2]) > 0.001) {
                    System.out.println("x : " + x[i * 2]);
                    System.out.println("y : " + y[i * 2]);
                    System.exit(2);
                }
            }
            tmp[debut_fenetre_ech + i] += (short) Math.round(x[i * 2]);
        }

    }

    public short[] compute() {
        short[] sig_out = new short[signal_tab.length];
        iterate();
        for (int i = 0; i < tmp.length; i++) {
            sig_out[i] = (short) Math.round(tmp[i] / 2.16);
        }
        return sig_out;
    }

    public static void main(String[] args) {
        SoundSignal ssignal = new SoundSignal();
        try {
            ssignal.setSignal("test_seg.wav");
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
        }
        short[] sig = ssignal.getSignal();

        Hamming hamming = new Hamming(32, 8, 22050, sig);

        short[] modifHamming = hamming.computeHammingSignal();

        Fourier test = new Fourier(32, 8, 22050, modifHamming, true);

        short[] modifFFT = test.compute();

        ssignal.setSignal(modifFFT, 22050);

        try {
            // todo problème le gain du nouveau signal semble plus elevé
            ssignal.exportSignal("test_fourrier.wav", true);
        } catch (IOException ex) {
            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
