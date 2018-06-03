package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报表查询
 *
 */
public interface IReportDao {

	/**
	 * 销售统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String,Object>> orderReport(Date startDate, Date endDate);
	
	/**
	 * 销售趋势
	 * @param year
	 * @param month
	 * @return
	 */
	Map<String,Object> trendReport(int year, int month);
}
