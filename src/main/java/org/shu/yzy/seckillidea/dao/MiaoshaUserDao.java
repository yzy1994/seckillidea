package org.shu.yzy.seckillidea.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.shu.yzy.seckillidea.domain.MiaoshaUser;
import org.shu.yzy.seckillidea.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);

    @Insert("insert into miaosha_user(id, nickname, password, salt) " +
            "values (#{id}, #{nickname}, #{password}, #{salt})")
    int insertUser(@Param("id") long id, @Param("nickname") String nickname,
                   @Param("password") String password, @Param("salt") String salt);

    @Select("select * from miaosha_user limit #{start}, #{count} ")
    List<MiaoshaUser> getUserList(Page page);
}
