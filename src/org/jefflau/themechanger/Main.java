package org.jefflau.themechanger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Main {
	public static final String APKSUFFIX=".apk";
	public static final String ORGDIR="org";
	public static final String ORGTMPDIR="org/tmp";
	
	public static final String ORGTMPRESDIR="org/tmp/res";
	
	public static final String ORGTMPRESXMLDIR="org/tmp/res/xml";
	
	public static final String DESTDIR="dest";
	public static final String DESTTMPDIR="dest/tmp";
	
	public static final String PREFIXDRAW="drawable";
	public static final String PREFIXMIPMAP="mipmap";
	public static final String RES = "res";
	
	private static boolean apktool(String apkName){
		boolean flag=false;
		
		File file=new File(ORGDIR+File.separator+apkName);
		if(null==file||!file.isFile()||!apkName.endsWith(APKSUFFIX)){
			Log.d(file.getAbsolutePath()+" 文件不存在或者不是"+APKSUFFIX+"文件");
		}else{
			String filePath=file.getAbsolutePath();
			Log.d("文件路径："+filePath);
			file=new File(ORGTMPDIR);
			if(null!=file&&!file.exists()){
				flag=file.mkdirs();
				Log.d(ORGTMPDIR+" 目录创建成功！");
			}
			String command="tools/apktool.bat d -f "+ORGDIR+File.separator+apkName+" "+ORGTMPDIR;
			try {
				Process proc=Runtime.getRuntime().exec(command);
				
				BufferedReader in=new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String out=null;
				while((out=in.readLine())!=null){
					Log.d("----"+out);
				}
				proc.waitFor();
				if(proc.exitValue()==0){
					Log.d("反编译成功！ file="+apkName);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return true;
	}
	private static void delFile(File file){
		if(null==file||!file.exists())return;
		if(file.isDirectory()){
			File[] list=file.listFiles();
			for(File child:list){
				delFile(child);
			}
		}
		file.delete();
	}
	private static void delEmptyDir(File file){
		if(null==file||!file.exists())return;
		if(file.isDirectory()){
			File[] list=file.listFiles();
			for(File child:list){
				delEmptyDir(child);
			}
			file.delete();
		}
		//file.delete();
	}
	private static boolean cleanDestTempFile(){
		File file=new File(DESTTMPDIR);
		delFile(file);
		
		return true;
	}
	
	private static boolean cleanDestTempEmptyDir(){
		File file=new File(DESTTMPDIR);
		delEmptyDir(file);
		
		return true;
	}
	
	
	private static boolean mkdirs(String pkgName,File[]res){
		/*File destResDir=new File(DESTTMPRESDIR+File.separator+pkgName);
		if(!destResDir.exists()){
			destResDir.mkdirs();
		}*/
		
		for(File draw:res){
			String drawName=draw.getName();
			if(draw.isDirectory()
				&&(drawName.startsWith(PREFIXDRAW)||drawName.startsWith(PREFIXMIPMAP))){
				File destResDir=new File(DESTTMPDIR+
						File.separator+pkgName+
						File.separator+RES+
						File.separator+draw.getName());
				if(!destResDir.exists()){
					destResDir.mkdirs();
				}
			}
		}
		return true;
	}

	private static boolean copyFile(String org, String dest) throws IOException {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(org);
			fout = new FileOutputStream(dest);
			byte[] b = new byte[512];
			int n;
			while ((n = fin.read(b)) != -1) {
				fout.write(b, 0, n);// 这里改改
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private static boolean change(File xmlFile,File[]res){
		boolean flag=false;
		Log.d("开始转换文件 path="+xmlFile.getAbsolutePath());
		HashMap<String,String>map=parseXml(xmlFile.getAbsolutePath());
		
		if(null==map||map.size()==0){
			Log.d("文件错误");
			return false;
		}
		String pkgName=xmlFile.getName();
		
		// 转换包名
		pkgName=pkgName.substring(0, pkgName.lastIndexOf("."));
		pkgName=pkgName.replace("_", ".");
		
		flag=mkdirs(pkgName,res);
		if(!flag)return flag;
		for(File resDir:res){
			String[] resDirFileList=resDir.list();
			//Log.d(resDirFileList);
			Set<String> keyList=map.keySet();
			
			for(String str:keyList){
				//Log.d("key="+str+" value="+map.get(str));
				// 1.从org/tmp/res/... 目录下拷贝到dest/tmp/$pkgName/res/... 目录下
				boolean nightFlag=true;
				String drawable=map.get(str);//@drawable/framework_btn_default_disabled_holo_light
				drawable=drawable.substring(drawable.lastIndexOf("/")+1)+".9.png";
				//Log.d(drawable);
				int index=Arrays.binarySearch(resDirFileList, drawable);
				if(index<0){
					drawable=drawable.replace(".9","");
					index=Arrays.binarySearch(resDirFileList, drawable);
					nightFlag=false;
				}
				if(index>=0){
					//System.out.println("ddddd "+resDirFileList[index]);
					String orgDrawable=resDir.getAbsolutePath()+File.separator+drawable;
					
					
					str=str.substring(str.lastIndexOf("/")+1);
					String destDrawable=DESTTMPDIR+
						File.separator+pkgName+
						File.separator+RES+
						File.separator+resDir.getName()+
						File.separator+str;
					if(nightFlag){
						destDrawable=destDrawable+".9.png";
					}else{
						destDrawable=destDrawable+".png";
					}
					//拷贝文件 orgDrawable到destDrawable
					
					try {
						copyFile(orgDrawable,destDrawable);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					
				}
				// 2.删除没有文件的目录
				
				
			}
		}
		
		return true;
	}
	private static HashMap<String,String> parseXml(String path){
		//create factory
		SAXParserFactory factory=SAXParserFactory.newInstance();
		XmlMapHandler handler=new XmlMapHandler();
		SAXParser sp;
		try {
			//get SAXParser
			sp = factory.newSAXParser();
			XMLReader reader=sp.getXMLReader();
			reader.setContentHandler(handler);
			
			reader.parse(path);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return handler.getMap();
		
		
	}
	private static boolean changDrawable(){
		File file=new File(ORGTMPRESDIR);
		if(null==file||!file.exists()||!file.isDirectory()){
			Log.d("res 文件不存在  resPath="+file.getAbsolutePath());
			return false;
		}
		File[] res=file.listFiles();
		if(null==res||res.length<=2){
			return false;
		}
		file=new File(ORGTMPRESXMLDIR);
		if(null==file||!file.exists()||!file.isDirectory()){
			Log.d("xml 文件目录错误");
			return false;
		}
		File[] xml=file.listFiles();
		
		// 删除dest/tmp的临时目录
		cleanDestTempFile();
		
		for(File xmlFile:xml){
			change(xmlFile,res);
		}
		//删除空的目录
		cleanDestTempEmptyDir();
		return true;
		
	}
	private static void printMap(HashMap<String,String>map){
		
		Set<String> keyList=map.keySet();
		for(String str:keyList){
			Log.d("key="+str+" value="+map.get(str));
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//apkName
		String apkName="CM10.1_HTC_One_Sense_5.0_theme.apk";
		boolean flag=apktool(apkName);
		if(!flag){
			Log.d("反编译失败【退出】");
			return;
		}
		// 图片转换
		changDrawable();
		// appt 编译图片
		
		// 压缩所以文件
		
	}
	
}
