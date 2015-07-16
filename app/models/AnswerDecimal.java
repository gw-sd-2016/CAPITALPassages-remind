package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

@Entity
public class AnswerDecimal extends Model {
	
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
	public double dbl;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public AnswerDecimal(double dbl) {
		this.dbl = dbl;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, AnswerDecimal> find = new Finder<Long, AnswerDecimal>(AnswerDecimal.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static AnswerDecimal create(AnswerDecimal answer_decimal) {
		answer_decimal.save();
		return answer_decimal;
	}

	public static void delete(Long id) {
		AnswerDecimal answer_decimal = find.ref(id);
		if (answer_decimal == null) {
			return;
		}

		answer_decimal.retired = true;
		answer_decimal.save();
	}
	
	
	
	

}
