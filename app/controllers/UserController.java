package controllers;

import forms.UserForm;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Arrays;

import static play.data.Form.form;


public class UserController extends Controller {

	/**********************
	 * Create a new course and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createInstructor() throws InvalidKeySpecException, NoSuchAlgorithmException {
		Form<UserForm> newInstructorForm = form(UserForm.class).bindFromRequest();

		if (newInstructorForm.hasErrors()) {
			return badRequest(newInstructor.render(newInstructorForm));
		} else {
			User newInstructor = new User(newInstructorForm.get());
			User.create(newInstructor);
		}
		return redirect(routes.UserController.showAllInstructorsPage());
	}


	/**********************
	 * Edit an existing instructor and overwrite their attributes in the database
	 * @permission A, I
	 **********************/
	public static Result modifyInstructor(String username) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Form<UserForm> form = form(UserForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(editInstructor.render(form, User.byUsername(username)));
		} else {
			User instructor = User.byUsername(username);
			UserForm instructorForm = form.get();
			instructor.firstName = instructorForm.firstName;
			instructor.lastName = instructorForm.lastName;
			instructor.username = instructorForm.username;
			instructor.email = instructorForm.email;
			instructor.password = instructorForm.password;
			
			User.create(instructor);
		}
		
		flash("success", "Instructor's profile has been modified.");
		return redirect(routes.UserController.showInstructorProfilePage(username));
	}


	/**********************
	 * Edit an existing student and overwrite their attributes in the database
	 * @permission A, I
	 **********************/
	public static Result modifyStudent(String username) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Form<UserForm> form = form(UserForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(editStudent.render(form, User.byUsername(username)));
		} else {
			User student = User.byUsername(username);
			UserForm studentForm = form.get();
			student.firstName = studentForm.firstName;
			student.lastName = studentForm.lastName;
			student.username = studentForm.username;
			student.email = studentForm.email;
			student.password = studentForm.password;

			User.create(student);
		}

		flash("success", "Student's profile has been modified.");
		return redirect(routes.UserController.showStudentProfilePage(username));
	}
	
	
	/**********************
	 * Load the page to view a single Student’s profile
	 * @permission A, I, S
	 * @param username - username of the student User being viewed
	 **********************/
	public Result showStudentProfilePage(String username) {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  true,  true};

		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}

		//redirect if desired profile doesn't exist
		else if (!User.exists(User.byUsername(username))) {
			return redirect(routes.UserController.showAllStudentsPage());
		}


		return ok(studentProfile.render(User.byUsername(username)));
	}


	/**********************
	 * Load the page to view a single Instructor’s profile
	 * @permission A, I
	 * @param username - username of the instructor User being viewed
	 **********************/
	public Result showInstructorProfilePage(String username) {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  true,  false};

		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}
		
		//redirect if desired profile doesn't exist
		else if (!User.exists(User.byUsername(username))) {
			return redirect(routes.UserController.showAllInstructorsPage());
		}
		
		return ok(instructorProfile.render(User.byUsername(username)));
	}


	/**********************
	 * Load the page to view all Instructors in an Institution
	 * @permission A
	 **********************/
	public Result showAllInstructorsPage() {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  false,  false};

		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(allInstructors.render(loggedInUser.institution));
	}


	/**********************
	 * Load the page to view all Students in an Institution
	 * @permission A, I
	 **********************/
	public Result showAllStudentsPage() {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  true,  false};
		
		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}
		
		return ok(allStudents.render(loggedInUser.institution));
	}

	/**********************
	 * Load the page to create a new instructor
	 * @permission A
	 **********************/
	public static Result showCreateInstructorPage() {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  false,  false};

		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(newInstructor.render(form(UserForm.class)));
	}



	/**********************
	 * Load the page to edit an instructor
	 * @permission A, I
	 **********************/
	public static Result showEditInstructorPage(String username) {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  true,  false};

		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}
		
		//only let logged in instructor edit their own page
		if (loggedInUser.type.equals(User.Role.getRole("Instructor"))) {
			if (loggedInUser.id != User.byUsername(username).id) {
				return redirect(routes.Application.showIndexPage());
			}
		}

		User instructor = User.byUsername(username);
		//TODO: hash password when editing
		UserForm data = new UserForm(instructor.firstName, instructor.lastName, instructor.email, instructor.username, "", "", instructor.creatorId);
		Form<UserForm> form = Form.form(UserForm.class).fill(data);

		return ok(editInstructor.render(form, User.byUsername(username)));
	}



	/**********************
	 * Load the page to edit an instructor
	 * @permission A
	 **********************/
	public static Result showEditStudentPage(String username) {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  false,  false};

		//logged-in user
		User loggedInUser = User.byId(Long.parseLong(session("userId")));

		//redirect to login page if session info is inaccurate
		if (!User.exists(loggedInUser)) {
			return redirect(routes.Application.showLoginPage());
		}

		//only allow users with permission to view this page
		else if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}

		User student = User.byUsername(username);

		UserForm data = new UserForm(student.firstName, student.lastName, student.email, student.username, "", "", student.creatorId);
		Form<UserForm> form = Form.form(UserForm.class).fill(data);

		return ok(editStudent.render(form, User.byUsername(username)));
	}

}

