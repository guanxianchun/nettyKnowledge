package com.martin.network.netty.pio.object;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 
 * @author 管贤春
 * @时间 2017年12月16日 下午3:43:18
 * @Email psyche19830113@163.com
 * @Description
 *   用户类
 */
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private int age;
	
	private int sex;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name,int age,int sex) {
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	@Override
	public String toString() {
		return new JSONObject(this).toString();
//		return this.name;
	}
}
