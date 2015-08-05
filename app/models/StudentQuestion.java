package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import models.Module.Type;
import play.data.validation.Constraints.*;


@Entity
public class StudentQuestion extends Model {
	
	/********************************
	 ENUMERATOR: For the different possible statuses of a Student Question
	 ********************************/
	public static enum Status {
		IN_REVIEW,
		ACCEPTED,
		REJECTED;
		
		public static Status getType(String typeString) {
			Status type = null;
			
			switch(typeString) {
				case "In Review":
					type = IN_REVIEW;
					break;
				case "Accepted":
					type = ACCEPTED;
					break;
				case "Rejected":
					type = REJECTED;
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
	@Required
	public Long questionId;
	
	@Required
	public Long courseId;
	
	@Required
	public Status status;
	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public StudentQuestion(Long courseId, Long questionId, Status status) {
		this.courseId = courseId;
		this.questionId = questionId;
		this.status = status;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, StudentQuestion> find = new Finder<Long, StudentQuestion>(StudentQuestion.class);
	
	
	
	
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static StudentQuestion create(StudentQuestion questionRecord) {
		questionRecord.save();
		return questionRecord;
	}

	public static void delete(Long id) {
		StudentQuestion questionRecord = find.ref(id);
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
	public static StudentQuestion byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}
	
	
	//-----------Group-------------//

	//Get all List Records in the system 
	public static List<StudentQuestion> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	//Aidan
	//Retuns a list of all Student Questions that are Marked with the given status
	public static List<StudentQuestion> getAllStudentQuestionsForStudentByStatus(Long studentId, String status)
	{
		
	String sql = "select * from student_question" +
				 " where question_id in " +
					"(select id from question" +
			 		" where submitter_id = " + studentId + 
					" and retired = false)" +
					" and status = "+ 
					Status.getType(status).ordinal();

		List<StudentQuestion> questions = new ArrayList<StudentQuestion>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			questions.add(StudentQuestion.byId(row.getLong("id")));
		}
		return questions;
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
	