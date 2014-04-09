/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2;

import Tool.IterateurSignal;
import Tool.SignalTool;
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

    private static final int NB_SPECTRE_BRUIT = 5;
    private double[] x;
    private final int fftOrder;
    private final boolean testing_fft;
    private final double[] tmp;
    private final double[] bruit_amp;
    private final double alpha;
    private final double beta;
    private final double gamma;

    public Fourier(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig, boolean testing, double alpha, double beta, double gamma) {
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig);
        fftOrder = 1024;
        testing_fft = testing;
        tmp = new double[sig.length];
        bruit_amp = new double[fftOrder];
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
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

        // transformer de fourier de la fenêtre
        fft.transform(x, false);

        // traitement dans le domaine spectral
        if (!testing_fft) {
            double[] spectreamplitude = SignalTool.spectreamplitude(x, fftOrder);
            double[] spectrephase = SignalTool.spectrephase(x, fftOrder);
            // transformer le spectre
            // calcul du bruit (question 8)
            if (indice_fenetre <= NB_SPECTRE_BRUIT) {
                int div = indice_fenetre == NB_SPECTRE_BRUIT ? NB_SPECTRE_BRUIT : 1;
                for (int i = 0; i < bruit_amp.length; i++) {
                    bruit_amp[i] = (bruit_amp[i] + spectreamplitude[i]) / div;
                }
            }
            // on applique la soustraction spectral
            spectreamplitude = SignalTool.soustractionspetrale(spectreamplitude, bruit_amp, fftOrder, alpha, beta, gamma);
            // on reconstruit le spectre
            x = SignalTool.spectrereconstruction(spectreamplitude, spectrephase, fftOrder);
        } else {
            //nothing to do
        }

        // transformé inverse de la fenêtre modifiée
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

        boolean testing = true;

        Fourier test = new Fourier(32, 8, 22050, modifHamming, /*testing*/false,2,1,0);

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
