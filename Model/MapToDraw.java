

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

import java.lang.*;

import java.io.*;

import java.net.*;

import java.util.ArrayList;

public class MapToDraw {



    private static ArrayList<Graph> graphs;

    public static void main(String[] args) {

  //************************

  // this is to map the current node coordinate to fit the size of the display screen

  //input: test.txt
  //output:Mapped.txt


  //******************************************
  //******************************************
  //you need determine the box width and height and center point of the box first
  //
  //********************************************



  graphs    = read("wsudata.txt");

  int numberOfGraphs = graphs.size();

     String strMapped="";

  for (int i = 0; i < numberOfGraphs; i++)
  {


       Graph g1 = new Graph();

    g1 = graphs.get(i);
    //g1.printGraph();




    double x, y, minX1=0,minY1=0,maxX1=0,maxY1=0;

    double maxWid=0, maxHei=0;

    for (int j=0; j<g1.size(); j++)
    {
     if (j==0)
     {
      maxX1 = g1.getVertex(0).getX();
      minX1 = g1.getVertex(0).getX();
      maxY1 = g1.getVertex(0).getY();
      minY1 = g1.getVertex(0).getY();
     }
     else
     {
      x = g1.getVertex(j).getX();
      y = g1.getVertex(j).getY();
      if (x < minX1) minX1 = x;
      if (x > maxX1) maxX1 = x;
      if (y < minY1) minY1 = y;
      if (y > maxY1) maxY1 = y;

     }

    }

    maxWid = Math.abs(maxX1-minX1);

    maxHei = Math.abs(maxY1-minY1);


    mapGraph(g1, maxWid, maxHei, boxWidth, boxHeight, new Point2D.Double(350,350));

    strMapped += "***Count= " + (i+1) + "**********\n" + g1.toString()+"\n";
  }

  /*

   try {
    BufferedWriter out;
    out = new BufferedWriter(new FileWriter("Mapped.txt", true));
    out.write(strMapped);
    out.close();

   } catch (IOException e) {
    System.out.println(e);
   }   */

   //write to a file

   try {
     FileOutputStream fo = new FileOutputStream("Mapped.txt");
           PrintWriter pw =new PrintWriter(fo,true);
              pw.println(strMapped);
     fo.close();

   } catch (IOException e) {
     System.out.println(e);
   }

    }

    //screen: 1024x768  (width x Height)
    //box: 700x600
    //topleft point of the box (0,50)
    //topright: (700,50)
    //bottomleft: (0,650)
    //bottomright: (700,650)
    //centrePoint of the box: cp = (350,350)

    private static double boxWidth = 680;
    private static double boxHeight = 600;

    private static void mapGraph(Graph g, double mw, double mh, double bw, double bh, Point2D.Double cp)
    {
   double boxScale;

   if (mh/bh <= mw/bw )
   {
    boxScale = bw/mw;
   }
   else
   {
    boxScale = bh/mh;
   }

   Point2D.Double gcp = g.getCentrePoint();

   for (int i=0; i<g.size(); i++)
   {
    //scale first, move to the centre of the graph, then move to the centre of box
    //note that the sign of y coordinats of vertices are changed without flipping the graph

    double x = g.getVertex(i).getX()*boxScale-gcp.getX()*boxScale+cp.getX();

    double y = -g.getVertex(i).getY()*boxScale+gcp.getY()*boxScale+cp.getY();


     y = 50 + (650-y);  //flip back

    g.getVertex(i).setLocation(x,y);
   }

 }



 public static ArrayList<Graph> read(String fileName)
 {

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
       gs.add(g);

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


     while (sLine.hasNextInt())
     {
      v.addAdjacentNode(sLine.nextInt());
     }

     g.addVertex(v);

    }

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
