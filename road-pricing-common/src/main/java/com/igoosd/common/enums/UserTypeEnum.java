package com.igoosd.common.enums;

/**
 * 2018/5/8.
 */
public enum  UserTypeEnum {

    ADMIN(0,"管理员",new String[]{"system","config","duty","operation","finance","main"}),
    FINA_STAFF(1,"财务人员",new String[]{"duty","operation","finance","main"});

    private int value;

    private String name;

    private String[] permissions;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    UserTypeEnum(int value, String name,String[] permissions){
        this.value = value;
        this.name = name;
        this.permissions =permissions;
    }


   public static UserTypeEnum getUserTypeEnumByVaule(Integer value){
        if(value != null){
            for (UserTypeEnum e : UserTypeEnum.values()){
                if(e.value == value){
                    return e;
                }
            }
        }
        return null;
    }

    public String[] getPermissions() {
        return permissions;
    }
}
