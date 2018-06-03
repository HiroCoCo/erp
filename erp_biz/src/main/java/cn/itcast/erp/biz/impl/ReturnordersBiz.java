package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;

import cn.itcast.erp.biz.IReturnordersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IReturnordersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Returnorders;
import cn.itcast.erp.exception.ErpException;

/**
 * 退货订单业务逻辑类
 * 
 * @author Administrator
 *
 */
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

	private IReturnordersDao returnordersDao;
	private IEmpDao empDao;
	private ISupplierDao supplierDao;

	public void setReturnordersDao(IReturnordersDao returnordersDao) {
		this.returnordersDao = returnordersDao;
		super.setBaseDao(this.returnordersDao);
	}

	@Override
	public List<Returnorders> getListByPage(Returnorders t1, Returnorders t2, Object param, int firstResult,
			int maxResults) {
		List<Returnorders> list = super.getListByPage(t1, t2, param, firstResult, maxResults);
		for (Returnorders returnorders : list) {
			// 设置下单员
			returnorders.setCreaterName(getEmpName(returnorders.getCreater()));
			// 采购员审核员
			returnorders.setCheckerName(getEmpName(returnorders.getChecker()));
			// 设置库管员
			returnorders.setEnderName(getEmpName(returnorders.getEnder()));
			// 设置供应商或客户
			returnorders.setSupplierName(supplierDao.get(returnorders.getSupplieruuid()).getName());
		}
		return list;
	}

	/**
	 * 获得员工名字
	 * 
	 * @param empuuid
	 *            员工编号
	 * @return
	 */
	private String getEmpName(Long empuuid) {
		if (empuuid == null) {
			return null;
		}
		return empDao.get(empuuid).getName();
	}

	@Override
	public void doCheck(Long uuid, Long empUuid) {
		// 持久化状态的对象
		Returnorders returnorders = returnordersDao.get(uuid);
		// 状态的判断
		if (!Returnorders.STATE_CREATE.equals(returnorders.getState())) {
			throw new ErpException("该订单已经审核过了，不能重复审核");
		}

		// 1. 审核日期 系统时间
		returnorders.setChecktime(new Date());
		// 2. 审核员 登陆用户的编号
		returnorders.setChecker(empUuid);
		// 3. 状态 已审核 1
		returnorders.setState(Returnorders.STATE_CHECK);
	}

	// --------------------------以下是get set-------------------------//
	public IEmpDao getEmpDao() {
		return empDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public ISupplierDao getSupplierDao() {
		return supplierDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public IReturnordersDao getReturnordersDao() {
		return returnordersDao;
	}

}
