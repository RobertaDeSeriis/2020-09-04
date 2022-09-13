package it.polito.tdp.imdb.model;


public class MovieMax implements Comparable<MovieMax>{
	Movie m; 
	double max;
	
	public MovieMax(Movie m, double max) {
		super();
		this.m = m;
		this.max = max;
	}

	public Movie getM() {
		return m;
	}

	public void setM(Movie m) {
		this.m = m;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return m + " (" + max + ")";
	}

	public int compareTo(MovieMax o) {
		// compare to con double ordine decrescente 
		if(this.getMax()>o.getMax())
		{
			return -1;
		}
		if(this.getMax()<o.getMax())
		{
			return 1;
		}
		return 0;
	}
	

}
