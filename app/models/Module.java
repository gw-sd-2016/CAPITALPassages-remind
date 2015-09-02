package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.*;


@Entity
public class Module extends Model {
	
	/********************************
	 ENUMERATOR: For each Module type
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


//		public static String toString(Type type) {
//			return type.toString();
//		}
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
	public String name;
	
	public String description;
	
	@Required
	public int orderIndex;
	
	@Required
	public Timestamp releaseDate;
	
	@Required
	public boolean hasSpacedRepetition;
	
	@Required
	public boolean isHidden;
	
	@Required
	public Type type;
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Question> questions = new HashSet<Question>();
	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Module(String name, String description, Timestamp releaseDate, boolean hasSpacedRepetition, boolean isHidden, Type type) {
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.hasSpacedRepetition = hasSpacedRepetition;
		this.isHidden = isHidden;
		this.type = type;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Module> find = new Finder<Long, Module>(Module.class);
	
	
	
	
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Module create(Module module) {
		module.save();
		return module;
	}

	public static void delete(Long id) {
		Module module = find.ref(id);
		if (module == null) {
			return;
		}
		
		module.retired = true;
		module.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Module by ID
	public static Module byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}
	
	
	//-----------Group-------------//

	//Get all Modules in the system 
	public static List<Module> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}


	//Get all Modules for an instructor
	public static List<Module> getAllModulesForCourse(Long courseId) {
		return find.where()
				.eq("retired", false)
				.eq("course_id", courseId)
				.findList();
	}


	//Get all Modules for an instructor
	public static List<Module> getAllModulesForCourseByType(Long courseId, Type type) {
		return find.where()
				.eq("retired", false)
				.eq("course_id", courseId)
				.eq("type", type)
				.findList();
	}
	
	
	//Get all Modules for an instructor
	public static List<Module> getAllModulesForInstructor(Long instructorId) {
		List<Long> li = new ArrayList<Long>();

		//get all courses for the given instructor
		for(Course course: Course.find.select("id").where().eq("retired", false).eq("instructor_id", instructorId).findList()) {
		    li.add(course.id);
		}
		
		return find.where()
					.eq("retired", false)
					.in("course_id", li)
				.findList();
	}


	//Get all Modules of a specific type for an instructor
	public static List<Module> getAllModulesForInstructorByType(Long instructorId, Type type) {
		List<Long> li = new ArrayList<Long>();

		//get all courses for the given instructor
		for(Course course: Course.find.select("id").where().eq("retired", false).eq("instructor_id", instructorId).findList()) {
			li.add(course.id);
		}

		return find.where()
				.eq("retired", false)
				.in("course_id", li)
				.eq("type", type)
				.findList();
	}
	
	
	
//	public static List<String> getAllModuleTypes() {
//		List<String> typeStrings = new ArrayList<>();
//		for (Type type : Type.values()) {
//			typeStrings.add(type.toString());
//		}
//		return typeStrings;
//	}
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomeModules(int numModules, Course course) {
		Random r = new Random();
		
		final List<Type> VALUES = Collections.unmodifiableList(Arrays.asList(Type.values()));

		for (int i = 0; i < numModules; i++) {
			User.NameGenerator ng = new User.NameGenerator();
			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
			long diff = end - offset + 1;
			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
			
			Module module = new Module(ng.getName(), "description of module", rand, r.nextBoolean(), r.nextBoolean(), VALUES.get(r.nextInt(VALUES.size())));
			
			module.questions.addAll(Question.createSomeQuestions(r.nextInt(20), course.instructor));
			module.orderIndex = course.modules.size();
			
			course.modules.add(module);
			course.save();
		}
	}
}
	