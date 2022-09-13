package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao; 
	List<Movie> vertici; 
	List<Adiacenza> archi; 
	Map<Integer, Movie> idMap;
	Graph<Movie, DefaultWeightedEdge> grafo; 
	double p;
	List<Movie> migliore;
	
	
	public Model() {
		this.dao= new ImdbDAO(); 
	}
	
	public String creaGrafo(double r) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<>(); 
		vertici= dao.listVertici(idMap);
		archi= dao.listArchi(idMap, r);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		
		for(Adiacenza a : archi) {
			Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
		}
		
		return "Grafo creato!\n# Vertici:"+grafo.vertexSet().size()+ "\n# Archi: "+grafo.edgeSet().size();	
	}
	
	
	public MovieMax getMAx() {
		List<MovieMax> bilancio= new LinkedList<>(); 
		 for(Movie mo: vertici) {
			 double bil= this.calcolaPeso(mo);
			 bilancio.add(new MovieMax(mo, bil));
		 }
		 Collections.sort(bilancio);
		 return bilancio.get(0); 
	 	 
	 }

	public List<Movie> getVertici() {
		return vertici;
	}

	public double calcolaPeso(Movie m) {
		double peso=0;
			List<Movie> vicini= Graphs.neighborListOf(this.grafo, m);
			for(Movie m1: vicini) {
			peso+=grafo.getEdgeWeight(this.grafo.getEdge(m, m1));
			
		}
		return peso;
	}
	
	public List<Movie> calcolaPercorso(Movie sorg)
	{
		migliore = new LinkedList<Movie>();
		List<Movie> parziale = new LinkedList<>();
		parziale.add(sorg);
		cercaRicorsiva(parziale);
		return migliore;
	}

	private void cercaRicorsiva(List<Movie> parziale) {
		 
			//condizione di terminazione
			if(parziale.size()>migliore.size()) { //percorso più lungo
						migliore = new LinkedList<>(parziale);
					}

				
				Movie ultimo= parziale.get(parziale.size()-1);
				Movie penultimo=null;
				
				if(parziale.size()>1)
					penultimo= parziale.get(parziale.size()-2);
				
				for(Movie v:Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) //scorro sui vicini dell'ultimo nodo sulla lista
				{
					if(!parziale.contains(v) && parziale.size()==1) {
						parziale.add(v);
						cercaRicorsiva(parziale);
						parziale.remove(parziale.size()-1);
						 
					} //peso dell'arco precedente minore di quello dell'arco successivo
					else if(!parziale.contains(v) && 
							grafo.getEdgeWeight(grafo.getEdge(ultimo,v))>=grafo.getEdgeWeight(grafo.getEdge(penultimo,ultimo)))
					{
						parziale.add(v);
						cercaRicorsiva(parziale);
						parziale.remove(parziale.size()-1);
						 
					}
				}
				
	}
	
	
}
