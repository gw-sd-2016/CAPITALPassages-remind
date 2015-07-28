package models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.Required;

@Entity
public class StudentInInstitution extends Model {
	
	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;
	
//	@Required
//	public boolean retired = false;
	
	@CreatedTimestamp
	public Timestamp createdTime;
//	
//	@UpdatedTimestamp
//	public Timestamp updatedTime;
	
	
	/* Specific */
	/*===========*/
	@Required
	public Long studentId;
	
	@Required
	public Long institutionId;
	
	@Required
	public boolean official;
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public StudentInInstitution(Long studentId, Long institutionId, boolean official) {
		this.studentId = studentId;
		this.institutionId = institutionId;
		this.official = official;
	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, StudentInInstitution> find = new Finder<Long, StudentInInstitution>(StudentInInstitution.class);
	
	
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static StudentInInstitution create(StudentInInstitution studentInInstitution) {
		studentInInstitution.save();
		return studentInInstitution;
	}

	public static void delete(Long id) {
		StudentInInstitution institution = find.ref(id);
		if (institution == null) {
			return;
		}
		
		institution.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	public static StudentInInstitution getByStudentAndInstitution(Long studentId, Long institutionId) {
		return find.where()
				.eq("student_id", studentId)
				.eq("institution_id", institutionId)
			.findUnique();
	}

}