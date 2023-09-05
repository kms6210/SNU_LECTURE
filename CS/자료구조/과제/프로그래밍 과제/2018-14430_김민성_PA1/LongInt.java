public class LongInt {
    public boolean sign = true;
    public int len;
    public String str;
    public char[] char_arr;

    // constructor
    public LongInt(String s) {
        if (s.charAt(0) == '-') {
            this.sign = false;
            s = s.substring(1);
        }

        this.len = s.length();
        this.str = s;
        this.char_arr = new char[s.length()];

        for (int i = 0; i < s.length(); i++) {
            this.char_arr[i] = s.charAt(i);
        }
    }

    public String adding(String str1, String str2) {
        String result = "";
        StringBuffer sb = new StringBuffer();
        int value;
        int next = 0;
        int len_diff = Math.abs(str1.length() - str2.length());

        if (str1.length() >= str2.length()) {
            for (int i = str1.length() - 1; i >= 0; i--) {
                if (i >= len_diff) {
                    value = (str1.charAt(i) - '0') + (str2.charAt(i - len_diff) - '0') + next;
                } else {
                    value = (str1.charAt(i) - '0') + next;
                }

                if (value >= 10) {
                    value = Math.floorMod(value, 10);
                    next = 1;
                } else {
                    next = 0;
                }

                String val = Integer.toString(value);
                sb.append(val);
            }
        } else {
            for (int i = str2.length() - 1; i >= 0; i--) {
                if (i >= len_diff) {
                    value = (str1.charAt(i - len_diff) - '0') + (str2.charAt(i) - '0') + next;
                } else {
                    value = (str2.charAt(i) - '0') + next;
                }

                if (value >= 10) {
                    value = Math.floorMod(value, 10);
                    next = 1;
                } else {
                    next = 0;
                }

                String val = Integer.toString(value);
                sb.append(val);
            }
        }

        result = sb.reverse().toString();
        if (next == 1) {
            result = '1' + result;
        }
        result = result.replaceFirst("^0+(?!$)", "");
        return result;
    }

    public String subtracting(String str1, String str2) {
        String result = "";
        StringBuffer sb = new StringBuffer();
        int value;
        int next = 0;
        int num = 0;
        int len_diff = Math.abs(str1.length() - str2.length());

        if (str1.length() == str2.length()) {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) - '0' > str2.charAt(i) - '0') {
                    num = 1;
                    break;
                } else if (str1.charAt(i) - '0' < str2.charAt(i) - '0') {
                    num = 2;
                    break;
                }
                if ((i == str1.length() - 1) && (str1.charAt(i) - '0' == str2.charAt(i) - '0')) {
                    num = 3;
                }
            }
        }

        if (str1.length() > str2.length() || num == 1) {
            for (int i = str1.length() - 1; i >= 0; i--) {
                if (i >= len_diff) {
                    value = (str1.charAt(i) - '0') - (str2.charAt(i - len_diff) - '0') - next;
                } else {
                    value = (str1.charAt(i) - '0') - next;
                }

                if (value < 0) {
                    value += 10;
                    next = 1;
                } else {
                    next = 0;
                }

                String val = Integer.toString(value);
                sb.append(val);
            }
        } else if (str1.length() < str2.length() || num == 2) {
            for (int i = str2.length() - 1; i >= 0; i--) {
                if (i >= len_diff) {
                    value = (str2.charAt(i) - '0') - (str1.charAt(i - len_diff) - '0') - next;
                } else {
                    value = (str2.charAt(i) - '0') - next;
                }

                if (value < 0) {
                    value += 10;
                    next = 1;
                } else {
                    next = 0;
                }

                String val = Integer.toString(value);
                sb.append(val);
            }
        } else {
            sb.append("0");
        }

        result = sb.reverse().toString();
        result = result.replaceFirst("^0+(?!$)", "");
        return result;
    }

    public String multiple_adding(String s, char ch) {
        String result = "0";

        for (int i = 0; i < ch - '0'; i++) {
            result = adding(result, s);
        }
        return result;
    }

    public String multiplying(String str1, String str2) {
        String result = "0";
        StringBuffer sb = new StringBuffer(str1);
        String temp;

        for (int i = str2.length() - 1; i >= 0; i--) {
            temp = multiple_adding(sb.toString() , str2.charAt(i));
            result = adding(temp, result);
            sb.append("0");
        }

        result = result.replaceFirst("^0+(?!$)", "");
        return result;
    }

    // returns 'this' + 'opnd'; Both inputs remain intact
    public LongInt add(LongInt opnd) {
        boolean ans_sign = true;
        String result;
        int num = 0;

        boolean a = this.sign;
        boolean b = opnd.sign;

        if (this.len == opnd.len) {
            for (int i = 0; i < this.len; i++) {
                if (this.char_arr[i] - '0' > opnd.char_arr[i] - '0') {
                    num = 1;
                    break;
                } else if (this.char_arr[i] - '0' < opnd.char_arr[i] - '0') {
                    num = 2;
                    break;
                }
            }
        }

        if (a && b) {
            result = adding(this.str, opnd.str);
        } else if (a && !b) {
            result = subtracting(this.str, opnd.str);
            if (this.len < opnd.len || num == 2) {
                ans_sign = false;
            }
        } else if (!a && b) {
            result = subtracting(this.str, opnd.str);
            if (this.len > opnd.len || num == 1) {
                ans_sign = false;
            }
        } else {
            result = adding(this.str, opnd.str);
            ans_sign = false;
        }

        LongInt ans = new LongInt(result);
        ans.sign = ans_sign;
        return ans;
    }

    // returns 'this' - 'opnd'; Both inputs remain intact.
    public LongInt subtract(LongInt opnd) {
        boolean ans_sign = true;
        String result;
        int num = 0;

        boolean a = this.sign;
        boolean b = opnd.sign;

        if (this.len == opnd.len) {
            for (int i = 0; i < this.len; i++) {
                if (this.char_arr[i] - '0' > opnd.char_arr[i] - '0') {
                    num = 1;
                    break;
                } else if (this.char_arr[i] - '0' < opnd.char_arr[i] - '0') {
                    num = 2;
                    break;
                }
            }
        }

        if (a && b) {
            result = subtracting(this.str, opnd.str);
            if (this.len < opnd.len || num == 2) {
                ans_sign = false;
            }
        } else if (a && !b) {
            result = adding(this.str, opnd.str);
        } else if (!a && b) {
            result = adding(this.str, opnd.str);
            ans_sign = false;
        } else {
            result = subtracting(this.str, opnd.str);
            if (this.len > opnd.len || num == 1) {
                ans_sign = false;
            }
        }

        LongInt ans = new LongInt(result);
        ans.sign = ans_sign;
        return ans;
    }

    // returns 'this' * 'opnd'; Both inputs remain intact.
    public LongInt multiply(LongInt opnd) {
        boolean ans_sign = true;
        String result;

        boolean a = this.sign;
        boolean b = opnd.sign;

        if ((a && !b) || (!a && b) ) {
            ans_sign = false;
        }

        result = multiplying(this.str, opnd.str);
        LongInt ans = new LongInt(result);
        ans.sign = ans_sign;
        return ans;
    }

    // print the value of 'this' element to the standard output.
    public void print() {
        String sign_str = (this.sign) ? "" : "-";
        System.out.print(sign_str);
        for (int i = 0; i < this.len; i++)
            System.out.print(this.char_arr[i]);
    }

}