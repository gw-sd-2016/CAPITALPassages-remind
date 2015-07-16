package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

@Entity
public class AnswerText extends Model {
	
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
	
	public AnswerText(String text) {
		this.text = text;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, AnswerText> find = new Finder<Long, AnswerText>(AnswerText.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static AnswerText create(AnswerText answer_text) {
		answer_text.save();
		return answer_text;
	}

	public static void delete(Long id) {
		AnswerText answer_text = find.ref(id);
		if (answer_text == null) {
			return;
		}

		answer_text.retired = true;
		answer_text.save();
	}
	
	
	
	

}
