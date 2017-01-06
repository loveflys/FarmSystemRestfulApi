package com.cay.Model.Enums;

public enum Sex {
	Man(1),Woman(0);
	
	private int value;
	
	private Sex(int sex) {
		this.value = sex;
	}
	
	@Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
