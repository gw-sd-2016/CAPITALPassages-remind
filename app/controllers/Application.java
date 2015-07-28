package controllers;

import forms.LoginForm;
import models.Course;
import models.Institution;
import models.User;
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
        return ok(index.render());
    }


	/**********************
	 * Load the page to login
	 * @permission A, I, S
	 **********************/
	public Result showLoginPage() {
		//permissions			  A      I      S
		Boolean[] permissions = {true,  true,  true};

		//only allow users with permission to view this page
		if (!User.hasPermission(session("userId"), Arrays.asList(permissions))) {
			return redirect(routes.Application.showIndexPage());
		}

		return ok(login.render(form(LoginForm.class), ""));
	}

	
	public static Result login() {
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
			return redirect((returnUrl.equals("none")) ? "/" : returnUrl);
		}
	}
}
