package com.batterydoctor.superfastcharger.fastcharger.fastercharger;

public class BatteryData {

	private int current_Battery;
	private int    health;
	private int    temperature;
	private int    voltage;
	private String technology;
	private int    plugged;
	private int    status;
	private int   level;
	private int   scale;
	
	public int getCurrent_Battery() {
		return current_Battery;
	}
	public void setCurrent_Battery(int current_Battery) {
		this.current_Battery = current_Battery;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getVoltage() {
		return voltage;
	}
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public int getPlugged() {
		return plugged;
	}
	public void setPlugged(int plugged) {
		this.plugged = plugged;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	

	}
