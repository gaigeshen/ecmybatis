package ecmybatis.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import ecmybatis.Activator;

/**
 * 基础类初始化器
 * 
 * @author gaigeshen
 */
public class Initializer {
  
  private static String SPRING_BEANS_LOCATOR_CONTENT;
  private static final String SPRING_BEANS_LOCATOR_SIMPLE = "SpringBeansLocator";
  private static final String SPRING_BEANS_LOCATOR_FILE = "/resource/base/SpringBeansLocator.java.txt";

  private static String BASE_DAO_CONTENT;
  private static final String BASE_DAO_SIMPLE = "BaseDao";
  private static final String BASE_DAO_FILE = "/resource/base/dao/BaseDao.java.txt";
  
  private static String CONDITION_CONTENT;
  private static final String CONDITION_SIMPLE = "Condition";
  private static final String CONDITION_FILE = "/resource/base/dao/Condition.java.txt";
  
  private static String DAO_CONTENT;
  private static final String DAO_SIMPLE = "Dao";
  private static final String DAO_FILE = "/resource/base/dao/Dao.java.txt";
  
  private static String PAGE_DATA_CONTENT;
  private static final String PAGE_DATA_SIMPLE = "PageData";
  private static final String PAGE_DATA_FILE = "/resource/base/dao/PageData.java.txt";
  
  private static String RESULT_MAPPINGS_CONTENT;
  private static final String RESULT_MAPPINGS_SIMPLE = "ResultMappings";
  private static final String RESULT_MAPPINGS_FILE = "/resource/base/dao/ResultMappings.java.txt";
  
  private static String BASE_MODEL_CONTENT;
  private static final String BASE_MODEL_SIMPLE = "BaseModel";
  private static final String BASE_MODEL_FILE = "/resource/base/domain/BaseModel.java.txt";
  
  private static String MODEL_CONTENT;
  private static final String MODEL_SIMPLE = "Model";
  private static final String MODEL_FILE = "/resource/base/domain/Model.java.txt";
  
  static {
    initializeSpringBeansLocatorContent();
    initializeBaseDaoContent();
    initializeConditionContent();
    initializeDaoContent();
    initializePageDataContent();
    initializeResultMappingsContent();
    initializeBaseModelContent();
    initializeModelContent();
  }
  
  public static Initializer create() {
    return new Initializer();
  }
  
  /**
   * 生成相关的文件
   * 
   * @param packageRoot 类的根路径
   * @param basePackage 领域对象的包
   * @param domainPackage 领域对象的包
   * @param daoPackage 数据访问对象的包
   */
  public void generate(IPackageFragmentRoot packageRoot,
                       IPackageFragment basePackage,
                       IPackageFragment domainPackage,
                       IPackageFragment daoPackage) {
    
    if (domainPackage.exists() && daoPackage.exists()) {
      generateSpringBeansLocator(basePackage);
      generateBaseDao(daoPackage, domainPackage);
      generateCondition(daoPackage);
      generateDao(daoPackage, domainPackage);
      generatePageData(daoPackage);
      generateResultMappings(daoPackage, basePackage);
      generateBaseModel(daoPackage, domainPackage);
      generateModel(domainPackage);
    }
  }

  /**
   * 
   * @param basepkg
   */
  private void generateSpringBeansLocator(IPackageFragment basepkg) {
    String content = SPRING_BEANS_LOCATOR_CONTENT
      .replaceAll("_basePackage_", basepkg.getElementName())
      .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));
    
    generateFile(SPRING_BEANS_LOCATOR_SIMPLE, content, basepkg);
  }

  /**
   * 
   * @param daopkg
   * @param domainpkg
   */
  private void generateBaseDao(IPackageFragment daopkg, IPackageFragment domainpkg) {
    String content = BASE_DAO_CONTENT
      .replaceAll("_daoPackage_", daopkg.getElementName())
      .replaceAll("_domainPackage_", domainpkg.getElementName())
      .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));
    
    generateFile(BASE_DAO_SIMPLE, content, daopkg);
  }
  
  /**
   * 
   * @param daopkg
   */
  private void generateCondition(IPackageFragment daopkg) {
    String content = CONDITION_CONTENT
        .replaceAll("_daoPackage_", daopkg.getElementName())
        .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));

    generateFile(CONDITION_SIMPLE, content, daopkg);
  }
  
  /**
   * 
   * @param daopkg
   * @param domainpkg
   */
  private void generateDao(IPackageFragment daopkg, IPackageFragment domainpkg) {
    String content = DAO_CONTENT
        .replaceAll("_daoPackage_", daopkg.getElementName())
        .replaceAll("_domainPackage_", domainpkg.getElementName())
        .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));

    generateFile(DAO_SIMPLE, content, daopkg);
  }
  
  /**
   * 
   * @param daopkg
   */
  private void generatePageData(IPackageFragment daopkg) {
    String content = PAGE_DATA_CONTENT
        .replaceAll("_daoPackage_", daopkg.getElementName())
        .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));

    generateFile(PAGE_DATA_SIMPLE, content, daopkg);
  }
  
  /**
   * 
   * @param daopkg
   * @param basepkg
   */
  private void generateResultMappings(IPackageFragment daopkg, IPackageFragment basepkg) {
    String content = RESULT_MAPPINGS_CONTENT
        .replaceAll("_daoPackage_", daopkg.getElementName())
        .replaceAll("_basePackage_", basepkg.getElementName())
        .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));

    generateFile(RESULT_MAPPINGS_SIMPLE, content, daopkg);
  }
  
  /**
   * 
   * @param daopkg
   * @param domainpkg
   */
  private void generateBaseModel(IPackageFragment daopkg, IPackageFragment domainpkg) {
    String content = BASE_MODEL_CONTENT
        .replaceAll("_domainPackage_", domainpkg.getElementName())
        .replaceAll("_daoPackage_", daopkg.getElementName())
        .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));

    generateFile(BASE_MODEL_SIMPLE, content, domainpkg);
  }
  
  /**
   * 
   * @param domainpkg
   */
  private void generateModel(IPackageFragment domainpkg) {
    String content = MODEL_CONTENT
        .replaceAll("_domainPackage_", domainpkg.getElementName())
        .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));

    generateFile(MODEL_SIMPLE, content, domainpkg);
  }

  private static void initializeSpringBeansLocatorContent() {
    if (SPRING_BEANS_LOCATOR_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(SPRING_BEANS_LOCATOR_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          SPRING_BEANS_LOCATOR_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize spring beans locator content", e);
      }
    }
  }
  
  private static void initializeBaseDaoContent() {
    if (BASE_DAO_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(BASE_DAO_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          BASE_DAO_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize base dao content", e);
      }
    }
  }

  private static void initializeConditionContent() {
    if (CONDITION_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(CONDITION_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          CONDITION_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize condition content", e);
      }
    }
  }

  private static void initializeDaoContent() {
    if (DAO_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(DAO_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          DAO_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize dao content", e);
      }
    }
  }

  private static void initializePageDataContent() {
    if (PAGE_DATA_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(PAGE_DATA_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          PAGE_DATA_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize page data content", e);
      }
    }
  }

  private static void initializeResultMappingsContent() {
    if (RESULT_MAPPINGS_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(RESULT_MAPPINGS_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          RESULT_MAPPINGS_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize result mappings content", e);
      }
    }
  }

  private static void initializeBaseModelContent() {
    if (BASE_MODEL_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(BASE_MODEL_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          BASE_MODEL_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize base model content", e);
      }
    }
  }

  private static void initializeModelContent() {
    if (MODEL_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(MODEL_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          MODEL_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize model content", e);
      }
    }
  }
  
  /**
   * 生成类文件
   * 
   * @param className 类名称
   * @param content 类的文本内容
   * @param pkg 所在包
   * @return 生成的编译单元
   */
  private ICompilationUnit generateFile(String className,
                                        String content,
                                        IPackageFragment pkg) {
    if (!pkg.exists()) {
      throw new IllegalStateException("Package not exists: " + pkg.getElementName());
    }
    
    String name = className + ".java";
    
    ICompilationUnit unit = pkg.getCompilationUnit(name);
    if (!unit.exists()) { // 已经存在的类不会再次生成
      try {
        return pkg.createCompilationUnit(name, content, true, new NullProgressMonitor());
      } catch (JavaModelException e) {
        throw new IllegalStateException("Generate file failed for class: " + className, e);
      }
    }
    return unit;
  }
  
}
