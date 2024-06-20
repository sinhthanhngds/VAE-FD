//package drawing.cosineforce;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.util.logging.Logger;

import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

//import graphs.Graph;

/**
 * Frame is class of establishing a window frame and drawing vertices and edges.
 *
 * @author Meng Xie (mxie4522@it.usyd.edu.au)
 */
public class Frame {

    /* Class variables */
    public final int SCREENWIDTH = 800;
    public final int SCREENHEIGHT = 620;
    public Graph graph = new Graph();

    public Graph tempGraph;

    public ArrayList<Graph> graphs = new ArrayList<Graph>();
    public int curr = -1;

    public String graphFileName;


    public double maxWidth = 0;
    public double maxHeight = 0;

    public boolean read = true;


	public boolean labelOn = false;

    private static Logger logger = Logger.getLogger("Frame");
    private JFrame mainFrame;

    //-------
    public JPanel graphPanel;

    /**
     * Constructor in which a frame with panels is established.
     */
    public Frame(){
        mainFrame = new JFrame("Cosine Force Drawing");

		//********
        mainFrame.setBackground(Color.white);
        //**********

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });

        JMenuBar menuBar = new MenuBar(this);

        JPanel contentPanel = new JPanel(new BorderLayout());


        graphPanel = new GraphPanel(this);

        graphPanel.setBackground(Color.white);

        JPanel optionPanel = new optionPanel(this);

        /* Pack graph panel into a border panel.*/
        JPanel graphBorderP = new JPanel(new BorderLayout());
        graphBorderP.add(graphPanel, BorderLayout.CENTER);
        graphBorderP.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        //graphBorderP.setBackground(Color.white);

        contentPanel.add(optionPanel, BorderLayout.WEST);
        contentPanel.add(graphBorderP, BorderLayout.CENTER);

        //mainFrame.setJMenuBar(menuBar);
        mainFrame.setContentPane(contentPanel);
        mainFrame.pack();
    }

    /**
     * Sets visibility of frame.
     *
     * @param b True or false.
     */
    public void setVisible(boolean b){
        mainFrame.setSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        mainFrame.setVisible(b);
        logger.info("Frame has been set visible.");
    }

    /**
     * Re-paint graph panel.
     */
    public void repaintGraphPanel(){
        graphPanel.repaint();
    }

}
