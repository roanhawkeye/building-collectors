package org.roan.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.roan.model.Actor;
import org.roan.model.Movie;

public class MainMoviesActors {

	public static void main(String[] args) throws IOException {
		
		Set<Movie> movies = new HashSet<>();
		
		Stream<String> lines = Files.lines(Paths.get("./files/movies-mpaa.txt"));
		
		lines.forEach((String line) -> {
			String[] elements = line.split("/");
			String title = elements[0].substring(0, elements[0].lastIndexOf("(")).trim();
			String releaseYear = elements[0].substring(elements[0].lastIndexOf("(") + 1, elements[0].lastIndexOf(")"));
			
			if (releaseYear.contains(",")){
				// with skip movies with a coma in their title
				return;
			}
			
			Movie movie = new Movie(title, Integer.valueOf(releaseYear));
			
			for(int i = 1; i < elements.length; i++){
				String[] name = elements[i].split(", ");
				String lastName = name[0].trim();
				String firstName = "";
				if( name.length > 1){
					firstName = name[1].trim();
				}
				
				Actor actor = new Actor(lastName, firstName);
				movie.AddActor(actor);
			}
			
			movies.add(movie);
			
		});
		
		System.out.println("# movies = " + movies.size());
	}
	
}