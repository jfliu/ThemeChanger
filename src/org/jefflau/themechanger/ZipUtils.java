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
 * @author  ѹ��ָ����Ŀ¼�Լ���ѹָ����ѹ���ļ�(����ZIP��ʽ).
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
		 * ��ָ��Ŀ¼�µ������ļ�ѹ��������ָ��·����ѹ���ļ�. ���ѹ���ļ���·����·��������, �����Զ�����.
		 * 
		 * @param src
		 *            ��Ҫ����ѹ����Ŀ¼
		 * @param zip
		 *            �������ɵ�ѹ���ļ���·��
		 */
	    public static void zip(File src, File zip) throws IOException {
	        zip(src, new FileOutputStream(zip));
	    }

	    /**
		 * ��ָ��Ŀ¼�µ������ļ�ѹ��������д��ָ�����������.
		 * 
		 * @param src
		 *            ��Ҫ����ѹ����Ŀ¼
		 * @param out
		 *            ���ڽ���ѹ���������ļ����������
		 */
	    public static void zip(File src, OutputStream out) throws IOException {
	        zip(src, new ZipOutputStream(out));
	    }

	    /**
		 * ��ָ��Ŀ¼�µ������ļ�ѹ��������д��ָ����ZIP�������.
		 * 
		 * @param src
		 *            ��Ҫ����ѹ����Ŀ¼
		 * @param zout
		 *            ���ڽ���ѹ���������ļ�����ZIP�����
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
		 * ��ָ������������ѹ��ָ����Ŀ��Ŀ¼��.
		 * 
		 * @param in
		 *            ��Ҫ��ѹ��������
		 * @param dest
		 *            ��ѹ������Ŀ��Ŀ¼
		 */
	    public static void unzip(InputStream in, File dest) throws IOException {
	        unzip(new ZipInputStream(in), dest);
	    }

	    

	    /**
		 * @param src Դ�ļ�Ŀ¼�����ļ�
		 * @param zout �������
		 * @param ns
		 */
	    private static void doZip(File src, ZipOutputStream zout, String base)
	            throws IOException {
	    	 if (src.isDirectory()) {  //�ж��Ƿ�ΪĿ¼  
	             File[] fl = src.listFiles();  
	             base = base.length() == 0 ? "" : base + "/";  
	           //  zout.putNextEntry(new ZipEntry(src.getName()));  
	             for (int i = 0; i < fl.length; i++) {  
	            	 doZip( fl[i],zout, base + fl[i].getName());  
	             }  
	         } else {                //ѹ��Ŀ¼�е������ļ�  
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
	    				zout.write(b, 0, n);// ����ĸ�
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
		 * ��ָ����ZIP��������ѹ��ָ����Ŀ��Ŀ¼��.
		 * 
		 * @param zin
		 *            ��Ҫ��ѹ��ZIP������
		 * @param dest
		 *            ��ѹ������Ŀ��Ŀ¼
		 */
	    /*public static void unzip(ZipInputStream zin, File dest) throws IOException {
	        try {
	            doUnzip(zin, dest);
	        } finally {
	            IOUtils.closeQuietly(zin);
	        }
	    }*/
	    /**
		 * ��ָ����ѹ���ļ���ѹ��ָ����Ŀ��Ŀ¼��. ���ָ����Ŀ��Ŀ¼�����ڻ��丸·��������, �����Զ�����.
		 * 
		 * @param zip
		 *            �����ѹ��ѹ���ļ�
		 * @param dest
		 *            ��ѹ������Ŀ¼Ŀ¼
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
				ZipUtils.zip(new File("D:\\evidence\\20120712\\�����ص�1997_2012071213114144\\����С˵��_www.xs8.cn")
				, new File("D:\\evidence\\20120712\\�����ص�1997_2012071213114144\\����.zip"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
	} 

