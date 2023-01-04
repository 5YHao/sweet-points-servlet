package localhost.mapper;

import localhost.pojo.Mission;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface MissionsMapper {
    List<Mission> selectMissionsByPublisherId(@Param("publisher_id") int publisher_id);

    Integer deleteMissionByUserId(@Param("user_id") int user_id);

    Integer deleteMissionById(@Param("id") int id);

    Integer publishMission(Mission mission);

    Integer changeMissionPointsById(@Param("id") int id, @Param("newPoints") int newPoints);

    Mission selectPointsById(@Param("id") int id);

    Mission selectMissionInfoById(@Param("id") int id);

    Integer updateMissionInfo(Mission mission);
}
