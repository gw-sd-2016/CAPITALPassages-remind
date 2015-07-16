package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.*;


@Entity
public class Institution extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;
	
	@Required
	public boolean retired = false;
	
	@CreatedTimestamp
	public Timestamp createdTime;
	
	@UpdatedTimestamp
	public Timestamp updatedTime;
	
	
	/* Specific */
	/*===========*/
	@Required
	public String name;
	
	public String location;
	
	public String description;
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<User> students = new HashSet<User>();
	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Institution(String name, String location, String description) {
		this.name = name;
		this.location = location;
		this.description = description;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Institution> find = new Finder<Long, Institution>(Institution.class);
	
	
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Institution create(Institution institution) {
		institution.save();
		return institution;
	}

	public static void delete(Long id) {
		Institution institution = find.ref(id);
		if (institution == null) {
			return;
		}
		
		institution.retired = true;
		institution.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/
	
	//Get all Institution in the system 
	public static List<Institution> all() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	
	//Get Institution by ID
	public static Institution byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}
	
	//Get Institution by name
	public static Institution byName(String name) {
		return find.where()
					.ne("retired", true)
					.eq("name", name)
				.findUnique();
	}
	
	
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomeInstitutions(int numInstitutions) {
		for (int i = 0; i < numInstitutions; i++) {
			User.NameGenerator ng = new User.NameGenerator();
			Institution in = new Institution("School of " + ng.getName(), ng.getName() + ", VA", "descriptionnnn");
			in.save();
		}
	}
}
	