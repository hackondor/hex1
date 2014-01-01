package view.command;

public class CommandExec implements Command {

	private Action action;
	
	public CommandExec(Action a){
		action = a;
	}
	
	@Override
	public boolean execute() {
		return action.run();
	}

}
