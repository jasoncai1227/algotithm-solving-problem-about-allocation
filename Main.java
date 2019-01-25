import java.util.ArrayList;
import java.util.Scanner;

import org.jgrapht.graph.*;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;

class Main
{
	public static void main(String[] args)
	{
		// read the input data (this is some REALLY ugly code...)
		Scanner reader = new Scanner(System.in);
		// number of projects
		int l = reader.nextInt();
		// max students per project
		int[] p = new int[l];
		for(int i = 0; i < l; ++i)
		{
			p[i] = reader.nextInt();
		}
		// number of teachers
		int m = reader.nextInt();
		// max students per teacher
		int[] s = new int[m];
		for(int i = 0; i < m; ++i)
		{
			s[i] = reader.nextInt();
		}
		// teacher preferences (which projects they are willing to supervise)
		reader.nextLine();
		ArrayList<ArrayList<Integer>> t = new ArrayList<ArrayList<Integer>>(m);
		for(int i = 0; i < m; ++i)
		{
			ArrayList<Integer> row = new ArrayList<Integer>();
			String line = reader.nextLine();
			Scanner lineReader = new Scanner(line);
			while(lineReader.hasNextInt())
			{
				row.add(lineReader.nextInt());
			}
			t.add(row);
		}
		// number of students
		int n = reader.nextInt();
		// student preferences (which projects they are willing to study)
		reader.nextLine();
		ArrayList<ArrayList<Integer>> w = new ArrayList<ArrayList<Integer>>(n);
		for(int i = 0; i < n; ++i)
		{
			ArrayList<Integer> row = new ArrayList<Integer>();
			String line = reader.nextLine();
			Scanner lineReader = new Scanner(line);
			while(lineReader.hasNextInt())
			{
				row.add(lineReader.nextInt());
			}
			w.add(row);
		}
		reader.close();

		// TODO: implement your algorithm (i.e. by building a suitable flow network)
		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		String vertex;
		int index;
		int size;
		//source
		graph.addVertex("S");
		//sink
		graph.addVertex("T");
		//student nodes from sources
		String student="s";
		String[] students=new String[n];
		for(index=0;index<n;index++){
			vertex=student+index;
			students[index]=vertex;
			graph.addVertex(vertex);
			graph.addEdge("S",vertex);
			graph.setEdgeWeight(graph.getEdge("S", vertex), 1);
		}
	
		//projects nodes from student nodes
		String project="p";
		String[] projects=new String[l];
		for(index=0;index<l;index++){
			vertex=project+index;
			projects[index]=vertex;
			graph.addVertex(vertex);
		}
		for(index=0;index<n;index++){
			size=w.get(index).size();
			for(int j=0;j<size;j++){
				graph.addEdge(students[index],projects[w.get(index).get(j)]);
				graph.setEdgeWeight(graph.getEdge(students[index],projects[w.get(index).get(j)]), 1);
			}
		}
		//project mirro nodes
		String project_m="p'";
		String[] projects_m=new String[l];
		for(index=0;index<l;index++){
			vertex=project_m+index;
			projects_m[index]=vertex;
			graph.addVertex(vertex);
			graph.addEdge(projects[index],projects_m[index]);
			graph.setEdgeWeight(graph.getEdge(projects[index],projects_m[index]), p[index]);
		}
		
		//teachers nodes from project mirro nodes
		String teacher="t";
		String[] teachers=new String[m];
		for(index=0;index<m;index++){
			vertex=teacher+index;
			teachers[index]=vertex;
			graph.addVertex(vertex);
		}
		
		for(index=0;index<m;index++){
			size=t.get(index).size();
			for(int k=0;k<size;k++){
				graph.addEdge(projects_m[t.get(index).get(k)],teachers[index]);
			if(s[index]>p[t.get(index).get(k)]){
				graph.setEdgeWeight(graph.getEdge(projects_m[t.get(index).get(k)],teachers[index]), p[t.get(index).get(k)]);
				}else if(s[index]<p[t.get(index).get(k)]){
					graph.setEdgeWeight(graph.getEdge(projects_m[t.get(index).get(k)],teachers[index]), s[index]);
				}else graph.setEdgeWeight(graph.getEdge(projects_m[t.get(index).get(k)],teachers[index]), p[t.get(index).get(k)]);
			}
		}
		//sink from teacher nodes
		for(index=0;index<m;index++){
			graph.addEdge(teachers[index],"T");
			graph.setEdgeWeight(graph.getEdge(teachers[index],"T"), s[index]);
		}
		
		EdmondsKarpMFImpl<String, DefaultWeightedEdge> ek = new EdmondsKarpMFImpl<String, DefaultWeightedEdge>(graph);
		double max_flow = ek.calculateMaximumFlow("S", "T");
		
		/* example of building a graph and running ford-fulkerson, using jgrapht
		 * see http://jgrapht.org and http://jgrapht.org/javadoc/
		 * you could use a different library (or implement your own graph etc.)
		 * ... but make sure you appropriately cite any 3rd party libraries you use
		 */
		// SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		// graph.addVertex("a");
		// graph.addVertex("b");
		// graph.addVertex("c");
		// graph.addEdge("a", "b");
		// graph.setEdgeWeight(graph.getEdge("a", "b"), 5);
		// graph.addEdge("b", "c");
		// graph.setEdgeWeight(graph.getEdge("b", "c"), 10);
		// EdmondsKarpMFImpl<String, DefaultWeightedEdge> ek = new EdmondsKarpMFImpl<String, DefaultWeightedEdge>(graph);
		// double max_flow = ek.calculateMaximumFlow("a", "c");
		// the variable max_flow is now the value of the maximum flow on this graph,
		// from vertex "a" to vertex "c"
		String answer;
		if((int)max_flow>=n){
			answer="YES";
		}else answer="NO";
		// TODO: deduce the answer to the original problem, and print it
		// String answer = "YES"; // or "NO"
		System.out.println(answer);
	}
}

