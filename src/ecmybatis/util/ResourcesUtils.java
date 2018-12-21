package ecmybatis.util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 
 * @author gaigeshen
 */
public final class ResourcesUtils {

  private ResourcesUtils() {}
  
  /**
   * 获取指定名称的编译单元
   * 
   * @param root 根路径
   * @param name 名称
   * @return 编译单元
   */
  public static ICompilationUnit compilationUnit(IPackageFragmentRoot root, String name) {
    
    IJavaElement[] elements = null;
    try {
      elements = root.getChildren();
    } catch (JavaModelException e) {
      throw new IllegalStateException("Could not get children of package root", e);
    }
    
    for (IJavaElement element : elements) {
      if (element.getElementType() == 4) {
        IPackageFragment pkg = (IPackageFragment) element;
        ICompilationUnit unit = pkg.getCompilationUnit(name);
        if (unit.exists()) {
          return unit;
        }
      }
    }
    
    return null;
  }
  
}
