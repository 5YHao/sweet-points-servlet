package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.CouplesMapper;
import localhost.pojo.Award;
import localhost.pojo.Couple;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/selectCoupleAwardsByUserId")
public class selectCoupleAwardsByUserId extends HttpServlet {
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
//        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
        CouplesMapper coupleMapper = sqlSession.getMapper(CouplesMapper.class);
        AwardsMapper awardsMapper = sqlSession.getMapper(AwardsMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int user_id = Integer.parseInt(request.getParameter("user_id"));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/selectCoupleInfoByUserId【" + string_date + "】收到user_id:" + user_id);
        //        调用Mapper接口
        Couple coupleInfo = coupleMapper.selectCoupleInfoByUserId(user_id);
        System.out.println(coupleInfo);


//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (coupleInfo != null) {
//            查询有结果
            List<Award> both_awards = awardsMapper.selectAwardsByPublisherId(coupleInfo.getBoy_id());
            both_awards.addAll(awardsMapper.selectAwardsByPublisherId(coupleInfo.getGirl_id()));

            if (both_awards.size()>0){
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(0);
                returnData.setMsg("查询有结果");
                returnData.setData(both_awards);
                writer.write(JSON.toJSONString(returnData));
            }else{
//            查询无结果
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(2);
                returnData.setMsg("该情侣双方未发布过商品");
                returnData.setData(null);
                writer.write(JSON.toJSONString(returnData));
            }


        } else {
//            查询无结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("该用户未绑定对象");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }

        //        关闭mysql会话
        sqlSession.close();
    }



}
