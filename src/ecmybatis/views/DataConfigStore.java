package ecmybatis.views;

import org.eclipse.jface.preference.IPreferenceStore;

import ecmybatis.Activator;

/**
 * 数据库配置存储
 * 
 * @author gaigeshen
 */
public final class DataConfigStore {

  /**
   * 保存配置
   * 
   * @param config 配置
   */
  public static void persist(DataConfig config) {
    
    IPreferenceStore store = Activator.preferenceStore();
    
    store.setValue("url", config.getUrl());
    store.setValue("user", config.getUser());
    store.setValue("password", config.getPassword());
  }
  
  /**
   * 获取配置
   * 
   * @return 配置
   */
  public static DataConfig fetch() {
    
    IPreferenceStore store = Activator.preferenceStore();
    
    String url = store.getString("url");
    String user = store.getString("user");
    String password = store.getString("password");
    
    if (url != null && user != null && password != null) {
      url = url.trim();
      user = user.trim();
      password = password.trim();
      
      if (!url.isEmpty() && !user.isEmpty() && !password.isEmpty()) {
        return new DataConfig(url, user, password);
      }
    }
    
    return null;
  }
  
}
