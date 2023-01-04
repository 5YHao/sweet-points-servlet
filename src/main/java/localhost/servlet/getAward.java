package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.GetAwardRecordMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.Award;
import localhost.pojo.GetAwardRecord;
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

@WebServlet("/getAward")
public class getAward extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
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

        GetAwardRecordMapper getAwardRecordMapper = sqlSession.getMapper(GetAwardRecordMapper.class);
        AwardsMapper awardsMapper = sqlSession.getMapper(AwardsMapper.class);
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int award_id = Integer.parseInt(request.getParameter("award_id"));
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        int status = 0;

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());
        String get_datetime = string_date;

        GetAwardRecord getAwardRecord = new GetAwardRecord();
        getAwardRecord.setAward_id(award_id);
        getAwardRecord.setUser_id(user_id);
        getAwardRecord.setStatus(status);
        getAwardRecord.setGet_datetime(get_datetime);

        System.out.println("/getAward【" + string_date + "】收到award_id:" + award_id + ",user_id:" + user_id + "," +
                "status:" + status + ",get_datetime:" + get_datetime);
        //        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        //        调用Mapper接口
        Award award = awardsMapper.selectAwardInfoById(award_id);
        Integer userPoints = usersMapper.selectUserHaveHowManyPointsById(user_id);
        if (userPoints!=null && award!=null){
            if (userPoints >= award.getPrice()) {
                //可以兑换
                Integer getAwardResult = getAwardRecordMapper.getAward(getAwardRecord);
                Integer decreasePointsByIdResult = usersMapper.decreasePointsById(user_id, award.getPrice());
                System.out.println("getAwardResult:" + getAwardResult);
                System.out.println("decreasePointsByIdResult:" + decreasePointsByIdResult);
                if (getAwardResult > 0 && decreasePointsByIdResult > 0) {
                    //提交事务
                    sqlSession.commit();
                    ReturnData returnData = new ReturnData();
                    returnData.setErr_code(0);
                    returnData.setMsg("兑换成功");
                    returnData.setData("getAwardResult:" + getAwardResult + ";decreasePointsByIdResult:" + decreasePointsByIdResult);
                    writer.write(JSON.toJSONString(returnData));

                }else{
                    sqlSession.rollback();
                    ReturnData returnData = new ReturnData();
                    returnData.setErr_code(1);
                    returnData.setMsg("兑换失败");
                    returnData.setData("getAwardResult:" + getAwardResult + ";decreasePointsByIdResult:" + decreasePointsByIdResult);
                    writer.write(JSON.toJSONString(returnData));
                }

            } else {
                //不可以兑换
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(2);
                returnData.setMsg("您的积分不足");
                returnData.setData("所需积分:" + award.getPrice() + ";用户积分:" + userPoints);
                writer.write(JSON.toJSONString(returnData));
            }

        }else{
            //不可以兑换
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(2);
            returnData.setMsg("查询出错");
            returnData.setData("所需积分:" + award + ";用户积分:" + userPoints);
            writer.write(JSON.toJSONString(returnData));
        }


        //关闭mysql会话
        sqlSession.close();

    }

}
