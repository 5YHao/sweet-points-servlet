package localhost.mapper;

import localhost.pojo.GetMissionRecord;
import localhost.pojo.Mission;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

public interface GetMissionRecordMapper {
    Integer deleteGetMissionRecordById(@Param("id") int id);
    Integer deleteGetMissionRecordByUserId(@Param("user_id") int user_id);
    Integer changeMissionStatus(@Param("getMission_id") int getMission_id,@Param("status") int status);
    Integer getMission(GetMissionRecord getMissionRecord);
    GetMissionRecord selectGetMissionRecordById(@Param("id") int id);
    Integer deleteGetMissionRecordByMissionId(@Param("mission_id") int mission_id);
    List<GetMissionRecord> selectGetMissionRecordByUserId(@Param("user_id") int user_id);
    List<GetMissionRecord> selectGetMissionRecordByUserIdAndStatus(@Param("user_id") int user_id,@Param("status") int status);
}
