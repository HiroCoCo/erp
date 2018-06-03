package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IReportDao;
@SuppressWarnings("unchecked")
public class ReportDao extends HibernateDaoSupport implements IReportDao {

	@Override
	public List<Map<String,Object>> orderReport(Date startDate, Date endDate) {
		String hql ="select new Map(gt.name as name,sum(od.money) as y,gt.name as name2) from "
				+ "Goodstype gt, Orders o, Orderdetail od, Goods g "
				+ "where "
				+ "o = od.orders "
				+ "and gt=g.goodstype "
				+ "and g.uuid=od.goodsuuid "
				+ "and o.type='2' ";
		//保存参数
		List<Date> params = new ArrayList<Date>();
		if(null != startDate){
			hql += "and o.createtime>=? ";
			params.add(startDate);
		}
		if(null != endDate){
			hql += "and o.createtime<=? ";
			params.add(endDate);
		}
		hql += "group by gt.name";
		
		return (List<Map<String,Object>>)this.getHibernateTemplate().find(hql, params.toArray());
	}

	@Override
	public Map<String, Object> trendReport(int year, int month) {
		// month/year 都会调用oracle中的extract函数: 从日期中抽取部分的值
		String hql = "select new Map(month(o.createtime) as name,sum(od.money) as y) "
				+ "from Orders o, Orderdetail od "
				+ "where o=od.orders "
				+ "and o.type='2' and year(o.createtime)=? "
				+ "and month(o.createtime)=? "
				+ "group by month(o.createtime)";
		// 有，且只有一条记录
		List<Map<String, Object>> list = (List<Map<String, Object>>) getHibernateTemplate().find(hql, year,month);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
