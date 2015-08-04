package forms;

public class ModuleForm {

	public String msg;

	public ModuleForm() {}
	public ModuleForm(String msg) {
		this.msg = msg;
	}

	public String validate() {
		if (msg.length() <= 0) {
			return "Please provide a message!";
		}

		return null;
	}

}
