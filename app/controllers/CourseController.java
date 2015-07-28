package controllers;


import forms.CourseForm;
import models.Course;
import models.User;
import static play.data.Form.*;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.Arrays;

public class CourseController extends Controller {


	/**********************
	 * 	Load the page to create a new Course
	 * @permission A, I
	 **********************/
	public static Result showCreateCoursePage() {
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

		return ok(createCourse.render(form(CourseForm.class)));
	}


	/**********************
	 * 	Load the page to show all Courses for the logged-in instructor
	 * @permission A, I
	 **********************/
	public Result showMyCoursesPage() {
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

		return ok(myCourses.render(loggedInUser));
	}


	/**********************
	 * 	Load the page to show all Courses in the admin's institution
	 * @permission A
	 **********************/
	public Result showAllCoursesPage() {
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

		return ok(allCourses.render(loggedInUser.institution));
	}



	/**********************
	 * Create a new course and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createCourse() {
		User user = User.byId(5L);
		if (user == null) {
			return redirect(routes.Application.showIndexPage());
		}

		Form<CourseForm> newCourseForm = form(CourseForm.class).bindFromRequest();

		if (newCourseForm.hasErrors()) {
			return badRequest(createCourse.render(newCourseForm));
		} else {
			Course newCourse = new Course(5L, newCourseForm.get());
			Course.create(newCourse);
		}
		return redirect(routes.CourseController.showCreateCoursePage());
	}
}
