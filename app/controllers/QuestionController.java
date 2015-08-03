package controllers;


import forms.CourseForm;
import models.Course;
import models.User;
import static play.data.Form.*;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.text.ParseException;
import java.util.Arrays;

public class QuestionController extends Controller {


	/**********************
	 * 	Load the page to create a new Course
	 * @permission A, I
	 **********************/
	public static Result showCreateQuestionPage() {
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

		return ok(createQuestion.render());
	}
}
