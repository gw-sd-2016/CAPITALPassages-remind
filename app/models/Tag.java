package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

@Entity
public class Tag extends Model {
	
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
	public String name;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public Tag(String name) {
		this.name = name;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Tag> find = new Finder<Long, Tag>(Tag.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Tag create(Tag tag) {
		tag.save();
		return tag;
	}

	public static void delete(Long id) {
		Tag tag = find.ref(id);
		if (tag == null) {
			return;
		}

		tag.retired = true;
		tag.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Tag by ID
	public static Tag byId(Long id) {
		return find.where()
				.ne("retired", true)
				.eq("id", id)
				.findUnique();
	}

	//Get Tag by name
	public static Tag byName(String name) {
		return find.where()
				.ne("retired", true)
				.eq("name", name)
				.findUnique();
	}

	
	//-----------Group-------------//

	//Get all Tags in the system 
	public static List<Tag> getAll() {
		return find.where()
				.ne("retired", true)
				.findList();
	}





	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////

	public static List<Tag> createSomeTags(int numTags) {
		List<Tag> tags = new ArrayList<Tag>();
		User.NameGenerator ng = new User.NameGenerator();

		for (int i=0; i<numTags; i++) {
			Tag tag = new Tag(ng.getName().toLowerCase());
			tags.add(tag);
		}

		return tags;
	}

}
