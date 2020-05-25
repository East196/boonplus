package cn.md.common.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.github.east196.ezboot.address.baidu.BaiduAddress2Geo;
import com.github.east196.ezboot.address.baidu.BaiduGeo2Address;
import com.github.east196.ezboot.address.chunzhen.IPSeeker;
import com.github.east196.ezboot.address.sina.SinaIp2Address;
import com.github.east196.ezboot.address.taobao.TaobaoIp2Address;
import com.google.gson.Gson;

public class AddressTest {

	@Test
	public void testSinaIp2Address() {
		RestTemplate restTemplate = new RestTemplate();
		String json = restTemplate.getForObject("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=123.123.123.123", String.class);// 响应的是html...
		SinaIp2Address realAddress = new Gson().fromJson(json, SinaIp2Address.class);
		String city = realAddress.getCity();
		System.out.println(realAddress);
		assertThat(city, is("北京"));
	}

	@Test
	public void testSinaIp2AddressLocal() {
		RestTemplate restTemplate = new RestTemplate();
		String json = restTemplate.getForObject("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=192.168.100.81", String.class);// 响应的是html...
		SinaIp2Address realAddress = new Gson().fromJson(json, SinaIp2Address.class);
		System.out.println(realAddress);
	}

	@Test
	public void testTaobaoIp2Address() {
		RestTemplate restTemplate = new RestTemplate();
		String json = restTemplate.getForObject("http://ip.taobao.com/service/getIpInfo.php?ip=123.123.123.123", String.class);// 响应的都是html...
		TaobaoIp2Address realAddress = new Gson().fromJson(json, TaobaoIp2Address.class);
		String city = realAddress.getData().getCity();
		System.out.println(realAddress);
		assertThat(city, is("北京市"));
	}

	@Test
	public void testTaobaoIp2AddressLocal() {
		RestTemplate restTemplate = new RestTemplate();
		String json = restTemplate.getForObject("http://ip.taobao.com/service/getIpInfo.php?ip=192.168.100.81", String.class);// 响应的都是html...
		TaobaoIp2Address realAddress = new Gson().fromJson(json, TaobaoIp2Address.class);
		System.out.println(realAddress);
	}

	@Test
	public void testQQwry() {
		// 指定纯真数据库的文件名，所在文件夹
		IPSeeker ip = new IPSeeker("qqwry.dat", "D:/Program Files (x86)/cz88.net/ip");
		// 测试IP 123.123.123.123
		System.out.println(ip.getIPLocation("123.123.123.123").getCountry());// Address
		System.out.println(ip.getIPLocation("123.123.123.123").getArea());// ISP
	}
	
	
	@Test
	public void testBaiduAddress2Geo() {
		System.out.println("renderOption&&renderOption(".length());
		RestTemplate restTemplate = new RestTemplate();
		String back = restTemplate.getForObject("http://api.map.baidu.com/geocoder/v2/?ak=key&callback=renderOption&output=json&address=百度大厦&city=北京市", String.class);// 响应的都是html...
		String json=back.substring(27, back.length()-1);
		BaiduAddress2Geo geo = new Gson().fromJson(json, BaiduAddress2Geo.class);
		System.out.println(geo);
	}
	
	@Test
	public void testBaiduGeo2Address() {
		RestTemplate restTemplate = new RestTemplate();
		String back = restTemplate.getForObject("http://api.map.baidu.com/geocoder/v2/?ak=key&callback=renderReverse&location=32.0699833,118.8147333&output=json&pois=0", String.class);// 响应的都是html...
		String json=back.substring(29, back.length()-1);
		System.out.println(json);
		BaiduGeo2Address realAddress = new Gson().fromJson(json, BaiduGeo2Address.class);
		System.out.println(realAddress);
	}
}
