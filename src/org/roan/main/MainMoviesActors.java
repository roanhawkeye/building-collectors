package org.roan.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
		
		//# of actors
		long numberOfActors = movies.stream()
				.flatMap(movie -> movie.actors().stream()) //Stream<Actor>
				.distinct()
				.count();
		
		System.out.println("# of actors : " + numberOfActors);
		
		// # of actors that played in the greatest # of movies
		
//		 Map<Actor, Long> collect = 
		
		Entry<Actor, Long> mostViewedActor = movies.stream()
						.flatMap(movie -> movie.actors().stream()) //Stream<Actor>
						.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet().stream() // Stream<Map.Entry<Actor, Long>>
						.max(
								//Comparator.comparing(entry -> entry.getValue())
								Map.Entry.comparingByValue()
							)
						.get();
		
		System.out.println("Most viewed actor: " + mostViewedActor);
		
		// actor that played in the greatest # of movies during a year
		// Map<release year, Map<Actor, # of movies during that year>>
		
//		Map<Integer, HashMap<Actor, AtomicLong>> collect = 
		movies.stream()
			.collect(
					Collectors.groupingBy(
							movie -> movie.releaseYear(),
							Collector.of(
								() -> new HashMap<Actor, AtomicLong>(), //supplier
								(map, movie) -> {
									movie.actors().forEach(
											actor -> map.computeIfAbsent(actor, a -> new AtomicLong()).incrementAndGet()
									);	
								}, //accumulator
								(map1, map2) -> {
									map2.entrySet().forEach(
											entry2 -> map1.merge(
													 	entry2.getKey(),
													 	entry2.getValue(),
													 	(al1, al2) -> {
													 		al1.addAndGet(al2.get());
													 		return al1;
													 	}
													  )
									);
									return map1;
								}, // combiner
								Collector.Characteristics.IDENTITY_FINISH
							)
					)
			)//	Map<Integer, HashMap<Actor, AtomicLong>>
			.entrySet().stream()
			.collect(
					Collectors.toMap(
							entry -> entry.getKey(),
							entry -> entry.getValue().entrySet().stream()
										.max(
												Map.Entry.comparingByValue(Comparator.comparing(l -> l.get()))
										)
										.get()
					)
			) // Map<Integer, Map.Entry<Actor, AtomicLong>>
			 // Map.Entry<Integer, Map.Entry<Actor, AtomicLong>>
			;
		
		
		
	}					
	
}
