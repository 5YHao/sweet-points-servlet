package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.MissionsMapper;
import localhost.pojo.Award;
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

@WebServlet("/publishAward")
public class publishAward extends HttpServlet {

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

        AwardsMapper awardsMapper = sqlSession.getMapper(AwardsMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        String name = request.getParameter("name");
        int publisher_id = Integer.parseInt(request.getParameter("publisher_id"));
        int price = Integer.parseInt(request.getParameter("price"));
        String description = request.getParameter("description");

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());
        String publish_datetime = string_date;

        System.out.println("/publishMission【" + string_date + "】收到name:" + name+",publisher_id:"+publisher_id+",price:"+price+",description:"+description);
        //        调用Mapper接口
        Award award = new Award();
        award.setName(name);
        award.setPrice(price);
        award.setPublisher_id(publisher_id);
        award.setPublish_datetime(publish_datetime);
        award.setDescription(description);

        Integer result = awardsMapper.publishAward(award);

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
            returnData.setMsg("发布成功");
            returnData.setData(award.getId());
            writer.write(JSON.toJSONString(returnData));

        } else {
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("发布失败");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }

}
