package lunadevs.luna.utils;

public class TimeHelper
{
  private long lastMS;
  
  public long getCurrentMS()
  {
    return System.nanoTime() / 1000000L;
  }
  
  public long getLastMS()
  {
    return this.lastMS;
  }
  
  public boolean hasReached(long milliseconds)
  {
    return getCurrentMS() - this.lastMS >= milliseconds;
  }
  
  public long getTime()
  {
    return getCurrentMS() - this.lastMS;
  }
  
  public void reset()
  {
    this.lastMS = getCurrentMS();
  }
  
  public void setLastMS(long currentMS)
  {
    this.lastMS = currentMS;
  }

  public boolean hasReachedPackets(float f)
  {
    return (float)(getCurrentMS() - this.lastMS) >= f;
  }
}
