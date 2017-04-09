package com.cay.Helper.Spider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegContent {
	/**
	 * @author 陈安一
	 * @功能 根据正则表达式匹配返回的网页信息
	 * @param reg
	 * @param info
	 * @return List<String>
	 */
	public static List<String> GetCon(String reg, String info) {
		List<String> result = new ArrayList<String>();
		Matcher m = Pattern.compile(reg).matcher(info);
		while (m.find()) {
			String r = m.group();
			result.add(r);
		}
		return result;
	}

//	public static String GetDiv(String info) {
//		SAXReader reader = new SAXReader();
//		Document doc;
//		try {
//			doc = reader.read(new StringReader(info));
//			Node node = doc.selectSingleNode("//body/div/div/div");
//			System.out.println(node.getText());
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//		}
//		return info;
//	}

	public static String GetOneCon(String reg, String info) {
		String result = info;
		Matcher m = Pattern.compile(reg).matcher(info);
		while (m.find()) {
			result = m.group();
		}
		return result;
	}

	/**
	 * @author 陈安一
	 * @功能 根据GetCon方法返回的List列表对数据进行重组，返回一个URL
	 * @param result
	 * @return
	 */
	public static List<String> GetallURL(List<String> result) {
		for (int i = 0; i < result.size(); i++) {
			result.set(i, "http://www.laossee.com/" + result.get(i) + ".html");
		}
		return result;
	}
}
