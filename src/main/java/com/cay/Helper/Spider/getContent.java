package com.cay.Helper.Spider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cay.Model.Division.vo.Division;

public class getContent {
//	public static void main(String args[]) {
//		String url = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201401/t20140116_501070.html";
//		String html = getContent.getContentFromUrl(url);
//		Document doc = Jsoup.parse(html);
//		Elements content = doc.getElementsByClass("xilan_con").select(".TRS_Editor").select("p");
//		for (Element temp : content) {
//			String[] data = temp.html().split("&nbsp;");
//			if (data.length > 0) {
//				System.out.println(data[data.length-1].trim());
//			}
//		}
//	}
	/**
	 * 根据URL抓取网页内容 此类要用到HttpClient组件
	 * 
	 * @author 陈安一
	 * @param url
	 * @return
	 */
	public static String getContentFromUrl(String url) {
		/* 实例化一个HttpClient客户端 */
		HttpClient client = new DefaultHttpClient();
		HttpGet getHttp = new HttpGet(url);

		String content = null;

		HttpResponse response;
		try {
			/* 获得信息载体 */
			response = client.execute(getHttp);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				/* 转化为文本信息 */
				content = EntityUtils.toString(entity,"utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return content;
	}
}
