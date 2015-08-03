package forms;

public class CourseForm {

	public Long instructorId;
	public String name;
	public String description;
	public String start;
	public String end;
//	public boolean hasOpenEnrollment;


	public CourseForm() {}
	public CourseForm(Long instructorId, String name, String description, String start, String end) {
		this.instructorId = instructorId;
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
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
