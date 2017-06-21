package lunadevs.luna.utils;

import lunadevs.luna.main.Parallaxa;
import lunadevs.luna.module.Module;
import net.minecraft.client.Minecraft;

public class Comparator
  implements java.util.Comparator<Module>
{
	
	String s1;
    String s2;

	
  public int compare(Module m1, Module m2)
  {
	  if (m1.value == true){
       s1 = m1.name + m1.getValue();
	  }else{
	   s1 = m1.name;
  }
	  if (m2.value == true){
		  s2 = m2.name + m2.getValue();
	  }else{
		  s2 = m2.name;
	  }
      final int cmp = (int) (Parallaxa.fontRenderer.getStringWidth(s2)
              - Parallaxa.fontRenderer.getStringWidth(s1));
      return (cmp != 0) ? cmp : s2.compareTo(s1);
  }
}
