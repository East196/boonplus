package cn.md.common.utils;

import static org.boon.Boon.atIndex;
import static org.boon.Boon.stringAtIndex;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.boon.Boon;
import org.boon.Ok;
import org.boon.core.Sys;
import org.boon.json.JsonParserFactory;
import org.junit.Test;

public class BoonTest0 {
	@Test
	public void testSys() {
		Boon.turnDebugOn();
		System.out.println(Sys.is1_7());
		System.out.println(Sys.is1_8());

		System.out.println(Sys.isWindows());
		System.out.println(Sys.inContainer());

		System.out.println(Sys.freeMemory());
		System.out.println(Sys.totalMemory());

		Boon.debug(new Date(Sys.time()));
		
		System.out.println(Sys.uptime());
		System.out.println(Sys.startTime());
	}

	@Test
	public void testBoon() {
		Boon.logTraceOn();
		Boon.debug("trace",Boon.isEmpty(""));
		Boon.debug("debug",Boon.isEmpty(""));
		System.out.println(Boon.isEmpty(""));
		System.out.println(Boon.isEmpty("           "));
		System.out.println(Boon.isEmpty(new String[] {}));
		Boon.logTraceOn();
	}
	
	@Test
	public void type() throws Exception {
		// puts(get("http://kpzs.md.cn"));
	}

	@Test
	public void json() throws Exception {
		// puts(toPrettyJson(fromJsonArray(read("json/SqlGroup.json"),
		// SqlGroup.class)));
	}

	@Test
	public void dataRepo() throws Exception {
		Ok.okOrDie("error", 11);
	}

	@Test
	public void jsonPath() throws Exception {
		String json = "{\"clazzName\":\"SqlGroup\",\"fieldMap\":{\"String\":\"name\",\"List<SqlItem>\":\"items\"}}";
		Object jsonObject = new JsonParserFactory().create().parse(json);
		assertEquals("SqlGroup", atIndex(jsonObject, "clazzName"));
		System.out.println(atIndex(jsonObject, "fieldMap").getClass());
		assertEquals("{List<SqlItem>=items, String=name}", stringAtIndex(jsonObject, "fieldMap"));
		// GOOD
		Object fromJson = Boon.fromJson(json);
		System.out.println(atIndex(fromJson, "fieldMap.String"));
	}

	@Test
	public void json2pojo() throws Exception {
		// TODO json2pojo
		String json = "{\"name\":\"Tung\",\"info\":{\"sex\":1,\"phone\":[\"11222\",\"1111\"]}}";
		@SuppressWarnings("unchecked")
		Map<String,Object> map =  Boon.fromJson(json,Map.class);
		System.out.println(map.size());
		System.out.println(map.entrySet());
		for (Entry<String,Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + entry.getValue().toString());
		}
	}

	@Test
	public void config() throws Exception {
		// TODO　advance config
//		Boon.readConfig("", "json");
//		System.out.println(Boon.sysProp("dataSource.url", "1111111111111111111"));
	}	
	
}
