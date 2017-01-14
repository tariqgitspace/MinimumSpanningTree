import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.*;

class MST_Tariq_Siddiqui_50208470
{
 static ArrayList<ArrayList<Integer>> adjacent_vertices_list;
 static ArrayList<ArrayList<Integer>> Corresponding_adjacent_Node_Weight_List;
 static Scanner s;
 static char []Visited_Status;
 static int []get_heap_index_from_vertice;   //position of vertice V in heap 
 static int []heap_vertice;  //Actual Vertice at ith index of heap
 static int []heap_Weight_Of_Node;
 static int number_of_vertices;
 static int number_of_edges;
 static Writer output;
 static BufferedReader input;
 static String line;
 static Boolean If_new_Test_Case;
 static int previous_vertice;
 static ArrayList<Integer> source_vertice;
 static ArrayList<Integer> destination_vertice;
 static ArrayList<Integer> Weight_Of_Edge;
 static int mst_weight;
 static int last_included_vertice=Integer.MAX_VALUE;
 
 public static void main(String []args) throws IOException
 {
	 String x= "input.txt";
	 String y= "output.txt";

	 input = new BufferedReader(new InputStreamReader(new FileInputStream(x)));
	 output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(y,false)));  //append=false(new file) 
	 output.close();  //empty the file
	 line = input.readLine();
	
	
		do {
			 	output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(y,true)));  //append 
				String[] words = line.split(" ");
				number_of_vertices=Integer.valueOf(words[0]);
		         number_of_edges=Integer.valueOf(words[1]);
		         initialize_all_variables();
		         Read_Parameters_For_A_Testcase_And_Update_Data_Structure();
		         reset_heap();
		         Minimum_Spanning_Tree();
		         Save_Output();
		 		 output.close();
			     line = input.readLine();  //set line for next test case
	     }while(line != null);
		

		input.close();
 }
 
  
 public static void Save_Output() throws IOException
 {
	 output.write(Integer.toString(mst_weight)+'\n');
	 System.out.println(mst_weight);
	 for(int i=0;i<(source_vertice.size());i++)
	 {
		 output.write(source_vertice.get(i).toString()+" ");
		 output.write(destination_vertice.get(i).toString()+'\n');	
		 //output.write(Weight_Of_Edge.get(i).toString() +'\n');	
		 //System.out.println(source_vertice.get(i).toString()+ " "+destination_vertice.get(i).toString()+" "+Weight_Of_Edge.get(i).toString());
	 }
 }
 
 

 public static void reset_heap()
 {
     heap_vertice = new int [number_of_vertices];
     heap_Weight_Of_Node = new int [number_of_vertices];
     for(int i=1;i<number_of_vertices;i++)
     {
         heap_vertice[i]=i;   //  heap_vertice is "vertice" at particular index . i is index of heap
         heap_Weight_Of_Node[i]=Integer.MAX_VALUE; // inititally Weight_Of_Edge of unreachable node 'i' from growing set is infinite 
         get_heap_index_from_vertice[i]=i;
     }
     get_heap_index_from_vertice[0]=0;
     heap_vertice[0]=0;   //actual  Vertice at 0th index of heap = 0
     heap_Weight_Of_Node[0]=0;  //Weight_Of_Edge of Starting node to itself        
 }
 
 
 public static void heapify(int parent)
 {
     int left=2*parent;
     int right=(2*parent)+1;
     int smallest_vertice;
     smallest_vertice=parent;
     if(left<=number_of_vertices&&heap_Weight_Of_Node[left-1]<heap_Weight_Of_Node[parent-1])
     {
    	 smallest_vertice=left;
     }
     else if(right<=number_of_vertices&&heap_Weight_Of_Node[right-1]<heap_Weight_Of_Node[parent-1])
     {
    	 smallest_vertice=right;
     }
     //System.out.println("Smallest"+ smallest);
     
     if(parent!=smallest_vertice)
     {
    	 swap_root_child_node(parent-1,smallest_vertice-1);
    	 heapify(smallest_vertice);
     }
 }
 
 
 public static void  Minimum_Spanning_Tree()
 {
     while(number_of_vertices!=0)
     {

    	 extract_value_from_min_heap();
         Update_Weights_After_Extract_Min();

     } 
 }
 public static void Update_Weights_After_Extract_Min()
 {
	 //System.out.println("size"+adjacent_vertices_list.size());
	 int source_vertex= Integer.MAX_VALUE;
	 int destination_vertex= Integer.MAX_VALUE;
	 int minimum_incident_node= Integer.MAX_VALUE;
     
     for(int i=0;i<Visited_Status.length;i++)
     {
    	 if(Visited_Status[i]=='U') //if element not in MST
         {
        	 //Find minimum element to be added in growing set
             int updated_weight_of_node=Integer.MAX_VALUE;
             //System.out.println("Corresponding_adjacent_Node_Weight_List.get(heap[0][i])"+  Corresponding_adjacent_Node_Weight_List.get(heap[0][get_heap_index_from_vertice[i]]) + "  "+ get_heap_index_from_vertice[i]);
             for(int j=0;j<adjacent_vertices_list.get(i).size();j++)
             {

                 int v=adjacent_vertices_list.get(i).get(j);  // see all neighbors for this unvisited vertice and see if there exists any neighour which is visited

                 if(Visited_Status[v]=='V')  //Element in growing set but neighbor of i which in-turn is not in growing set
                 {
                     if(Corresponding_adjacent_Node_Weight_List.get(i).get(j)<updated_weight_of_node)
                     {
                    	 //System.out.println(Corresponding_adjacent_Node_Weight_List.get(i).get(j) +"  Corresponding_adjacent_Node_Weight_List.get(i)"+  Corresponding_adjacent_Node_Weight_List.get(i));
                    	 updated_weight_of_node=Corresponding_adjacent_Node_Weight_List.get(i).get(j);
                         if(updated_weight_of_node<minimum_incident_node)
                         {
                        	 minimum_incident_node = updated_weight_of_node;
                        	 destination_vertex = i;
                        	 source_vertex=v;
                        }
                     }
                 }
             }
              //System.out.println("updated_weight_of_node  "+updated_weight_of_node + "  "+heap_Weight_Of_Node[get_heap_index_from_vertice[i]]);
             if(updated_weight_of_node<=heap_Weight_Of_Node[get_heap_index_from_vertice[i]]&&Visited_Status[i]=='U')
             {
            	 Decrease_Key(get_heap_index_from_vertice[i],updated_weight_of_node);  //Update the value from infinity to min value
             }

         }
      }
     
    if(minimum_incident_node!=Integer.MAX_VALUE)
	{
    	 Decrease_Key(get_heap_index_from_vertice[destination_vertex],minimum_incident_node);
		 source_vertice.add(source_vertex+1);  //as we subtracted -1 when updating the DB
		 destination_vertice.add(destination_vertex+1);
	}
 }
 
  
 public static void Decrease_Key(int vertice,int value_to_insert)
 {
     heap_Weight_Of_Node[vertice]=value_to_insert;
     int parent;
     while(vertice>0)
     {
         parent=vertice/2;
         if(heap_Weight_Of_Node[parent]>=heap_Weight_Of_Node[vertice])
         {
        	 swap_root_child_node(vertice,parent);
         }
         else
         {
        	 break;
         }
         vertice=vertice/2;
     }
 }
 
 public static void extract_value_from_min_heap()
 {
     
     last_included_vertice = heap_vertice[0];
     if(heap_Weight_Of_Node[0]!=0)
     {
    	 Weight_Of_Edge.add(heap_Weight_Of_Node[0]);
    	 mst_weight+=heap_Weight_Of_Node[0];
     }
     Visited_Status[heap_vertice[0]]='V'; //Extract the minimum value from root and set it as visited
     swap_root_child_node(0,number_of_vertices-1);  //Swap Root and Last node
     number_of_vertices=number_of_vertices-1;
     heapify(1);   

 }

 
public static void swap_root_child_node(int i,int j) //indexes
 {
	 
     
	 get_heap_index_from_vertice[heap_vertice[i]]=j;
	 get_heap_index_from_vertice[heap_vertice[j]]=i;
     int temporary_node=heap_Weight_Of_Node[i];
     heap_Weight_Of_Node[i]=heap_Weight_Of_Node[j];
     heap_Weight_Of_Node[j]=temporary_node;
     
     
     temporary_node=heap_vertice[i];
     heap_vertice[i]=heap_vertice[j];
     heap_vertice[j]=temporary_node;
    
 }
 
 
 
 public static void initialize_all_variables()
 {
	 int i=0;
     mst_weight=0;
     Visited_Status=new char[number_of_vertices];
     get_heap_index_from_vertice=new int[number_of_vertices];
     source_vertice = new ArrayList<Integer>();
     destination_vertice = new ArrayList<Integer>();
     Weight_Of_Edge = new ArrayList<Integer>();
     adjacent_vertices_list=new ArrayList<ArrayList<Integer>>();
     Corresponding_adjacent_Node_Weight_List=new ArrayList<ArrayList<Integer>>();
     
     
     for(i=0;i<number_of_vertices;i++)
     {
    	 Visited_Status[i]='U';  //mark all vertices as unvisited
     }
    
      for (i = 0; i <number_of_vertices; i++) 
      {
    	  adjacent_vertices_list.add(i,new ArrayList<Integer>());
    	  Corresponding_adjacent_Node_Weight_List.add(i,new ArrayList<Integer>());
      }
 
 }
 
 public static void Read_Parameters_For_A_Testcase_And_Update_Data_Structure() throws IOException
 {
	 int i;
	 String[] words;
     for(i=0;i<number_of_edges;++i)
     {
          line = input.readLine();
     	  words = line.split(" ");
	      int u=Integer.valueOf(words[0]);
	      int v=Integer.valueOf(words[1]);
	      int Weight_Of_Edge=Integer.valueOf(words[2]);
	      //System.out.println(u+ "  "+v + "  "+Weight_Of_Edge);
    	  adjacent_vertices_list.get(u-1).add(v-1); 
          Corresponding_adjacent_Node_Weight_List.get(u-1).add(Weight_Of_Edge);
          adjacent_vertices_list.get(v-1).add(u-1);
          Corresponding_adjacent_Node_Weight_List.get(v-1).add(Weight_Of_Edge);
	   }	 
 }
}