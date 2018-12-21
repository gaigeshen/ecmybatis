package ecmybatis.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;

import ecmybatis.PreferencePage;

/**
 * 打开首选项对话框
 * 
 * @author gaigeshen
 */
public class ShowPreferenceHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Shell shell = HandlerUtil.getActiveShell(event);

    WorkbenchPreferenceDialog dialog = WorkbenchPreferenceDialog.createDialogOn(shell, PreferencePage.ID);
    
    dialog.open();
    
    return null;
  }

}
