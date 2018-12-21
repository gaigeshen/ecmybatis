package ecmybatis;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Auto generated
 * 
 * @author gaigeshen
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "me.gaigeshen.eclipse.ecmybatis"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	public Activator() { }

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return The shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * Returns image descriptor of path
	 * 
	 * @param path The path
	 * @return Image descriptor
	 */
	public static ImageDescriptor imageDescriptor(String path) {
	  return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * 
	 * @return
	 */
	public static IPreferenceStore preferenceStore() {
	  return plugin.getPreferenceStore();
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 */
  public static InputStream resourceFile(String path) throws IOException {
    return getDefault().getBundle().getResource(path).openStream();
  }
	
}
