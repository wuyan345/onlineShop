package com.shop.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {

	// 防止被实例化
	private BigDecimalUtil(){
		
	}
	
	public static BigDecimal add(double a, double b){
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal b1 = new BigDecimal(Double.toString(b));
		return a1.add(b1);
	}
	
	public static BigDecimal sub(double a, double b){
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal b1 = new BigDecimal(Double.toString(b));
		return a1.subtract(b1);
	}
	
	public static BigDecimal mul(double a, double b){
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal b1 = new BigDecimal(Double.toString(b));
		return a1.multiply(b1);
	}
	
	public static BigDecimal div(double a, double b){
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal b1 = new BigDecimal(Double.toString(b));
		return a1.divide(b1, 2, BigDecimal.ROUND_HALF_UP);
	}
}
