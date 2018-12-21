package ecmybatis.data;

import ecmybatis.util.JdbcTypeMapping;
import ecmybatis.util.ParamUtils;

/**
 * 
 * @author gaigeshen
 */
public final class ColumnDef {

  private final String tableName;
  private final String columnName;
  private final String description;
  private final String propertyName;
  private final String jdbcType;
  private final String javaType;

  /**
   * 
   * @param tableName
   * @param columnName
   * @param description
   * @param jdbcType
   */
  public ColumnDef(String tableName, String columnName, String description, String jdbcType) {
    
    if (tableName == null || columnName == null) {
      throw new IllegalArgumentException("Table name or column name is null");
    }
    
    this.tableName = tableName;
    this.columnName = columnName;
    this.description = description;
    this.propertyName = ParamUtils.underlineToCamel(columnName);
    
    // Parse to java type from jdbc type
    if (jdbcType == null) {
      throw new IllegalArgumentException("Jdbc type is null");
    }
    this.jdbcType = jdbcType;
    int idx = jdbcType.indexOf("(");
    if (idx != -1) {
      jdbcType = jdbcType.substring(0, idx);
    }
    this.javaType = JdbcTypeMapping.fromJdbcType(jdbcType.toUpperCase()).getJavaType().getName();
  }

  public String getTableName() {
    return tableName;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getDescription() {
    return description;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public String getJavaType() {
    return javaType;
  }
}
