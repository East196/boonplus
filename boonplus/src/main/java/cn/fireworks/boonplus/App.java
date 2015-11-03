package cn.fireworks.boonplus;

import static org.boon.Boon.BOON_SYSTEM_CONF_DIR;
import static org.boon.Boon.add;
import static org.boon.Boon.className;
import static org.boon.Boon.cls;
import static org.boon.Boon.debug;
import static org.boon.Boon.fromJson;
import static org.boon.Boon.info;
import static org.boon.Boon.jsonResource;
import static org.boon.Boon.putSysProp;
import static org.boon.Boon.simpleName;
import static org.boon.Boon.sysProp;
import static org.boon.Boon.toJson;
import static org.boon.Boon.turnDebugOff;
import static org.boon.Boon.turnDebugOn;
import static org.boon.Boon.warn;

import java.util.Date;

import org.boon.Boon;
import org.boon.Runner;
import org.boon.core.Conversions;
import org.boon.core.reflection.BeanUtils;

import com.google.common.base.Converter;
import com.google.common.primitives.Ints;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	turnDebugOff();
    	debug("dddd");
    	turnDebugOn();
    	String a="";
    	debug(Ints.tryParse(a));
    	debug();
//    	Integer b=null;
    	debug("ddddOn");
    	
    	Date date=new Date();
    	info(date);
    	info(className(date));
    	info(simpleName(date));
    	info(cls(date));
    	info(toJson(date));
//    	info(fromJson("1443063259568",Date.class).getClass());
    	info(add("12123","222"));
    	info(sysProp("tt.mysql", 111));
    	putSysProp("tt.mysql", "mysql");
    	info(sysProp("tt.mysql", 111));
    	info();
//    	info(Boon.gets());
        warn( "Hello World!" );
        
//        Runner.exec("ping","www.baidu.com");
        
        
    }
}
