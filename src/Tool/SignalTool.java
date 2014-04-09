/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tool;

/**
 *
 * @author Nikolai
 */
public class SignalTool {

    /**
     * calcul le spectre d'amplitude d'un signal dans le domaine spectral
     *
     * @param x_fourier
     * @param fftorder
     * @return
     */
    public static double[] spectreamplitude(double[] x_fourier, int fftorder) {
        //la partie réelle : x_fourier[i*2]
        //la partie imaginaire : x_fourier[i*2+1]
        double[] spectre_amplitude = new double[fftorder];
        for (int i = 0; i < spectre_amplitude.length; i++) {
            double re = x_fourier[i * 2];
            double im = x_fourier[i * 2 + 1];
            spectre_amplitude[i] = Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
        }
        return spectre_amplitude;
    }

    /**
     * calcul le spectre de phase d'un signal dans le domaine spectral
     *
     * @param x_fourier
     * @param fftorder
     * @return
     */
    public static double[] spectrephase(double[] x_fourier, int fftorder) {
        double[] spectre_phase = new double[fftorder];
        for (int i = 0; i < spectre_phase.length; i++) {
            double re = x_fourier[i * 2];
            double im = x_fourier[i * 2 + 1];
            spectre_phase[i] = Math.atan2(im, re);
        }
        return spectre_phase;
    }

    /**
     * reconstruit le spectre avec son spectre d'amplitude et sa phase. Le
     * tableau retourné a pour taille 2*fftorder (x[2i] pour la partie réelle et
     * x[2i+1] pour la partie imaginaire)
     *
     * @param spectre_amplitude
     * @param spectre_phase
     * @param fftorder
     * @return
     */
    public static double[] spectrereconstruction(double[] spectre_amplitude, double[] spectre_phase, int fftorder) {
        double[] spectre = new double[2 * fftorder];
        for (int i = 0; i < fftorder; i++) {
            spectre[i] = spectre_amplitude[i] * Math.cos(spectre_phase[i]);
            spectre[i + 1] = spectre_amplitude[i] * Math.sin(spectre_phase[i]);
        }
        return spectre;
    }

    /**
     * Question 9 on soustrait au spectre d’amplitude le spectre de l’estimation
     * du bruit.
     *
     * @param spectre_amplitude
     * @param spectre_bruit
     * @param fftorder
     * @param alpha
     * @param beta
     * @param gamma
     * @return
     */
    public static double[] soustractionspetrale(double[] spectre_amplitude, double[] spectre_bruit, int fftorder, double alpha, double beta, double gamma) {
        double[] soustration = new double[fftorder];
        for (int k = 0; k < fftorder; k++) {
            double value = Math.pow(Math.pow(spectre_amplitude[k], alpha) - beta * Math.pow(spectre_bruit[k], alpha), 1. / alpha);
            soustration[k] = Math.abs(value) < 0.0001 ? gamma * spectre_bruit[k] : value;
        }
        return soustration;
    }

}
