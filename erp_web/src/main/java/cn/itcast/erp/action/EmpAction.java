package cn.itcast.erp.action;
import java.util.List;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.util.WebUtil;

/**
 * 员工Action 
 * @author Administrator
 *
 */
public class EmpAction extends BaseAction<Emp> {

	private IEmpBiz empBiz;
	private String oldPwd; // 原密码
	private String newPwd; // 新密码

	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		super.setBaseBiz(this.empBiz);
	}
	
	/**
	 * 修改密码
	 */
	public void updatePwd(){
		Emp loginUser = WebUtil.getLoginUser();
		if(null == loginUser){
			WebUtil.ajaxReturn(false, "请先登陆!");
			return;
		}
		try {
			empBiz.updatePwd(oldPwd, newPwd, loginUser.getUuid());
			WebUtil.ajaxReturn(true, "修改密码成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "修改密码失败");
		}
	}
	
	/**
	 * 重置密码
	 */
	public void updatePwd_reset(){
		try {
			Long uuid = getId();// 前端传过来的员工编号
			empBiz.updatePwd_reset(newPwd, uuid);
			WebUtil.ajaxReturn(true, "重置密码成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "重置密码失败");
		}
	}
	
	/**
	 * 读取用户角色设置信息
	 */
	public void readEmpRoles(){
		List<Tree> list = empBiz.readEmpRoles(getId());
		WebUtil.write(list);
	}
	
	private String ids; // 角色的编号，多个用逗号分割
	
	/**
	 * 更新用户的角色
	 */
	public void updateEmpRoles(){
		try {
			empBiz.updateEmpRoles(getId(), ids);
			WebUtil.ajaxReturn(true, "保存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "保存失败");
		}
	}
	

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
