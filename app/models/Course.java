package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.*;


@Entity
public class Course extends Model {
	
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
	public Timestamp startDate;
	
	public Timestamp endDate;
	
	@Required
	public boolean isArchived;
	
	@Required
	public boolean hasOpenEnrollment;
	
	@ManyToOne
	public User instructor;

	@ManyToMany(cascade = CascadeType.ALL)
	public Set<Message> announcements = new HashSet<Message>();

	@OneToMany(cascade = CascadeType.ALL)
	public Set<Module> modules = new HashSet<Module>();
	
	@ManyToMany
	public Set<User> students = new HashSet<User>();
	
	
	
	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Course(String name, String description, Timestamp startDate, Timestamp endDate, boolean hasOpenEnrollment) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hasOpenEnrollment = hasOpenEnrollment;
	}





	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Course> find = new Finder<Long, Course>(Course.class);
	
	
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Course create(Course course) {
		course.save();
		return course;
	}

	public void delete(Long id) {
		Course course = find.ref(id);
		if (course == null) {
			return;
		}
		
		course.retired = true;
		course.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/
	
	//Get all Courses in the system 
	public static List<Course> all() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	
	//Get Course by ID
	public static Course byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}
	
	//Get Course by name
	public static Course byName(String name) {
		return find.where()
					.ne("retired", true)
					.eq("name", name)
				.findUnique();
	}
	
	
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomeCourses(int numCourses) {
		List<User> allStudents = User.getAllByRole("Student");
		
		Random r = new Random();
		
		for (int i = 0; i < numCourses; i++) {
			User.NameGenerator ng = new User.NameGenerator();
			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
			long diff = end - offset + 1;
			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
			
			
			int randomInst = r.nextInt((User.getAllByRole("Instructor").size() - 1) + 1) + 1;
			List<User> allInstructors = User.getAllByRole("Instructor");
			
			Course course = new Course(ng.getName(), "desc", new Timestamp(offset), rand, r.nextBoolean());
			course.instructor = allInstructors.get(randomInst-1);
			
			int numStudents = r.nextInt(User.getAllByRole("Student").size());
			
			for (int j = 0; j < numStudents; j++) {
				course.students = new HashSet<User>(allStudents.subList(0, j));
			}
			
	    	Module.createSomeModules(r.nextInt(8), course);
	    	
	    	for (int j=0; j<r.nextInt(10); j++) {
				Message message = Message.createAMessage(Message.Type.ANNOUNCEMENT);
				course.announcements.add(message);
			}
			
			course.save();
		}
	}

}
	