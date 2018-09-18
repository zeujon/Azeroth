package com.velcro.base;

import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

public final class IDGernerator {
	static IdentifierGenerator gen = new UUIDHexGenerator();
	static {
		( (Configurable) gen ).configure(Hibernate.STRING, new Properties(), null);
	}
	public static String getUnquieID(){
		return(String) gen.generate(null, null);
	}
	public static void main(String[] args) {

		long startTime=System.currentTimeMillis();   //获取开始时间
		for(int i=0;i<100;i++){
			System.out.println( gen.generate(null, null));
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		
		System.out.println(("程序getDataByType运行时间： "+(endTime-startTime)+"ms"));
	}
}
