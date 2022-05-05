package com.linka39.code07.jctreeUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * 熵类
 * @author：Dyl
 */
public class Gain {

	/**
	 * D的熵值
	 * @param testData：样本数据
	 * @param index：下标
	 * @return：infoD
	 */
	public static double infoD(Vector<Object>[] testData, int index) {
		double infoD = 0;
		Map<Object, Integer> mapCount = SXCount(testData, index);// 统计下各个类别下的各个属性
		int size = testData[index].size();// 最后一列的大小
		Object[] ob = mapCount.keySet().toArray();
		for (int i = 0; i < ob.length; i++) {
			double pi = (double) mapCount.get(ob[i]) / size;
			infoD += (-1) * pi * Math.log(pi) / Math.log(2);
//			System.out.println("info:"+infoD);
		}
		return infoD;
	}

	/**
	 * 统计下各个类别下的各个属性
	 * @param testData：样本数据
	 * @param index：下标
	 * @return
	 */
	private static Map<Object, Integer> SXCount(Vector<Object>[] testData,
												int index) {
		Vector<Object> Item = testData[index];
		Map<Object, Integer> mapCount = new HashMap<Object, Integer>();
		for (int i = 0; i < Item.size(); i++) {
			Object object = Item.get(i);
			if (mapCount.containsKey(object)) {
				mapCount.put(object, mapCount.get(object) + 1);// 一个Map中不能包含相同的key，每个key只能映射一个value
			} else {
				mapCount.put(object.toString(), 1);
			}
		}
		return mapCount;
	}

	/**
	 * 得到第i类下的属性
	 * @param i:第i类
	 * @param testData
	 * @return
	 */
	public static Vector<Object> getSX(Vector<Object>[] testData, int i) {
		Vector<Object> objects = new Vector<Object>();
		Vector<Object> list = testData[i];
		for (int j = 0; j < list.size(); j++) {
			Object o = list.get(j);
			if (!objects.contains(o)) {
				objects.add(o);
			}
		}
		return objects;
	}

	/**算指定列上的信息熵
	 * @param ob1
	 * @param index1
	 * @param index2
	 * @return:不同属性的熵值
	 */
	public static double infoSX(Vector<Object>[] ob1, int index1, int index2) {
		Map<Object, Integer> map = SXCount(ob1, index1);
		Vector<Object> value1 = getSX(ob1, index1);
		//获取属性，计算数据集的香农熵
		double infoX= 0.000;
		for (int i = 0; i < value1.size(); i++) {
			Map<Object, Integer> temp = new HashMap<Object, Integer>();
			// 这个写法是遍历数据集中的每一行，把其中的第i个数据取出来组合成一个列表，每个i列表示一种属性
			Object object1 = value1.get(i);
			for (int j = 0; j < ob1[index1].size(); j++) {
				Object object2 = ob1[index1].get(j);
				if (object1.toString().equals(object2.toString())) {
					Object object3 = ob1[index2].get(j);
					//去掉数据的重复元素
					if (temp.containsKey(object3)) {
						temp.put(object3, temp.get(object3) + 1);
					} else {
						temp.put(object3, 1);
					}
				}
			}
			double[] pi = new double[temp.size()];
			Object[] tt = temp.keySet().toArray();
			//找列表的第一个列表里遍历，在遍历列表里的第二个列表，以此类推
			for (int j = 0; j < pi.length; j++) {
				pi[j] = (double) temp.get(tt[j]) / map.get(object1);
			}//给出在属性i下不同的特征值获取每种不同划分方式的数据集
			//对应到决策树的情况就是每次选判断条件（特征值），通过这个判断条件之后剩下来的数据集的信息熵是否减少
			double item = 0;
			for (int j = 0; j < pi.length; j++) {
				if (pi[j] == 0) {
					pi[j] = 1;
				}
				item += (double) pi[j] * Math.log(pi[j]) / Math.log(2);
			}
			infoX+= (double) map.get(object1) / ob1[index1].size() * (-1 * (item));
		}
		//找到最好的划分方式特征并返回
		return infoX;
	}
}
