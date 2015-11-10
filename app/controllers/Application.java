package controllers;

import forms.LoginForm;
import models.Course;
import models.Institution;
import models.User;
import play.Routes;
import play.mvc.*;

import static play.data.Form.*;
import play.data.Form;

import views.html.*;

import java.util.Arrays;


public class Application extends Controller {

    public Result populate() {
    	Institution.createSomeInstitutions(4);
    	
    	User.createSomeUsers(1, User.Role.SUPERADMIN);
    	User.createSomeUsers(2, User.Role.ADMIN);
    	User.createSomeUsers(3, User.Role.INSTRUCTOR);
    	User.createSomeUsers(10, User.Role.STUDENT);
    	
    	Course.createSomeCourses(10);
    	    	
        return redirect(routes.Application.showLoginPage());
    }
    
    
    public Result showIndexPage() {
		//redirect to login page if not already logged in
		if (session("userId") == null || User.byId(Long.parseLong(session("userId"))) == null) {
			return redirect(routes.Application.showLoginPage());
		}
        return ok(index.render());
    }


	/**********************
	 * Load the page to login
	 **********************/
	public Result showLoginPage() {
		return ok(login.render(form(LoginForm.class), ""));
	}

	
	public static Result login() {
		//get the return URL (assuming the user was directed to the login page from another page
		String returnUrl = session("returnUrl");
		if (returnUrl == null) {
			returnUrl = "none";
		}

		Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm, returnUrl));
		} else {
			session().clear();
			User user = User.byUsernameOrEmail(loginForm.get().username);
			session("userId", user.id.toString());
			session("userFirstName", user.firstName);
			session("institutionId", user.institution.id.toString());
			session("institutionName", user.institution.name);
			session("userType", user.type.toString());
			return redirect((returnUrl.equals("none")) ? "/" : returnUrl);
		}
	}





    public Result showAdminTestPage() {
        return ok(admin.render());
    }

    public Result showAdminProfileTestPage() {
        return ok(adminprofile.render());
    }









    public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(
				Routes.javascriptRouter("jsRoutes",
						routes.javascript.MessageController.delete(),
						routes.javascript.MessageController.modifyAnnouncement(),
						routes.javascript.MessageController.createAnnouncement(),
						routes.javascript.CourseController.deleteCourse()
				)
		);
	}
}
