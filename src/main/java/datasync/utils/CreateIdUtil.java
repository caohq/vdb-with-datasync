package datasync.utils;


import java.text.SimpleDateFormat;

public class CreateIdUtil {

    public static int Guid=100;

    public static String getId() {
        CreateIdUtil.Guid+=1;
        long now = System.currentTimeMillis();
        // 获取4位年份数字
         SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
        //   获取时间戳
        	String time=dateFormat.format(now);
         	String info=now+"";
        //   获取三位随机数
        //   int ran=(int) ((Math.random()*9+1)*100);
        //      要是一段时间内的数据连过大会有重复的情况，所以做以下修改
            int ran=0;
            if(CreateIdUtil.Guid>999){
                CreateIdUtil.Guid=100;
              }
         	ran=CreateIdUtil.Guid;
           // System.out.println(time+info.substring(2, info.length())+ran);
         	return time+info.substring(2, info.length())+ran;

    }

}
