package com.witl.kalimba.webplayer.common;

/*@auther : Ambarish Kumar
 * 
 */
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.witl.kalimba.webplayer.common.User;

public class UserMapper implements RowMapper<User> {
   public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setId(rs.getInt("id"));
      user.setEmail(rs.getString("email"));
      user.setName(rs.getString("name"));
      user.setFirst_Name(rs.getString("first_name"));
      user.setLast_Name(rs.getString("last_name"));
      user.setGender(rs.getString("gender"));
      user.setBirthday(rs.getString("birthday"));
      user.setLocation(rs.getString("location"));
      user.setHometown(rs.getString("hometown"));
      user.setRelationship(rs.getString("relationship"));
      user.setTimezone(rs.getString("timezone"));
      user.setProvider(rs.getString("provider"));
      user.setProvider(rs.getString("provider_id"));
      user.setUserType(rs.getString("user_type"));
      
       return user;
   }
}