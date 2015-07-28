package models;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

@Entity
public class Content extends Model {
	
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
	public Long entityId;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public Content(Long entityId) {
		this.entityId = entityId;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Content> find = new Finder<Long, Content>(Content.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Content create(Content Basis) {
		Basis.save();
		return Basis;
	}

	public static void delete(Long id) {
		Content content = find.ref(id);
		if (content == null) {
			return;
		}

		content.retired = true;
		content.save();
	}
	
	
	
	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get Basis by ID
	public static Content byId(Long id) {
		return find.where()
					.ne("retired", true)
					.eq("id", id)
				.findUnique();
	}

	
	//-----------Group-------------//

	//Get all Bases in the system 
	public static List<Content> getAll() {
		return find.where()
					.ne("retired", true)
				.findList();
	}



	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////
	
	public static void createSomeContent(Long userId, int type) {
		User.NameGenerator ng = new User.NameGenerator();
		Random r = new Random();
		
		switch(type) {
			case 0:
				ContentFile cf = new ContentFile(ng.getName().toLowerCase(), "pdf", userId);
				ContentFile.create(cf);
				Content.create(new Content(cf.id));
				break;
			case 1:
				String text = ng.getName();
				for (int i=0; i < r.nextInt(200); i++) {
					text += ng.getName().toLowerCase();
				}
				ContentText ct = new ContentText(text, userId);
				ContentText.create(ct);
				Content.create(new Content(ct.id));
				break;
			default:
				break;
		}
		
	}



}
