package org.jefflau.themechanger;

public class Log {
	public static final String DEFAULTTAG="Log";
	
	public static void d(String TAG,Object obj){
		System.out.println("["+TAG+"] "+obj);
	}
	
	public static void d(Object obj){
		d(DEFAULTTAG,obj);
	}
	
	/**
	public static void d(String format,Object ...args){
		d(DEFAULTTAG,format,args);
	}
	public static void d(String format){
		d(DEFAULTTAG,format);
		
	}
	public static void d(String TAG,String format, Object ... args){
		System.out.printf("[%s] "+format,TAG,args);
		
	}
	**/
}
