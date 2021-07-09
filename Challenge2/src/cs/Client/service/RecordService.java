package cs.Client.service;

import cs.Client.domain.Message_Client;
import cs.Client.domain.Message_Record;
import util.DataBaseCon.JDBC_util;
import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class RecordService {


    // 设计一个方法  通过时间获取数据库中二进制的 图片 ,并写到指定路径上 返回下载的路径

    public static String getAndDownloadImg(Message_Client message_client) {

        String[] value = message_client.getPath().split("\\\\");
        String ImgNameAll = value[value.length - 1];
        String ImgName = ImgNameAll.substring(0, ImgNameAll.length() - 4);
        String path = "src\\downLoadFile\\"+ ImgName +".jpg";
        /*String testPath = "src\\downLoadFile\\" + ImgNameAll;*/

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;
        try{

            String senderTableName = "chat_"+message_client.getSender();
            // 获得连接
            connection = JDBC_util.getConnection();
            // 写SQL语句
            String sql = "select * from "+senderTableName+" where date=?";
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 写参数
            preparedStatement.setString(1,message_client.getDate());
            // 执行
            resultset = preparedStatement.executeQuery();
            byte[] b = new byte[10240 * 10];
            while (resultset.next()) {

                //获取photo字段的图片数据
                InputStream in=resultset.getBinaryStream("imgOrFile");
                //将数据存储在字节数组b中
                int count = in.read(b);
                //从数据库获取图片保存的位置
                File f=new File(path);
                FileOutputStream out=new FileOutputStream(f);
                while (count != -1) {
                    out.write(b, 0, count);
                    count = in.read(b);
                }
                out.close();
                System.out.println("成功下载图片");





/*                // 获得Blob对象
                Blob file = resultset.getBlob(5);
                // 每2048bytes地读
                byte[] bs = new byte[10240*10];
                InputStream in = file.getBinaryStream();
                int count = in.read(bs);
                // 接完之后，需要从内存（小推车bs）-->硬盘（文件） 而不需要网络传输，所以直接new字节型文件输出流；读到小推车，然后写出去
                // 先将文档下载到downLoadFile包中
                // .jpg 强制设定图片类型这样行么。。。。。。

                File newFile = new File(path);
                FileOutputStream fileOut = new FileOutputStream(newFile);
                while (count != -1) {
                    fileOut.write(bs, 0, count);
                    fileOut.flush();
                    count = in.read(bs);
                }*/
                //关闭
/*                fileOut.close();   //注意管道流的close写法
                in.close();*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            JDBC_util.release(connection,preparedStatement,resultset);
        }
        return path;
    }

    // 设计一个方法 将 date 和 downloadPath 写入 txt

    public static void writeDateAndDownloadPath(String date, String downloadPath) {

        FileWriter fw = null;
        BufferedWriter bw = null;
        if (downloadPath != null) {
            try {
                File file = new File("src//dbfile//dateToDownload.txt");
                fw = new FileWriter(file, true);
                bw = new BufferedWriter(fw);
                StringBuilder builder = new StringBuilder(date);
                builder.append("#");
                builder.append(downloadPath);
                // 字符型高级流   三连击
                bw.write(builder.toString());
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }




    // 设计一个方法  将dateToDownload.txt的内容读到缓存中

    public static HashMap<String,String> readToMap() {

        // 建一个map最后返回去，代替那个静态属性
        HashMap<String, String> dateToPath_File = new HashMap<>();

        FileReader fr = null;
        BufferedReader br = null;
        try {
            File file = new File("src//dbfile//dateToDownload.txt");
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String value = br.readLine();
            while (value != null) {
                String date = value.split("#")[0];
                String downloadPath = value.split("#")[1];
                dateToPath_File.put(date, downloadPath);
                value = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dateToPath_File;
    }
}
