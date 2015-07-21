package controllers;

import java.util.Random;

import models.Course;
import models.Institution;
import models.Message;
import models.Module;
import models.User;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result populate() {
    	Institution.createSomeInstitutions(4);
    	
    	User.createSomeUsers(1, User.Role.SUPERADMIN);
    	User.createSomeUsers(2, User.Role.ADMIN);
    	User.createSomeUsers(3, User.Role.INSTRUCTOR);
    	User.createSomeUsers(5, User.Role.STUDENT);
    	
    	Course.createSomeCourses(10);
    	    	
        return redirect(routes.Application.index());
    }
    
    
    public Result index() {	    	
        return ok(index.render());
    }

}
