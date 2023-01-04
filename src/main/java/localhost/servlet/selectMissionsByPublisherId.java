package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.MissionsMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.Mission;
import localhost.pojo.ReturnData;
import localhost.pojo.User;
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
import java.util.List;

@WebServlet("/selectMissionsByPublisherId")
public class selectMissionsByPublisherId extends HttpServlet {
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
        MissionsMapper missionsMapper = sqlSession.getMapper(MissionsMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int publisher_id = Integer.parseInt(request.getParameter("publisher_id"));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/selectMissionsByPublisherId【" + string_date + "】收到publisher_id:" + publisher_id);
        //        调用Mapper接口
        List<Mission> missions = missionsMapper.selectMissionsByPublisherId(publisher_id);
        System.out.println(missions);

        //        关闭mysql会话
        sqlSession.close();

//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (missions != null) {
//            查询有结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("查询有结果");
            returnData.setData(missions);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            查询无结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("查询无结果");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
