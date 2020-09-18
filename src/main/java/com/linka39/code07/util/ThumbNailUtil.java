package com.linka39.code07.util;

import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片处理工具类
 * @author Administrator
 *
 */
public class ThumbNailUtil {

	public static void toThumbPic(String src,String name){

		//创建图片文件(此处为1024×768px的图片)和处理后的图片文件
		File fromPic = new File("E:\\my_project\\code07\\src\\main\\java\\com\\linka39\\code07\\util\\slides\\1.jpg");
		File toPic = new File("E:\\my_project\\code07\\src\\main\\java\\com\\linka39\\code07\\util\\slides\\thumbs\\2.jpg");

	}
	/**
	 * @return
	 */
	public static void test() throws IOException {
		//创建图片文件(此处为1024×768px的图片)和处理后的图片文件
		File fromPic = new File("E:\\my_project\\code07\\src\\main\\java\\com\\linka39\\code07\\util\\slides\\1.jpg");
		File toPic = new File("E:\\my_project\\code07\\src\\main\\java\\com\\linka39\\code07\\util\\slides\\thumbs\\2.jpg");
		File waterPic = new File("E:\\my_project\\code07\\src\\main\\java\\com\\linka39\\code07\\util\\slides\\thumbs\\tree.jpg");//作为水印的图片
		//按指定大小把图片进行缩和放（会遵循原图高宽比例）
		//此处把图片压成400×500的缩略图
		Thumbnails.of(fromPic).size(100,75).toFile(toPic);//变为400*300,遵循原图比例缩或放到400*某个高度

		//不按比例，就按指定的大小进行缩放
		//Thumbnails.of(fromPic).size(100, 100).keepAspectRatio(false).toFile(toPic);
	}

	public static void main(String[] args) throws IOException {
		test();
	}
}
