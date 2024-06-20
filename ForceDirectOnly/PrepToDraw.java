// 1) read graphs
// 2) determine start and end nodes and sp


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

public class PrepToDraw {

    private static int START;
    private static int END;

    private static int pathLength;

    private static int sp1=0;
    private static int sp2=0;

    static  boolean found = false;

    private static Path path ;



    //private static ArrayList<Path>    paths = new ArrayList<Path>();



    //private static ArrayList<Graph>  graphs ;



    //public static void main(String[] args) {

 public static void prepData(ArrayList<Graph> graphs, ArrayList<Path> paths)
 {




  //graphs    = read("Mapped.txt");

  int numberOfGraphs = graphs.size();

  //initialize paths to a desited number of elements

  for (int i=0; i<numberOfGraphs; i++)
  {
   paths.add(new Path());

  }



  for (int i = 0; i < numberOfGraphs; i++)
  {


       Graph graph = new Graph();

    graph = graphs.get(i);
    //graph.printGraph();


    //for each graph, the starting node is different
    int k = (int)(0 + ((graph.size()-1)-0+1)*Math.random());

    path    = new Path();



    //int k=11;

    for (int m = 0; m < graph.size(); m++)
    {


     START=m+k;
     if (START > graph.size()-1)
      START = START - graph.size();

     for (int n = 0; n < graph.size(); n++)
     {
      if (n == START)
       continue;

      END=n;

      //System.out.print("start= "+START+"  end= "+END+"\n");
      //System.out.print("m= "+m+"  n= "+n+"\n");


      //**********
      //**********
      //initialize variable for each run
      //**********
      sp1=0;
      sp2=0;
      pathLength=6;           //longest possible length=5



      found = false;
      ArrayList<Integer> visited = new ArrayList();
      //***********************************************

      visited.add(new Integer(START));
      //new Search().breadthFirst(graph, visited);

      //System.out.print(" size=  "+ visited.size()+ " "+ visited.get(0).intValue()+"\n");
      breadthFirst(graph, visited);



      //System.out.print("sp1= "+sp1+"  sp2= "+sp2+"\n");




      if (sp1 != sp2 && sp2>2)
      {
       found = true;
          break;
      }


     }

     if (found)  break;
    }

    if (path.getLength()<3)
    {
     System.out.print("i=  " + i + "  k=  " + k +"\n");
    }
    
  //++++++++  27/06/2017
    //to make the pair of plannar graph drawings (forec directed and planner drawing, such as 0-80, 1,81..) use the same path
    if (i>79)
        path = paths.get(i-80);
       
    paths.set(i, path);
  //++++++
  }

     for (int i=0; i<paths.size(); i++)
     {
          System.out.print("i=  "+i +"  ");

         paths.get(i).printString();
     }


    }



    private static void breadthFirst(Graph graph, ArrayList<Integer> visited)
    {



        ArrayList<Integer> nodes = graph.getVertex(visited.get(visited.size()-1).intValue()).getAdjacentNodes();


       /*for (int i=0; i<nodes.size(); i++)
        {
   System.out.print(nodes.get(i).intValue()+"  ");

  }
  System.out.print("\n");
  */

        // examine adjacent nodes
        for (Integer node : nodes) {
   //System.out.print("size= "+ visited.size() + "  id=  " + node.intValue() +" visted= "+visited.get(visited.size()-1).intValue() +"\n");

            if (visited.contains(node)) {
                continue;
            }

            if ((visited.size()) > pathLength)
            {
             break;
   }
            if (node.equals(new Integer(END))) {
    visited.add(node);
    if (visited.size()-1< pathLength)
                {
                 pathLength = visited.size()-1;

    }
                //printPath(visited);

    sp1=sp2;
    sp2=pathLength;


    path = new Path();
    path.addPath(visited);

    visited.remove(visited.size()-1);



    //System.out.print("length= "+(pathLength)+"\n");

                break;
            }
        }
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (Integer node : nodes)
        {

            if (visited.contains(node) || node.equals(new Integer(END)))
            {

                continue;
            }
            if ((visited.size()) > pathLength)
             continue;
            visited.add(node);

            breadthFirst(graph, visited);
            visited.remove(visited.size()-1);
        }
    }

    private static void printPath(ArrayList<Integer> visited)
    {
        for (Integer node : visited) {
            System.out.print(node);
            System.out.print(" ");
        }
        System.out.println();
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