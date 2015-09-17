//章琦 141250199
public class ALU {
	// 1
	public static String calculation(String formula) {
		String result = null;
		int i;
		if(formula.charAt(0)=='('){i=2;}else{i=1;}
		for (; ((formula.charAt(i) > 47)||(formula.charAt(i)<42)||(formula.charAt(i)=='.')); i++) {
		}
		String op1 = formula.substring(0, i);
		String op2 = formula.substring(i + 1, formula.length() - 1);
		
		//System.out.println(op1+"\n"+op2);
		
		char operate = formula.charAt(i);
		int o1 = op1.indexOf('.');
		int o2 = op2.indexOf('.');
		
		if(op1.charAt(0)=='('){op1=op1.substring(1, op1.length()-1);}
		if(op2.charAt(0)=='('){op2=op2.substring(1, op2.length()-1);}
		
		if ((o1 < 0) && (o2 < 0)) {
			op1 = integerRepresentation(op1, 32);
			op2 = integerRepresentation(op2, 32);
			
			//System.out.println(op1+"\n"+op2);
			
			if (operate == '+') {
				result = integerAddition(op1, op2, '0', 32);
				
				System.out.println(result);
				
				if (result.charAt(32) == '1') {
					if (result.charAt(0) == '1') {
						result = "1" + result.substring(0,32);
					} else {
						result = "0" + result.substring(0,32);
					}
				}
				else{
					result=result.substring(0,32);
				}
				
				
				//System.out.println(result);
				
				result = integerTrueValue(result);
			}
			if (operate == '-') {
				result = integerSubtraction(op1, op2, 32);
				if (result.charAt(32) == '1') {
					if (result.charAt(0) == '1') {
						result = "1" + result.substring(0,32);
					} else {
						result = "0" + result.substring(0,32);
					}
				}else{
					result = result.substring(0,32);
				}
				result = integerTrueValue(result);
			}
			if (operate == '*') {
				result = integerMultiplication(op1, op2, 32);
				result = integerTrueValue(result);
			}
			if (operate == '/') {
				result = integerDivision(op1, op2, 32);
				result=result.substring(0, 31);
				result = integerTrueValue(result);
			}
		} else {
			if( (o1 >= 0)&&(o2<0) ){
				op2 = op2 + ".0";
			}
			if ((o2 >= 0)&&(o1<0)) {
				op1 = op1 + ".0";
			}
			
			System.out.println(op1+"\n"+op2);
			
			op1 = ieee754(op1, 32);
			op2 = ieee754(op2, 32);
			
			System.out.println("hhhhh"+op1+"\n"+op2);
			
			
			if (operate == '+') {
				result = floatAddition(op1, op2, 23, 8, 8);
			}
			if (operate == '-') {
				result = floatSubtraction(op1, op2, 23, 8, 8);
			}
			if (operate == '*') {
				result = floatMultiplication(op1, op2, 23, 8);
			}
			if (operate == '/') {
				result = floatDivision(op1, op2, 23, 8);
			}
			result = floatTrueValue(result, 23, 8);

		}
		return result;
	}

	// 2
	public static String integerRepresentation(String number, int length) {
		int p = Integer.parseInt(number);
		char[] words = new char[length];

		for (int i = 1; i <= length; i++) {
			if ((p & (1 << (i - 1))) != 0)
				words[length - i] = '1';
			else
				words[length - i] = '0';
		}
		String numberin2 = String.valueOf(words);

		return numberin2;
	}

	// 3
	public static String floatRepresentation(String number, int sLength,
			int eLength) {

		String numberin2;
		if (number.charAt(0) != '-') {
			numberin2 = floatRepresentationHelp(number, sLength, eLength);
			numberin2 = "0" + numberin2;
		} else {
			number.replaceAll("-", "");
			numberin2 = floatRepresentationHelp(number, sLength, eLength);
			numberin2 = "1" + numberin2;
		}

		return numberin2;
	}

	// mythod for 3;
	public static String floatRepresentationHelp(String number, int sLength,
			int eLength) {
		String numberin2 = null;
		char[] sto2 = null;
		char[] slengthto2 = new char[sLength];
		String elengthto2 = null;

		// String[] part = number.split(".");
		// int p = Integer.parseInt(part[0]);
		// int q = Integer.parseInt(part[1]);
		int i;
		for (i = 0; number.charAt(i) != '.'; i++) {
		}
		String part1 = number.substring(0, i);
		String part2 = number.substring(i + 1, number.length());
		
		System.out.println(part1+"\n"+part2);
		
		Long p = Long.parseLong(part1);
		Long q = Long.parseLong(part2);
		int bige = (1 << eLength) - 1; // 255
		int cpre = (1 << (eLength - 1)) - 1; // 127
		if (p != 0) {
			// to be the biggest number;
//			Long use=(long) (1 << cpre);
//			System.out.println(use);
			
//			if (p >= (1 << cpre)) {
//				elengthto2 = integerRepresentationHelp(bige, eLength);
//				for (i = 0; i < sLength; i++)
//					slengthto2[i] = '0';
//				numberin2 = elengthto2 + String.valueOf(slengthto2);
//
//			}
			// normal number>1
			
				i = 0;
				do {
					i++;
				} while ((p / (1 << i)) != 0);
				i = i - 1;
				int t = i + cpre;
				
				if(t>=(2*cpre+1)){
					elengthto2 = integerRepresentationHelp(bige, eLength);
					for (i = 0; i < sLength; i++)
						slengthto2[i] = '0';
					numberin2 = elengthto2 + String.valueOf(slengthto2);
				}else{

				int j, v;
				elengthto2 = integerRepresentationHelp(t, eLength);
				// System.out.println(elengthto2);;
				int length2 = part2.length();
				j = 0;
				for (; j < i; j++) {
					if ((p & (1 << (i - j - 1))) != 0) {
						slengthto2[j] = '1';
					} else {
						slengthto2[j] = '0';
					}
				}
				double snum;
				snum = (double) q;
				for (j = 0; j < length2; j++) {
					snum = snum / 10;
				}
				do {
					snum = snum * 2;
					if (snum >= 1) {
						snum = snum - 1;
						slengthto2[i] = '1';
					} else {
						slengthto2[i] = '0';
					}
					i++;
				} while (i < sLength);

				numberin2 = elengthto2 + String.valueOf(slengthto2);
				}
		}
		// =0;
		else {
			double snum;
			int length2 = part2.length();
			snum = (double) q;
			for (i = 0; i < length2; i++) {
				snum = snum / 10;
			}
			// 0
			if (snum < (1 >> (cpre - 1 + sLength))) {
				elengthto2 = integerRepresentationHelp(0, eLength);
				for (i = 0; i < sLength; i++)
					slengthto2[i] = '0';
				numberin2 = elengthto2 + String.valueOf(slengthto2);
			}
			// not regular
			else if ((snum > (1 >> (cpre - 1 + sLength)))
					&& (snum < (1 >> (cpre - 1)))) {
				int t = 0;
				elengthto2 = integerRepresentationHelp(0, eLength);
				snum = snum * (1 << cpre);
				i = 0;
				do {
					snum = snum * 2;
					if (snum >= 1) {
						snum = snum - 1;
						slengthto2[i] = '1';
					} else {
						slengthto2[i] = '0';
					}
					i++;
				} while (i < sLength);
				numberin2 = elengthto2 + String.valueOf(slengthto2);
			}
			// regular but <1;
			else {
				i = 0;
				do {
					snum = snum * 2;
					i++;
				} while (snum < 1);
				int t = cpre - i;
				int v;
				elengthto2 = integerRepresentationHelp(t, eLength);
				snum = snum - 1;
				i = 0;
				do {
					snum = snum * 2;
					if (snum >= 1) {
						snum = snum - 1;
						slengthto2[i] = '1';
					} else {
						slengthto2[i] = '0';
					}
					i++;
				} while (i < sLength);

				numberin2 = elengthto2 + String.valueOf(slengthto2);
			}

		}
		return numberin2;
	}

	public static String integerRepresentationHelp(int number, int length) {
		// just for mythod 3
		char[] temp = new char[length];
		int i;
		for (i = length - 1; i >= 0; i--) {
			if ((number & (1 << i)) != 0) {
				temp[length - i - 1] = '1';
			} else
				temp[length - i - 1] = '0';
		}
		String result = String.valueOf(temp);
		return result;
	}

	// 4
	public static String ieee754(String number, int length) {
		if (length == 32) {
			return (floatRepresentation(number, 23, 8));
		} else
			return (floatRepresentation(number, 52, 11));
	}

	// 5
	public static String integerTrueValue(String operand) {
		int number = 0;
		int t = operand.length();
		int i = 0;

		if (operand.charAt(0) == '0')
			for (; i < t; i++) {
				if (operand.charAt(i) == '1')
					number = number + (1 << (t - i - 1));
			}
		else {
			for (; i < t; i++) {
				if (operand.charAt(i) == '0')
					number = number + (1 << (t - i - 1));
			}
			number = 0 - number - 1;
		}
		String result = "" + number;
		return result;
	}

	// 6
	public static String floatTrueValue(String operand, int sLength, int eLength) {
		char o = operand.charAt(0);
		String enumber = operand.substring(1, eLength + 1);
		String snumber = operand.substring(eLength + 1, eLength + sLength + 1);
		String result = null;
		Long t = (long) 0;
		double v = 0.0;
		int i = 0;
		int j;

		for (; i < eLength; i++) {
			if (enumber.charAt(i) == '1')
				t = t + (1 << (eLength - i - 1));
		}
		// System.out.println(t);
		for (i = 1; i <= sLength; i++) {
			if ((snumber.charAt(i - 1)) == '1') {
				double k = 1.0;
				for (j = 0; j < i; j++) {
					k = k / 2;
				}
				System.out.println(k);
				v = v + k;
				System.out.println(v);
			}
		}
		if ((t == 0) && (v == 0))
			result = "0";

		if ((t == ((1 << eLength) - 1)) && (v != 0))
			result = "NaN";

		if ((t == ((1 << eLength) - 1)) && (v == 0)) {
			if (o == '0')
				result = "+Inf";
			else
				result = "-Inf";
		}

		if ((t == 0) && (v != 0)) {
			t = (long) ((1 << (eLength - 1)) - 2);
			double e = 1.0;
			for (i = 0; i < t; i++) {
				e = e / 2;
			}
			v = e * v;
			result = Double.toString(v);
			if (o == '1')
				result = "-" + result;
		}

		if ((t > 0) && (t < (1 << eLength - 1))) {
			t = t - (1 << (eLength - 1) - 1);
			double e = 0;
			if (t >= 0) {
				e = 1.0;
				for (i = 0; i < t; i++) {
					e = e * 2;
				}
			}
			if (t < 0) {
				e = 1.0;
				for (i = 0; i < t; i++) {
					e = e / 2;
				}
			}
			v = e * (1 + v);
			result = Double.toString(v);
			if (o == '1')
				result = "-" + result;
		}
		return result;
	}

	// 7
	public static String negation(String operand) {
		char[] number = operand.toCharArray();
		int length = operand.length();
		int i = 0;
		for (; i < length; i++) {
			if (number[i] == '1')
				number[i] = '0';
			else
				number[i] = '1';
		}
		String result = String.valueOf(number);
		return result;
	}

	// 8
	public static String leftShift(String operand, int n) {
		char[] number = operand.toCharArray();
		int length = operand.length();
		char[] newnumber = new char[length];
		int i = 0;

		for (; i < length; i++) {
			newnumber[i] = '0';
		}
		for (i = 0; i < length - n; i++) {
			newnumber[i] = number[i + n];
		}

		String result = String.valueOf(newnumber);
		return result;
	}

	// 9
	public static String rightAriShift(String operand, int n) {
		char o = operand.charAt(0);
		char[] number = operand.toCharArray();
		int length = operand.length();
		char[] newnumber = new char[length];
		int i;
		if (o == '0') {
			for (i = 0; i < length; i++)
				newnumber[i] = '0';
			for (i = 0; i < length - n; i++)
				newnumber[i + n] = number[i];
		} else {
			for (i = 0; i < length; i++)
				newnumber[i] = '1';
			for (i = 0; i < length - n; i++)
				newnumber[i + n] = number[i];
		}

		String result = String.valueOf(newnumber);
		return result;
	}

	// 10
	public static String rightLogShift(String operand, int n) {
		char[] number = operand.toCharArray();
		int length = operand.length();
		char[] newnumber = new char[length];
		int i;
		for (i = 0; i < length; i++) {
			newnumber[i] = '0';
		}
		for (i = 0; i < length - n; i++) {
			newnumber[i + n] = number[i];
		}

		String result = String.valueOf(newnumber);
		return result;
	}

	// 11
	public static String fullAdder(char x, char y, char c) {
		String result = null;
		String S;
		String C;
		if (((x == '1') && (y == '1')) || ((c == '1') && (y == '1'))
				|| ((x == '1') && (c == '1')))
			C = "1";
		else
			C = "0";
		if (((x == '1') && (y == '1') && (c == '1'))
				|| ((x == '1') && (y == '0') && (c == '0'))
				|| ((x == '0') && (y == '1') && (c == '0'))
				|| ((x == '0') && (y == '0') && (c == '1')))
			S = "1";
		else
			S = "0";
		result = S + C;
		return result;
	}

	// 12
	public static String claAdder(String operand1, String operand2, char c) {
		String result = null;
		char[] x = new char[8];
		char[] y = new char[8];
		int length1 = operand1.length();
		int length2 = operand2.length();
		int i = 0;
		if (operand1.charAt(0) == '1') {
			for (; i < 8; i++) {
				x[i] = '1';
			}
		} else {
			for (i = 0; i < 8; i++) {
				x[i] = '0';
			}
		}
		for (i = 0; i < length1; i++) {
			x[i + 8 - length1] = operand1.charAt(i);
		}
		if (operand2.charAt(0) == '1') {
			for (i = 0; i < 8; i++)
				y[i] = '1';
		} else {
			for (i = 0; i < 8; i++) {
				y[i] = '0';
			}
		}
		for (i = 0; i < length2; i++) {
			y[i + 8 - length2] = operand2.charAt(i);
		}

		i = 7;
		String[] temp = new String[8];
		char[] s = new char[8];
		for (; i > -1; i--) {
			temp[i] = fullAdder(x[i], y[i], c);
			c = temp[i].charAt(1);
			s[i] = temp[i].charAt(0);
		}
		result = String.valueOf(s) + String.valueOf(c);
		return result;
	}

	// 13
	public static String integerAddition(String operand1, String operand2,
			char c, int length) {
		int count = length / 8;
		if ((length % 8) != 0) {
			count++;
		}
		int length1 = operand1.length();
		int length2 = operand2.length();
		String[] o1 = new String[count];
		String[] o2 = new String[count];
		char[] x = new char[8 * count];
		char[] y = new char[8 * count];
		String[] S = new String[count];
		int i = 0;
		if (operand1.charAt(0) == '1') {
			for (; i < (8 * count); i++) {
				x[i] = '1';
			}
		} else {
			for (i = 0; i < (8 * count); i++) {
				x[i] = '0';
			}
		}
		for (i = 0; i < length1; i++) {
			x[i + 8 * count - length1] = operand1.charAt(i);
		}

		if (operand2.charAt(0) == '1') {
			for (i = 0; i < (8 * count); i++)
				y[i] = '1';
		} else {
			for (i = 0; i < (8 * count); i++) {
				y[i] = '0';
			}
		}
		for (i = 0; i < length2; i++) {
			y[i + 8 * count - length2] = operand2.charAt(i);
		}

		String t;
		for (i = 0; i < count; i++) {
			o1[i] = "";
			o2[i] = "";
			int j = 0;
			for (; j < 8; j++) {
				t = "" + x[8 * i + j];
				o1[i] = o1[i] + t;
				t = "" + y[8 * i + j];
				o2[i] = o2[i] + t;
			}
		}

		for (i = count - 1; i >= 0; i--) {
			S[i] = claAdder(o1[i], o2[i], c);
			c = S[i].charAt(8);
		}
		String result = "";
		int j;
		String k = "";
		for (i = 0; i < count; i++) {
			for (j = 0; j < 8; j++) {
				result = result + S[i].charAt(j);
			}
		}
		if ((x[0] == y[0]) && (x[0] != result.charAt(0))) {
			result = result + '1';
		} else
			result = result + '0';
		result = result.substring(8 * count - length, 8 * count + 1);
		return result;
	}

	// 14
	public static String integerSubtraction(String operand1, String operand2,
			int length) {
		operand2 = negation(operand2);
		String result = integerAddition(operand1, operand2, '1', length);
		return result;
	}

	// 15
	public static String integerMultiplication(String operand1,
			String operand2, int length) {

		char[] temp = new char[2 * length];
		int length1 = operand1.length();
		int length2 = operand2.length();
		char[] x = new char[length];
		char[] y = new char[length + 1]; // y0=0;
		// String[] S = new String[count];
		int i = 0;
		if (operand1.charAt(0) == '1') {
			for (; i < (length); i++) {
				x[i] = '1';
			}
		} else {
			for (i = 0; i < (length); i++) {
				x[i] = '0';
			}
		}
		for (i = 0; i < length1; i++) {
			x[i + length - length1] = operand1.charAt(i);
		}

		if (operand2.charAt(0) == '1') {
			for (i = 0; i < (length); i++)
				y[i] = '1';
		} else {
			for (i = 0; i < (length); i++) {
				y[i] = '0';
			}
		}
		// System.out.println(String.valueOf(y));

		for (i = 0; i < length2; i++) {
			y[i + length - length2] = operand2.charAt(i);
		}
		y[length] = '0';

		// System.out.println(String.valueOf(y));

		for (i = 0; i < 2 * length; i++) {
			temp[i] = '0';
		}

		String S = null;
		for (i = 0; i < length; i++) {
			String assis1 = "";
			String assis2 = "";
			int j;
			for (j = 0; j < length; j++) {
				assis1 = assis1 + temp[j];
				assis2 = assis2 + x[j];
			}

			// System.out.println(assis1+"\n"+assis2);

			if (y[length - i] > y[length - i - 1]) {
				S = integerAddition(assis1, assis2, '0', length);
				for (int v = 0; v < length; v++) {
					temp[v] = S.charAt(v);
				}

				//
				// System.out.println("ADD!!!!!!!!!!!!!!");
				// System.out.println(S);
				// System.out.println(String.valueOf(temp));

				// System.out.println(String.valueOf(temp));
			} else if (y[length - i] < y[length - i - 1]) {
				S = integerSubtraction(assis1, assis2, length);
				for (int v = 0; v < length; v++) {
					temp[v] = S.charAt(v);

				}

				// System.out.println("SUB!!!!!!!!!!!!!!!!!!!!!!!!!!");
				// System.out.println(S);
				// System.out.println(String.valueOf(temp));
			} else {
				// System.out.println("The temp I want:"+String.valueOf(temp));
			}

			int k;
			for (k = 2 * length - 2; k >= 0; k--) {

				temp[k + 1] = temp[k];
			}
			// System.out.println("aAAAAAA");
			// System.out.println(String.valueOf(temp));

		}

		String result = String.valueOf(temp);
		return result;
	}

	// 16
	public static String integerDivision(String operand1, String operand2,
			int length) {
		char[] temp = new char[2 * length];
		int length1 = operand1.length();
		int length2 = operand2.length();
		char[] y = new char[length];
		// String[] S = new String[count];
		int i = 0;
		if (operand1.charAt(0) == '1') {
			for (; i < (2 * length); i++) {
				temp[i] = '1';
			}
		} else {
			for (i = 0; i < (2 * length); i++) {
				temp[i] = '0';
			}
		}
		for (i = 0; i < length1; i++) {
			temp[i + length * 2 - length1] = operand1.charAt(i);
		}

		if (operand2.charAt(0) == '1') {
			for (i = 0; i < length; i++)
				y[i] = '1';
		} else {
			for (i = 0; i < length; i++) {
				y[i] = '0';
			}
		}
		for (i = 0; i < length2; i++) {
			y[i + length - length2] = operand2.charAt(i);
		}

		String S = null;
		for (int count = 0; count < length; count++) {

			for (int j = 0; j < 2 * length - 1; j++) {
				temp[j] = temp[j + 1];
			}

			String assis1 = "";
			String assis2 = "";
			for (int j = 0; j < length; j++) {
				assis1 = assis1 + temp[j];
				assis2 = assis2 + y[j];
			}

			if (operand1.charAt(0) != operand2.charAt(0)) {
				S = integerAddition(assis1, assis2, '0', length);
				if (S.charAt(0) == assis1.charAt(0)) {
					temp[2 * length - 1] = '1';
					for (i = 0; i < length; i++) {
						temp[i] = S.charAt(i);
					}
				} else {
					temp[2 * length - 1] = '0';
				}

				//System.out.println(String.valueOf(temp));
			} else {
				S = integerSubtraction(assis1, assis2, length);
				if (S.charAt(0) == assis1.charAt(0)) {
					temp[2 * length - 1] = '1';
					for (i = 0; i < length; i++) {
						temp[i] = S.charAt(i);
					}
				} else {
					temp[2 * length - 1] = '0';
				}
			}
		}
		String result = String.valueOf(temp);
		String r1 = result.substring(0, length);
		String r2 = result.substring(length, length * 2);

		if (operand1.charAt(0) != operand2.charAt(0)) {
			r2 = negation(r2);
			r2 = integerAddition(r2, "0", '1', length);
			r2 = r2.substring(1, length);
		}
		result = r2 + r1;

		return result;
	}

	// 17 不考虑非规格化浮点数；
	public static String floatAddition(String operand1, String operand2,
			int sLength, int eLength, int gLength) {
		String result = null;
		int cpre = (1 << (eLength - 1)) - 1;
		int e1 = 0;
		int e2 = 0;
		int s1 = 0;
		int s2 = 0;
		// if x==0 or y==0;
		String o1 = floatTrueValue(operand1, sLength, eLength);
		String o2 = floatTrueValue(operand1, sLength, eLength);
		
		System.out.println(o1+"\n"+o2);
		
		if (o1 == "0") {
			result = operand2;
		} else if (o2 == "0") {
			result = operand1;
		} else {
			String efor1 = operand1.substring(1, eLength + 1);
			String efor2 = operand2.substring(1, eLength + 1);
			String sfor1 = operand1.substring(eLength + 1, eLength + sLength
					+ 1);
			String sfor2 = operand2.substring(eLength + 1, eLength + sLength
					+ 1);
			int i = 0;
			for (; i < eLength; i++) {
				if (efor1.charAt(eLength - i - 1) == '1') {
					e1 = e1 + (1 << i);
				}
				if (efor2.charAt(eLength - i - 1) == '1') {
					e2 = e2 + (1 << i);
				}
			}
			for (i = 0; i < eLength; i++) {
				if (sfor1.charAt(sLength - i - 1) == '1') {
					s1 = s1 + (1 << i);
				}
				if (sfor2.charAt(sLength - i - 1) == '1') {
					s2 = s2 + (1 << i);
				}
			}
			if ((e1 == e2) && (e1 == 0)) {

			}
			// e1 = e1 - cpre;
			// e2 = e2 - cpre;
			//
			// System.out.println(e1+"  "+e2);

			// let operand1>=operand2
			if ((e1 < e2) || ((e1 == e2) && (s1 < s2))) {
				int trans;
				String Trans;
				trans = e1;
				e1 = e2;
				e2 = trans;
				Trans = operand1;
				operand1 = operand2;
				operand2 = Trans;
				Trans = sfor1;
				sfor1 = sfor2;
				sfor2 = Trans;
			}

			// System.out.println(operand1+"aaaa");
			// System.out.println(operand2+"hhhh");

			String ox = null;
			String oy = null;
			sfor2 = sfor2 + integerRepresentationHelp(0, gLength);
			if (operand1.charAt(0) == operand2.charAt(0)) {
				ox = "01" + sfor1 + integerRepresentationHelp(0, gLength);
				if ((e1 - e2) >= (1 + sLength + gLength)) {
					oy = integerRepresentationHelp(0, 2 + sLength + gLength);
				} else if (((e1 - e2) == (sLength + gLength))) {
					oy = integerRepresentationHelp(0, sLength + gLength) + "01";
				} else {
					oy = integerRepresentationHelp(0, e1 - e2) + "01"
							+ sfor2.substring(0, gLength + sLength - (e1 - e2));
				}

				// System.out.println(ox);
				// System.out.println(oy);

				String tempre = integerAddition(ox, oy, '0', 2 + sLength
						+ gLength);
				// the biggest number;
				if (tempre.charAt(0) == '1') {
					if ((e1 + 1) == (2 * cpre + 1)) {
						result = ""
								+ operand1.charAt(0)
								+ integerRepresentationHelp(2 * cpre + 1,
										eLength)
								+ integerRepresentationHelp(0, sLength) + '1';
					} else {
						result = "" + operand1.charAt(0)
								+ integerRepresentationHelp(e1 + 1, eLength)
								+ tempre.substring(1, sLength + 1) + '0';
					}
				} else {
					result = "" + operand1.charAt(0)
							+ integerRepresentationHelp(e1, eLength)
							+ tempre.substring(2, sLength + 2) + '0';
				}
			}

			else {
				ox = "01" + sfor1 + integerRepresentationHelp(0, gLength);
				if ((e1 - e2) >= (1 + sLength + gLength)) {
					oy = integerRepresentationHelp(0, 2 + sLength + gLength);
				} else if (((e1 - e2) == (sLength + gLength))) {
					oy = integerRepresentationHelp(0, sLength + gLength) + "01";
				} else {
					oy = integerRepresentationHelp(0, e1 - e2) + "01"
							+ sfor2.substring(0, gLength + sLength - (e1 - e2));
				}
				// System.out.println(ox);
				// System.out.println(oy);
				oy = negation(oy);
				String tempre = integerAddition(ox, oy, '1', 2 + sLength
						+ gLength);

				// System.out.println(tempre);

				for (i = 0; tempre.charAt(i) != '1'; i++) {
				}
				if (i == 1) {
					result = "" + operand1.charAt(0)
							+ integerRepresentationHelp(e1, eLength)
							+ tempre.substring(2, sLength + 2) + '0';
				} else {
					if ((e1 - (i - 1)) == 0) {
						result = "" + operand1.charAt(0)
								+ integerRepresentationHelp(0, eLength)
								+ integerRepresentationHelp(0, sLength) + '1';
					} else if ((1 + gLength - i) >= 0) {
						result = ""
								+ operand1.charAt(0)
								+ integerRepresentationHelp(e1 - (i - 1),
										eLength)
								+ tempre.substring(i + 1, i + 1 + sLength)
								+ '0';
					} else {
						result = ""
								+ operand1.charAt(0)
								+ integerRepresentationHelp(e1 - (i - 1),
										eLength)
								+ tempre.substring(i + 1, 2 + sLength + gLength)
								+ integerRepresentationHelp(0, i - gLength - 1)
								+ '0';
					}
				}

			}
		}
		return result;
	}

	// 18
	public static String floatSubtraction(String operand1, String operand2,
			int sLength, int eLength, int gLength) {
		char change = operand2.charAt(0);
		operand2 = operand2.substring(1);
		if (change == '1') {
			operand2 = "0" + operand2;
		} else {
			operand2 = "1" + operand2;
		}
		String result = floatAddition(operand1, operand2, sLength, eLength,
				gLength);
		return result;
	}

	// 19
	public static String floatMultiplication(String operand1, String operand2,
			int sLength, int eLength) {
		String result;
		String efor1 = operand1.substring(1, eLength + 1);
		String efor2 = operand2.substring(1, eLength + 1);
		String sfor1 = operand1.substring(eLength + 1, eLength + sLength + 1);
		String sfor2 = operand2.substring(eLength + 1, eLength + sLength + 1);
		int cpre = (1 << (eLength - 1)) - 1;
		int e1 = 0;
		int e2 = 0;
		int e;
		int i = 0;
		// if x==0 or y==0;
		String o1 = floatTrueValue(operand1, sLength, eLength);
		String o2 = floatTrueValue(operand1, sLength, eLength);
		if ((o1 == "0") || (o2 == "0")) {
			result = integerRepresentationHelp(0, eLength + sLength);
		} else {
			for (; i < eLength; i++) {
				if (efor1.charAt(eLength - i - 1) == '1') {
					e1 = e1 + (1 << i);
				}
				if (efor2.charAt(eLength - i - 1) == '1') {
					e2 = e2 + (1 << i);
				}
			}
			e = e1 + e2 - cpre;

			System.out.println(e);
			// too small to impress;
			if (e <= 0) {
				result = integerRepresentationHelp(0, eLength + sLength);
			}
			// too big to impress
			else if (e >= (cpre * 2 + 1)) {
				result = integerRepresentationHelp((cpre * 2 + 1), eLength)
						+ integerRepresentationHelp(0, sLength);
			} else {
				String x = "01";
				String y = "01";
				for (i = 1; i <= sLength; i++) {
					x = x + operand1.charAt(i + eLength);
					y = y + operand2.charAt(i + eLength);

				}
				String temp = integerMultiplication(x, y, (sLength + 2));
				result = integerRepresentationHelp(e, eLength)
						+ temp.substring(4, 4 + sLength);
			}
		}
		if (operand1.charAt(0) == operand2.charAt(0)) {
			result = "0" + result;
		} else {
			result = "1" + result;
		}
		return result;
	}

	// 20
	public static String floatDivision(String operand1, String operand2,
			int sLength, int eLength) {
		String result;
		String efor1 = operand1.substring(1, eLength + 1);
		String efor2 = operand2.substring(1, eLength + 1);
		String sfor1 = operand1.substring(eLength + 1, eLength + sLength + 1);
		String sfor2 = operand2.substring(eLength + 1, eLength + sLength + 1);
		int cpre = (1 << (eLength - 1)) - 1;
		int e1 = 0;
		int e2 = 0;
		int e;
		int i = 0;
		// if x==0 or y==0;
		String o1 = floatTrueValue(operand1, sLength, eLength);
		String o2 = floatTrueValue(operand1, sLength, eLength);
		if ((o1 == "0") || (o2 == "0")) {
			if ((o1 == "0") && (o2 == "0")) {
				result = "NaN";
			} else if ((o1 == "0") && (o2 != "0")) {
				result = integerRepresentationHelp(0, eLength + sLength);
			} else {
				result = integerRepresentationHelp((cpre * 2 + 1), eLength)
						+ integerRepresentationHelp(0, sLength);
			}
		} else {
			for (; i < eLength; i++) {
				if (efor1.charAt(eLength - i - 1) == '1') {
					e1 = e1 + (1 << i);
				}
				if (efor2.charAt(eLength - i - 1) == '1') {
					e2 = e2 + (1 << i);
				}
			}
			e = e1 - e2 + cpre;
			// too small to impress;
			if (e <= 0) {
				result = integerRepresentationHelp(0, eLength + sLength);
			}
			// too big to impress
			else if (e >= (cpre * 2 + 1)) {
				result = integerRepresentationHelp((cpre * 2 + 1), eLength)
						+ integerRepresentationHelp(0, sLength);
			} else {
				String x = "0001";
				String y = "01";
				for (i = 1; i <= sLength; i++) {
					x = x + operand1.charAt(i + eLength);
					y = y + operand2.charAt(i + eLength);

				}
				x = x + integerRepresentationHelp(0, sLength);

				System.out.println(x);
				System.out.println(y);

				String temp = integerDivision(x, y, (2 * sLength + 4));

				System.out.println(temp);

				result = integerRepresentationHelp(e, eLength)
						+ temp.substring(4 + sLength + 1, 4 + 2 * sLength + 1);

				System.out.println(temp.substring(4 + sLength + 1,
						4 + 2 * sLength + 1));
			}
		}
		if (operand1.charAt(0) == operand2.charAt(0)) {
			result = "0" + result;
		} else {
			result = "1" + result;
		}
		return result;
	}
}
