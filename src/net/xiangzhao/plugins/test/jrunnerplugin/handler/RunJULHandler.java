/**
 * 
 */
package net.xiangzhao.plugins.test.jrunnerplugin.handler;

import java.io.File;
import java.io.IOException;

import laser.ddg.visualizer.PrefuseGraphBuilder;
import laser.juliette.agent.AbstractAgent;
import laser.juliette.driver.Driver;

import net.xiangzhao.plugins.test.jrunnerplugin.FileUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author xiang
 * 
 */
public class RunJULHandler extends AbstractHandler {

	private static final String CONSOLE_NAME = "JRunner Console";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		ISelection currentSel = HandlerUtil.getActiveSite(event)
				.getSelectionProvider().getSelection();
		if (currentSel instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) currentSel;

			Object firstElement = selection.getFirstElement();
			IFile file = (IFile) Platform.getAdapterManager().getAdapter(
					firstElement, IFile.class);
			if (file == null) {
				if (firstElement instanceof IAdaptable) {
					file = (IFile) ((IAdaptable) firstElement)
							.getAdapter(IFile.class);
				}
			}
			if (file != null) {
				try {
					new Thread(new JULFileRunner(file)).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			new Thread(new JULFileRunner(null)).start();
		}

		return null;
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			System.out.println(existing[i].getName());
			if (name.equals(existing[i].getName())) {
				return (MessageConsole) existing[i];
			}
		}
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	private void executeShellCmd(String cmd) throws IOException,
			InterruptedException {
		Process pr = Runtime.getRuntime().exec(cmd);
		pr.waitFor();
	}

	public String getCurrentEditorContent() {
		ITextEditor activeEditor = (ITextEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor == null)
			return null;
		IEditorInput input = activeEditor.getEditorInput();
		IDocumentProvider provider = activeEditor.getDocumentProvider();
		IDocument document = provider.getDocument(input);

		if (document == null)
			return null;

		return document.get();
	}

	private void printCompilationUnitDetails(ICompilationUnit unit)
			throws JavaModelException {
		System.out.println("Source file " + unit.getElementName());
		Document doc = new Document(unit.getSource());
		System.out.println("Has number of lines: " + doc.getNumberOfLines());
		printIMethods(unit);
	}

	private void printIMethods(ICompilationUnit unit) throws JavaModelException {
		IType[] allTypes = unit.getAllTypes();
		for (IType type : allTypes) {
			printIMethodDetails(type);
		}
	}

	private void printIMethodDetails(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for (IMethod method : methods) {

			System.out.println("Method name " + method.getElementName());
			System.out.println("Signature " + method.getSignature());
			System.out.println("Return Type " + method.getReturnType());

		}
	}
}
