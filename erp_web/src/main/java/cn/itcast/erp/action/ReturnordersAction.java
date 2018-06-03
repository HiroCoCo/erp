package cn.itcast.erp.action;
import org.apache.shiro.authz.UnauthorizedException;

import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IReturnordersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Returnorders;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

/**
 * 退货订单Action 
 * @author Administrator
 *
 */
public class ReturnordersAction extends BaseAction<Returnorders> {

	private IReturnordersBiz returnordersBiz;
	private String json;
	private IWaybillWs waybillWs;
	private Long waybillsn;

	public void setReturnordersBiz(IReturnordersBiz returnordersBiz) {
		this.returnordersBiz = returnordersBiz;
		super.setBaseBiz(this.returnordersBiz);
	}
	
	/**
	 * 审核
	 */
	public void doCheck(){
		Emp loginUser = WebUtil.getLoginUser();
		if(null == loginUser){
			WebUtil.ajaxReturn(false, "您还没有登陆");
			return;
		}
		
		try {
			returnordersBiz.doCheck(getId(), loginUser.getUuid());
			WebUtil.ajaxReturn(true, "审核成功");
		} catch (ErpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "没有权限");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "审核失败");
		}
	}
	
	
	//---------------------以下是get set-------------------//

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public IWaybillWs getWaybillWs() {
		return waybillWs;
	}

	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}

	public Long getWaybillsn() {
		return waybillsn;
	}

	public void setWaybillsn(Long waybillsn) {
		this.waybillsn = waybillsn;
	}
	

}
