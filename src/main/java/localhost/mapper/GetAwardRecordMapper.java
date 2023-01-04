package localhost.mapper;

import localhost.pojo.GetAwardRecord;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface GetAwardRecordMapper {
    Integer deleteGetAwardRecordByAwardId(@Param("award_id") int award_id);
    Integer deleteGetAwardRecordByUserId(@Param("user_id") int user_id);
    Integer changeAwardStatus(@Param("getAward_id") int getAward_id,@Param("status") int status);
    Integer getAward(GetAwardRecord getAwardRecord);
    List<GetAwardRecord> selectGetAwardRecordByUserId(@Param("user_id") int user_id);
    List<GetAwardRecord> selectGetAwardRecordByUserIdAndStatus(@Param("user_id") int user_id,@Param("status") int status);
    GetAwardRecord selectGetAwardRecordById(@Param("id") int id);
}
