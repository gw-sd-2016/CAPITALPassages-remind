package models;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import forms.CourseForm;
import org.joda.time.format.DateTimeFormatter;
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
	public Date startDate;
	
	public Date endDate;
	
	@Required
	public boolean isArchived;
	
	@Required
	public boolean hasOpenEnrollment;
	
	@ManyToOne
	public User instructor;

	@ManyToMany(cascade = CascadeType.ALL)
	public Set<Message> announcements = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	public Set<Module> modules = new HashSet<>();
	
	@ManyToMany
	public Set<User> students = new HashSet<>();


	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Course(String name, String description, Date startDate, Date endDate, boolean hasOpenEnrollment) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hasOpenEnrollment = hasOpenEnrollment;
	}


	public Course(CourseForm courseForm) throws ParseException {
		SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = from.parse(courseForm.start);
		Date endDate = from.parse(courseForm.end);
		
		this.instructor = User.byId(courseForm.instructorId);
		this.name = courseForm.name;
		this.description = courseForm.description;
		this.startDate = startDate;
		this.endDate = endDate;
//		this.hasOpenEnrollment = courseForm.hasOpenEnrollment;
	}
	


	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Course> find = new Finder<>(Course.class);
	
	
	
	
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

	//-----------Single-------------//

	//Get Course by ID
	public static Course byId(Long id) {
		return find.where()
				.eq("retired", false)
				.eq("id", id)
				.findUnique();
	}

	//Get Course by name
	public static Course byName(String name) {
		return find.where()
				.eq("retired", false)
				.eq("name", name)
				.findUnique();
	}

	
	//-----------Group-------------//

	//Get all Courses in the system 
	public static List<Course> getAll() {
		return find.where()
					.eq("retired", false)
				.findList();
	}
	
	
	//Get all Courses for an instructor
	public static List<Course> getAllCoursesForInstructor(Long instructorId) {
		return find.where()
					.eq("retired", false)
					.eq("instructor_id", instructorId)
				.findList();
	}


	//Get getAll Courses for a student
	public static List<Course> getAllCoursesForStudent(Long studentId) {
		return find
				.where()
					.eq("retired", false)
					.eq("students.id", studentId)
				.findList();
	}


	//Get getAll Courses at an Institution
	public static List<Course> getAllCoursesForInstitution(Long institutionId) {
		String sql = "select * from course " +
					 "where instructor_id in " +
						"(select id from user " +
						"where institution_id="+institutionId +
					 	" and retired=0)" +
					" and retired=0";

		List<Course> allCourses = new ArrayList<>();
		for (SqlRow row : Ebean.createSqlQuery(sql).findList()) {
			allCourses.add(Course.byId(row.getLong("id")));
		}

		return allCourses;
	}
	
	
	
	public static long calculateDaysRemainingForCourse(Course course) {
		Calendar endDay = Calendar.getInstance();
		endDay.setTime(course.endDate);
		
		return (endDay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (24 * 60 * 60 * 1000);
	}
	
	
	public static void addAnnouncement(Course course, Message announcement) {
		course.announcements.add(announcement);
		course.save();
	}
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomeCourses(int numCourses) {
		List<User> allStudents = User.getAllByRole("Student");
		
		Random r = new Random();
		
		for (int i = 0; i < numCourses; i++) {
			//get a random name for the course
			User.NameGenerator ng = new User.NameGenerator();
			
			//get a random start and end time for the course
			long offset = Timestamp.valueOf("2015-07-14 00:00:00").getTime();
			long end = Timestamp.valueOf("2016-12-30 00:00:00").getTime();
			long diff = end - offset + 1;
			Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
			
			//create a new course
			Course course = new Course(ng.getName(), "desc", new Timestamp(offset), rand, r.nextBoolean());
			
			//get a random instructor in the system and set them as the course's instructor
			List<User> allInstructors = User.getAllByRole("Instructor");
			allInstructors.addAll(User.getAllByRole("Administrator"));
			int randomInst = r.nextInt((allInstructors.size() - 1) + 1) + 1;
			course.instructor = allInstructors.get(randomInst-1);
			
			//get a random number of students in the system and add them to the course
			int numStudents = r.nextInt(User.getAllByRole("Student").size());
			course.students = new HashSet<>(allStudents.subList(0, numStudents));
						
			for (User student : course.students) {
				//if the student is not currently associated with the institution, add them to the institution
				//(if adding to an open course, student is not official; if adding to a closed course, student is official)
				System.out.println(student.id);
				System.out.println(course.instructor.institution.id);
				if (StudentInInstitution.getByStudentAndInstitution(student.id, course.instructor.institution.id) == null) {
					StudentInInstitution.create(new StudentInInstitution(student.id, course.instructor.institution.id, !course.hasOpenEnrollment));
				}
			}
			
			//create some modules and add them to the course
	    	Module.createSomeModules(r.nextInt(8), course);
	    	
//	    	//create some messages (Announcements) and add them to the course
//	    	for (int j=0; j<r.nextInt(10); j++) {
//				Message message = Message.createAMessage(Message.Type.ANNOUNCEMENT);
//				course.announcements.add(message);
//			}
			
			course.save();
		}
	}

}
	