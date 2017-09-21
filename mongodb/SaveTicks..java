package com.dfitc.stp485ab6dc0c34.strategy.baseStrategy;

import com.dfitc.stp.annotations.*;
import com.dfitc.stp.indicator.*;
import com.dfitc.stp.market.*;
import com.dfitc.stp.trader.*;
import com.dfitc.stp.strategy.*;
import com.dfitc.stp.entity.Time;
import com.dfitc.stp.util.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
  *策略描述:
  */
@Strategy(name = "SaveTicks",version = "1.0",outputMode = OutputMode.TIMER, outputPeriod = 3000, contractNumber = 1)
public class SaveTicks extends BaseStrategy {
	/**
	 * file path
	 */
	 String SavePath="f:/history/data/tick/";
	 
	/**
	 * Will write tick data to the file.
	 */
	FileWriter tickFileWriter;
	
	 
	/**
	 * csv header 
	 */
	 String HeaderString="symbol,date,price,volume,openInterest,askPrice1,askVolume1,bidPrice1,bidVolume1";
	 
	 
	 /**
	  * True will write csv header otherwise skip 
	  */
	 boolean isWriteheader=true;
	 
	 /**
		 * init write file
		 */
		public  void initFile(){
			String contractCode=this.getContractCode();
			Date today = new Date();
			String fileName=contractCode+"_"+today.getTime();
			File tickFile = new File(this.SavePath, fileName+".csv");  
	        try {  

	            if(tickFile.exists()){  //判断当前文件是否存在  
	            	tickFile.delete();      //存在就删除  
	            }  
	            tickFile.createNewFile(); // 创建文件  
	            this.tickFileWriter = new FileWriter(tickFile);  
	            //write csv header
	            if(this.isWriteheader)
	            	this.writeLine(this.HeaderString);
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	       
		}
		
		/**
		 * 处理TICK行情
		 * 
		 * @param md
		 *            触发此次调用的行情快照
		 */
		public void writeTickMD(MD md) {
				 String data = new String(
					        md.getContractCode()+","+
					        this.dateToString(md.getTimeStamp())+","+
					        //md.getTradingTimeStamp().getTime()+","+
							md.latestPrice+","+
							md.vol+","+
				            md.openInt+
					        md.askPrice1+","+
					        md.askVol1+","+
					        md.bidPrice1+","+
					        md.bidVol1
					     );  
				 this.writeLine(data);

		}
		/**
		 * write text to stream by line
		 */
		public void writeLine(String data) {
			 try {  

	            this.tickFileWriter.write(data+"\n");
	            this.tickFileWriter.flush();
		        } catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        } 
		}


		@Override
		protected void finalize(){
			 try {  
			this.tickFileWriter.close(); 
			  } catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
		}

	/**
	 * 初始化方法，在策略创建时调用
	 * 
	 * @param contracts
	 *            策略关联的合约
	 */
	@Override
	public void initialize(String[] contracts) {
		
		this.setProperty(Prop.AUTO_PAUSE_WHEN_CLOSE, false);//设置休市时是否暂停策略
		this.setProperty(Prop.AUTO_RESUME_WHEN_OPEN, false);//设置开市时是否启动策略
		this.setProperty(Prop.AUTO_PAUSE_WHEN_LIMIT, false);//设置涨跌停时是否暂停策略
		this.setProperty(Prop.CLOSE_TODAY_FIRST, false);//设置平仓时优先平昨
		this.setProperty(Prop.QUERY_HISTORY_BACKCOUNT, 50);//向前查询历史K线条数(限1分钟或以上周期)
		this.initFile();
	}
	
	/**
	 * 初始化K线周期，在策略创建时被调用(在initialize之后调用)
	 * 
	 * @param contracts
	 *            策略相关联的合约
	 */
	
	@Override
	public void setBarCycles(String[] contracts) {
	}

	/**
	 * 初始化指标，在策略创建时被调用(在initialize之后调用)
	 * 
	 * @param contracts
	 *            策略相关联的合约
	 */
	@Override
	public void setIndicators(String[] contracts) {
	}
	
	
	/**
	 * 处理TICK行情
	 * 
	 * @param md
	 *            触发此次调用的行情快照
	 */
	@Override
	public void processMD(MD md) {
		this.writeTickMD(md);
	}
	
	/**
	 * convert date to ISODate
	 * @param time
	 * @return
	 */
	String dateToString(Date time){ 
		time=new Date(time.getTime());
	    SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSSS'+08:00'"); 
	    String ctime = formatter.format(time); 
	    return ctime; 
	    
	} 

}
