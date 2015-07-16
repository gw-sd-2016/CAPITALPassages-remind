package models;

import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.Logger;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import models.User.Role;
import play.data.validation.Constraints.*;


@Entity
public class Message extends Model {

	/********************************
	 ENUMERATOR: For each User type
	 ********************************/
	public static enum Type {
		QUESTION,
		ANNOUNCEMENT;

		public static Type getType(String typeString) {
			Type type = null;

			switch(typeString) {
			case "Question":
				type = QUESTION;
				break;
			case "Announcement":
				type = ANNOUNCEMENT;
				break;
			}

			return type;
		}
	}

	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	@Id
	public Long id;

	@Required
	public boolean retired = false;

	@CreatedTimestamp
	public Timestamp createdTime;

	@UpdatedTimestamp
	public Timestamp updatedTime;


	/* Specific */
	/*===========*/
	@Required
	@ManyToOne
	public User creator;

	@ManyToOne
	public Prompt prompt;

	@Required
	public Type type;

	//	@ManyToMany (cascade = CascadeType.ALL)
	//	public Set<Content> content = new HashSet<Content>();

	@ManyToMany (cascade = CascadeType.ALL)
	public Set<User> usersReceiving = new HashSet<User>();



	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Message(User creator, Type type, Prompt prompt) {
		this.creator = creator;
		this.type = type;
		this.prompt = prompt;
	}



	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Message> find = new Finder<Long, Message>(Message.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Message create(Message module) {
		module.save();
		return module;
	}

	public static void delete(Long id) {
		Message module = find.ref(id);
		if (module == null) {
			return;
		}

		module.retired = true;
		module.save();
	}



	/********************************
	 GETTERS 
	 ********************************/

	//Get all Messages in the system 
	public static List<Message> all() {
		return find.where()
				.ne("retired", true)
				.findList();
	}

	//Get Message by ID
	public static Message byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}





	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////

	public static Message createAMessage(Type type) {
		Random r = new Random();
		List<User> allInstructors = User.getAllByRole("Instructor");
		User instructor = allInstructors.get(r.nextInt(allInstructors.size()));

		User.NameGenerator ng = new User.NameGenerator();
		String text = ng.getName();
		for (int i=0; i < r.nextInt(5); i++) {
			text += " " + ng.getName().toLowerCase();
		}
		text += "?";

		Prompt prompt = new Prompt(text);
		Prompt.create(prompt);
		return new Message(instructor, type, prompt);
	}
}
