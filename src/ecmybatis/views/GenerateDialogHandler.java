package ecmybatis.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import ecmybatis.Activator;
import ecmybatis.data.ColumnDef;
import ecmybatis.data.TableDef;
import ecmybatis.util.DatabaseUtils;
import ecmybatis.util.MonitorUserJob;

/**
 * 
 * @author gaigeshen
 */
public class GenerateDialogHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    
    IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);
    
    TableDef tableDef = (TableDef) selection.getFirstElement();
    if (tableDef == null) { return null; }
    
    Shell shell = HandlerUtil.getActiveShell(event);
    
    DataConfig config = DataConfigStore.fetch();
    if (config == null) {
      MessageDialog.openInformation(shell, "Missing preferences", "Please configure preferences first");
      return null;
    }
    
    String tableName = tableDef.getName();
    
    new MonitorUserJob("Show fields of table " + tableName) {
      @Override
      protected void doWork() throws Exception {
        String url = config.getUrl();
        String user = config.getUser();
        String password = config.getPassword();
        
        List<ColumnDef> columnDefs = new ArrayList<>();
        
        Map<String, String[]> columnTypes = DatabaseUtils.columnTypes(url, user, password, tableName);
        if (!columnTypes.isEmpty()) {
          columnTypes.forEach((c, t) -> {
            columnDefs.add(new ColumnDef(tableName, c, t[0], t[1]));
          });
        }
        if (!columnDefs.isEmpty()) {
          shell.getDisplay().asyncExec(() -> {
            GenerateDialog dialog = new GenerateDialog(shell, columnDefs);
            if (dialog.open() == Window.OK) {
              try {
                Generator.create(
                    dialog.getClassName(),
                    dialog.getTableName(),
                    dialog.getColumnDefs())
                .generate(
                    dialog.getPackagesRoot(),
                    dialog.getDomainPackage(),
                    dialog.getDaoPackage(),
                    dialog.getMapperDirectory());;
              } catch (Exception e) {
                ErrorDialog.openError(
                    shell, null, "Some error occurred, see details",
                    new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not generate files", e));
              }
            }
          });
        }
      }
    }.start();
    
    return null;
  }

}
