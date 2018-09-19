package cc.alpha.base.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class SimpleJsonResult implements Code {

	private static final long serialVersionUID = 1458048301294721051L;
	
	private String code = SUCCESS;
	private Map<String, Object> payload = new HashMap<String, Object>();

	public SimpleJsonResult() {
		super();
	}
	
	public SimpleJsonResult(String code) {
		this();
		this.code = code;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this, true);
	}

	public String getCode() {
		return this.code;
	}

	public Map<String, Object> getPayload() {
		return this.payload;
	}
	
	public SimpleJsonResult putPayloadObject(String name, Object obj) {
		payload.put(name, obj);
		return this;
	}
	
	public SimpleJsonResult put(String name, Object obj) {
		return putPayloadObject(name, obj);
	}

	public SimpleJsonResult setCode(String code) {
		this.code = code;
		return this;
	}

	public SimpleJsonResult setPayload(Map<String, Object> payload) {
		this.payload = payload;
		return this;
	}

	public SimpleJsonResult removeAllPayload() {
		this.payload.clear();
		return this;
	}
}
