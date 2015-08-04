package forms;

public class AnnouncementForm {

	public String msg;

	public AnnouncementForm() {}
	public AnnouncementForm(String msg) {
		this.msg = msg;
	}

	public String validate() {
		if (msg.length() <= 0) {
			return "Please provide a message!";
		}

		return null;
	}

}
