package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
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
	
	@Required
	public String location;
	
	public String description;

	
	
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
	
	//-----------Single-------------//
	
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

	
	//-----------Group-------------//

	//Get all Institutions in the system 
	public static List<Institution> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}
	public static List<Institution> getAllInstitutionsForStudent(Long studentId) {
		String sql = "select * from institution " +
					 "where id in " +
						"(select institution_id from student_in_institution " +
						"where student_id="+studentId+")";
		
		List<Institution> allInstitutions = new ArrayList<Institution>();
		
		List<SqlRow> rows = Ebean.createSqlQuery(sql).findList();
		for (SqlRow row : rows) {
			allInstitutions.add(Institution.byId(row.getLong("id")));
		}
		
		return allInstitutions;
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
	