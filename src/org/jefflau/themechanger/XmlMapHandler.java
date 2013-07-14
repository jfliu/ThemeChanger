package org.jefflau.themechanger;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlMapHandler extends DefaultHandler {
	
	private static final String TAG_ITEM="item";
	private static final String TAG_REDIRE="resource-redirections";
	
	private HashMap<String,String> map=null;
	private String currentTAG;
	private String key;
	private String value;
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		map=new HashMap<String,String>();
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentTAG=null;
		key=null;
		if(TAG_REDIRE.equals(qName)){
			Log.d("================>");
		}
		if(TAG_ITEM.equals(qName)){
			
			int nameIndex=attributes.getIndex("name");
			if(nameIndex>=0){
				currentTAG=qName;;
				key=attributes.getValue(attributes.getLocalName(nameIndex));
				//Log.d(key+" value="+key);
				
			}
			//map.put(key, value);
		}
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(null!=currentTAG&&!"".equals(currentTAG)
		   &&null!=key&&!"".equals(key)
		   &&null!=value&&!"".equals(value)){
			map.put(key, value);
		}else
		{
			Log.d("ÌõÄ¿´íÎó£ºkey="+key+" value="+value);
		}
		if(TAG_REDIRE.equals(qName)){
			Log.d("<================");
		}
		super.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		value=null;
		if(TAG_ITEM.equals(currentTAG)&&length>2){
			value=new String(ch, start, length);
		}
		super.characters(ch, start, length);
	}
	
	public HashMap<String,String> getMap(){
		return map;
	}
	
}
