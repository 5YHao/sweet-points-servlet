package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.MissionsMapper;
import localhost.pojo.Award;
import localhost.pojo.Mission;
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

@WebServlet("/updateMissionInfo")
public class updateMissionInfo extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //        加载mybatis核心配置文件，获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
//        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
        MissionsMapper missionsMapper = sqlSession.getMapper(MissionsMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        Mission mission = new Mission();
        mission.setId(Integer.parseInt(request.getParameter("id")));
        mission.setName(request.getParameter("name"));
        mission.setDescription(request.getParameter("description"));
        mission.setPoints(Integer.parseInt(request.getParameter("points")));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/updateMissionInfo" + "【" + string_date + "】收到mission:" + mission.toString());
        //        调用Mapper接口
        Integer result = missionsMapper.updateMissionInfo(mission);
        System.out.println(mission.toString());


        sqlSession.commit();
        //        关闭mysql会话
        sqlSession.close();

//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (result > 0) {
//            查询有结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("修改成功");
            returnData.setData(mission);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            查询无结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("修改操作无影响记录");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
