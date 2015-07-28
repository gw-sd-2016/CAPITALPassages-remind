package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import models.Module.Type;
import play.data.validation.Constraints.*;


@Entity
public class Response extends Model {
	
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
	
	@Required
	public Long submitterId;
	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Response(Long entityId, Long submitterId) {
		this.entityId = entityId;
		this.submitterId = submitterId;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Response> find = new Finder<Long, Response>(Response.class);
	
	
	
	
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Response create(Response questionRecord) {
		questionRecord.save();
		return questionRecord;
	}

	public static void delete(Long id) {
		Response questionRecord = find.ref(id);
		if (questionRecord == null) {
			return;
		}
		
		questionRecord.retired = true;
		questionRecord.save();
	}
	
	
	
	
	/********************************
	 GETTERS 
	 ********************************/
	
	//-----------Single-------------//

	//Get single List Record by ID
	public static Response byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}


	//-----------Group-------------//

	//Get all List Records in the system 
	public static List<Response> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomequestionRecords(int numquestionRecords, Course course) {
//		Random r = new Random();
//		
//		final List<Type> VALUES = Collections.unmodifiableList(Arrays.asList(Type.values()));
//
//		for (int i = 0; i < numquestionRecords; i++) {
//			User.NameGenerator ng = new User.NameGenerator();
//			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
//			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
//			long diff = end - offset + 1;
//			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
//			
//			questionRecord questionRecord = new questionRecord("description of questionRecord", rand, r.nextBoolean(), r.nextBoolean(), VALUES.get(r.nextInt(VALUES.size())));
//			
//			questionRecord.questions.addAll(Question.createSomeQuestions(r.nextInt(20), course.instructor));
//			
//			course.save();
//		}
	}
}
	