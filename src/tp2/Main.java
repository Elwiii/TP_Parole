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
 * @todo tofinish thomas (tester avec différents alpha/beta/gamma
 * @author Nikolai
 */
public class Main {
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

        Fourier fourier = new Fourier(32, 8, 22050, modifHamming, false,2,1,0);

        short[] modifFFT = fourier.compute();

        ssignal.setSignal(modifFFT, 22050);

        try {
            ssignal.exportSignal("soustraction.wav", true);
        } catch (IOException ex) {
            Logger.getLogger(Hamming.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
