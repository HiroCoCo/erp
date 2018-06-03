package cn.itcast.erp.biz.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.Const;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private IEmpDao empDao;
	private IRoleDao roleDao;
	private JedisPool jedisPool;
	
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
	}

	@Override
	public Emp findByUsernameAndPwd(String username, String pwd) {
		pwd = encrypt(pwd, username);
		System.out.println("pwd:" + pwd);
		return empDao.findByUsernameAndPwd(username, pwd);
	}
	
	@Override
	public void add(Emp t) {
		// 参数1： 要加密的 内容
		// 参数2： 盐          扰乱码
		// 参数3：散列次数    再多次md5
		Md5Hash md5 = new Md5Hash(t.getUsername(),t.getUsername(),3);
		// 加密
		String newPwd = md5.toString();
		// 设置成加密后的密码
		t.setPwd(newPwd);
		super.add(t);
	}
	
	private String encrypt(String src, String salt){
		Md5Hash md5 = new Md5Hash(src,salt,3);
		return md5.toString();
	}

	@Override
	public void updatePwd(String oldPwd, String newPwd, Long uuid) {
		Emp emp = empDao.get(uuid);
		// 加密旧密码
		oldPwd = encrypt(oldPwd, emp.getUsername());
		if(!emp.getPwd().equals(oldPwd)){
			throw new ErpException("原密码不正确");
		}
		// 加密新密码
		newPwd = encrypt(newPwd, emp.getUsername());
		empDao.updatePwd(uuid, newPwd);
	}

	@Override
	public void updatePwd_reset(String newPwd, Long uuid) {
		Emp emp = empDao.get(uuid);
		// 加密新密码
		newPwd = encrypt(newPwd, emp.getUsername());
		empDao.updatePwd(uuid, newPwd);
	}

	@Override
	public List<Tree> readEmpRoles(Long uuid) {
		Emp emp = empDao.get(uuid);
		List<Role> empRoles = emp.getRoles();
		
		List<Tree> result = new ArrayList<Tree>();
		// 所有的角色信息
		List<Role> roles = roleDao.getList(null, null, null);
		// 把角色转成树的节点
		for (Role role : roles) {
			// 把角色转成树的节点
			Tree t = createTree(role);
			if(empRoles.contains(role)){
				// 用户的角色集合中包含这个角色，让它选中
				t.setChecked(true);
			}
			
			// 把节点添加到树中
			result.add(t);
		}
		
		return result;
	}

	@Override
	public void updateEmpRoles(Long uuid, String ids) {
		// 获取用户对象，进入持久状
		Emp emp = empDao.get(uuid);
		// 清除原有的关系
		// delete from emp_role where empuuid=?
		emp.setRoles(new ArrayList<Role>());
		// 分割的角色的编号
		String[] roleIds = ids.split(",");
		
		for (String roleId : roleIds) {
			// 让角色进入持久态
			Role role = roleDao.get(Long.valueOf(roleId));
			// 重新设置用户下的角色
			emp.getRoles().add(role);
		}
		
		// 清除redis用户权限的缓存
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = Const.MENU_KEY + uuid;
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(null != jedis){
				try {
					jedis.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jedis = null;
			}
		}
		
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	/**
	 * 把角色数据转成树的节点
	 * @param role
	 * @return
	 */
	private Tree createTree(Role role){
		Tree tree = new Tree();
		tree.setId(role.getUuid() + "");
		tree.setText(role.getName());
		// 解决添加子节点时的空异常
		tree.setChildren(new ArrayList<Tree>());
		return tree;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
}
