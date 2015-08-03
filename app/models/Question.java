package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.*;


@Entity
public class Question extends Model {
	
	/********************************
	 ENUMERATOR: For each Question type
	 ********************************/
	public static enum Type {
		PRACTICE,
		EVALUATION,
		SURVEY;
		
		public static Type getType(String typeString) {
			Type type = null;
			
			switch(typeString) {
				case "Practice":
					type = PRACTICE;
					break;
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
	@OneToOne
	public Message message;
	
	@Required
	public Type type;

//	@Required
//	EvaluationType evalType;
	
	@Required
	public User submitter;

	@Required
	public boolean hasSubquestions;

	@Required
	public boolean isGlobal;

	public Question parentQuestion;

	
	@OneToMany (cascade = CascadeType.ALL)
	public Set<Choice> choices = new HashSet<Choice>();
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Basis> bases = new HashSet<Basis>();
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Tag> tags = new HashSet<Tag>();


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Question(Message message, Type type, User submitter, boolean hasSubquestions, boolean isGlobal, Question parentQuestion) {
		this.message = message;
		this.type = type;
		this.submitter = submitter;
		this.hasSubquestions = hasSubquestions;
		this.isGlobal = isGlobal;
//		this.parentQuestion = parentQuestion;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Question> find = new Finder<Long, Question>(Question.class);
	
	
	

	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Question create(Question question) {
		question.save();
		return question;
	}

	public static void delete(Long id) {
		Question question = find.ref(id);
		if (question == null) {
			return;
		}
		
		question.retired = true;
		question.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/
	
	//-----------Single-------------//

	//Get Institution by ID
	public static Question byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}
	
	//Get Institution by name
	public static Question byName(String name) {
		return find.where()
					.ne("retired", true)
					.eq("name", name)
				.findUnique();
	}


	//-----------Group-------------//

	//Get all Institution in the system 
	public static List<Question> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}


	//Get all Institution in the system 
	public static List<Question> getAllQuestionsForModule(Module module) {
		List<Long> questionIds = new ArrayList<>();
		for (Question question : module.questions) {
			questionIds.add(question.id);
		}
		return find.where()
				.ne("retired", true)
				.in("id", questionIds)
				.findList();
	}
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static List<Question> createSomeQuestions(int numQuestions, User instructor) {
		List<Question> questions = new ArrayList<Question>();
		Random r = new Random();
	
		final List<Type> TYPES = Collections.unmodifiableList(Arrays.asList(Type.values()));
		
		for (int i=0; i < numQuestions; i++) {
			Message message = Message.createAMessage(Message.Type.QUESTION);
			message.save();
			
			Question question = new Question(message, TYPES.get(r.nextInt(TYPES.size())), instructor, r.nextBoolean(), r.nextBoolean(), null);
			question.tags.addAll(Tag.createSomeTags(r.nextInt(5)));
			
			Choice.createSomeChoices(question, r.nextInt((4 - 2) + 1) + 2, r.nextInt(4));
			
			questions.add(question);
			question.save();
		}
		
		return questions;
	}
}
	