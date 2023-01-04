package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.GetMissionRecordMapper;
import localhost.mapper.MissionsMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.GetMissionRecord;
import localhost.pojo.Mission;
import localhost.pojo.ReturnData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/finishMission")
public class finishMission extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //        加载mybatis核心配置文件，获取SqlSessionFactory对象
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        GetMissionRecordMapper getMissionRecordMapper= sqlSession.getMapper(GetMissionRecordMapper.class);
        MissionsMapper missionsMapper = sqlSession.getMapper(MissionsMapper.class);
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int id = Integer.parseInt(request.getParameter("id"));
        int status = 1;

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());
        String get_datetime = string_date;

//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        System.out.println("/finishMission【" + string_date + "】收到id:" + id);
        //        调用Mapper接口
        GetMissionRecord getMissionRecord = getMissionRecordMapper.selectGetMissionRecordById(id);
        if(getMissionRecord!=null){
            Mission mission = missionsMapper.selectMissionInfoById(getMissionRecord.getMission_id());
            if(mission!=null){
                Integer increasePointsByIdResult = usersMapper.increasePointsById(getMissionRecord.getUser_id(), mission.getPoints());
                Integer changeMissionStatusResult = getMissionRecordMapper.changeMissionStatus(id,status);
                System.out.println("increasePointsByIdResult:"+increasePointsByIdResult);
                System.out.println("changeMissionStatusResult:"+changeMissionStatusResult);
                if(increasePointsByIdResult>0&&changeMissionStatusResult>0){
                    ReturnData returnData = new ReturnData();
                    returnData.setErr_code(0);
                    returnData.setMsg("完成任务成功");
                    returnData.setData(mission.getPoints());
                    writer.write(JSON.toJSONString(returnData));
                }else{
                    ReturnData returnData = new ReturnData();
                    returnData.setErr_code(1);
                    returnData.setMsg("完成任务失败");
                    returnData.setData("increasePointsByIdResult:"+increasePointsByIdResult+";changeMissionStatusResult:"+changeMissionStatusResult);
                    writer.write(JSON.toJSONString(returnData));
                }
            }
           
        }
        
        //提交事务
        sqlSession.commit();
        //关闭mysql会话
        sqlSession.close();



    }
}
