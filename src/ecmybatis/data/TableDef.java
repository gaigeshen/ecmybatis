package ecmybatis.data;

import java.util.StringJoiner;

/**
 * 
 * @author gaigeshen
 */
public final class TableDef {

  private final String name;
  private final String[] fields;

  /**
   * 
   * @param name
   * @param fields
   */
  public TableDef(String name, String[] fields) {
    this.name = name != null ? name : "";
    this.fields = fields != null ? fields : new String[0];
  }

  public String getName() {
    return name;
  }

  public String[] getFields() {
    return fields;
  }
  
  public String getFieldsDescription() {
    StringJoiner delimiter = new StringJoiner(", ");
    for (String field : fields) {
      delimiter.add(field);
    }
    return delimiter.toString();
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    
    TableDef other = (TableDef) obj;
    return name.equals(other.name);
  }
  
}
