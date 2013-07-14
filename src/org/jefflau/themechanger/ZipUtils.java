package org.jefflau.themechanger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author  压缩指定的目录以及解压指定的压缩文件(仅限ZIP格式).
 */
	public class ZipUtils {
		
		public static void zip(File file, String zip) throws IOException {
			// TODO Auto-generated method stub
			zip(file,new File(zip));
		}
		public static void zip(String src, String zip) throws IOException {
			zip(new File(src),new File(zip));
		}
	    /**
		 * 将指定目录下的所有文件压缩并生成指定路径的压缩文件. 如果压缩文件的路径或父路径不存在, 将会自动创建.
		 * 
		 * @param src
		 *            将要进行压缩的目录
		 * @param zip
		 *            最终生成的压缩文件的路径
		 */
	    public static void zip(File src, File zip) throws IOException {
	        zip(src, new FileOutputStream(zip));
	    }

	    /**
		 * 将指定目录下的所有文件压缩并将流写入指定的输出流中.
		 * 
		 * @param src
		 *            将要进行压缩的目录
		 * @param out
		 *            用于接收压缩产生的文件流的输出流
		 */
	    public static void zip(File src, OutputStream out) throws IOException {
	        zip(src, new ZipOutputStream(out));
	    }

	    /**
		 * 将指定目录下的所有文件压缩并将流写入指定的ZIP输出流中.
		 * 
		 * @param src
		 *            将要进行压缩的目录
		 * @param zout
		 *            用于接收压缩产生的文件流的ZIP输出流
		 */
	    public static void zip(File src, ZipOutputStream zout) throws IOException {
	        try {
	            doZip(src, zout, "");
	        } finally {
	            if(null!=zout){
	            	zout.close();
	            }
	        }
	    }

	   

	    /**
		 * 将指定的输入流解压到指定的目标目录下.
		 * 
		 * @param in
		 *            将要解压的输入流
		 * @param dest
		 *            解压操作的目标目录
		 */
	    public static void unzip(InputStream in, File dest) throws IOException {
	        unzip(new ZipInputStream(in), dest);
	    }

	    

	    /**
		 * @param src 源文件目录或者文件
		 * @param zout 输出的流
		 * @param ns
		 */
	    private static void doZip(File src, ZipOutputStream zout, String base)
	            throws IOException {
	    	 if (src.isDirectory()) {  //判断是否为目录  
	             File[] fl = src.listFiles();  
	             base = base.length() == 0 ? "" : base + "/";  
	           //  zout.putNextEntry(new ZipEntry(src.getName()));  
	             for (int i = 0; i < fl.length; i++) {  
	            	 doZip( fl[i],zout, base + fl[i].getName());  
	             }  
	         } else {                //压缩目录中的所有文件  
	        	 if(null!=base&&!base.equals("")){
	        		 zout.putNextEntry(new ZipEntry(base));  
	        		 fillZip(new FileInputStream(src),zout);
	        		 
	        	 }
	         }  
	    }
	    /**
		 * @param in
		 * @param zout
		 */
	    private static void fillZip(InputStream in, ZipOutputStream zout)
	            throws IOException {
	       
	    		try {
	    			
	    			byte[] b = new byte[512*1024];
	    			int n;
	    			while ((n = in.read(b)) != -1) {
	    				zout.write(b, 0, n);// 这里改改
	    			}
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		} finally {
	    			try {
	    				in.close();
	    				//zout.close();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	        
	    }
	    /**
		 * 将指定的ZIP输入流解压到指定的目标目录下.
		 * 
		 * @param zin
		 *            将要解压的ZIP输入流
		 * @param dest
		 *            解压操作的目标目录
		 */
	    /*public static void unzip(ZipInputStream zin, File dest) throws IOException {
	        try {
	            doUnzip(zin, dest);
	        } finally {
	            IOUtils.closeQuietly(zin);
	        }
	    }*/
	    /**
		 * 将指定的压缩文件解压到指定的目标目录下. 如果指定的目标目录不存在或其父路径不存在, 将会自动创建.
		 * 
		 * @param zip
		 *            将会解压的压缩文件
		 * @param dest
		 *            解压操作的目录目录
		 */
	    /*public static void unzip(File zip, File dest) throws IOException {
	        unzip(FileUtils.openInputStream(zip), dest);
	    }*/

	    /**
		 * @param zin
		 * @param dest
		 */
	    /*private static void doUnzip(ZipInputStream zin, File dest)
	            throws IOException {
	        for (ZipEntry e; (e = zin.getNextEntry()) != null; zin.closeEntry()) {
	            File file = new File(dest, e.getName());
	            if (e.isDirectory()) {
	                System.out.println(" creating: {}"+file.getName() + File.separator);
	                FileUtils.forceMkdir(file);
	            } else {
	                System.out.println("inflating: {}"+file);
	                flushZip(zin, FileUtils.openOutputStream(file));
	            }
	        }
	    }*/

	    

	    /**
		 * @param zin
		 * @param out
		 */
	   /* private static void flushZip(ZipInputStream zin, OutputStream out)
	            throws IOException {
	        try {
	            IOUtils.copy(zin, out);
	        } finally {
	            IOUtils.closeQuietly(out);
	        }
	    }*/

	    
	    public static void main(String[] args){
	    	try {
				ZipUtils.zip(new File("D:\\evidence\\20120712\\重生回到1997_2012071213114144\\言情小说吧_www.xs8.cn")
				, new File("D:\\evidence\\20120712\\重生回到1997_2012071213114144\\言情.zip"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
	} 

