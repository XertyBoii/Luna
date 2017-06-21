package lunadevs.luna.utils.faithsminiutils;

import java.util.ArrayList;

public class ValueTwo
{
  public String name;
  public static ArrayList<ValueTwo> modes = new ArrayList();
  public int counter;
  public Float value;
  public Float min;
  public Float max;
  public Float increment;
  private Float defaultfloat;
  public boolean isafloat = false;
  public boolean boolvalue;
  public boolean defaultboolean;
  public boolean isaboolean = false;
  public String stringvalue;
  public String defaultstring;
  public String[] allothers;
  public boolean isamode = false;
  public String editvalue;
  public int maxsize;
  public boolean iseditable = false;
  public Value neededval;
  public boolean needsvalopen;
  
  public ValueTwo(String name, Float value, Float min, Float max, Float increment)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.value = value;
    this.min = min;
    this.max = max;
    this.increment = increment;
    this.defaultfloat = value;
    this.isafloat = true;
    modes.add(this);
  }
  
  public ValueTwo(String name, Boolean value)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.boolvalue = value.booleanValue();
    this.defaultboolean = value.booleanValue();
    this.isaboolean = true;
    modes.add(this);
  }
  
  public ValueTwo(String name, String value, String[] allothers)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.stringvalue = value;
    this.defaultstring = value;
    this.allothers = allothers;
    this.isamode = true;
    modes.add(this);
  }
  
  public ValueTwo(String name, String value, int maxsize)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.editvalue = value;
    this.maxsize = maxsize;
    this.iseditable = true;
    modes.add(this);
  }
  
  public void interact()
  {
    if (this.isaboolean) {
      this.boolvalue = (!this.boolvalue);
    }
    if (this.isamode)
    {
      this.counter += 1;
      if (this.counter == this.allothers.length) {
        this.counter = 0;
      }
      this.stringvalue = this.allothers[this.counter];
    }
  }
  
  public ValueTwo(String name, Float value, Float min, Float max, Float increment, Value neededval)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.value = value;
    this.min = min;
    this.max = max;
    this.increment = increment;
    this.defaultfloat = value;
    this.isafloat = true;
    this.neededval = neededval;
    this.needsvalopen = true;
    modes.add(this);
  }
  
  public ValueTwo(String name, Boolean value, Value neededval)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.boolvalue = value.booleanValue();
    this.defaultboolean = value.booleanValue();
    this.isaboolean = true;
    this.neededval = neededval;
    this.needsvalopen = true;
    modes.add(this);
  }
  
  public ValueTwo(String name, String value, String[] allothers, Value neededval)
  {
    if (name.contains(" ")) {
      name = name.replace(" ", "");
    }
    this.name = name;
    this.stringvalue = value;
    this.defaultstring = value;
    this.allothers = allothers;
    this.isamode = true;
    this.neededval = neededval;
    this.needsvalopen = true;
    modes.add(this);
  }
}
