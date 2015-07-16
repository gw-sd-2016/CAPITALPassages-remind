package models;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.*;
import utilities.PasswordHash;


@Entity
public class User extends Model {
	
	/********************************
	 ENUMERATOR: For each User type
	 ********************************/
	public static enum Role {
		SUPERADMIN,
		ADMIN,
		INSTRUCTOR,
		STUDENT;
		
		public static Role getRole(String roleString) {
			Role role = null;
			
			switch(roleString) {
				case "Super Administrator":
					role = SUPERADMIN;
					break;
				case "Administrator":
					role = ADMIN;
					break;
				case "Instructor":
					role = INSTRUCTOR;
					break;
				case "Student":
					role = STUDENT;
					break;
				}
			
			return role;
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
	public String firstName;

	@Required
	public String lastName;
	
	@Required
	@Column(unique = true)
	public String username;

	@Required
	public String password;	// hashed using utilities.PasswordHash
	
	@Email
	public String email;
		
	@Required
	public Role type;
	
	public Long creatorId;
	
	@ManyToOne (cascade = CascadeType.ALL)
	public Institution institution;
	
	

	/********************************
	 CONSTRUCTORS
	 ********************************/
	public User(String email, String username, String password, String firstName, String lastName, Role type, Long creatorId) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.email = email;
		this.username = username;
		this.password = PasswordHash.createHash(password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.creatorId = creatorId;
	}
	
//	public User(RegistrationData data, String type) throws NoSuchAlgorithmException, InvalidKeySpecException {
//		this.email = data.email;
//		this.username = data.username;
//		this.password = PasswordHash.createHash(data.password);
//		this.firstName = data.firstName;
//		this.lastName = data.lastName;
//		this.grade = data.grade;
//		this.creatorId = data.creatorId;
//		this.type = Role.getRole(type);
//		
//	}
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, User> find = new Finder<Long, User>(User.class);
	
	
	
	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static User create(User user) {
		user.save();
		return user;
	}

	public static void delete(Long id) {
		User user = find.ref(id);
		if (user == null) {
			return;
		}
		
		user.retired = true;
		user.save();
	}
	
	
	/********************************
	 GETTERS 
	 ********************************/
	
	//Get all Users in the system 
	public static List<User> all() {
		return find.where()
					.ne("retired", true)
				.findList();
	}
	
	// Get all Users in the system by role
	public static List<User> getAllByRole(String role) {
		return find.where()
					.ne("retired", true)
					.eq("type", Role.getRole(role))
					.orderBy().asc("lastName")
				.findList();
	}
	
	//Get User by ID
	public static User byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}

	//Get User by email address
	public static User byEmail(String email) {
		return find.where()
					.ne("retired", true)
					.eq("email", email)
				.findUnique();
	}
	
	//Get User by username
	public static User byUsername(String username) {
		return find.where()
					.eq("username", username)
				.findUnique();
	}
	
	//Get user by username OR email address (used for logging in)
	public static User byUsernameOrEmail(String usernameOrEmail) {
		User user = find.where()
						.ne("retired", true)
						.or(Expr.eq("username", usernameOrEmail), Expr.eq("email", usernameOrEmail))
					.findUnique();
		
		return user;
	}	

	
	
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	
	public static class NameGenerator {

		private List<String> vocals = new ArrayList<String>();
		private List<String> startConsonants = new ArrayList<String>();
		private List<String> endConsonants = new ArrayList<String>();
		private List<String> nameInstructions = new ArrayList<String>();

		public NameGenerator() {
			String demoVocals[] = { "a", "e", "i", "o", "u", "ei", "ai", "ou", "j",
					"ji", "y", "oi", "au", "oo" };

			String demoStartConsonants[] = { "b", "c", "d", "f", "g", "h", "k",
					"l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
					"ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
			"th" };

			String demoEndConsonants[] = { "b", "d", "f", "g", "h", "k", "l", "m",
					"n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
					"sh", "th", "tt", "ss", "pf", "nt" };

			String nameInstructions[] = { "vd", "cvdvd", "cvd", "vdvd" };

			this.vocals.addAll(Arrays.asList(demoVocals));
			this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
			this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
			this.nameInstructions.addAll(Arrays.asList(nameInstructions));
		}

		/**
		 * 
		 * The names will look like this
		 * (v=vocal,c=startConsonsonant,d=endConsonants): vd, cvdvd, cvd, vdvd
		 * 
		 * @param vocals
		 * pass something like {"a","e","ou",..}
		 * @param startConsonants
		 * pass something like {"s","f","kl",..}
		 * @param endConsonants
		 * pass something like {"th","sh","f",..}
		 */
		public NameGenerator(String[] vocals, String[] startConsonants,
				String[] endConsonants) {
			this.vocals.addAll(Arrays.asList(vocals));
			this.startConsonants.addAll(Arrays.asList(startConsonants));
			this.endConsonants.addAll(Arrays.asList(endConsonants));
		}

		/**
		 * see {@link NameGenerator#NameGenerator(String[], String[], String[])}
		 * 
		 * @param vocals
		 * @param startConsonants
		 * @param endConsonants
		 * @param nameInstructions
		 * Use only the following letters:
		 * (v=vocal,c=startConsonsonant,d=endConsonants)! Pass something
		 * like {"vd", "cvdvd", "cvd", "vdvd"}
		 */
		public NameGenerator(String[] vocals, String[] startConsonants,
				String[] endConsonants, String[] nameInstructions) {
			this(vocals, startConsonants, endConsonants);
			this.nameInstructions.addAll(Arrays.asList(nameInstructions));
		}

		public String getName() {
			return firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));
		}

		private int randomInt(int min, int max) {
			return (int) (min + (Math.random() * (max + 1 - min)));
		}

		private String getNameByInstructions(String nameInstructions) {
			String name = "";
			int l = nameInstructions.length();

			for (int i = 0; i < l; i++) { char x = nameInstructions.charAt(0); switch (x) { case 'v': name += getRandomElementFrom(vocals); break; case 'c': name += getRandomElementFrom(startConsonants); break; case 'd': name += getRandomElementFrom(endConsonants); break; } nameInstructions = nameInstructions.substring(1); } return name; } private String firstCharUppercase(String name) { return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1); } private String getRandomElementFrom(List<String> v) {
				return (String) v.get(randomInt(0, v.size() - 1));
			}
		}


	public static void createSomeUsers(int numUsers, Role role) {
		for (int i = 0; i < numUsers; i++) {
			NameGenerator ng = new NameGenerator();
			String name = ng.getName();
			
			Random r = new Random();
		
			User user;
			if (User.byUsername(name) != null) {
				continue;
			}
			try {
				user = new User(name.toLowerCase()+"@email.com", name.toLowerCase(), name.toLowerCase(), name, ng.getName(), role, Long.valueOf(0));
				int instId = r.nextInt((Institution.all().size() - 1) + 1) + 1;
				if (role != Role.STUDENT) {
					user.institution = Institution.byId(Long.valueOf(instId));
				} else {
					Institution inst = Institution.byId(Long.valueOf(instId));
					inst.students.add(user);
					inst.save();
				}
				user.save();
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
	}
	
}