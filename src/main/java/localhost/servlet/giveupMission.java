package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.GetMissionRecordMapper;
import localhost.pojo.GetMissionRecord;
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

@WebServlet("/giveupMission")
public class giveupMission extends HttpServlet {
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

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        String id = request.getParameter("id");
        int status = 0;

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());
        String get_datetime = string_date;


        System.out.println("/giveupMission【" + string_date + "】收到id:" + id);
        //        调用Mapper接口
        Integer result = getMissionRecordMapper.deleteGetMissionRecordById(Integer.parseInt(id));
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
            returnData.setMsg("放弃任务成功");
            returnData.setData(result);
            writer.write(JSON.toJSONString(returnData));

        } else {
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("放弃任务失败");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
