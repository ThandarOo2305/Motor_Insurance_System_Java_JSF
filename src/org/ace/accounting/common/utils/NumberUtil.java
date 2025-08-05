package org.ace.accounting.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {

	public static double getTwoDecimalPoint(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	public static double getOneDecimalPoint(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}
	
	public static double divide(double dividend, double divisor) {
		BigDecimal dividendBig = new BigDecimal(dividend);
		BigDecimal divisorBig = new BigDecimal(divisor);
		return dividendBig.divide(divisorBig, 2, RoundingMode.HALF_UP).doubleValue();
	}

	public static double multiply(double num1, double num2) {
		BigDecimal numBig1 = new BigDecimal(num1);
		BigDecimal numBig2 = new BigDecimal(num2);
		return numBig1.multiply(numBig2).doubleValue();
	}
	
}
