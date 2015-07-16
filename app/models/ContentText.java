package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.google.gson.annotations.Expose;

import play.data.validation.Constraints.Required;

@Entity
public class ContentText extends Model {
	
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
	public String text;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public ContentText(String text) {
		this.text = text;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, ContentText> find = new Finder<Long, ContentText>(ContentText.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static ContentText create(ContentText content_text) {
		content_text.save();
		return content_text;
	}

	public static void delete(Long id) {
		ContentText content_text = find.ref(id);
		if (content_text == null) {
			return;
		}

		content_text.retired = true;
		content_text.save();
	}
	
	
	
	

}
