package util;

import java.util.UUID;

/**
 * 产生UUID随机字符串工具类，当作激活码
 */
public final class UuidUtil {
	private UuidUtil(){}
	public static String getUuid(){
		// UUID.randomUUID() 可生成一个唯一的字符串（因为和电脑硬件信息和时间的毫秒值相关）
		return UUID.randomUUID().toString().replace("-","");
	}
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(UuidUtil.getUuid());
		System.out.println(UuidUtil.getUuid());
		System.out.println(UuidUtil.getUuid());
		System.out.println(UuidUtil.getUuid());
	}
}
