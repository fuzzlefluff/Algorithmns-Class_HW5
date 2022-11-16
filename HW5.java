// Matthew Prindle Homework 5
// CSC 4520 Fall 2022
 /* Usage 
  * 1) create points, give it a name and the cordinate values you want it to have (x,y)
  * 2) add those points to an array list
  * 3) use the pointPair class to get all the possible edges with manhattan values, excluding duplicates with the getEdges() method
  * 4) create a new Minimum Spanning Tree from the list of edges.
  * 5) the MST object will have the minimum cost and a list of edges, which can be output easily with it's tostring() method.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.lang.Math;


public class HW5 {
    public static void main(String[] args) {
        
        // create our testcase data from the graph
        ArrayList<point> pointList = new ArrayList<point>();
        
        point a = new point("a",1,3);
        pointList.add(a);
        point b = new point("b",2,1);
        pointList.add(b);
        point c = new point("c",3,2);
        pointList.add(c);
        point d = new point("d",4,3);
        pointList.add(d);
        point e = new point("e",4,5);
        pointList.add(e);

        System.out.println("\nCase 1, Graph");
        ArrayList<pointPair> allValidEdges = pointPair.getEdges(pointList);
        MST mst = new MST(allValidEdges);
        System.out.println(mst.toString());

        // create a random testcase 
        System.out.println("\nCase 2, Random Points");
        pointList = randomPointList(100);
        allValidEdges = pointPair.getEdges(pointList);
        mst = new MST(allValidEdges);
        System.out.println(mst.toString());   
        
        // a testcase that is too big
        System.out.println("\nCase 3, more than 100 points");
        pointList = randomPointList(200);
        allValidEdges = pointPair.getEdges(pointList);
        mst = new MST(allValidEdges);
        System.out.println(mst.toString());
    } 

    // A method that generates points with random cordinate values between 0 and 99
    public static ArrayList<point> randomPointList(int size) {
        
        ArrayList<point> pointList = new ArrayList<point>();
        String n;
        Random rand = new Random();

        for(int i = 0; i<size; i++){
            n = toAlphabetic(i);
            pointList.add(new point(n,rand.nextInt(100),rand.nextInt(100)));
        }
        return pointList;
    }
    
    // A method that converts an integer to an alphabetic code, so we can have tons of points in our lists easily
    public static String toAlphabetic(int i) {
        if( i<0 ) { return "-"+toAlphabetic(-i-1); }
    
        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);

        if( quot == 0 ) { return ""+letter; } 
        else { return toAlphabetic(quot-1) + letter; }
    }

    // A method that prints a list of pointPairs for us
    public static String printPairList(ArrayList<pointPair> pairList){
        String r = "Edge \t Weight\n";
        for (pointPair p : pairList) {
            r += p.toString() +"\n";
        }
        return r;
    }
    
}

// A class for our points that wraps a string name, and the int x and y cordinate values together.
class point {
    
    public String name;
    public int x;
    public int y;
    
    public point(String n, int xCord, int yCord) {
        name = n;
        x = xCord;
        y = yCord;
    }
    public String toString(){
        String r = name + " " + x + "|" + y;
        return r;
    }
}

// A class that wraps two points together in a pair with their distance and contains helpful functions for sorting and getting valid pointPair edges from a list of points
class pointPair {
    
    public int distance;
    public point p1;
    public point p2;

    public pointPair(point point1, point point2) {
        p1 = point1;
        p2 = point2;
        distance = pointDistance(p1, p2);
    }

    public String toString(){
        String r = p1.name + " - " + p2.name + "\t w: " + distance;
        return r;
    }

    public Integer getdistance(){
        Integer r = distance;
        return r;
    }

    // A method that simply calculates the manhattan distance between two points
    public static int pointDistance(point p1,point p2){
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    //A method that returns a list of all possible pairs, excluding self-referential pairs like a-a or b-b or duplicates like b-a if a-b already exists
    public static ArrayList<pointPair>getEdges(ArrayList<point> pointList){
        int vertexCount = pointList.size();
        ArrayList<pointPair> rPairs = new ArrayList<pointPair>();
        if(vertexCount > 100){
            System.out.println("\n******* too many points! ******* \nlimited to a list of less than 100 points!\n\tempty data follows!\n");
            return rPairs;
        }
        boolean seen = false;
        for(int i=0; i<vertexCount; i++){
            for(int j=0; j<vertexCount; j++){
                pointPair pPair = new pointPair(pointList.get(i), pointList.get(j));
                if(pPair.p1.name == pPair.p2.name){ seen = true;}
                for (pointPair p : rPairs) {
                    if(p.p1.name == pPair.p2.name && p.p2.name == pPair.p1.name) { seen = true; break;}
                }
                if(!seen){rPairs.add(pPair);}
                seen = false;
            }
        }
        return rPairs;
    }

    //A method that returns how many points are in a list of pairs based on point names.
    public static int getVertexCount(ArrayList<pointPair> allValidEdges){
        int r = -1;
        ArrayList<point> pointStack = new ArrayList<point>();
        for (pointPair pointPair : allValidEdges) {
            if(!pointStack.contains(pointPair.p1)){
                pointStack.add(pointPair.p1);
            }
            if(!pointStack.contains(pointPair.p2)){
                pointStack.add(pointPair.p2);
            }
        }
        r = pointStack.size();
        return r;
    }

    // a method that sorts a list of point pairs by distance
    public static ArrayList<pointPair> sortPairsbyDistance(ArrayList<pointPair> pairList){

        pairList.sort(Comparator.comparing(pointPair::getdistance));
        return pairList;
    }

    // a method that sorts a list by the string name alphabetically
    public static ArrayList<pointPair> sortPairsbyName(ArrayList<pointPair> pairList){

        pairList.sort(Comparator.comparing(pointPair::toString));
        return pairList;
    }
}

// A class that stores a list of point edges and the cost of the whole tree. Determins the best connected edges and minimum cost from a given list of points.
class MST{
    public int minCost;
    public ArrayList<pointPair> connectedPoints;
    
    public MST(ArrayList<pointPair> allValidEdges) {
        minCost = 0;
        connectedPoints = kruskalMST(allValidEdges);
        for (pointPair p : connectedPoints) {
            minCost += p.distance;
        }
    }

    // A method that returns the MST point pairs using the Kruskal algorithm, sorts the edges alphabetically before returning
    ArrayList<pointPair> kruskalMST(ArrayList<pointPair> allValidEdges){
        
        int vertexCount = pointPair.getVertexCount(allValidEdges);
        ArrayList<pointPair> result = new ArrayList<pointPair>();
        if(allValidEdges.size() == 0) {return result;}
        allValidEdges = pointPair.sortPairsbyDistance(allValidEdges);
        result.add(allValidEdges.get(0));
        boolean included = false;
        for (pointPair p : allValidEdges) {
            for (pointPair r : result) {
                if(p.p2.name == r.p2.name) {included = true;}
            }
            if(!included) {result.add(p);}
            if(result.size() == vertexCount) {break;}
            included = false;
        }
        result = pointPair.sortPairsbyName(result);
        return result;
    }

    public String toString(){
        String rS = "";
        rS += "Min Cost: " + minCost;
        rS += "\nVertex Count: " + (connectedPoints.size()+1);
        rS += "\nEdge Count: " + (connectedPoints.size());
        rS += "\nEdge \tWeight";
        for (pointPair pointPair : connectedPoints) {
            rS +="\n" + pointPair.toString();
        }
        return rS;
    }
}
