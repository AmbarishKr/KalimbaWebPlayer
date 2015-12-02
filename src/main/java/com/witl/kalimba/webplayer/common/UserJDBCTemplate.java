package com.witl.kalimba.webplayer.common;
/*@auther : Ambarish Kumar
 * 
 */

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.witl.kalimba.webplayer.common.User;
import com.witl.kalimba.webplayer.common.UserMapper;

public class UserJDBCTemplate {
   private JdbcTemplate jdbcTemplateObject;
   
   public UserJDBCTemplate(DataSource dataSource) {
	   jdbcTemplateObject = new JdbcTemplate(dataSource);
   }

  // public void create(String email,String name,String first_name,String last_name,String gender,String birthday,String location,String hometown,String bio,String relationship , String timezone,String provider,Integer provider_id)
	public void create(User user)
   {
	
	String SQLCNT="select count(*) from USERS where email=? and user_type=?";
	int cnt=jdbcTemplateObject.queryForInt(SQLCNT,user.getEmail(),user.getUserType());
	System.out.println("cnt======"+cnt);
	if(cnt==0){
      String SQL = "insert into USERS (email, name, first_name, last_name, gender, birthday, location, hometown, relationship, timezone, provider, provider_id, login_time,creation_date, user_type) values (?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),sysdate(),?)";
      
      jdbcTemplateObject.update( SQL,user.getEmail(), user.getName(), user.getFirst_Name(), user.getLast_Name(), user.getGender(), user.getBirthday(), user.getLocation(), user.getHometown(), user.getRelationship(), user.getTimezone(), user.getProvider(), user.getProvider_id(),user.getUserType());
      System.out.println("Created Record Name = " + user.getName() + " Email = " + user.getEmail());
      return;
	}
	else{
		String SQL = "update USERS set login_time=sysdate(),name=?,first_name=?, last_name=?, gender=?, birthday=?,location=?,hometown=?,relationship=?,timezone=?,provider=?,provider_id=? where email=? and user_type=?";
	      
	      jdbcTemplateObject.update( SQL, user.getName(), user.getFirst_Name(), user.getLast_Name(), user.getGender(), user.getBirthday(),user.getLocation(),user.getHometown(),user.getRelationship(),user.getTimezone(),user.getProvider(),user.getProvider_id(),user.getEmail(),user.getUserType());
	      System.out.println("update Record Name = " + user.getName() + " Email = " + user.getEmail());
	      return;
	}
   }

   public User getUser(String email) {
      String SQL = "select * from USERS where email = ?";
      User user = jdbcTemplateObject.queryForObject(SQL, 
                        new Object[]{email}, new UserMapper());
      
      return user;
   }

   public List<User> listUsers() {
      String SQL = "select * from USERS";
      List <User> users = jdbcTemplateObject.query(SQL, 
                                new UserMapper());
      return users;
   }

   public void delete(String email){
      String SQL = "delete from USERS where email = ?";
      jdbcTemplateObject.update(SQL, email);
      System.out.println("Deleted Record with email = " + email );
      return;
   }

   public void update(String email, String name){
      String SQL = "update USERS set name = ? where email = ?";
      jdbcTemplateObject.update(SQL, name, email);
      System.out.println("Updated Record with email = '" + email );
      return;
   }

   public int validateTransaction(String pnrId, String TrnxToken)
   {
	
	String SQLCNT="select count(*) from Transaction where tnsId=? and pnrId=? and isDownloaded <> 'YES'";
	int cnt=jdbcTemplateObject.queryForInt(SQLCNT,TrnxToken,pnrId);
	System.out.println("cnt======"+cnt);
	if(cnt>0){
		String SQL = "update Transaction set isDownloaded='YES' where tnsId=? and pnrId=? and isDownloaded <> 'YES'";
	      
	      jdbcTemplateObject.update( SQL, TrnxToken,pnrId);
	      //System.out.println("update Record Name = " + user.getName() + " Email = " + user.getEmail());
	     
	}
	  return cnt ;
   }
   
}