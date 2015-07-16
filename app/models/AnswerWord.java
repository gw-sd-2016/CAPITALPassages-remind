package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

@Entity
public class AnswerWord extends Model {
	
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
	
	public AnswerWord(String text) {
		this.text = text;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, AnswerWord> find = new Finder<Long, AnswerWord>(AnswerWord.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static AnswerWord create(AnswerWord answer_word) {
		answer_word.save();
		return answer_word;
	}

	public static void delete(Long id) {
		AnswerWord answer_word = find.ref(id);
		if (answer_word == null) {
			return;
		}

		answer_word.retired = true;
		answer_word.save();
	}
	
	
	
	

}
