package ecmybatis.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ecmybatis.Activator;

/**
 * 可监测的任务，会有进度窗口提示，但进度不可展示真实情况
 * 
 * @author gaigeshen
 */
public abstract class MonitorUserJob extends Job {

  private static final String STATUS_OK = "ok";

  private static final String STATUS_ERROR = "error";

  private static final String PREFIX_TASK_NAME = "Task: ";
  
  private static final Status OK = new Status(IStatus.OK, Activator.PLUGIN_ID, STATUS_OK);
  
  /**
   * 创建任务
   * 
   * @param name 任务名称
   */
  public MonitorUserJob(String name) {
    super(name);
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    try {
      monitor.beginTask(PREFIX_TASK_NAME + getName(), IProgressMonitor.UNKNOWN);
      
      doWork();
      return OK;
    } catch (Exception e) {
      return new Status(IStatus.ERROR, Activator.PLUGIN_ID, STATUS_ERROR, e);
    } finally {
      monitor.done();
    }
  }
  
  /**
   * 开始调度执行任务
   */
  public void start() {
    setUser(true);
    schedule();
  }

  /**
   * 执行具体的任务
   * 
   * @throws Exception 执行任务过程中发生异常
   */
  protected abstract void doWork() throws Exception;

}
