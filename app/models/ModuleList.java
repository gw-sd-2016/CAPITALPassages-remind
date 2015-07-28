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

import models.Module.Type;
import play.data.validation.Constraints.*;


@Entity
public class ModuleList extends Model {
	
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
	@Required
	public Timestamp startDate;
	
	@Required
	public Timestamp endDate;
	
	@Required
	public Type type;
	
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<Module> modules = new HashSet<Module>();
	
	@ManyToMany (cascade = CascadeType.ALL)
	public Set<User> students = new HashSet<User>();
	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public ModuleList(Timestamp startDate, Timestamp endDate, Type type) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, ModuleList> find = new Finder<Long, ModuleList>(ModuleList.class);
	
	
	
	
	

	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static ModuleList create(ModuleList moduleList) {
		moduleList.save();
		return moduleList;
	}

	public static void delete(Long id) {
		ModuleList moduleList = find.ref(id);
		if (moduleList == null) {
			return;
		}
		
		moduleList.retired = true;
		moduleList.save();
	}
	
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get single Question List by ID
	public static ModuleList byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	
	//-----------Group-------------//

	//Get all Question Lists in the system 
	public static List<ModuleList> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}
	
	
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomemoduleLists(int nummoduleLists, Course course) {
//		Random r = new Random();
//		
//		final List<Type> VALUES = Collections.unmodifiableList(Arrays.asList(Type.values()));
//
//		for (int i = 0; i < nummoduleLists; i++) {
//			User.NameGenerator ng = new User.NameGenerator();
//			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
//			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
//			long diff = end - offset + 1;
//			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
//			
//			moduleList moduleList = new moduleList("description of moduleList", rand, r.nextBoolean(), r.nextBoolean(), VALUES.get(r.nextInt(VALUES.size())));
//			
//			moduleList.questions.addAll(Question.createSomeQuestions(r.nextInt(20), course.instructor));
//			
//			course.save();
//		}
	}
}
	