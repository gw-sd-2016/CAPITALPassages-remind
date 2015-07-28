package models;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

@Entity
public class Basis extends Model {
	
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
	public Long entityId;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public Basis(Long entityId) {
		this.entityId = entityId;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Basis> find = new Finder<Long, Basis>(Basis.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Basis create(Basis Basis) {
		Basis.save();
		return Basis;
	}

	public static void delete(Long id) {
		Basis Basis = find.ref(id);
		if (Basis == null) {
			return;
		}

		Basis.retired = true;
		Basis.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Basis by ID
	public static Basis byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	
	//-----------Group-------------//

	//Get all Bases in the system 
	public static List<Basis> getAll() {
		return find.where()
				.eq("retired", false)
				.findList();
	}





}
