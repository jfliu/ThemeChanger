package org.jefflau.themechanger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
	public static final String APKSUFFIX=".apk";
	public static final String ORGDIR="org";
	public static final String ORGTMPDIR="org/tmp";
	
	public static final String ORGTMPRESDIR="org/tmp/res";
	
	public static final String ORGTMPRESXMLDIR="org/tmp/res/xml";
	private static boolean apktool(String apkName){
		boolean flag=false;
		
		File file=new File(ORGDIR+File.separator+apkName);
		if(null==file||!file.isFile()||!apkName.endsWith(APKSUFFIX)){
			Log.d(file.getAbsolutePath()+" �ļ������ڻ��߲���"+APKSUFFIX+"�ļ�");
		}else{
			String filePath=file.getAbsolutePath();
			Log.d("�ļ�·����"+filePath);
			file=new File(ORGTMPDIR);
			if(null!=file&&!file.exists()){
				flag=file.mkdirs();
				Log.d(ORGTMPDIR+" Ŀ¼�����ɹ���");
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
					Log.d("������ɹ��� file="+apkName);
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
	
	private static boolean getRedMap(){
		File file=new File(ORGTMPRESDIR);
		if(null==file||!file.exists()||!file.isDirectory()){
			Log.d("res �ļ�������  resPath="+file.getAbsolutePath());
			return false;
		}
		File[] res=file.listFiles();
		
		file=new File(ORGTMPRESXMLDIR);
		if(null==file||!file.exists()||!file.isDirectory()){
			Log.d("xml �ļ�Ŀ¼����");
			return false;
		}
		File[] xml=file.listFiles();
		for(File redire:xml){
			Log.d(redire.getAbsolutePath());
		}
		
		return true;
		
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
			Log.d("������ʧ�ܡ��˳���");
			return;
		}
		getRedMap();
		
	}
	
}
