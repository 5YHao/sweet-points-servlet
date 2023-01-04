package localhost.util;

import java.io.File;

public class ClearFiles {
    public static void deleteFile(String filePath)
    {
        File file = new File(filePath);
        if (file.isFile())  //判断是否为文件，是，则删除
        {
            file.delete();
        }
        else //不为文件，则为文件夹
        {
            String[] childFilePath = file.list();//获取文件夹下所有文件相对路径
            for (String path:childFilePath)
            {
                deleteFile(file.getAbsoluteFile()+"/"+path);//递归，对每个都进行判断
            }
            //file.delete(); // 如果不保留文件夹本身 则执行此行代码
        }
    }

}

