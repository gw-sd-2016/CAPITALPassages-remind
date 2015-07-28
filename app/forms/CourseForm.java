package forms;

import java.sql.Timestamp;

public class CourseForm {

	public Long instructorId;
	public String name;
	public String description;
//	public Timestamp startDate;
//	public Timestamp endDate;
//	public boolean hasOpenEnrollment;


	public CourseForm() {}
	public CourseForm(Long instructorId, String name, String description/*, Timestamp startDate, Timestamp endDate, boolean hasOpenEnrollment*/) {
		this.instructorId = instructorId;
		this.name = name;
		this.description = description;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.hasOpenEnrollment = hasOpenEnrollment;
	}

	public String validate() {
		if (name.length() <= 0) {
			return "No course name specified! Please provide one, then try again.";
		}

		if (name.length() >= 50) {
			return "Course name is too long! Please provide a smaller one.";
		}
//

		return null;
	}

}
