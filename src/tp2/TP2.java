package tp2;

import Tool.IterateurSignal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import Tool.SoundSignal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikolai
 */
public class TP2 extends IterateurSignal{

    public TP2(){
        try {
            SoundSignal signal = new SoundSignal();
            signal.setSignal("test_seg.wav");
            signal_tab = signal.getSignal();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(TP2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TP2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    
    
    public static void main(String[] args){
        TP2 tp2 = new TP2();
        System.out.println("nombre_fenetre : "+tp2.iterate(30, 8, 22050));
    }
}
