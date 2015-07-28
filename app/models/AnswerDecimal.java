package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

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
	public double answer;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public AnswerDecimal(double dbl) {
		this.answer = dbl;
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
		AnswerDecimal ad = AnswerDecimal.byAnswer(answer_decimal.answer);
		if (ad != null) {
			return ad;
		} else {
			answer_decimal.save();
			return answer_decimal;
		}
	}

	public static void delete(Long id) {
		AnswerDecimal answer_decimal = find.ref(id);
		if (answer_decimal == null) {
			return;
		}

		answer_decimal.retired = true;
		answer_decimal.save();
	}



	/********************************
	 GETTERS 
	 ********************************/

	//-----------Single-------------//

	//Get AnswerDecimal by ID
	public static AnswerDecimal byId(Long id) {
		return find.where()
					.eq("retired", false)
					.eq("id", id)
				.findUnique();
	}

	//Get AnswerDecimal by text
	public static AnswerDecimal byAnswer(double answer) {
		return find.where()
					.eq("retired", false)
					.eq("answer", answer)
				.findUnique();
	}


	//-----------Group-------------//

	//Get all AnswerDecimals in the system 
	public static List<AnswerDecimal> getAll() {
		return find.where()
					.eq("retired", false)
				.findList();
	}

}
