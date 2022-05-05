package com.linka39.code07.jctreeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class TreeNode {
	private Object nodeName;//节点名称
	private List<Object> splitShuXing; // 分裂属性名
	private ArrayList<TreeNode> sonNode;//决策树的子节点
	private Vector<Object> shuXing;//属性

	public TreeNode() {
		sonNode=new ArrayList<TreeNode>();
	}
	public Vector<Object> getShuXing() {
		return shuXing;
	}
	public void setShuXing(Vector<Object> shuXing) {
		this.shuXing = shuXing;
	}
	public Object getNodeName() {
		return nodeName;
	}
	public void setNodeName(Object nodeName) {
		this.nodeName = nodeName;
	}
	public List<Object> getSplitShuXing() {
		return splitShuXing;
	}
	public void setSplitShuXing(List<Object> splitShuXing) {
		this.splitShuXing = splitShuXing;
	}


	public ArrayList<TreeNode> getSonNode() {
		return sonNode;
	}

	public void addSonNode(TreeNode sonNode2) {
		this.sonNode.add(sonNode2);
	}




}
