package cookbook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

public class TestThumbnails {

	public static void main1(String[] args) throws IOException{
		File file = new File("C:\\Users\\fish\\Desktop\\应理营业执照\\微信图片_20171114175204.jpg");
		InputStream input = new FileInputStream(new File("C:\\Users\\fish\\Desktop\\应理营业执照\\微信图片_20171114175204.jpg"));
		
		String fileFix = file.getName().substring(file.getName().lastIndexOf("."));
		System.out.print(fileFix);
		BufferedImage bufImg = ImageIO.read(input);// 把图片读入到内存中
		OutputStream out = new FileOutputStream(new File("C:\\Users\\fish\\Desktop\\应理营业执照\\微信图片_20171114175204-1.jpg"));
		// 压缩代码
		bufImg = Thumbnails.of(bufImg).width(300).keepAspectRatio(true).outputQuality(0.8).asBufferedImage();
		//ByteArrayOutputStream bos = new ByteArrayOutputStream();// 存储图片文件byte数组
		ImageIO.write(bufImg, "jpg", out); // 图片写入到 ImageOutputStream
		//input = new ByteArrayInputStream(bos.toByteArray());
	}
	
	public static void main(String[] args){
		 int d = 68;
		 System.out.println(d/60+"分"+d%60+"秒");
	}
}
