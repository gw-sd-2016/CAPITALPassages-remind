package controllers;


import forms.AnnouncementForm;
import models.Course;
import models.Message;
import models.Prompt;
import models.User;
import static play.data.Form.*;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.Arrays;


public class MessageController extends Controller {


	/**********************
	 * 	Load the page to create a new Course
	 * @permission A, I
	 **********************/
	public static Result showCreateAnnouncementPage(Long courseId) {
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

		Course course = Course.byId(courseId);
		return ok(newAnnouncement.render(form(AnnouncementForm.class), course));
	}

	
	/**********************
	 * Create a new course and add it to the database
	 * @permission A, I
	 **********************/
	public static Result createAnnouncement(Long courseId) {
		Course course = Course.byId(courseId);
		Form<AnnouncementForm> newAnnouncementForm = form(AnnouncementForm.class).bindFromRequest();

		if (newAnnouncementForm.hasErrors()) {
			return badRequest(newAnnouncement.render(newAnnouncementForm, course));
		} else {
			Message announcement = new Message(User.byId(Long.parseLong(session("userId"))), Message.Type.ANNOUNCEMENT, newAnnouncementForm.get().msg);
//			Message.create(announcement);
			Course.addAnnouncement(course, announcement);
		}
		return redirect(routes.CourseController.showCoursePage(courseId));
	}
}
