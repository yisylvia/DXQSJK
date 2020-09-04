package cn.edu.zucc.personplan.comtrol.example;

import java.sql.Date;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import cn.edu.zucc.personplan.itf.IUserManager;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.BusinessException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;

public class ExampleUserManager implements IUserManager {

	@Override
	public BeanUser reg(String userid, String pwd,String pwd2) throws BaseException {
		if(userid==null || "".equals(userid)) throw new BusinessException("�˺Ų���Ϊ��");
		if(pwd==null || "".equals(pwd)) throw new BusinessException("���벻��Ϊ��");
		if(!pwd.equals(pwd2)) throw new BusinessException("�������������벻һ��");
		Connection conn=null;
		
		try {
			conn=(Connection) DBUtil.getConnection();
			String sql = "select user_id from tbl_user where user_id=?";
			java.sql.PreparedStatement pst =conn.prepareStatement(sql);
			pst.setString(1, userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) {
				rs.close();
				pst.close();
				throw new BusinessException("�û��Ѿ�����");
			}
			rs.close();
			pst.close();
			sql="insert into tbl_user(user_id,user_pwd,register_time) values(?,?,?)";
			pst=conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, pwd);
			pst.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
			pst.execute();
			BeanUser user=new BeanUser();
			user.setRegister_time(new Date(0));
			user.setUser_id(userid);
			return user;
		}catch(SQLException ex) {
			throw new DbException(ex);
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				}catch(SQLException e) {
						e.printStackTrace();
					}
			}
		}
		
	}

	
	@Override
	public BeanUser login(String userid, String pwd) throws BaseException {
		Connection conn=null;
		BeanUser user=null;
		if(pwd==null) {
			
		}
		try {
			conn=(Connection) DBUtil.getConnection();
			String sql = "select register_time"
					+ " from tbl_user where user_id = ? and user_pwd = ?";
			java.sql.PreparedStatement pst =conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, pwd);
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) {
				user=new BeanUser();
				user.setUser_id(userid);
				user.setRegister_time(rs.getTimestamp(1));
			}
			else {
				throw new BusinessException("�û��Ѿ�����");
			}
		}catch(SQLException ex) {
			throw new DbException(ex);
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				}catch(SQLException e) {
						e.printStackTrace();
					}
			}
		}
		return null;
	}


	@Override
	public void changePwd(BeanUser user, String oldPwd, String newPwd,
			String newPwd2) throws BaseException {
		// TODO Auto-generated method stub
		
	}

}
