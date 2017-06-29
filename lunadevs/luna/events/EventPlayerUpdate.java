package lunadevs.luna.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventPlayerUpdate extends EventCancellable{
	
	 private EventType type;
	  public double x;
	  public double y;
	  public double z;
	  
	  public EventPlayerUpdate(EventType type)
	  {
	    this.type = type;
	  }
	  
	  public EventType getType()
	  {
	    return this.type;
	  }
	  
	  public double getX()
	  {
	    return this.x;
	  }
	  
	  public double getY()
	  {
	    return this.y;
	  }
	  
	  public double getZ()
	  {
	    return this.z;
	  }
	  
	  public void setX(double x)
	  {
	    this.x = x;
	  }
	  
	  public void setY(double y)
	  {
	    this.y = y;
	  }
	  
	  public void setZ(double z)
	  {
	    this.z = z;
	  }

}
