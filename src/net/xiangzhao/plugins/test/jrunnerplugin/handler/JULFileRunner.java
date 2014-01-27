/**
 * 
 */
package net.xiangzhao.plugins.test.jrunnerplugin.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import laser.ddg.visualizer.PrefuseGraphBuilder;
import laser.juliette.driver.Driver;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

/**
 * @author xiang
 * 
 */
public class JULFileRunner implements Runnable {

	private IFile file;

	public JULFileRunner(IFile file) {
		// TODO Auto-generated constructor stub
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			if (file == null)
				Driver.main(new String[] { "/home/xiang/LJWorkspace/runtime-EclipseApplication/Test/teaseapartinheritance.jul" });
			else {
				Driver.main(new String[] { file.getLocation().toOSString() });

			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new Job("Console read job") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub
				try {
					String word = null;
					while (!(word = reader.readLine()).equals("end")) {
						if (word.equals("ddg"))
							Display.getDefault().syncExec(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									PrefuseGraphBuilder
											.main(new String[] { System
													.getProperty("user.home")
													+ "/provenancedata.txt" });
								}

							});
					}
				} catch (IOException e) {
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}
}
