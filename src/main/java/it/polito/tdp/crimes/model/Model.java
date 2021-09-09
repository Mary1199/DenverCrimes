package it.polito.tdp.crimes.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
     
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listaCategorieReati(categoria, mese));
		
		for(Adiacenza a : dao.getAdiacenze(categoria, mese)) {
			if(this.grafo.getEdge(a.getV1(), a.getV2()) == null) {
				Graphs.addEdgeWithVertices(grafo, a.getV1(), 
						a.getV2(), a.getPeso());
			}
		}

	}
	public List<Adiacenza> getArchi(){
	
		double pesoMedio = 0.0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoMedio += this.grafo.getEdgeWeight(e);
		}
		pesoMedio = pesoMedio/this.grafo.edgeSet().size();


		List<Adiacenza> result = new LinkedList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > pesoMedio)
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), 
						this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
		}

		return result;
	}
	
	public List<String> getCategorie(){
		return dao.getCategorie();
	}

}
