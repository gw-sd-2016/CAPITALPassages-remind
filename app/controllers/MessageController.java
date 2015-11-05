package controllers;


import forms.AnnouncementForm;
import forms.UserForm;
import models.Course;
import models.Message;
import models.Prompt;
import models.User;
import static play.data.Form.*;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;


public class MessageController extends Controller {


	/**********************
	 * 	Load the page to create a new Announcement
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
	 * 	Load the page to edit an existing Announcement
	 * @permission A, I
	 **********************/
	public static Result showEditAnnouncementPage(Long announcementId) {
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

		Message announcement = Message.byId(announcementId);
		System.out.println("announcement = " + announcement.id);
		System.out.println("prompt = " + Prompt.byId(announcement.prompt.id));
		System.out.println("text = " + announcement.prompt.text);


		AnnouncementForm data = new AnnouncementForm(announcement.prompt.text);
		Form<AnnouncementForm> form = Form.form(AnnouncementForm.class).fill(data);
		
		return ok(editAnnouncement.render(form, announcement));
	}

	
	/**********************
	 * Create a new Message of type Announcement and add it to the database
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
		flash("success", "Success! Your Announcement has been created.");
//		return ok();
		return redirect(routes.MessageController.showCreateAnnouncementPage(courseId));
	}


	/**********************
	 * Edit an existing Announcement and overwrite its attributes in the database
	 * @permission A, I
	 **********************/
	public static Result modifyAnnouncement(Long announcementId) {
		Form<AnnouncementForm> form = form(AnnouncementForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(editAnnouncement.render(form, Message.byId(announcementId)));
		} else {
			Message announcement = Message.byId(announcementId);
			AnnouncementForm announcementForm = form.get();
			
			Prompt prompt = announcement.prompt;
			prompt.text = announcementForm.msg;
			Prompt.create(prompt);
			announcement.prompt = prompt;

			Message.create(announcement);
		}

		flash("success", "Success! Announcement has been modified.");
		return redirect(routes.MessageController.showEditAnnouncementPage(announcementId));
	}




	public static Result delete(Long messageId) {
		Message message = Message.byId(messageId);
		if (message == null) {
			return ok("false");
		}
		
		Message.delete(messageId);

		return ok("true");
	}
}
