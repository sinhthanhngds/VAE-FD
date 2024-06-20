//package drawing.cosineforce;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

//import graphs.Vertex;

/**
 * Graph Panel is of drawing the graph.
 *
 * @author Meng Xie (mxie4522@it.usyd.edu.au)
 */
@SuppressWarnings("serial")
public class GraphPanel extends JPanel{

    /* Class variables */
    private static Logger logger = Logger.getLogger("GraphPanel");
    private final double DIAMETER = 5;

	private double centreX = 0;
	private double centreY = 0;


    private double minX = 0;
    private double maxX = 0;
    private double minY = 0;
    private double maxY = 0;
    private double screenScale = 0;
    private double offsetX = 0;
    private double offsetY = 0;

    private int height;
    int width;

    private Graphics2D g2;
    private ArrayList<int[]> drawn = new ArrayList<int[]>();
    private ArrayList<Vertex> isolations = new ArrayList<Vertex>();
    private Frame frame;
    private ArrayList<Vertex> vertices;

    /**
     * Constructor in which doing nothing.
     *
     * @param f The father frame.
     */
    public GraphPanel(Frame f){
        frame = f;
    }

    /**
     * Implementation of paint(), in which drawing the graph.
     */
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());

        if (frame.graph == null || frame.graph.getVertices() == null || frame.graph.getVertices().size() == 0){
            logger.log(Level.WARNING, "Painting is stopped due to vertex list is null or empty!");
            return;
        }

        vertices = frame.graph.getVertices();
        setDrawingParameters();

        /* Draws the vertices with their edges. */
        for (int i=0; i<vertices.size(); i++){
            Vertex v = vertices.get(i);
            drawVertex(v);
        }

        /* Draws isolated vertices. */
        for (int i=0; i<isolations.size(); i++){
            Vertex v = isolations.get(i);
            //Point2D p = v.getLocation();
            //g2.setPaint(Color.red);
            g2.setPaint(Color.black);
            g2.fill(new Ellipse2D.Double(mapX(v.getX())-DIAMETER*0.5, mapY(v.getY())-DIAMETER*0.5, DIAMETER, DIAMETER));

            //***********
            if (frame.labelOn == true)
            	g2.drawString(""+v.getId(),(int)mapX(v.getX())+5,(int)mapY(v.getY())+5);
            //logger.info("drew a isolated vertex: " + v.getId());
        }

        drawn.clear();
        isolations.clear();
    }

    /**
     * Draws a vertex with its edges.
     *
     * @param v The given vertex.
     */
    private void drawVertex(Vertex v){
        ArrayList<Integer> adjNodes = v.getAdjacentNodes();

        /* Checks if this is an isolated vertex. */
        if (adjNodes == null || adjNodes.size() == 0){
            isolations.add(v);
            return;
        }

        /* Draws vertex and its edges. */
        int[] line = {v.getId(), 0};

        Point2D.Double ep;

        for (int i=0; i<adjNodes.size(); i++){
            Integer I = adjNodes.get(i);
            line[1] = I.intValue();

            if (isDrawn(line)) return;

            ep = edgeLocation(I.intValue());
            g2.setPaint(Color.black);
            g2.draw(new Line2D.Double( mapX(v.getX()), mapY(v.getY()), mapX(ep.getX()), mapY(ep.getY()) ));

            //g2.setPaint(Color.red);
            g2.setPaint(Color.black);
            g2.fill(new Ellipse2D.Double(mapX(ep.getX())-DIAMETER*0.5, mapY(ep.getY())-DIAMETER*0.5, DIAMETER, DIAMETER));

			//**************
            if (frame.labelOn == true)
				g2.drawString(""+I.intValue(),(int)mapX(ep.getX())+5,(int)mapY(ep.getY())+5);
            //logger.info("Drew edge: " + line[0] + ", " + line[1]);
        }
        g2.fill(new Ellipse2D.Double(mapX(v.getX())-DIAMETER*0.5, mapY(v.getY())-DIAMETER*0.5, DIAMETER, DIAMETER));

        logger.info("id: " + v.getId() + "   "+mapX(v.getX())+", " + mapY(v.getY()));

        //g2.drawString(""+v.getId(),(int)mapX(v.getX())+5,(int)mapY(v.getY())+5);

    }

    /**
     * Finds the vertex with an given Id and returns its location.
     *
     * @param Id The given vertex Id.
     * @return   The location of the vertex.
     */
    private Point2D.Double edgeLocation(int Id)
    {
 		return new Point2D.Double(frame.graph.getVertex(Id).getX(), frame.graph.getVertex(Id).getY());

    }

    /**
     * Checks if the line has been drawn.
     *
     * @param line The line is presented by its two end vertices IDs.
     */
    private boolean isDrawn(int[] line){
        for (int i=0; i<drawn.size(); i++){
            int[] l = drawn.get(i);
            if ( (l[0]==line[0] || l[1]==line[0]) && (l[0]==line[1] || l[1]==line[1]) ){
                return true;
            }
        }
        return false;
    }

    /**
     * Sets drawing parameters. In which, firstly finding graph boundary, then expanding boundary 5% for margin, consequently computing 'screenScale' for mapping to screen and 'offset' for place graph in centre.
     */
    private void setDrawingParameters(){
        for (int i=0; i<vertices.size(); i++){
            Point2D vp = vertices.get(i);

            /* Find graph boundary. */
            if (i == 0){
                minX = vp.getX();
                maxX = vp.getX();
                minY = vp.getY();
                maxY = vp.getY();
                continue;
            }

            if (vp.getX() < minX) minX = vp.getX();
            if (vp.getX() > maxX) maxX = vp.getX();
            if (vp.getY() < minY) minY = vp.getY();
            if (vp.getY() > maxY) maxY = vp.getY();
        }

        /* Expand boundary 5% for margin. */
        //minX = minX - frame.maxWidth*0.05/2;
        //maxX = maxX + frame.maxWidth*0.05/2;
        //minY = minY - frame.maxHeight*0.05/2;
        //maxY = maxY + frame.maxHeight*0.05/2;

		//centre point of the graph
		centreX=(maxX+minX)/2;
		centreY=(maxY+minY)/2;

		if (frame.read == false)
		{
			frame.maxWidth=maxX-minX;
			frame.maxHeight=maxY-minY;
		}


        /* Compute 'screenScale' for mapping and 'offset' for place graph in centre. */
        width  = getWidth()-(int)(getWidth()*0.05);
        height = getHeight()-(int)(getHeight()*0.05);



        if ( frame.maxHeight/height <= frame.maxWidth/width ){
            screenScale = width/frame.maxWidth;
        }
        else{
            screenScale = height/frame.maxHeight;
        }

/*
        logger.info("Searched boundary: \n"
                + "minX: " + minX + "\n"
                + "maxX: " + maxX + "\n"
                + "minY: " + minY + "\n"
                + "maxY: " + maxY + "\n"
                + "screenScale: " + screenScale);
*/
    }

    /**
     * Maps X coordinate from generic number to screen pixel.
     *
     * @param d The generic number of X coordinate.
     * @return  The screen pixel of X coordinate.
     */
    private double mapX(double d){

		 return (d-minX)*screenScale+ getWidth()/2-(maxX-minX)/2*screenScale;

    }

    /**
     * Maps Y coordinate from generic number to screen pixel.
     *
     * @param d The generic number of Y coordinate.
     * @return  The screen pixel of Y coordinate.
     */
    private double mapY(double d){

			return (-d+maxY)*screenScale + getHeight()/2-(maxY-minY)/2*screenScale;
    }
}
