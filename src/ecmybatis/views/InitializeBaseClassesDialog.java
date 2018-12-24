package ecmybatis.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * 初始化基础类对话框
 * 
 * @author gaigeshen
 */
public class InitializeBaseClassesDialog extends TitleAreaDialog {

  private ComboViewer projectsField;
  private ComboViewer packagesRootField;;
  
  private Text basePackageField;
  private Button basePackageButton;
  private Text domainPackageField;
  private Button domainPackageButton;
  private Text daoPackageField;
  private Button daoPackageButton;
  
  private IPackageFragmentRoot packagesRoot;
  private IPackageFragment basePackage;
  private IPackageFragment domainPackage;
  private IPackageFragment daoPackage;
  
  /**
   * 
   * @param parentShell
   */
  public InitializeBaseClassesDialog(Shell parentShell) {
    super(parentShell);
  }
  
  public IPackageFragmentRoot getPackagesRoot() {
    return packagesRoot;
  }

  public IPackageFragment getBasePackage() {
    return basePackage;
  }

  public IPackageFragment getDomainPackage() {
    return domainPackage;
  }

  public IPackageFragment getDaoPackage() {
    return daoPackage;
  }


  @Override
  public void create() {
    super.create();
    
    setTitle("Initialize base classes");
    
    setMessage("Type base package, dao package and domain package\n"
        + " Then press OK button to initialize",
        IMessageProvider.INFORMATION);
  }
  
  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Initialize");
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);
    
    Composite container = new Composite(composite, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    container.setLayout(new GridLayout(3, false));
    
    initializeProjectsField(container);
    initializePackagesRootField(container);
    initializeBasePackageField(container);
    initializeDomainPackageField(container);
    initializeDaoPackageField(container);
    
    // 获取当前工作空间的所有项目
    // 并初始化项目选择下拉选择框
    IJavaProject[] javaProjects = javaProjects();
    if (javaProjects != null && javaProjects.length > 0) {
      projectsField.setInput(javaProjects);
      projectsField.setSelection(new StructuredSelection(javaProjects[0]));
    }
    
    return composite;
  }
 
  @Override
  protected void okPressed() {
    // 前置检查
    if (beforeOkPressedCheckData()) {
      // 检查数据合格继续
      super.okPressed();
    }
  }
  
  /**
   * 返回工作空间中的所有项目
   * 
   * @return 所有的项目
   */
  private IJavaProject[] javaProjects() {
    
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    
    IJavaModel jModel = JavaCore.create(root);
    
    try {
      return jModel.getJavaProjects();
    } catch (JavaModelException e) { }
    
    return null;
  }

  /**
   * 初始化项目下拉选择框
   * 
   * @param container 容器
   */
  private void initializeProjectsField(Composite container) {
    
    Label label = new Label(container, SWT.NONE);
    label.setText("Project:");
    
    projectsField = new ComboViewer(container);
    projectsField.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    
    projectsField.setContentProvider(new ArrayContentProvider());
    projectsField.setLabelProvider(new ProjectsFieldLabelProvider());
    
    projectsField.addSelectionChangedListener(e -> {
      StructuredSelection selection = (StructuredSelection) e.getSelection();
      if (selection != null && !selection.isEmpty()) {
        IPackageFragmentRoot[] packageRoots = null;
        try {
          // 获取选择的项目下所有的根路径
          packageRoots = ((IJavaProject) selection.getFirstElement()).getPackageFragmentRoots();
        } catch (JavaModelException ex) { }
        
        if (packageRoots != null) {
          List<IPackageFragmentRoot> sourcePackageRoots = new ArrayList<>();
          for (IPackageFragmentRoot root : packageRoots) {
            try {
              // 排除掉二进制类型的根路径
              if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
                sourcePackageRoots.add(root);
              }
            } catch (JavaModelException ex) {
              return; // 如果在获取根路径类型的时候发生错误，放弃初始化根路径的下拉选择框
            }
          }
          if (!sourcePackageRoots.isEmpty()) {
            // 更新根路径下拉选择框
            packagesRootField.setInput(sourcePackageRoots.toArray());
            packagesRootField.setSelection(new StructuredSelection(sourcePackageRoots.get(0)));
          }
        }
      }
    });
  }

  /**
   * 初始化包的根路径下拉选择框
   * 
   * @param container 容器
   */
  private void initializePackagesRootField(Composite container) {
    
    Label label = new Label(container, SWT.NONE);
    label.setText("Packages root:");
    
    packagesRootField = new ComboViewer(container);
    packagesRootField.getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    
    packagesRootField.setContentProvider(new ArrayContentProvider());
    packagesRootField.setLabelProvider(new PackagesRootFieldLabelProvider());
    
    packagesRootField.addSelectionChangedListener(e -> {
      StructuredSelection selection = (StructuredSelection) e.getSelection();
      if (selection != null && !selection.isEmpty()) {
        packagesRoot = ((IPackageFragmentRoot) selection.getFirstElement());
      }
    });
    
  }

  /**
   * 初始化顶级包输入框
   * 
   * @param container 容器
   */
  private void initializeBasePackageField(Composite container) {
    Label label = new Label(container, SWT.NONE);
    label.setText("Base package:");
    
    basePackageField = new Text(container, SWT.BORDER);
    basePackageButton = new Button(container, SWT.NONE);
    basePackageField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    basePackageButton.setText("Browse...");
    basePackageButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        // 获取当前选择的根路径
        IStructuredSelection selection = packagesRootField.getStructuredSelection();
        if (selection != null && !selection.isEmpty()) {
          // 之前已经输入的内容
          String inputed = basePackageField.getText().trim();
          
          IPackageFragmentRoot root = (IPackageFragmentRoot) selection.getFirstElement();
          // 显示包选择器
          IPackageFragment pkg = choosePackage(root, inputed, getShell());
          if (pkg != null) {
            if (pkg.isDefaultPackage()) {
              basePackageField.setText("(default)");
            } else {
              basePackageField.setText(pkg.getElementName());
            }
          }
        }
      }
    });
  }
  
  /**
   * 初始化领域对象包输入框
   * 
   * @param container 容器
   */
  private void initializeDomainPackageField(Composite container) {
    Label label = new Label(container, SWT.NONE);
    label.setText("Domain package:");
    
    domainPackageField = new Text(container, SWT.BORDER);
    domainPackageButton = new Button(container, SWT.NONE);
    domainPackageField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    domainPackageButton.setText("Browse...");
    domainPackageButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        // 获取当前选择的根路径
        IStructuredSelection selection = packagesRootField.getStructuredSelection();
        if (selection != null && !selection.isEmpty()) {
          // 之前已经输入的内容
          String inputed = domainPackageField.getText().trim();
          
          IPackageFragmentRoot root = (IPackageFragmentRoot) selection.getFirstElement();
          // 显示包选择器
          IPackageFragment pkg = choosePackage(root, inputed, getShell());
          if (pkg != null) {
            if (pkg.isDefaultPackage()) {
              domainPackageField.setText("(default)");
            } else {
              domainPackageField.setText(pkg.getElementName());
            }
          }
        }
      }
    });
  }

  /**
   * 初始化数据访问对象包输入框
   * 
   * @param container 容器
   */
  private void initializeDaoPackageField(Composite container) {
    Label label = new Label(container, SWT.NONE);
    label.setText("Dao package:");
    
    daoPackageField = new Text(container, SWT.BORDER);
    daoPackageButton = new Button(container, SWT.NONE);
    daoPackageField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    daoPackageButton.setText("Browse...");
    daoPackageButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        // 获取当前选择的根路径
        IStructuredSelection selection = packagesRootField.getStructuredSelection();
        if (selection != null && !selection.isEmpty()) {
          // 之前已经输入的内容
          String inputed = daoPackageField.getText().trim();
          
          IPackageFragmentRoot root = (IPackageFragmentRoot) selection.getFirstElement();
          // 显示包选择器
          IPackageFragment pkg = choosePackage(root, inputed, getShell());
          if (pkg != null) {
            if (pkg.isDefaultPackage()) {
              daoPackageField.setText("(default)");
            } else {
              daoPackageField.setText(pkg.getElementName());
            }
          }
        }
      }
    });
  }
  
  /**
   * 显示包选择器然后返回选择的包
   * 
   * @param packageRoot 根路径
   * @param selected 初始选择的包
   * @param shell 窗口
   * @return 选择的包
   */
  private IPackageFragment choosePackage(IPackageFragmentRoot packageRoot, String selected, Shell shell) {
    SelectionDialog dialog = null;
    
    try {
      if (packageRoot != null) {
        dialog = JavaUI.createPackageDialog(shell, packageRoot);
      }
    } catch (JavaModelException e) { }

    if (dialog != null) {
      dialog.setTitle("Packages");
      dialog.setMessage("Choose package and continue");
      if (selected != null) {
        IPackageFragment pkg = packageRoot.getPackageFragment(selected);
        if (pkg.exists()) {
          dialog.setInitialSelections(new Object[] { pkg });
        }
      }
      if (dialog.open() == IDialogConstants.OK_ID) {
        Object[] result = dialog.getResult();
        if (result != null) {
          return (IPackageFragment) result[0];
        }
      }
    }
    
    return null;
  }

  /**
   * 在点击对话框的确认按钮之前的数据检查操作
   * 
   * @return 返回值决定是否已经准备好
   */
  private boolean beforeOkPressedCheckData() {
    
    IStructuredSelection selection = packagesRootField.getStructuredSelection();
    if (selection == null) {
      // 没有选择任何根路径无法进行接下来的操作，例如创建包的操作
      setMessage("No packages root selected", IMessageProvider.WARNING);
      return false;
    }
    IPackageFragmentRoot root = (IPackageFragmentRoot) selection.getFirstElement();
    if (!root.exists()) {
      // 不存在选择的根路径，用户此时可以选择重新打开对话框
      setMessage("Packages root not found, please reopen this dialog", IMessageProvider.WARNING);
      return false;
    }
    
    boolean packagesCreated = necessaryCreatePackages(root);
    if (!packagesCreated) {
      // 创建包的时候发生异常无法进行接下来的操作
      setMessage("Packages create failed", IMessageProvider.WARNING);
      return false;
    }
    
    return true;
  }
  
  /**
   * 创建必要的包
   * 
   * @param root 根路径
   * @return 是否创建成功
   */
  private boolean necessaryCreatePackages(IPackageFragmentRoot root) {
    try {
      /*
       * 得到当前领域对象输入框和数据访问对象输入框的内容
       * 分别作为包名判断是否存在该包，如果不存在则新建
       * 用户可以手动输入根路径下不存在的包
       */
      String domainPackageStr = domainPackageField.getText().trim();
      domainPackage = root.getPackageFragment(domainPackageStr);
      if (!domainPackage.exists()) {
        root.createPackageFragment(domainPackageStr, true, new NullProgressMonitor());
      }
      String daoPackageStr = daoPackageField.getText().trim();
      daoPackage = root.getPackageFragment(daoPackageStr);
      if (!daoPackage.exists()) {
        root.createPackageFragment(daoPackageStr, true, new NullProgressMonitor());
      }
      String basePackageStr = basePackageField.getText().trim();
      basePackage = root.getPackageFragment(basePackageStr);
      if (!basePackage.exists()) {
        root.createPackageFragment(basePackageStr, true, new NullProgressMonitor());
      }
      return true;
    } catch (JavaModelException e) {
      // 在创建包的时候发生异常
      return false;
    }
  }

  /**
   * 
   * @author gaigeshen
   */
  private class ProjectsFieldLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
      return null;
    }

    @Override
    public String getText(Object element) {
      return ((IJavaProject) element).getElementName();
    }
  }
  
  /**
   * 
   * @author gaigeshen
   */
  private class PackagesRootFieldLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
      return null;
    }

    @Override
    public String getText(Object element) {
      return ((IPackageFragmentRoot) element).getPath().removeFirstSegments(1).toString();
    }
  }
  
}
