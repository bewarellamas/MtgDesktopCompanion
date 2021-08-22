package org.magic.api.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountAuthenticator implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PASSWORD = "PASSWORD";
	public static final String LOGIN = "LOGIN";
	protected Map<String,String> tokens;
	private String name;
	
	public AccountAuthenticator(String name) {
		tokens = new HashMap<>();
		this.name=name;
	}
	
	
	

	public AccountAuthenticator(String name, String login,String password) {
		tokens = new HashMap<>();
		tokens.put(LOGIN, login);
		tokens.put(PASSWORD, password);
		this.name=name;
	}

	
	
	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public Map<String, String> getTokens() {
		return tokens;
	}
	
	public void addToken(String k, String val)
	{
		tokens.put(k, val);
	}
	
	
	public void addLoginPassword(String login,String password)
	{
		tokens.put(LOGIN, login);
		tokens.put(PASSWORD, password);
	}
	
	public String get(String key)
	{
		return tokens.get(key);
	}
	
	public String getLogin()
	{
		return get(LOGIN);
	}
	
	public String getPassword()
	{
		return get(PASSWORD);
	}

	
	
}