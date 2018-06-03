package cn.itcast.erp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.util.WebUtil;

/**
 * 报表action
 *
 */
public class ReportAction {

	private IReportBiz reportBiz;

	public void setReportBiz(IReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}
	
	private Date startDate; // 开始日期
	private Date endDate; // 结束日期
	private int year;// 年份
	
	/**
	 * 销售统计
	 */
	public void orderReport(){
		List<Map<String,Object>> list = reportBiz.orderReport(startDate, endDate);
		WebUtil.write(list);
	}
	
	/**
	 * 销售趋势
	 */
	public void trendReport(){
		List<Map<String, Object>> list = reportBiz.trendReport(year);
		WebUtil.write(list);
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
