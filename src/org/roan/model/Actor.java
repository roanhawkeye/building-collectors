package org.roan.model;

import java.util.Objects;

public class Actor {
	
	private String lastname, firstname;
	
	public Actor(String lastName, String firstName){
		this.lastname = lastName;
		this.firstname = firstName;
	}
	
	public String lastName(){
		return this.lastname;
	}
	
	public String firstName(){
		return this.firstname;
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.lastname);
		hash = 67 * hash + Objects.hashCode(this.firstname);
		return hash;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		
		if(getClass() != obj.getClass()){
			return false;
		}
		
		final Actor other = (Actor) obj;
		if (!Objects.equals(this.lastname, other.lastname)){
			return false;
		}
		return Objects.equals(this.firstname, other.firstname);
	}
	
	@Override
	public String toString(){
		return "Actor {" + "lastName=" + lastname + ", firstName=" + firstname + '}';
	}
	
}
