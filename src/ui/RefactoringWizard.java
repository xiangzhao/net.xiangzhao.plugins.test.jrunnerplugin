package ui;

import org.eclipse.jface.wizard.Wizard;

public class RefactoringWizard extends Wizard {

	public RefactoringWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
