package ecmybatis;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 首选项配置页
 * 
 * @author gaigeshen
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public static final String ID = "ecmybatis.preference";
  
  public PreferencePage() {
    super(GRID);
  }

  @Override
  public void init(IWorkbench workbench) {
    setPreferenceStore(Activator.preferenceStore());
    
    getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
      
      @Override
      public void propertyChange(PropertyChangeEvent event) {
        
      }
    });
  }

  @Override
  protected void createFieldEditors() {

    Group group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    group.setText("Data config");

    addField(new StringFieldEditor("url", "Url:", group));
    addField(new StringFieldEditor("user", "User:", group));
    addField(new StringFieldEditor("password", "Password:", group));

    GridLayout layout = (GridLayout) group.getLayout();
    layout.marginWidth = layout.marginHeight = layout.verticalSpacing = 10;

    group.layout();
  }

}
