/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Graphe;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

/**
 * VueCourbe
 * @author Tristan
 */
public class Graphe extends JFrame {
    
    
    //////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////////// VARIABLES ///////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    
    
    protected String _titre;
    protected String _legende;
    protected int _pas;
    protected double[] _tab;
    protected ChartPanel _chartPanel;
    
    
    //////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    //////////////////////////// CONSTRUCTEUR /////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    
    
    public Graphe(double[] tab, int pas, String titre, String legende) {
        
        this._titre = titre;
        this._legende = legende;
        this._pas = pas;
        this._tab = tab;

        // This will create the dataset 
        DefaultXYDataset dataset = getData();
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, this._titre);
        // we put the chart into a panel
        this._chartPanel = new ChartPanel(chart);
        // default size
        this._chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        // add it to our application
        this.add(this._chartPanel);
      
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    } // Graphe(double[] tab, int pas)
    
    
    //////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    ///////////////////////////// FONCTIONS ///////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Met a jour le graph des resultats
     */
    protected void afficherGraph() {
        
        // This will create the dataset 
        DefaultXYDataset dataset = getData();
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, this._titre);
        this._chartPanel.setChart(chart);
        this.repaint();
        
    } // paintComponent(Graphics g)
    
    
    /**
     * Cree les donnee de la courbe
     * @return le tableau de donnees de la courbe
     */
    protected DefaultXYDataset getData() {
        
        double[][] yValues = new double[2][this._tab.length];
        double[][] xValues = new double[2][this._tab.length];

        // X values
        for (int i=0; i<this._tab.length; i++)
        {
           yValues[0][i] = Double.valueOf(i*this._pas);
           xValues[0][i] = Double.valueOf(i*this._pas);
        }

        // Y values
        for (int i=0; i<this._tab.length; i++)
        {
           yValues[1][i] = Double.valueOf(this._tab[i]);
           xValues[1][i] = Double.valueOf(this._tab[i]);
        }

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries(this._legende, yValues);
        dataset.addSeries("Pas: " + this._pas, xValues);
        
        return dataset;
        
    } // updateData()
    
    
    /**
     * Cree la courbe
     * @param dataset donnees de la courbe
     * @param title titre de la courbe
     * @return la courbe correspondant aux donnees
     */
    private JFreeChart createChart(DefaultXYDataset dataset, String title) {
        
      // Create the chart
      JFreeChart chart = ChartFactory.createXYLineChart(
         title,                    // The chart title
         "Pas: " + this._pas,      // x axis label
         this._legende,            // y axis label
         dataset,                  // The dataset for the chart
         PlotOrientation.VERTICAL,
         false,                    // Is a legend required?
         false,                    // Use tooltips
         false                     // Configure chart to generate URLs?
      );
      
      return chart;
        
    } // createChart(PieDataset dataset, String title)
    
    
} // Graphe
