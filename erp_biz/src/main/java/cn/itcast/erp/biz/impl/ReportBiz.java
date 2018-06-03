package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.dao.IReportDao;

public class ReportBiz implements IReportBiz {
	
	private IReportDao reportDao;

	@Override
	public List<Map<String,Object>> orderReport(Date startDate, Date endDate) {
		return reportDao.orderReport(startDate, endDate);
	}

	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	@Override
	public List<Map<String, Object>> trendReport(int year) {
		// 构建返回的数据
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// map<name,vlaue>
		Map<String, Object> monthData = null;
		// 循环12个进行查询
		for(int i = 1; i <= 12; i++){
			// 查询每个月销售额
			monthData = reportDao.trendReport(year, i);
			if(null == monthData){
				// 这个月没有销售额, 补0
				//[{name:"1",y:52.1},{name:"2",y:931.2}]
				monthData = new HashMap<String, Object>();
				monthData.put("name", i);
				monthData.put("y", 0);
			}
			result.add(monthData);
		}
		
		return result;
	}

}
