package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.Award;
import localhost.pojo.ReturnData;
import localhost.pojo.User;
import localhost.util.ClearFiles;
import localhost.util.Dev;
import localhost.util.SqlSessionFactoryUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebServlet("/updateUserInfoById")
public class updateUserInfoById extends HttpServlet {
    private static String avatar_url = "";
    private static List<FileItem> fileItems;
    private static User user = new User();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            //        加载mybatis核心配置文件，获取SqlSessionFactory对象
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

            //        获取SqlSession对象
            SqlSession sqlSession = sqlSessionFactory.openSession();
            //        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
            UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);


//        解决跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();

            // 判断上传的文件普通表单还是带文件的表单
            if (!ServletFileUpload.isMultipartContent(request)) {
                return;//终止方法运行,说明这是一个普通的表单,直接返回
            }
            //创建上传文件的保存路径,建议在WEB-INF路径下,安全,用户无法直接访问上传的文件;
            String uploadPath = this.getServletContext().getRealPath("/upload/images/avatar");
            File uploadFile = new File(uploadPath);
            //判断目录是否存在，不存在则创建
            if (!uploadFile.exists()) {
                uploadFile.mkdir();
            }
            //若超过规定大小则放在临时文件
            //创建保存临时文件目录
            String tmpPath = this.getServletContext().getRealPath("/upload/tmp");
            File file = new File(tmpPath);
            //判断目录是否存在，不存在则创建
            if (!file.exists()) {
                file.mkdir();
            }
            /*
                处理上传的文件,一般都需要通过流来获取,
                我们可以使用 request.getInputstream(),原生态的文件上传流获取,十分麻烦
                但是我们都建议使用 Apache的文件上传组件来实现,
                common-fileupload,它需要commons-io组件;
             */
            // 1、创建DiskFileItemFactory对象，处理文件路径或者大小限制
            //     若超过规定大小则放在临时文件
            DiskFileItemFactory factory = getDiskFileItemFactory(file);
            // 2、获取ServletFileUpload  监听上传进度, 处理乱码等
            ServletFileUpload upload = getServletFileUpload(factory);
            // 3、处理上传文件
            // 把前端请求解析，封装成FileItem对象，需要从ServletFileUpload对象中获取
            String msg = uploadParseRequest(upload, request, uploadPath);

            // Servlet请求转发消息
            System.out.println(msg);
            String string_date = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            string_date = sdf.format(new Date());

            user.setAvatar_url(avatar_url);
            System.out.println("/updateUserInfoById" + "【" + string_date + "】收到User:" + user.toString());
            Integer result = usersMapper.updateUserInfoById(user);
            System.out.println(result);

            if (msg == "文件上传成功!") {
                //文件上传成功，提交事务
                sqlSession.commit();
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(0);
                returnData.setMsg("上传成功");
                returnData.setData(user);
                writer.write(JSON.toJSONString(returnData));
            } else {
                //文件上传失败，回滚数据库记录
                sqlSession.rollback();
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(1);
                returnData.setMsg("头像上传失败");
                returnData.setData(null);
                writer.write(JSON.toJSONString(returnData));
            }
            //        关闭mysql会话
            sqlSession.close();

        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }


    //    ==================================以下为封装的方法=========================================
    public static DiskFileItemFactory getDiskFileItemFactory(File file) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 通过这个工厂设置一个缓冲区,当上传的文件大于这个缓冲区的时候,将他放到临时文件中;
        factory.setSizeThreshold(1024 * 1024);// 缓冲区大小为2M
        factory.setRepository(file);// 临时目录的保存目录,需要一个file
        return factory;
    }

    public static ServletFileUpload getServletFileUpload(DiskFileItemFactory factory) {
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 监听上传进度
        upload.setProgressListener(new ProgressListener() {

            // pBYtesRead:已读取到的文件大小enctype="multipart/form-data"
            // pContextLength:文件大小
            public void update(long pBytesRead, long pContentLength, int pItems) {
                System.out.println("总大小：" + pContentLength + "已上传：" + pBytesRead + "，进度：" + ((double) pBytesRead / pContentLength) * 100 + "%");
            }
        });

        // 处理乱码问题
        upload.setHeaderEncoding("UTF-8");
        // 设置单个文件的最大值
        upload.setFileSizeMax(1024 * 1024 * 10);
        // 设置总共能够上传文件的大小
        // 1024 = 1kb * 1024 = 1M * 10 = 10м

        return upload;
    }

    public static String uploadParseRequest(ServletFileUpload upload, HttpServletRequest request, String uploadPath) throws FileUploadException, IOException {

        String msg = "";

        // 把前端请求解析，封装成FileItem对象
        fileItems = upload.parseRequest(request);
        for (FileItem fileItem : fileItems) {
            if (fileItem.isFormField()) {// 判断上传的文件是普通的表单还是带文件的表单
                // getFieldName指的是前端表单控件的name;

                String name = fileItem.getFieldName();
                String value = fileItem.getString("UTF-8"); // 处理乱码
                if (name.equals("id")) {
                    user.setId(Integer.parseInt(value));
                }
                if (name.equals("username")) {
                    user.setUsername(value);
                }
                if (name.equals("gender")) {
                    user.setGender(value);
                }
                System.out.println(name + ": " + value);
            }
        }
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
//                如果fileItem不是普通表单（即是文件)
                // ============处理文件==============

                // 拿到文件路径
                String uploadFileName = fileItem.getName();
                System.out.println("上传的文件名: " + uploadFileName);
                if (uploadFileName.trim().equals("") || uploadFileName == null) {
                    continue;
                }

                // 获得上传的文件名
                String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("/") + 1);
                // 获得文件的后缀名
                String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);

                /*
                 * 如果文件后缀名fileExtName不是我们所需要的 就直按return.不处理,告诉用户文件类型不对。
                 */

                System.out.println("文件信息[文件名: " + fileName + " ---文件类型" + fileExtName + "]");

//                一个用户头像一个文件夹，不可多存，所以不用UUID建文件夹，而用id建
                // UUID. randomUUID(),随机生一个唯一识别的通用码;
//                String uuidPath = UUID.randomUUID().toString();

                // ================处理文件完毕==============

                // 存到哪? uploadPath
                // 文件真实存在的路径realPath
                String realPath = uploadPath + "/" + user.getId();
                System.out.println("realPath=" + realPath);
                avatar_url = "/upload/images/avatar/" + user.getId() + "/" + fileName;
                System.out.println(avatar_url);
                // 给文件创建一个名为id的文件夹
                File realPathFile = new File(realPath);
                if (!realPathFile.exists()) {
                    realPathFile.mkdir();
                }else{
                    ClearFiles.deleteFile(realPathFile.getPath());
                }
                // ==============存放地址完毕==============


                // 获得文件上传的流
                InputStream inputStream = fileItem.getInputStream();
                // 创建一个文件输出流
                FileOutputStream fos =
                        new FileOutputStream(realPathFile.getPath() + "/" + fileName);
                // 创建一个缓冲区
                byte[] buffer = new byte[1024 * 1024];
                // 判断是否读取完毕
                int len = 0;
                // 如果大于0说明还存在数据;
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                // 关闭流
                fos.close();
                inputStream.close();

                msg = "文件上传成功!";
                fileItem.delete(); // 上传成功,清除临时文件
                System.out.println("文件传输完成");
                //=============文件传输完成=============
            }
        }
        return msg;
    }
}
