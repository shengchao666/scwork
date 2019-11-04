package hsdx.com.wjcxapp.Util;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    /**
     * JsonArray 转为可用于adapter的list
     *
     * @param array JsonArray
     * @return List<Map                                                                                                                               <                                                                                                                               String                                                                                                                               ,                                                                                                                                                                                                                                                               String>>
     * @throws Exception jsonException
     */
    public List<Map<String, String>> jsonArray2ListMap(JSONArray array) throws Exception {

        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject it = array.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            Iterator<String> iterator = it.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, it.optString(key));
            }
            list.add(map);
        }
        //去掉重复的信息
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).get("UUID") != null && list.get(i).get("UUID") != null){
                    if ((list.get(j).get("UUID")).equals(list.get(i).get("UUID"))) {
                        list.remove(j);
                    }
                }
            }
        }
        //集合标量
        int index = 0;
        //修改集合，加序号
        for (Map<String, String> map : list) {
            map.put("NUM", String.valueOf(index + 1));
            list.set(index, map);
            index++;
        }
        /*
        //去掉重复的信息
        HashSet<Map<String, String>> hashSet = new HashSet<>(list);
        list.clear();
        list.addAll(hashSet);
        */
        return list;
    }

    public List<Map<String, String>> jsonArray2ListMap(JSONArray array,int loadtotal) throws Exception {

        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject it = array.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            Iterator<String> iterator = it.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, it.optString(key));
            }
            list.add(map);
        }
        //去掉重复的信息
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).get("UUID") != null && list.get(i).get("UUID") != null){
                    if ((list.get(j).get("UUID")).equals(list.get(i).get("UUID"))) {
                        list.remove(j);
                    }
                }
            }
        }
        //集合标量
        int index = 0;
        //修改集合，加序号
        for (Map<String, String> map : list) {
            map.put("NUM", String.valueOf(loadtotal + index + 1));
            list.set(index, map);
            index++;
        }
        return list;
    }

    /**
     * 特殊转化，只有特定格式可用(该数组只包含1个JsonObject时)
     * 将JsonArray转化为JsonObject
     * 例如：[{aaa:dasdas,bbb:sadsfd}]
     *
     * @param jsonArray jsonArray
     * @return jsonObject
     */
    public JSONObject JsonArray2JsonObject(JSONArray jsonArray) {
        if (jsonArray == null) return null;
        String ress = jsonArray.toString().substring(1, jsonArray.toString().length());
        String res = ress.substring(0, ress.length() - 1);
        try {
            return new JSONObject(res);
        } catch (Exception e) {
            return null;
        }
    }

    public Bundle json2bundle(JSONObject jsonObject) {
        Bundle bundle = new Bundle();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                bundle.putString(key, jsonObject.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }

    public JSONObject getObjectByValue(JSONArray jsonArray, String key, String value) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.get(key).equals(value)) {
                    return jsonObject;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * JsonArray 转为可用于下拉框的list
     *
     * @param jsonArray
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public ArrayList<SpinnerOption2> JsonArray2ListSpinnerOption2(JSONArray jsonArray, String key, String value) throws Exception {
        ArrayList<SpinnerOption2> list = new ArrayList<>();
        SpinnerOption2 so1 = new SpinnerOption2("", "请选择");
        list.add(so1);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            SpinnerOption2 so2 = new SpinnerOption2(obj.optString(key), obj.optString(value));
            list.add(so2);
        }
        return list;
    }

    public ArrayList<SpinnerOption3> JsonArray2ListSpinnerOption3(JSONArray jsonArray, String key, String value,String parmThree) throws Exception {
        ArrayList<SpinnerOption3> list = new ArrayList<>();
        SpinnerOption3 so1 = new SpinnerOption3("", "请选择","");
        list.add(so1);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            SpinnerOption3 so2 = new SpinnerOption3(obj.optString(key), obj.optString(value),obj.optString(parmThree));
            list.add(so2);
        }
        return list;
    }

}
