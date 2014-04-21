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

    public Fourier(int wsize_seconde, int stepsize_seconde, int frequence_echantillonage, short[] sig_tab, boolean testing, double alpha, double beta, double gamma) {
        super(wsize_seconde, stepsize_seconde, frequence_echantillonage, sig_tab);
        fftOrder = 1024;
        testing_fft = testing;
        tmp = new double[sig_tab.length];
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
                // c'estla partie imaginaire pure
            }
        }

        /**
         * x est un tableau java, donc initialisé à zero par defaut, donc pas
         * besoin de rajouter des zero pour passer de 705 à 1024 échantillons
         */
        /**
         * on fait une copie du tableau de depart dans l'optique de faire des
         * tests dans la suite
         */
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

        /**
         * on vérifie que la fft + fft^-1 n'a pas modifié le signal d'origine
         */
        if (testing_fft) {
            for (int i = 0; i < fftOrder; i++) {
                if (Math.abs(x[i * 2] - y[i * 2]) > 0.001) {
                    /**
                     * cette condition ne pop plus, donc la fft se fait bien
                     * correctement, le bug vient surement après
                     */
                    System.err.println("x : " + x[i * 2]);
                    System.err.println("y : " + y[i * 2]);
                    System.exit(5);
                }
            }
        }

        // on reconstruit le signal en temporel
        for (int i = 0; i < wsize_ech; i++) {
            tmp[debut_fenetre_ech + i]/* +*/ = /*(short) Math.round(*/ x[i * 2]/*)*/;
        }

    }

    public short[] compute() {

        short[] sig_out = new short[signal_tab.length];
        iterate();
        for (int i = 0; i < tmp.length; i++) {
            sig_out[i] = (short) Math.round(tmp[i]/*/2.16*/);
        }

        /**
         * si on test, on vérifie que la fft+fftinverse n'a pas modifié le
         * signal d'entrée
         */
        if (testing_fft) {
            /**
             * pas de problème ici ...
             */
            System.out.print("testing if same sounds ... ");
            for (int i = 0; i < signal_tab.length; i++) {
                if (sig_out[i] != signal_tab[i]) {
                    System.err.println("sig_out[" + i + "] : " + sig_out[i]);
                    System.err.println("sig_tab[" + i + "] : " + signal_tab[i]);
                    System.exit(4);
                }
            }
            System.out.println("succeeded");
        }

        return sig_out;
    }

    public static void main(String[] args) {
        SoundSignal ssignal = new SoundSignal();
        String[] ss = {"signal_avec_bruit_0dB", "test_seg", "test_seg_bruit_0dB", "test_seg_bruit_10dB"};
        for (String s : ss) {
            System.out.println("processing : " + s);
            try {
                ssignal.setSignal("sons/" + s + ".wav");
            } catch (UnsupportedAudioFileException | IOException ex) {
                Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
            }
            short[] input = ssignal.getSignal();

            Hamming hamming = new Hamming(32, 8, 22050, input);

            short[] modifHamming = hamming.computeHammingSignal();

            try {
                // semble ok
                ssignal.exportSignal("sons/" + "results/" + s + "_hamming.wav", true);
            } catch (IOException ex) {
                Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
            }

            boolean testing_fft = true;

            Fourier fourier = new Fourier(32, 8, 22050, modifHamming, testing_fft, -666, -666, -666);

            short[] modifFFT = fourier.compute();

            if (testing_fft && input.length != modifFFT.length) {
                System.err.println("" + input.length + "!=" + modifFFT.length);
                System.exit(3);
            }

            ssignal.setSignal(modifFFT, 22050);

            try {
                /**
                 * @resolved problème le gain du nouveau signal semble plus
                 * elevé + changement des fréquences, la recontruction du signal
                 * se fait mal la fft se fait correctement mais c'est la
                 * reconstruition qui semble proser problème
                 *
                 * @todo new prob : seul le fichier signal_avec_bruit_0dB est
                 * accéléré de 1s
                 */
                ssignal.exportSignal("sons/" + "results/" + s + "_fourier.wav", true);
            } catch (IOException ex) {
                Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
