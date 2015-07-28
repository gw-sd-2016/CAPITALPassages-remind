package models;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.google.gson.annotations.Expose;

import play.data.validation.Constraints.Required;

@Entity
public class Choice extends Model {
	
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
	@Expose
	public Long entity_id;	// e.g. Word.id
	
	@Required
	@Expose
	public boolean isCorrect;
	
	@Required
	public boolean isActive;


	
	/********************************
	 CONSTRUCTORS
	 ********************************/
	
	public Choice(Long entity_id, boolean isCorrect, boolean isActive) {
		this.entity_id = entity_id;
		this.isCorrect = isCorrect;
		this.isActive = isActive;
	}
	
	
	
	/********************************
	 FINDER
	 ********************************/
	//Initialize Ebean Finder
	public static Finder<Long, Choice> find = new Finder<Long, Choice>(Choice.class);




	/********************************
	 CREATE / DELETE 
	 ********************************/
	public static Choice create(Choice choice) {
		choice.save();
		return choice;
	}

	public static void delete(Long id) {
		Choice choice = find.ref(id);
		if (choice == null) {
			return;
		}

		choice.retired = true;
		choice.save();
	}
	
	
	
	/********************************
	 GETTERS / SETTERS
	 ********************************/
	
	//-----------Single-------------//

	//get all Choices in the system
	public static List<Choice> getAll() {
		return find.where()
				.eq("retired", false)
			.findList();
	}
	
	
	////////////////////////////////////////////
	//TEST
	////////////////////////////////////////////

	public static void createSomeChoices(Question question, int numChoices, int type) {
		Random r = new Random();

		User.NameGenerator ng = new User.NameGenerator();
		String text = ng.getName().toLowerCase() + " ";
		for (int i=0; i < r.nextInt(2); i++) {
			text += ng.getName().toLowerCase() + " ";
		}
		text = text.substring(0, text.length()-1);

		switch(type) {
			case 0:
				for (int i=0; i < numChoices; i++) {
					boolean correct = r.nextBoolean();
					AnswerDecimal ad = new AnswerDecimal(r.nextDouble()*r.nextInt(10));
					ad.save();
					question.choices.add(new Choice(ad.id, correct, true));
					if (correct) {
						question.bases.add(new Basis(ad.id));
					}
				}
				break;
			case 1:
				for (int i=0; i < numChoices; i++) {
					boolean correct = r.nextBoolean();
					AnswerInteger ai = new AnswerInteger(r.nextInt(200));
					ai.save();
					question.choices.add(new Choice(ai.id, correct, true));
					if (correct) {
						question.bases.add(new Basis(ai.id));
					}
				}
				break;
			case 2:
				for (int i=0; i < numChoices; i++) {
					boolean correct = r.nextBoolean();

					String phrase = ng.getName().toLowerCase();
					for (int j=0; j < r.nextInt((100 - 1) + 1) + 1; j++) {
						phrase += ng.getName().toLowerCase() + " ";
					}
					phrase = phrase.substring(0, phrase.length()-1);
					
					AnswerText at = new AnswerText(phrase);
					at.save();
					question.choices.add(new Choice(at.id, correct, true));
					if (correct) {
						question.bases.add(new Basis(at.id));
					}
				}
				break;
			case 3:
				for (int i=0; i < numChoices; i++) {
					boolean correct = r.nextBoolean();

					AnswerWord aw = new AnswerWord(ng.getName().toLowerCase());
					aw.save();
					question.choices.add(new Choice(aw.id, correct, true));
					if (correct) {
						question.bases.add(new Basis(aw.id));
					}
				}
				break;
			default:
				break;
		}
	}

}
