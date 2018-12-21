package ecmybatis.views;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.part.ViewPart;

import ecmybatis.data.TableDef;

/**
 * 
 * 
 * @author gaigeshen
 */
public class DatabaseView extends ViewPart {

  public static final String ID = "ecmybatis.views.DatabaseView";
  
  private TableViewer viewer;
  
  private TableColumn nameColumn;
  private TableColumn fieldsColumn;
  
  @Override
  public void createPartControl(Composite parent) {
    
    createViewer(parent);
    
    initContextMenu();
  }

  @Override
  public void saveState(IMemento memento) {
    
    
  }

  @Override
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  @Override
  public void dispose() {
    
    
    
    super.dispose();
  }

  private void createViewer(Composite parent) {
    
    viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
    
    Table table = viewer.getTable();
    nameColumn = new TableColumn(table, SWT.LEFT);
    nameColumn.setText("Table name");
    nameColumn.setWidth(150);
    
    fieldsColumn = new TableColumn(table, SWT.LEFT);
    fieldsColumn.setText("Fields");
    fieldsColumn.setWidth(300);
    
    table.setHeaderVisible(true);
    table.setLinesVisible(false);
    
    viewer.setContentProvider(new TablesContentProvider());
    viewer.setLabelProvider(new TablesLabelProvider());
    
  }
  
  public void refreshViewer(List<TableDef> content) {
    if (!isViewerDisposed()) {
      viewer.setInput(content);
    }
  }
  
  public void updateViewer(TableDef tableDef) {
    if (!isViewerDisposed()) {
      String[] fields = new String[] { tableDef.getName(), tableDef.getFieldsDescription()};
      viewer.update(tableDef, fields);
    }
  }
  
  public void clearViewer() {
    if (!isViewerDisposed()) {
      viewer.setInput(null);
    }
  }
  
  private boolean isViewerDisposed() {
    return viewer.getControl().isDisposed();
  }
  
  private void initContextMenu() {
    
    MenuManager manager = new MenuManager();
    
    Menu menu = manager.createContextMenu(viewer.getTable());
    
    viewer.getTable().setMenu(menu);
    
    getSite().registerContextMenu(manager, viewer);
    
    getSite().setSelectionProvider(viewer);
    
  }
  
}
