package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import models.Module.Type;
import play.data.validation.Constraints.*;


@Entity
public class ListRecord extends Model {
	
	/********************************
	 ENUMERATOR: For each Question List type
	 ********************************/
	public static enum Type {
		EVALUATION,
		SURVEY;
		
		public static Type getType(String typeString) {
			Type type = null;
			
			switch(typeString) {
				case "Evaluation":
					type = EVALUATION;
					break;
				case "Survey":
					type = SURVEY;
					break;
				}
			
			return type;
		}
	}
	
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
	@ManyToOne
	public Long listId;
	
	@ManyToOne
	public Long submitterId;
	
	@Required
	public int attemptNumber;
	
	@Required
	public double timeToComplete;
	
	@Required
	public boolean isCleared;
	

	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public ListRecord(Long submitterId, Long listId, double timeToComplete, int attemptNumber, boolean isCleared) {
		this.submitterId = submitterId;
		this.listId = listId;
		this.timeToComplete = timeToComplete;
		this.attemptNumber = attemptNumber;
		this.isCleared = isCleared;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, ListRecord> find = new Finder<Long, ListRecord>(ListRecord.class);
	
	
	
	
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static ListRecord create(ListRecord listRecord) {
		listRecord.save();
		return listRecord;
	}

	public static void delete(Long id) {
		ListRecord listRecord = find.ref(id);
		if (listRecord == null) {
			return;
		}
		
		listRecord.retired = true;
		listRecord.save();
	}
	
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get single List Record by ID
	public static ListRecord byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}


	//-----------Group-------------//

	//Get all List Records in the system 
	public static List<ListRecord> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}
	
	
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomelistRecords(int numlistRecords, Course course) {
//		Random r = new Random();
//		
//		final List<Type> VALUES = Collections.unmodifiableList(Arrays.asList(Type.values()));
//
//		for (int i = 0; i < numlistRecords; i++) {
//			User.NameGenerator ng = new User.NameGenerator();
//			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
//			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
//			long diff = end - offset + 1;
//			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
//			
//			listRecord listRecord = new listRecord("description of listRecord", rand, r.nextBoolean(), r.nextBoolean(), VALUES.get(r.nextInt(VALUES.size())));
//			
//			listRecord.questions.addAll(Question.createSomeQuestions(r.nextInt(20), course.instructor));
//			
//			course.save();
//		}
	}
}
	