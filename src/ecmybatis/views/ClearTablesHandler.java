package ecmybatis.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author gaigeshen
 */
public class ClearTablesHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    
    DatabaseView view = (DatabaseView) HandlerUtil.getActivePart(event);
    
    view.clearViewer();
    
    return null;
  }

}
