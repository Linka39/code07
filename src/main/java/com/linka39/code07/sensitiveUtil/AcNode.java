package com.linka39.code07.sensitiveUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class AcNode
{
    //孩子节点用HashMap存储，能够在O(1)的时间内查找到，效率高
    Map<Character,AcNode> children=new HashMap<>();
    AcNode failNode;
    //使用set集合存储字符长度，防止敏感字符重复导致集合内数据重复
    Set<Integer> wordLengthList = new HashSet<>(); //exist数组，记录敏感词的终止位，方便回溯查找匹配

    public Map<Character, AcNode> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, AcNode> children) {
        this.children = children;
    }

    public AcNode getFailNode() {
        return failNode;
    }

    public void setFailNode(AcNode failNode) {
        this.failNode = failNode;
    }

    public Set<Integer> getWordLengthList() {
        return wordLengthList;
    }

    public void setWordLengthList(Set<Integer> wordLengthList) {
        this.wordLengthList = wordLengthList;
    }
}
