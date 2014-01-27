package net.xiangzhao.plugins.test.jrunnerplugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import laser.ddg.visualizer.PrefuseGraphBuilder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.xiangzhao.plugins.test.jrunnerplugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		IOConsole myConsole = findConsole(CONSOLE_NAME);
		IOConsoleInputStream ics = myConsole.getInputStream();
		final IOConsoleOutputStream ocs = myConsole.newOutputStream();
		ocs.setActivateOnWrite(true);
		System.setOut(new PrintStream(ocs));
		System.setIn(ics);
//		final BufferedReader reader = new BufferedReader(new InputStreamReader(
//				ics));
//		new Job("Console read job") {
//
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				// TODO Auto-generated method stub
//				try {
//					String word = null;
//					while (!(word = reader.readLine()).equals("end")) {
//						if (word.equals("ddg")) {
//							PrefuseGraphBuilder.main(new String[] { System
//									.getProperty("user.home")
//									+ "/provenancedata.txt" });
//						}
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return Status.OK_STATUS;
//			}
//		}.schedule();

		// MessageConsole myConsole = findConsole(CONSOLE_NAME);
		// MessageConsoleStream out = myConsole.newMessageStream();
		// out.setActivateOnWrite(true);
		// System.setOut(new PrintStream(out));
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private static final String CONSOLE_NAME = "JRunner Console";

	private IOConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			System.out.println(existing[i].getName());
			if (name.equals(existing[i].getName())) {
				return (IOConsole) existing[i];
			}
		}
		// no console found, so create a new one
		IOConsole myConsole = new IOConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
}
