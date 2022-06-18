package me.mucloud.plugin.MK.Spawn.i18nModule;

public enum Messages {

    PLUGIN_INFO,
    PLUGIN_LAUNCHING,
    PLUGIN_SHUTDOWN,
    Teleport_Finished,
    Teleporting,
    Teleport_Permission_Denied,
    Teleport_Money_Denied,
    Teleport_Item_Denied,
    Teleport_EXP_Denied,
    Teleport_Achievement_Denied,
    SpawnPoint_Selected,
    SpawnPoint_Deleted,
    SpawnPoint_Teleported,
    SpawnPoint_Select_UnSafe,
    SpawnPoint_Lacked,
    Sign_Created,
    Sign_Invalid;

    private static String Message;

    public void setMessage(String message){
        Message = message;
    }

    public String getMessage(){
        return Message;
    }
}
