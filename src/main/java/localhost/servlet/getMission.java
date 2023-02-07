package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.GetAwardRecordMapper;
import localhost.mapper.GetMissionRecordMapper;
import localhost.pojo.GetAwardRecord;
import localhost.pojo.GetMissionRecord;
import localhost.pojo.ReturnData;
import localhost.util.SqlSessionFactoryUtil;
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

@WebServlet("/getMission")
public class getMission extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        GetMissionRecordMapper getMissionRecordMapper= sqlSession.getMapper(GetMissionRecordMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int mission_id = Integer.parseInt(request.getParameter("mission_id"));
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        int status = 0;

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());
        String get_datetime = string_date;

        GetMissionRecord getMissionRecord = new GetMissionRecord();
        getMissionRecord.setMission_id(mission_id);
        getMissionRecord.setUser_id(user_id);
        getMissionRecord.setStatus(status);
        getMissionRecord.setGet_datetime(get_datetime);

        System.out.println("/publishMission【" + string_date + "】收到mission_id:" + mission_id+",user_id:"+user_id+",status:"+status+",get_datetime:"+get_datetime);
        //        调用Mapper接口
        getMissionRecordMapper.getMission(getMissionRecord);
        int result =getMissionRecord.getId();
        System.out.println(result);
        //提交事务
        sqlSession.commit();
        //关闭mysql会话
        sqlSession.close();

//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (result > 0) {
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("领取任务成功");
            returnData.setData(result);
            writer.write(JSON.toJSONString(returnData));

        } else {
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("领取任务失败");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
