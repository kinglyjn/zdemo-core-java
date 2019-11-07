package test13_json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Test02 {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/zhangqingli/Desktop/rc_channel.bhhj.20181210.txt"))));
		String line = null;
		
		Map<String, String> deviceAndChannelMap = new HashMap<String,String>();
		// 放款量(新) = 新激活放款量（新） + 激活延时放款量（新）
		// 放款量 = 放款量（新） + 放款量（老）
		// 放款金额（总） = 放款金额（新） + 放款金额（老）
		Integer totalNewLoan = 0;
		Integer totalNewDelay = 0;
		Integer totalOldLoan = 0;
		Double totalNewLoanMoney = 0.0;
		Double totalOldLoanMoney = 0.0;
		
		String targetDeviceAndChannelKey = "ANDROID:BHHJ_MF_samsung";
		Double IOS_IOS_BHHJ_BD_bdqudao23_newLoanMoney = 0.0;
		Double IOS_IOS_BHHJ_BD_bdqudao23_oldLoanMoney = 0.0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_newOrder = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_oldOrder = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_newLoan = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_oldLoan = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_newApply = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_oldApply = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_newDelay = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_oldApplyRegister = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_register = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_activation = 0;
		Integer IOS_IOS_BHHJ_BD_bdqudao23_dau = 0;
		
		while ( (line=br.readLine()) != null ) {
			JSONObject root = JSON.parseObject(line);
			JSONObject t = root.getJSONObject("t");
			
			String device = t.getString("device");
			String channel = t.getString("channel");
			String deviceAndChannelKey = device + ":" + channel;
			deviceAndChannelMap.put(deviceAndChannelKey, null);
			
			Integer isNewLoan = t.getInteger("isNewLoan");
			Integer isNewDelay = t.getInteger("isNewDelay");
			Integer isOldLoan = t.getInteger("isOldLoan");
			totalNewLoan += isNewLoan;
			totalNewDelay += isNewDelay;
			totalOldLoan += isOldLoan;
			
			Double newLoanMoney = t.getDouble("newLoanMoney");
			Double oldLoanMoney = t.getDouble("oldLoanMoney");
			totalNewLoanMoney += newLoanMoney;
			totalOldLoanMoney += oldLoanMoney;
			
			
			if (targetDeviceAndChannelKey.equals(deviceAndChannelKey)) {
				IOS_IOS_BHHJ_BD_bdqudao23_newLoanMoney += newLoanMoney;
				IOS_IOS_BHHJ_BD_bdqudao23_oldLoanMoney += oldLoanMoney;
				IOS_IOS_BHHJ_BD_bdqudao23_newOrder += t.getInteger("isNewOrder");
				IOS_IOS_BHHJ_BD_bdqudao23_oldOrder += t.getInteger("isOldOrder");
				IOS_IOS_BHHJ_BD_bdqudao23_newLoan += isNewLoan;
				IOS_IOS_BHHJ_BD_bdqudao23_oldLoan += isOldLoan;
				IOS_IOS_BHHJ_BD_bdqudao23_newApply += t.getInteger("isNewApply");
				IOS_IOS_BHHJ_BD_bdqudao23_oldApply += t.getInteger("isOldApply");
				IOS_IOS_BHHJ_BD_bdqudao23_newDelay += t.getInteger("isNewDelay");
				IOS_IOS_BHHJ_BD_bdqudao23_oldApplyRegister += t.getInteger("isOldApplyRegister");
				IOS_IOS_BHHJ_BD_bdqudao23_register += t.getInteger("isRegister");
				IOS_IOS_BHHJ_BD_bdqudao23_activation += t.getInteger("isActivation");
				IOS_IOS_BHHJ_BD_bdqudao23_dau += t.getInteger("isDau");
			}
		}
		br.close();
		
		System.out.println("总渠道量：" + deviceAndChannelMap.size());
		System.out.println("新放款量：" + (totalNewLoan+totalNewDelay) 
				+ ", 老放款量：" + totalOldLoan 
				+ ", 总放款量：" + (totalNewLoan+totalNewDelay+totalOldLoan));
		System.out.println("新放款金额：" + totalNewLoanMoney 
				+ ", 老放款金额：" + totalOldLoanMoney
				+ ", 总放款金额：" + (totalNewLoanMoney+totalOldLoanMoney) + "\n");
		
		System.out.println("=======" + targetDeviceAndChannelKey + "=======");
		System.out.println("x:newLoanMoney=" + IOS_IOS_BHHJ_BD_bdqudao23_newLoanMoney);
		System.out.println("x:oldLoanMoney=" + IOS_IOS_BHHJ_BD_bdqudao23_oldLoanMoney);
		System.out.println("x:newOrder=" + IOS_IOS_BHHJ_BD_bdqudao23_newOrder);
		System.out.println("x:oldOrder=" + IOS_IOS_BHHJ_BD_bdqudao23_oldOrder);
		System.out.println("x:newLoan=" + IOS_IOS_BHHJ_BD_bdqudao23_newLoan);
		System.out.println("x:oldLoan=" + IOS_IOS_BHHJ_BD_bdqudao23_oldLoan);
		System.out.println("x:newApply=" + IOS_IOS_BHHJ_BD_bdqudao23_newApply);
		System.out.println("x:oldApply=" + IOS_IOS_BHHJ_BD_bdqudao23_oldApply);
		System.out.println("x:newDelay=" + IOS_IOS_BHHJ_BD_bdqudao23_newDelay);
		System.out.println("x:oldApplyRegister=" + IOS_IOS_BHHJ_BD_bdqudao23_oldApplyRegister);
		System.out.println("x:register=" + IOS_IOS_BHHJ_BD_bdqudao23_register);
		System.out.println("x:activation=" + IOS_IOS_BHHJ_BD_bdqudao23_activation);
		System.out.println("x:dau=" + IOS_IOS_BHHJ_BD_bdqudao23_dau);
	}
}
