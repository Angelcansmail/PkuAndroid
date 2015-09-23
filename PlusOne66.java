import java.util.Scanner;

public class Solution {
    public int[] plusOne(int[] digits) {
        int flag = 1;
        int i = 0;
        for(i = digits.length - 1;i >= 0;i--)
       {
           digits[i] = digits[i] + flag;
            if(digits[i] > 9)
            {
                digits[i] = 0;
            }else
                return digits;
       }
       int[] digits2 = new int[digits.length + 1];
       if(i == -1 && flag == 1)
        {
            digits2[0] = 1;
            for(i = 1;i < digits2.length;i++)
                digits2[i] = digits[i - 1];
        }
        return digits2;
    }
    public static void main(String[] args) {
	String num = in.nextLine();
	int arr[] = new int[num.length()];
	for (int i = arr.length - 1; i >= 0; i--) {
		arr[i] = num.charAt(i) - 48;
	}
	arr = plusOne(arr);
	for (int i = 0; i < arr.length; i++) {
		System.out.print(arr[i]);
	}
   }
}