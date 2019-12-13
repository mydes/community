package com.example.community.mapper;

import com.example.community.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where token=#{token}")
    User findBiToken(@Param("token") String token);
    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);
    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);
    @Update("UPDATE USER SET NAME = #{name},token = #{token},gmt_modified = #{gmtModified},avatar_url=#{avatarUrl} WHERE id = #{id}")
    void updateUser(User dbUser);
}
