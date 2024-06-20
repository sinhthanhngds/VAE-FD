//package drawing.cosineforce;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.GridLayout;
import java.lang.NumberFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.*;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import javax.swing.JFileChooser;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Font;

import java.io.*;


import java.io.BufferedReader;
import java.util.Scanner;


import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.FileDialog;



//import graphs.Graph;
//import graphs.CrossAngle;

/**
 * Option Panel is of setting up graph variables.
 *
 * @author Meng Xie (mxie4522@it.usyd.edu.au)
 */
@SuppressWarnings("serial")
public class optionPanel extends JPanel implements ActionListener{  //, ItemListener{

    /* Class variables */
    private static Logger logger = Logger.getLogger("optionPanel");

    private JTextField gravityText;
    private JTextField epsilonText;
    private JTextField springStrengthText;
    private JTextField springLengthText;
    private JTextField cosineText;

    private JTextField pointText;

    private JTextField rotateText;
    private JTextField iterationText;

    private JTextField indexText;


    private JTextField sizeText;
    private JTextField densityText;

    private JTextField nameText;

    private JTextArea textArea;



    JRadioButton randomYesButton;
    JRadioButton labelYesButton;

    private Frame frame;

    //**************
        private ArrayList<Graph> manyGraphs = new ArrayList<Graph>();
	    private int manyCurr = -1;
	//*************

    private double minX = 0;
    private double maxX = 0;
    private double minY = 0;
    private double maxY = 0;



    /**
     * Constructor in which whole optional items are initiated.
     *
     * @param f The father frame.
     */
    public optionPanel(Frame f){
        super();
        super.setPreferredSize(new Dimension(200, 0));

        frame = f;




  		//************
        JButton readButton = new JButton("Read");
        readButton.addActionListener(this);

        JPanel buttonPanel0 = new JPanel(new GridLayout(1, 1));
        buttonPanel0.add(readButton);


        JButton upButton = new JButton("Up");
        upButton.addActionListener(this);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(this);


        JPanel buttonPanel00 = new JPanel(new GridLayout(2, 1));
        buttonPanel00.add(upButton);
        buttonPanel00.add(nextButton);



        this.add(buttonPanel0);
        this.add(buttonPanel00);


        //***********************


        JLabel indexLable = new JLabel("Index: ");
        indexText = new JTextField(new Integer(1).toString());

        JPanel itemsPanel0 = new JPanel(new GridLayout(1, 2));

        itemsPanel0.add(indexLable);
        itemsPanel0.add(indexText);

        this.add(itemsPanel0);

        JButton goButton = new JButton("Go");
        goButton.addActionListener(this);

        JPanel buttonPanel01 = new JPanel(new GridLayout(1, 1));
        buttonPanel01.add(goButton);
        this.add(buttonPanel01);


        //**********************


		//++++++++++++++++++++++++++++++


		        JLabel nameLable = new JLabel("Name: ");
		        nameText = new JTextField("");

		        JPanel itemsPanel2 = new JPanel(new GridLayout(1, 2));

		        itemsPanel2.add(nameLable);
		        itemsPanel2.add(nameText);

		        this.add(itemsPanel2);

		        JButton saveButton = new JButton("Save");
		        saveButton.addActionListener(this);


		        JPanel buttonPanel4 = new JPanel(new GridLayout(1, 1));
		        buttonPanel4.add(saveButton);

		        this.add(buttonPanel4);

		//++++++++++++++++++++++++++++++


	 	//+++++++++++++++++++++
	 	textArea = new JTextArea("",5,15);
		textArea.setFont(new Font("Serif", Font.ITALIC, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JPanel itemsPanel3 = new JPanel(new GridLayout(1, 1));
		itemsPanel3.add(textArea);
		this.add(itemsPanel3);
	 	//+++++++++++++++++++++



		//******************


        JLabel gravityLable = new JLabel("Gravity Strength: ");
        gravityText = new JTextField(""+frame.graph.GRAVITY_STRENGTH);

        JLabel epsilonLable = new JLabel("Epsilon: ");
        epsilonText = new JTextField(""+frame.graph.EPSILON);

        JLabel springStrengthLable = new JLabel("Spring Strength: ");
        springStrengthText = new JTextField(""+frame.graph.SPRING_STRENGTH);

        JLabel springLengthLable = new JLabel("Spring Length: ");
        springLengthText = new JTextField(""+frame.graph.SPRING_LENGTH);

        JLabel cosineLable = new JLabel("Cross Angle: ");
		cosineText = new JTextField(""+frame.graph.COSINE_STRENGTH);

        JLabel rotateLable = new JLabel("Angular Resolu: ");
		rotateText = new JTextField(""+frame.graph.ROTATE_STRENGTH);

        JLabel pointLable = new JLabel("Point Edge: ");
		pointText = new JTextField(""+frame.graph.POINTEDGE_STRENGTH);

        JLabel iterationLable = new JLabel("Iteration Times: ");
		iterationText = new JTextField(""+frame.graph.NUM_ITERATIONS);

        JPanel itemsPanel = new JPanel(new GridLayout(8, 2));

        itemsPanel.add(springLengthLable);
        itemsPanel.add(springLengthText);

        itemsPanel.add(springStrengthLable);
        itemsPanel.add(springStrengthText);
        itemsPanel.add(gravityLable);
        itemsPanel.add(gravityText);

        itemsPanel.add(cosineLable);
        itemsPanel.add(cosineText);

        itemsPanel.add(rotateLable);
        itemsPanel.add(rotateText);

        itemsPanel.add(pointLable);
        itemsPanel.add(pointText);


        itemsPanel.add(epsilonLable);
        itemsPanel.add(epsilonText);
        itemsPanel.add(iterationLable);
        itemsPanel.add(iterationText);



        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        buttonPanel.add(applyButton);

        this.add(itemsPanel);
        this.add(buttonPanel);



		//**************

		randomYesButton = new JRadioButton("Rndm Mv?", false);
		labelYesButton = new JRadioButton("Labl On?", false);

        JButton revertButton = new JButton("Revert");
        revertButton.addActionListener(this);
        JButton repaintButton = new JButton("RePaint");
        repaintButton.addActionListener(this);

        JPanel buttonPanel1 = new JPanel(new GridLayout(2, 2));
        buttonPanel1.add(randomYesButton);
        buttonPanel1.add(revertButton);
		buttonPanel1.add(labelYesButton);
		buttonPanel1.add(repaintButton);

        this.add(buttonPanel1);

        //************

        JButton backButton = new JButton("Backward");
        backButton.addActionListener(this);

        JButton forButton = new JButton("Forward");
        forButton.addActionListener(this);


        JPanel buttonPanel3 = new JPanel(new GridLayout(1, 2));
        buttonPanel3.add(backButton);
        buttonPanel3.add(forButton);

        this.add(buttonPanel3);




        //**************************************
        //**************************************

        JLabel sizeLable = new JLabel("Size: ");
        sizeText = new JTextField(new Integer(12).toString());

        JLabel densityLable = new JLabel("Density: ");
        densityText = new JTextField(new Double(0.00).toString());

        JPanel itemsPanel1 = new JPanel(new GridLayout(1, 2));

        itemsPanel1.add(sizeLable);
        itemsPanel1.add(sizeText);
        //itemsPanel1.add(densityLable);
        //itemsPanel1.add(densityText);

        this.add(itemsPanel1);

        JButton erGraphButton = new JButton("ER");
        erGraphButton.addActionListener(this);

        JButton wattsGraphButton = new JButton("Watts");
        wattsGraphButton.addActionListener(this);

        JButton eppsteinGraphButton = new JButton("Eppstein");
        eppsteinGraphButton.addActionListener(this);


        JPanel buttonPanel2 = new JPanel(new GridLayout(1, 3));
        buttonPanel2.add(erGraphButton);
        buttonPanel2.add(wattsGraphButton);
        buttonPanel2.add(eppsteinGraphButton);
        //buttonPanel1.add(randomNoButton);

        this.add(buttonPanel2);

        //**************************************
        //**************************************




    }

    //*****************
    // for radio button
    //*****************
   // public void itemStateChanged (ItemEvent e)
    //{


	//}

    /**
     * Implementation of 'actionPerformed()', in which handling button action events.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Apply")){
            try{
                frame.graph.EPSILON          = new Double(epsilonText.getText());
                frame.graph.SPRING_STRENGTH  = new Double(springStrengthText.getText());
                frame.graph.GRAVITY_STRENGTH = new Double(gravityText.getText());

                frame.graph.SPRING_LENGTH     = new Double(springLengthText.getText());

                frame.graph.COSINE_STRENGTH  = new Double(cosineText.getText());
                frame.graph.ROTATE_STRENGTH  = new Double(rotateText.getText());

                frame.graph.NUM_ITERATIONS   = new Integer(iterationText.getText());

                frame.graph.POINTEDGE_STRENGTH   = new Double(pointText.getText());


                logger.info("Set parameters: \n"
                        + "GRAVITY_STRENGTH = " + frame.graph.GRAVITY_STRENGTH + "\n"
                        + "EPSILON = " + frame.graph.EPSILON + "\n"
                        + "SPRING_STRENGTH = " + frame.graph.SPRING_STRENGTH + "\n"
                        + "SPRING_LENGTH = " + frame.graph.SPRING_LENGTH + "\n"
                        + "POINTEDGE_STRENGTH = " + frame.graph.POINTEDGE_STRENGTH + "\n"
                        + "COSINE_STRENGTH = " + frame.graph.COSINE_STRENGTH);
            }catch(NumberFormatException ne){
                //logger.log(Level.WARNING, ne.getMessage());
                JOptionPane.showMessageDialog(this, "Got NumberFormatException: '" + ne.getMessage() + "'", "Warning", JOptionPane.ERROR_MESSAGE);
            }

            frame.read = false;

			logger.info("Drawing in progress.........\n");

			//frame.graph.setParameters(p1,p2,p3,p4,p5,p6,NUM_ITERATIONS);

            /* Apply all forces on the graph. */
            double maxForce = 1000;

            for (int i = 0; i < frame.graph.NUM_ITERATIONS; i++)
            {

                maxForce = frame.graph.computeAllForces();

                CrossAngle.computeAngleForceElements(frame.graph);

				System.out.println("working and i= " +i+" max force= " + maxForce +"\n");

                if (maxForce < 0.0)
                {
					System.out.println("breaked (maxForce<0) and i= " +i+"\n");
                	break;
				}

				if (maxForce >= 0.0 && maxForce <= 0.00005)
				{
					System.out.println("stopped (maxForce<0.00005) and i= " +i+"\n");
					break;
				}

            }

            while (frame.curr < frame.graphs.size()-1)
            {
				frame.graphs.remove(frame.graphs.size()-1);
			}

            frame.graphs.add(frame.graph.copy());
            frame.curr++;


			showData();

            frame.repaintGraphPanel();

            logger.info("Drawing done!!\n");

        }

        if (e.getActionCommand().equals("Revert"))
        {
            //if (frame.graphFileName != null){
                //frame.graph = new Graph(frame.graphFileName);


	            frame.graph = frame.tempGraph.copy();


                 //******************
                 if (randomYesButton.isSelected() == true)
                 {
					frame.read = false;

                	frame.graph.randMove();
				 }

			    frame.graphs.clear();
			    frame.graphs.add(frame.graph.copy());
			    frame.curr = 0;


                CrossAngle.computeAngleForceElements(frame.graph);
				showData();

                //******************

                frame.repaintGraphPanel();
			//}

        }

        if (e.getActionCommand().equals("ER"))
        {
			frame.read = false;

			int    n = new Integer(sizeText.getText());
			double d = new Double(densityText.getText());
			//frame.graph = new Graph(n,d);

			boolean connected = false;
			do {
				frame.graph = new Graph();
				frame.graph.erGraph(n);
				connected = frame.graph.connected();
			} while (!connected);


			frame.tempGraph = frame.graph.copy();

			frame.graphs.clear();
			frame.graphs.add(frame.graph.copy());
			frame.curr = 0;


			CrossAngle.computeAngleForceElements(frame.graph);
			showData();

            frame.repaintGraphPanel();

		}

        if (e.getActionCommand().equals("Watts"))
        {
			frame.read = false;

			int    n = new Integer(sizeText.getText());
			double d = new Double(densityText.getText());
			//frame.graph = new Graph(n,d);

			boolean connected = false;
			do {
				frame.graph = new Graph();
				frame.graph.wattsGraph(n);
				connected = frame.graph.connected();
			} while (!connected);

			frame.tempGraph = frame.graph.copy();

			frame.graphs.clear();
			frame.graphs.add(frame.graph.copy());
			frame.curr = 0;


			CrossAngle.computeAngleForceElements(frame.graph);
			showData();

            frame.repaintGraphPanel();

		}

        if (e.getActionCommand().equals("Eppstein"))
        {
			frame.read = false;

			int    n = new Integer(sizeText.getText());
			double d = new Double(densityText.getText());
			//frame.graph = new Graph(n,d);

			boolean connected = false;
			do {
				frame.graph = new Graph();
				frame.graph.eppsteinGraph(n);
				connected = frame.graph.connected();
			} while (!connected);

			frame.tempGraph = frame.graph.copy();

			frame.graphs.clear();
			frame.graphs.add(frame.graph.copy());
			frame.curr = 0;


			CrossAngle.computeAngleForceElements(frame.graph);
			showData();

            frame.repaintGraphPanel();

		}

        if (e.getActionCommand().equals("Go"))
        {
			int    n = new Integer(indexText.getText());

			if (n <= manyGraphs.size() && n > 0)
			{
		    	manyCurr = n-1;

			    frame.graph= manyGraphs.get(manyCurr).copy();

				//frame.graph.randMove();
				CrossAngle.computeAngleForceElements(frame.graph);
				showData();

				//frame.graph.printGraph();

				//******************

				frame.tempGraph = frame.graph.copy();

				frame.graphs.clear();
				frame.graphs.add(frame.graph.copy());
				frame.curr = 0;

				frame.repaintGraphPanel();
			}


	    }

        if (e.getActionCommand().equals("Backward"))
        {
			if (frame.curr != -1)
			{
			    if (frame.curr != 0)
			    	frame.curr--;

				frame.graph = frame.graphs.get(frame.curr).copy();

				CrossAngle.computeAngleForceElements(frame.graph);
				showData();

				frame.repaintGraphPanel();

			}

		}

        if (e.getActionCommand().equals("Forward"))
        {
			if (frame.curr != -1)
			{
			    if (frame.curr != frame.graphs.size()-1)
			    	frame.curr++;

				frame.graph = frame.graphs.get(frame.curr).copy();

				CrossAngle.computeAngleForceElements(frame.graph);
				showData();

				frame.repaintGraphPanel();

			}

		}

        if (e.getActionCommand().equals("Save"))
        {
			String fn;
			fn = nameText.getText().trim();

			System.out.println("length =" + fn.length());

			if (fn.length() != 0)
			{
				fn = System.getProperty("user.dir") +"\\"+fn;

				if (frame.graphs.size()>0 )
				{
					frame.graph.write(fn +".txt");
					save(fn);
					System.out.println("all saved.");
				}
				else
				{
					System.out.println("draw something first!");
				}
			}
			else
			{
				System.out.println("enter a file name first!");
		    }

		}

		if (e.getActionCommand().equals("Read"))
		{

			frame.read = true;

			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{


			    manyGraphs = read(fc.getSelectedFile().getAbsolutePath());
			    manyCurr   = 0;

			    frame.graph= manyGraphs.get(manyCurr).copy();

				//frame.graph.randMove();
				CrossAngle.computeAngleForceElements(frame.graph);

				//frame.graph.printGraph();

				//******************

				frame.tempGraph = frame.graph.copy();

				frame.graphs.clear();
				frame.graphs.add(frame.graph.copy());
				frame.curr = 0;

				frame.repaintGraphPanel();

				showData();

			    frame.graphFileName = fc.getSelectedFile().getAbsolutePath();
			    logger.info("Have chosen '" + frame.graphFileName + "'.");

			}
		}
		if (e.getActionCommand().equals("Up"))
		{
			if (manyCurr != -1)
			{
			    if (manyCurr != 0)
			    	manyCurr --;

			    frame.graph= manyGraphs.get(manyCurr).copy();

				//frame.graph.randMove();
				CrossAngle.computeAngleForceElements(frame.graph);
				showData();
				//frame.graph.printGraph();

				//******************

				frame.tempGraph = frame.graph.copy();

				frame.graphs.clear();
				frame.graphs.add(frame.graph.copy());
				frame.curr = 0;

				frame.repaintGraphPanel();


			}

		}
		if (e.getActionCommand().equals("Next"))
		{
			if (manyCurr != -1)
			{
			    if (manyCurr != manyGraphs.size()-1)
			    	manyCurr ++;

			    frame.graph= manyGraphs.get(manyCurr).copy();

				//frame.graph.randMove();
				CrossAngle.computeAngleForceElements(frame.graph);
				showData();
				//frame.graph.printGraph();

				//******************

				frame.tempGraph = frame.graph.copy();

				frame.graphs.clear();
				frame.graphs.add(frame.graph.copy());
				frame.curr = 0;

				frame.repaintGraphPanel();



			}

		}

		if (e.getActionCommand().equals("RePaint"))
		{
			if (labelYesButton.isSelected() == true)
				frame.labelOn = true;
			else
				frame.labelOn = false;

			frame.repaintGraphPanel();
		}




    }

    public void showData()
    {
		 textArea.setText(" cur = " + (manyCurr+1) + "/" + manyGraphs.size() + "  crossNo = "+frame.graph.getNumOfCrosses()+"\n"+
		  				  "  angMean = " + ((int)(100*frame.graph.getDegreeMean()))/100.0 +"\n"+
		  				  "  minAng =" + ((int)(100*frame.graph.getMinCrossAngle()))/100.0 +"\n"+
		 				  "   edgeDev = " + ((int)(100*frame.graph.getEdgeDeviation()))/100.0 +"\n"+
					      " minVetx = "  + ((int)(100*frame.graph.getMinVertexAngle()))/100.0 +"\n"+
					      " minPtEdgD = "  + ((int)(100*frame.graph.getMinPtEdgDist()))/100.0);

	}


    public void save(String filename)
    {

		  JPanel win = frame.graphPanel;
		  Dimension size = win.getSize();

		  BufferedImage image = (BufferedImage)win.createImage(size.width, size.height);

		  Graphics g = image.getGraphics();
		  win.paint(g);
		  g.dispose();
		  try
		  {
			ImageIO.write(image, "png", new File(filename +".png"));
		  }
		  catch (IOException e1)
		  {
			e1.printStackTrace();
		  }



		/*
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("png"))
        {
            try
            {
				ImageIO.write(image, suffix, file);
			}
            catch (IOException e)
            {
				e.printStackTrace();
			}
        }
        else
        {
            System.out.println("Error: filename must end in .jpg or .png");
        }  */

	}


     public ArrayList<Graph> read(String fileName)
     {

		 frame.maxWidth  = 0;
		 frame.maxHeight = 0;

		 ArrayList<Graph> gs = new ArrayList<Graph>();

		 try
		 {
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				String str;


				Graph g = new Graph();

                boolean empty = true;

				while ((str = in.readLine()) != null)
				{
					str = str.trim();
					if (str.length() ==0 || str.indexOf("===") != -1)
					   continue;
					if (str.indexOf("Count") != -1)
					{
						if (!empty)
						{
							if (Math.abs(maxX-minX)>frame.maxWidth)
								frame.maxWidth = Math.abs(maxX-minX);
							if (Math.abs(maxY-minY)>frame.maxHeight)
								frame.maxHeight = Math.abs(maxY-minY);

							gs.add(g);
						}

						System.out.println("id===" + gs.size());

						empty = false;

						g = new Graph();

						continue;
					}


					Scanner sLine = new Scanner(str);

					int vId = sLine.nextInt();


					double x = sLine.nextDouble();
					double y = sLine.nextDouble();

					Vertex v = new Vertex(vId, x, y);

					//for screen scale leter
					if (vId==0)
					{
						minX=x;
						maxX=x;
						minY=y;
						maxY=y;
					}
					else
					{
						if (x < minX) minX = x;
						if (x > maxX) maxX = x;
						if (y < minY) minY = y;
						if (y > maxY) maxY = y;

					}



					while (sLine.hasNextInt())
					{
						v.addAdjacentNode(sLine.nextInt());
					}

					g.addVertex(v);

				}

				if (Math.abs(maxX-minX)>frame.maxWidth)
					frame.maxWidth = Math.abs(maxX-minX);
				if (Math.abs(maxY-minY)>frame.maxHeight)
					frame.maxHeight = Math.abs(maxY-minY);


				gs.add(g);

				in.close();
				System.out.println("================" + gs.size()+"======================\n");
		 }
	     catch (IOException e)
	     {
			 e.printStackTrace();
         }


         return gs;
	 }

}
