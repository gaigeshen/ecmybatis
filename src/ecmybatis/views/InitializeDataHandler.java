package ecmybatis.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import ecmybatis.data.TableDef;
import ecmybatis.util.DatabaseUtils;
import ecmybatis.util.MonitorUserJob;

/**
 * 装载数据处理器
 * 
 * @author gaigeshen
 */
public class InitializeDataHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell shell = HandlerUtil.getActiveShell(event);
    
    DataConfig config = DataConfigStore.fetch();
    
    if (config == null) {
      MessageDialog.openInformation(shell, "Missing preferences", "Please configure preferences first");
      return null;
    }

    DatabaseView view = (DatabaseView) HandlerUtil.getActivePart(event);

    new MonitorUserJob("Configuring data") {

      @Override
      protected void doWork() throws Exception {

        String url = config.getUrl();
        String user = config.getUser();
        String password = config.getPassword();

        List<TableDef> tableDefs = new ArrayList<>();
        
        Map<String, List<String>> fields = DatabaseUtils.tableFields(url, user, password);

        fields.forEach((n, f) -> {
          tableDefs.add(new TableDef(n, f.toArray(new String[f.size()])));
        });
        
        shell.getDisplay().asyncExec(() -> { view.refreshViewer(tableDefs); });
      }
    }.start();

    return null;
  }

}
