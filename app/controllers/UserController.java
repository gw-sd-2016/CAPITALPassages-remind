package controllers;

import forms.UserForm;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.allInstructors;
import views.html.allStudents;
import views.html.createInstructor;
import views.html.instructorProfile;
import views.html.studentProfile;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static play.data.Form.form;


public class UserController extends Controller {

	/**********************
	 * Create a new course and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createInstructor() {
		Form<UserForm> newInstructorForm = form(UserForm.class).bindFromRequest();

		if (newInstructorForm.hasErrors()) {
			return badRequest(createInstructor.render(newInstructorForm));
		} else {
			User newInstructor = null;
			try {
				newInstructor = new User(newInstructorForm.get());
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			User.create(newInstructor);
		}
		return redirect(routes.UserController.showCreateInstructorPage());
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

		return ok(createInstructor.render(form(UserForm.class)));
	}
}
