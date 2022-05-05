package com.linka39.code07.jctreeUtil;

import java.io.*;
import java.util.Vector;

/**文件操作类
 * @author：Dyl
 *
 */
public class MyFile {

	/**
	 *读数据
	 * @throws Exception
	 */
	public  static  Vector<Object>[] readData(String url) throws Exception {
		Vector<Object>[]vector = null;
		String[] list;
		File file = new File(url);
		InputStreamReader in = new InputStreamReader(new FileInputStream(file));
		BufferedReader reader = new BufferedReader(in);
		String line = reader.readLine();
		line = reader.readLine();//读了两行，跳过属性
		if (line!=null) {
			vector=new Vector[line.split(",").length];
			for (int i = 0; i < vector.length; i++) {
				vector[i]=new Vector<Object>();
			}
		}
		while ((line=reader.readLine()) != null) {
			String []s=line.split(",");
			for (int i = 0; i < vector.length; i++) {
				vector[i].add(s[i]);
			}
		}
		in.close();
		reader.close();
		return vector;
	}



	/**获取属性
	 * @param urlTest：文件路径
	 * @return
	 * @throws IOException
	 */
	public static Vector<Object> getShuXing(String urlTest) throws Exception {
		Vector<Object> vector=new Vector<Object>();
		File file = new File(urlTest);
		InputStreamReader in = new InputStreamReader(new FileInputStream(file));
		BufferedReader reader = new BufferedReader(in);
		String string=reader.readLine();
		if (!string.equals("")) {
			String []t=string.split(",");//读了两行
			for (String st : t) {
				vector.add(st);
			}
		}
		in.close();
		reader.close();
		return vector;
	}



	/**横着保存predict的数据
	 * @param urlPredict：文件路径
	 * @return
	 * @throws IOException
	 */
	public static Vector<Object[]> readPredictData(String urlPredict) throws IOException {
		Vector<Object[]>vector = new Vector<Object[]>();
		File file = new File(urlPredict);
		InputStreamReader in = new InputStreamReader(new FileInputStream(file));
		BufferedReader reader = new BufferedReader(in);
		String line = reader.readLine();
		line = reader.readLine();//读了两行，跳过属性
		while ((line=reader.readLine()) != null) {
			String []s=line.split(",");
			vector.add(s);
		}
		in.close();
		reader.close();
		return vector;
	}
}
