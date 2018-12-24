package ecmybatis.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import ecmybatis.Activator;
import ecmybatis.util.MonitorUserJob;

/**
 * 初始化基础类处理器
 * 
 * @author gaigeshen
 */
public class InitializeBaseClassesDialogHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell shell = HandlerUtil.getActiveShell(event);
    
    new MonitorUserJob("Show dialog for input informations to base classes generation") {
      @Override
      protected void doWork() throws Exception {
        shell.getDisplay().asyncExec(() -> {
          InitializeBaseClassesDialog dialog = new InitializeBaseClassesDialog(shell);
          if (dialog.open() == Window.OK) {
            try {
              Initializer.create().generate(dialog.getPackagesRoot(), dialog.getBasePackage(), dialog.getDomainPackage(), dialog.getDaoPackage());
            } catch (Exception e) {
              ErrorDialog.openError(
                  shell, null, "Some error occurred, see details",
                  new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not generate files", e));
            }
          }
        });
      }
    }.start();
    
    return null;
  }

}
