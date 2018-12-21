package ecmybatis.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ecmybatis.data.TableDef;

/**
 * 
 * @author gaigeshen
 */
public class TablesLabelProvider extends LabelProvider implements ITableLabelProvider {

  @Override
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  @Override
  public String getColumnText(Object element, int columnIndex) {
    
    if (element != null) {
      switch (columnIndex) {
      case 0: return ((TableDef) element).getName();
      case 1: return ((TableDef) element).getFieldsDescription();
      }
    }
    
    return null;
  }
  
}
