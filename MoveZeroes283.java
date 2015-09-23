public class Solution {
    public void moveZeroes(int[] nums) {
        int i = 0;
        int flag = 0;
        while(flag < nums.length){
            if(nums[i] == 0)
             {
                 for(int j = i+1;j<nums.length;j++)
                    nums[j-1] = nums[j];
                 nums[nums.length - 1] = 0;
             }else 
                    i++;
            flag++;
         }
    }
}