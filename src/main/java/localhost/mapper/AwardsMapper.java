package localhost.mapper;

import localhost.pojo.Award;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AwardsMapper {
    Integer updateAwardInfo(Award award);
    Award selectAwardInfoById(@Param("id") int id);
    List<Award> selectAwardsByPublisherId(@Param("publisher_id") int publisher_id);
    Integer deleteAwardByUserId(@Param("user_id") int user_id);
    Integer deleteAwardById(@Param("id") int id);
    Integer publishAward(Award award);
    Integer changeAwardPriceById(@Param("id") String id,@Param("newPrice") String newPrice);
}
