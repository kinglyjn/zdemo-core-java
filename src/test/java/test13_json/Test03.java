package test13_json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Test03 {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/Users/zhangqingli/Desktop/hbw_20181210.txt"))));
		
		String line = null;
		int i = 0;
		// 放款量(新) = 新激活放款量（新） + 激活延时放款量（新）
		// 放款量 = 放款量（新） + 放款量（老）
		// 放款金额（总） = 放款金额（新） + 放款金额（老）
		Integer totalNewLoan = 0;
		Integer totalNewDelay = 0;
		Integer totalOldLoan = 0;
		Double totalNewLoanMoney = 0.0;
		Double totalOldLoanMoney = 0.0;
		
		while ( (line=br.readLine()) != null ) {
			if (i==0) {
				i++;
				continue;
			}
			i++;
			String[] splits = line.split(",");
			
			
			
		}
		
		System.out.println("总单量：" + (i-1));
		System.out.println("新放款量：" + (totalNewLoan+totalNewDelay) 
				+ ", 老放款量：" + totalOldLoan 
				+ ", 总放款量：" + (totalNewLoan+totalNewDelay+totalOldLoan));
		System.out.println("新放款金额：" + totalNewLoanMoney 
				+ ", 老放款金额：" + totalOldLoanMoney
				+ ", 总放款金额：" + (totalNewLoanMoney+totalOldLoanMoney));
		br.close();
	}
}
