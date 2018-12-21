package ecmybatis.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author gaigeshen
 */
public class TablesContentProvider implements IStructuredContentProvider {

  @Override
  public Object[] getElements(Object inputElement) {
    
    if (inputElement != null && inputElement instanceof List) {
      return ((List<?>) inputElement).toArray();
    }
    
    return null;
  }

  @Override
  public void dispose() {
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

}
