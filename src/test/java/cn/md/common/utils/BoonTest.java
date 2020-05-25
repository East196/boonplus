package cn.md.common.utils;

import static org.boon.Boon.atIndex;
import static org.boon.Boon.debugOn;
import static org.boon.Boon.println;
import static org.boon.Boon.stringAtIndex;
import static org.boon.Ok.okOrDie;
import static org.boon.di.DependencyInjection.classes;
import static org.boon.di.DependencyInjection.context;
import static org.boon.di.DependencyInjection.fromMap;
import static org.boon.di.DependencyInjection.objects;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.boon.Boon;
import org.boon.Maps;
import org.boon.core.reflection.BeanUtils;
import org.boon.di.Context;
import org.boon.di.Module;
import org.boon.json.JsonParserFactory;
import org.junit.Test;

import cn.md.testmodel.Person;

public class BoonTest {

	@Test
	public void testDi() {
		debugOn();
		Module  module=objects(new Person().setName("2132321"));
		Module  module2=fromMap(Maps.map("user", new Person().setName("111")));
		Context context=context(module,module2);
		Person user=context.get(Person.class);
		println(user);
		println(context.get("user"));
		
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("a", 1);
		map.put("user", context.get("user"));
		System.out.println(map);
		System.out.println(Boon.atIndex(map, "user.name"));
		System.out.println(Boon.isEmpty(Boon.atIndex(map, "user.name")));
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
	public void instantiation() throws Exception {
		debugOn();
		Context context = context(classes(Person.class));
		Person person = context.get(Person.class);
		person.setName("tung");
		person.setSex("male");
		// puts(toPrettyJson(person));
	}

	@Test
	public void dataRepo() throws Exception {
		okOrDie("error", 11);
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
