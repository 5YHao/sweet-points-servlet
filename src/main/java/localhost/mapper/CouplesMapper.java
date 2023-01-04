package localhost.mapper;

import localhost.pojo.Couple;
import org.apache.ibatis.annotations.Param;

public interface CouplesMapper{
//    根据用户id查找绑定记录
    Couple selectCoupleInfoByUserId(@Param("userId") int userId);
//    根据id查找情侣信息
    Couple selectCoupleInfoById(@Param("couple_id") int couple_id);
    //对象绑定
    Integer bindCouple(Couple couple);
    //解绑对象
    Integer breakCouple(@Param("couple_id") int couple_id);
}
