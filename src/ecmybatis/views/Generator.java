package ecmybatis.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import ecmybatis.Activator;
import ecmybatis.data.ColumnDef;
import ecmybatis.util.ResourcesUtils;

/**
 * 生成器
 * 
 * @author gaigeshen
 */
public class Generator {
  
  private static String MODEL_CONTENT;
  private static final String MODEL_BASE = "BaseModel.java";
  private static final String MODEL_BASE_SIMPLE = "BaseModel";
  private static final String MODEL_CONTENT_FILE = "/resource/Model1.java.txt";
  
  private static String DAO_CONTENT;
  private static final String DAO_BASE = "BaseDao.java";
  private static final String DAO_BASE_SIMPLE = "BaseDao";
  private static final String DAO_CONTENT_FILE = "/resource/Dao1.java.txt";
  
  private static String MAPPER_CONTENT;
  private static final String MAPPER_CONTENT_FILE = "/resource/Dao.xml.txt";
  
  private final String className; 
  private final String tableName;
  private final List<ColumnDef> columnDefs;
  
  // 初始化相关的模板内容
  static {
    initializeModelContent();
    initializeDaoContent();
    initializeMapperContent();
  }
  
  public Generator(String className,
                   String tableName,
                   List<ColumnDef> columnDefs) {
    this.className = className;
    this.tableName = tableName;
    this.columnDefs = columnDefs;
  }
  
  /**
   * 创建生成器
   * 
   * @param className 类名称
   * @param tableName 数据库表名称
   * @param columnDefs 数据库所有的字段
   * @return 生成器
   */
  public static Generator create(String className,
                                 String tableName,
                                 List<ColumnDef> columnDefs) {
    
    return new Generator(className, tableName, columnDefs);
    
  }
  
  /**
   * 生成相关的文件
   * 
   * @param packageRoot 类的根路径
   * @param domainPackage 领域对象的包
   * @param daoPackage 数据访问对象的包
   * @param mapperDirectory 映射文件的路径
   */
  public void generate(IPackageFragmentRoot packageRoot,
                       IPackageFragment domainPackage,
                       IPackageFragment daoPackage,
                       String mapperDirectory) {
    
    if (domainPackage.exists() && daoPackage.exists()) {
      generateModel(packageRoot, domainPackage);
      generateDao(packageRoot, daoPackage);
      generateMapper(packageRoot, mapperDirectory);
    }
  }
  
  /**
   * 初始化实体类的模板内容
   */
  private static void initializeModelContent() {
    if (MODEL_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(MODEL_CONTENT_FILE)) {
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
   * 初始化数据访问类的模板内容
   */
  private static void initializeDaoContent() {
    if (DAO_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(DAO_CONTENT_FILE)) {
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
  
  /**
   * 初始化映射文件的模板内容
   */
  private static void initializeMapperContent() {
    if (MAPPER_CONTENT == null) {
      try (InputStream in = Activator.resourceFile(MAPPER_CONTENT_FILE)) {
        if (in != null) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();

          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
          out.flush();
          MAPPER_CONTENT = out.toString("utf-8");
        }
      } catch (IOException e) {
        throw new IllegalStateException("Could not initialize mapper content", e);
      }
    }
  }
  
  /**
   * 生成实体类
   * 
   * @param pkgRoot 根路径
   * @param pkg 所在包
   */
  private void generateModel(IPackageFragmentRoot pkgRoot, IPackageFragment pkg) {
    
    ICompilationUnit baseModelUnit = ResourcesUtils.compilationUnit(pkgRoot, MODEL_BASE);
    
    if (baseModelUnit == null) {
      throw new IllegalStateException(MODEL_BASE + " file not found");
    }
    
    IType baseModel = baseModelUnit.getType(MODEL_BASE_SIMPLE);
    
    String content = MODEL_CONTENT
      .replaceAll("_package_", pkg.getElementName())
      .replaceAll("_baseModel_", baseModel.getFullyQualifiedName())
      .replaceAll("_typeName_", className)
      .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));
    
    StringBuilder fields = new StringBuilder();
    columnDefs.forEach(col -> {
      if (!col.getColumnName().equals("id")) {
        if (fields.length() != 0) {
          fields.append("\n  ");
        }
        fields.append("private ")
              .append(col.getJavaType()).append(" ")
              .append(col.getPropertyName())
              .append(";")
              .append(" //")
              .append(col.getDescription());
        }
    });
    content = content.replaceAll("_modelFields_", fields.toString());
    
    generateFile(className, content, pkg);
  }
  
  /**
   * 生成数据访问类
   * 
   * @param pkgRoot 根路径
   * @param pkg 所在包
   */
  private void generateDao(IPackageFragmentRoot pkgRoot, IPackageFragment pkg) {
    
    ICompilationUnit baseDaoUnit = ResourcesUtils.compilationUnit(pkgRoot, DAO_BASE);
    if (baseDaoUnit == null) {
      throw new IllegalStateException(DAO_BASE + " file not found");
    }
    
    String classFile = className + ".java";
    ICompilationUnit classUnit = ResourcesUtils.compilationUnit(pkgRoot, classFile);
    if (classUnit == null) {
      throw new IllegalStateException(classFile + " file not found");
    }

    IType baseDao = baseDaoUnit.getType(DAO_BASE_SIMPLE);
    IType classType = classUnit.getType(className);
    
    String content = DAO_CONTENT
      .replaceAll("_package_", pkg.getElementName())
      .replaceAll("_typeName_", className)
      .replaceAll("_type_", classType.getFullyQualifiedName())
      .replaceAll("_baseDao_", baseDao.getFullyQualifiedName())
      .replaceAll("_since_", new SimpleDateFormat("MM/dd yyyy").format(new Date()));
    
    generateFile(className + "Dao", content, pkg);
  }
  
  /**
   * 生成映射文件
   * 
   * @param pkgRoot 类的根路径
   * @param directory 映射文件的路径
   */
  private void generateMapper(IPackageFragmentRoot pkgRoot, String directory) {
    createMissingDirectory(directory);
    String fileName = directory + "/" + className + "Dao.xml";
    File file = new File(fileName);
    if (file.exists()) return;
    
    String daoName = className + "Dao";
    String dao = daoName + ".java";
    ICompilationUnit daoUnit = ResourcesUtils.compilationUnit(pkgRoot, dao);
    if (!daoUnit.exists()) {
      throw new IllegalStateException(dao + " file not found");
    }
    
    StringBuilder fields = new StringBuilder("<id property=\"id\" column=\"id\"></id>");
    columnDefs.forEach(col -> {
      if (!col.getColumnName().equals("id")) {
        fields.append("\n    ")
              .append("<result property=\"")
              .append(col.getPropertyName())
              .append("\" column=\"")
              .append(col.getColumnName())
              .append("\"/>");
      }
    });
    
    String content = MAPPER_CONTENT
      .replaceAll("_namespace_", daoUnit.getType(daoName).getFullyQualifiedName())
      .replaceAll("_table_", tableName)
      .replaceAll("_fields_", fieldsFromColumns())
      .replaceAll("_type_", className)
      .replaceAll("_properties_", fields.toString());
    
    try (OutputStream out = new FileOutputStream(file)) {
      out.write(content.getBytes(Charset.forName("utf-8")));
    } catch (IOException e) {
      throw new IllegalStateException("Could not write content to mapper file: " + fileName, e);
    }
  }
  
  /**
   * 创建缺失的目录
   * 
   * @param directory 目录
   */
  private void createMissingDirectory(String directory) {
    Path path = Paths.get(directory);
    if (Files.exists(path)) {
      return;
    }
    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
      } catch (IOException e) {
        throw new IllegalStateException("Could not create directory: " + directory);
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
        return pkg.createCompilationUnit(
            name, content, true, new NullProgressMonitor());
      } catch (JavaModelException e) {
        throw new IllegalStateException("Generate file failed for class: " + className, e);
      }
    }
    return unit;
  }
  
  /**
   * 生成逗号分隔的所有数据库字段
   * 
   * @return 逗号分隔的字符串
   */
  private String fieldsFromColumns() {
    
    StringBuilder fields = new StringBuilder();
    
    for (ColumnDef col : columnDefs) {
      fields.append(", ").append(col.getColumnName());
    }
    return fields.substring(2);
  }
  
}
