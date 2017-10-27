package com.zjxtwvf.smarttime.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static final String TEXT_FORMAT = "<font color='#1479ad'>%s</font> �Ƽ��� <font color='#1479ad'><b>%s</b></font>";

	public static final String TEXT_ADDFRD_FORMAT_WITHFROM = "<font color='#1479ad'>%s</font> �� <font color='#1479ad'>%s</font> ��Ϊ�˺���";

	public static final String TEXT_ADDFRD_NOFROM = "�� <font color='#1479ad'><b>%s</b></font> ��Ϊ�˺���";
	


	public static List<String> fromString(String [] strings){
		ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<strings.length;i++){
			result.add(strings[i]);
		}
		return result;
	}
}
