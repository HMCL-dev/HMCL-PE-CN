package cosine.boat;
import java.util.HashMap;
import com.google.gson.*;
import com.google.gson.JsonSyntaxException;
import java.io.UnsupportedEncodingException;
import android.util.Log;
import java.io.FileInputStream;
import org.json.JSONObject;
import android.widget.Toast;


public class LauncherConfig extends HashMap<String, String>
{

	
	public static void toFile(String filePath, LauncherConfig config){
		try
		{
			//Log.i("Launcher", "Trying to save config to file.");
			Utils.writeFile(filePath, new Gson().toJson(config, LauncherConfig.class));

		}
		catch (JsonSyntaxException e)
		{
			e.printStackTrace();
		}
		
		
	}
	public static LauncherConfig fromFile(String filePath){
		try
		{
			return new Gson().fromJson(new String(Utils.readFile(filePath), "UTF-8"), LauncherConfig.class);
			
		}
		catch (JsonSyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public String get(String key){
		String value;

		if (super.get(key) == null){
			value = "";
			Log.w("Boat", "Value required can not found: key=" + key);
		}
		else{
			value = super.get(key);
		}
		return value;
	}
	public static String privateDir() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("allVerLoad");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "false";
    }
	
	public static String loadgl() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("openGL");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "libGL112.so.1";
    }

	public static String api() {
		try {
			FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
			byte[] b=new byte[in.available()];
			in.read(b);
			in.close();
			String str=new String(b);
			JSONObject json=new JSONObject(str);
			return json.getString("LoginApi");
		} catch (Exception e) {
			System.out.println(e);
		}
		return "Microsoft";
	}

}
