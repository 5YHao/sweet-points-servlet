package localhost.mapper;

import localhost.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

//Mybatis 参数封装底层原理
/*单个参数
    1.POJO类型：直接使用，属性名 和 参数占位符名称 一致
    2.Map集合：直接使用，键名 和 参数占位符名称 一致
    3.Collection：封装成Map。【建议使用@Param朱姐，替换Map集合中默认的arg键名】
        param.put("arg0",collection集合)
        param.put("collection",collection集合)
    4.List：封装成Map。【建议使用@Param朱姐，替换Map集合中默认的arg键名】
        param.put("arg0",collection集合)
        param.put("collection",collection集合)
        param.put("list",list集合)
    5.Array：封装成Map。【建议使用@Param朱姐，替换Map集合中默认的arg键名】
        param.put("arg0",数组)
        param.put("array",数组)
        param.put("list",list集合)
    6.其他类型
 */

/*多个参数 封装为Map集合。【建议使用@Param朱姐，替换Map集合中默认的arg键名】
    param.put("arg0",参数值1)
    param.put("arg1",参数值2)
    param.put("param1",参数值1)
    param.put("param2",参数值2)
 */

public interface UsersMapper {
    //    简单用户登录
    User login(@Param("username") String username, @Param("password") String password);

    //    根据username查询用户
    User selectUserByUsername(@Param("username") String username);
    //    根据id查询用户
    User selectUserById(@Param("id") int id);
    // 根据openid查询用户
    User selectUserByOpenid(@Param("openid") String openid);
    //    查询所有用户
    List<User> selectAllUsers();

    //    添加用户 [MyBatis插入数据后返回主键id]
    Integer addUser(User user);

    //    根据id修改密码
    Integer updatePasswordById(@Param("password") String password, @Param("id") int id);
    //根据id修改用户信息
    Integer updateUserInfoById(User user);
    //根据id修改分值(解绑时用)
    Integer clearPointsById(@Param("id") int id);

    //    根据id批量删除用户
    Integer deleteUserByIds(@Param("ids") int[] ids);

    //修改密码
    Integer changePassword(@Param("username") String username,@Param("newPassword") String newPassword);

    //加分
    Integer increasePointsById(@Param("id") int id,@Param("pointsNum") int pointsNum);
    //消费
    Integer decreasePointsById(@Param("id") int id,@Param("awardPrice") int awardPrice);

    Integer selectUserHaveHowManyPointsById(@Param("id") int id);
}
