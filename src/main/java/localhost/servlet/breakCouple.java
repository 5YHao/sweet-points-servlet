package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.*;
import localhost.pojo.*;
import localhost.util.SqlSessionFactoryUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.ognl.security.UserMethod;
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

@WebServlet("/breakCouple")
public class breakCouple extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
//        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
        CouplesMapper couplesMapper = sqlSession.getMapper(CouplesMapper.class);
        GetMissionRecordMapper getMissionRecordMapper = sqlSession.getMapper(GetMissionRecordMapper.class);
        GetAwardRecordMapper getAwardRecordMapper = sqlSession.getMapper(GetAwardRecordMapper.class);
        MissionsMapper missionsMapper = sqlSession.getMapper(MissionsMapper.class);
        AwardsMapper awardsMapper = sqlSession.getMapper(AwardsMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        //        接收前端数据
        Integer couple_id = Integer.parseInt(request.getParameter("couple_id"));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/breakCouple【" + string_date + "】收到couple_id:" + couple_id);
        //        调用Mapper接口
        Couple couple = couplesMapper.selectCoupleInfoById(couple_id);
        if (couple != null) {
            Integer[] results = new Integer[9];
            System.out.println(0);
            results[0] = couplesMapper.breakCouple(couple_id);
            sqlSession.commit();
            System.out.println(1);
            results[1] = getAwardRecordMapper.deleteGetAwardRecordByUserId(couple.getBoy_id());
            sqlSession.commit();
            System.out.println(2);
            results[2] = getAwardRecordMapper.deleteGetAwardRecordByUserId(couple.getGirl_id());
            sqlSession.commit();
            System.out.println(3);
            results[3] = getMissionRecordMapper.deleteGetMissionRecordByUserId(couple.getBoy_id());
            sqlSession.commit();
            System.out.println(4);
            results[4] = getMissionRecordMapper.deleteGetMissionRecordByUserId(couple.getGirl_id());
            sqlSession.commit();
            System.out.println(5);
            results[5] = missionsMapper.deleteMissionByUserId(couple.getBoy_id());
            sqlSession.commit();
            System.out.println(6);
            results[6] = missionsMapper.deleteMissionByUserId(couple.getGirl_id());
            sqlSession.commit();
            System.out.println(7);
            results[7] = awardsMapper.deleteAwardByUserId(couple.getBoy_id());
            sqlSession.commit();
            System.out.println(8);
            results[8] = awardsMapper.deleteAwardByUserId(couple.getGirl_id());
            sqlSession.commit();
            System.out.println(9);
            results[9] = usersMapper.clearPointsById(couple.getGirl_id());
            sqlSession.commit();
            System.out.println(10);
            results[10] = usersMapper.clearPointsById(couple.getGirl_id());
            sqlSession.commit();

            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("解绑成功");
            returnData.setData(results[0]);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            解绑失败
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("couple_id不存在");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }

        //关闭mysql会话
        sqlSession.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }
}
