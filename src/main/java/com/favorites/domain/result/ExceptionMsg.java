
package com.favorites.domain.result;

public enum ExceptionMsg {
	SUCCESS("000000", "操作成功"),
	FAILED("999999","操作失败"),
    ParamError("000001", "参数错误！"),
    TokenTimeOut("000002","登陆超时"),
    
    LoginNameOrPassWordError("000100", "用户名或者密码错误！"),
    EmailUsed("000101","该邮箱已被注册"),
    UserNameUsed("000102","该登陆名称已存在"),
    EmailNotRegister("000103","该邮箱地址未注册"), 
    LinkOutdated("000104","该链接已过期，请重新请求"),
    PassWordError("000105","密码输入错误"),
    UserNotExist("000106","用户不存在"),
    OldPassWordWrong("000107","原始密码错误"),
    UserNameLengthLimit("000108","用户名长度超限"),
    UserNameSame("000109","新用户名与原用户名一致"),
    UserConfigNotExist("000110","用户配置不存在"),
    UserConfigTypeNotExist("000111","用户配置类型不存在"),

    FavoritesNameIsNull("000200","收藏夹名称不能为空"),
    FavoritesNameUsed("000201","收藏夹名称已被创建"),
    FavoritesNotExist("000202","收藏夹不存在"),
    FavoritesNotUsers("000203","收藏夹不为该用户所有"),
    
    CollectExist("000300","该文章已被收藏"),
    
    FileEmpty("000400","上传文件为空"),
    LimitPictureSize("000401","图片大小必须小于2M"),
    LimitPictureType("000402","图片格式必须为'jpg'、'png'、'jpge'、'gif'、'bmp'")
    ;
   private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;
    
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}

    
}

