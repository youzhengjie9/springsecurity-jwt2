package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.dto.UserFormDto;
import com.boot.entity.User;
import com.boot.entity.UserRole;
import com.boot.security.LoginUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserService通过继承IService接口，便可以获得IService接口提供的基础功能
 * IService的泛型：为我们要操作的实体类
 * @author youzhengjie 2022-09-22 22:45:02
 */
public interface UserService extends IService<User> {


    /**
     * 更新Redis中的loginUser。
     * 作用：当我们修改了用户的一些信息就可以通过调用这个方法更新Redis中的loginUser，使得前端可以实时获取到当前用户最新数据
     * @param loginUser
     * @return true为更新成功，false为更新失败
     */
    boolean updateLoginUser(LoginUser loginUser);

    /**
     * 查询所有用户并分页（并对分页功能进行性能调优）
     */
    List<User> selectAllUserByLimit(int page,int size);

    /**
     * 查询总用户数
     */
    int selectAllUserCount();

    /**
     * 添加用户
     *
     * @param userFormDto 用户表单dto
     * @return int
     */
    int addUser(UserFormDto userFormDto);

    /**
     * 更新用户
     *
     * @param userFormDto 用户表单dto
     * @return int
     */
    int updateUser(UserFormDto userFormDto);


    /**
     * 删除用户
     *
     * @param id id
     * @return int
     */
    int deleteUser(long id);


    /**
     * 将角色分配给用户
     *
     * @param userRoleList 用户角色列表
     * @return boolean
     */
    boolean assignRoleToUser(List<UserRole> userRoleList);

    /**
     * mysql通过userName关键字搜索（后期为了性能要放到elasticsearch中，mysql速度不高）
     */
    List<User> searchUserByUserNameAndLimit(String userName,
                                            int page,
                                            int size);


    /**
     * 按用户名搜索用户数量
     *
     * @param userName 用户名
     * @return int
     */
    int searchUserCountByUserName(String userName);


}
