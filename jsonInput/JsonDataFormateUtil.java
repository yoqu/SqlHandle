package com.y3technologies.dms.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openbravo.base.structure.BaseOBObject;

/**
 * this class is entity list convert JSON list .
 * 
 * @author Jiang Weichuan
 *
 * @param <T>
 */
public class JsonDataFormateUtil<T extends BaseOBObject>
{
    
    private static final SimpleDateFormat DATEFORMATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * though entity change to json object list (Key value)
     * 
     * @param t
     * @param classentity
     * @return
     * @throws JSONException
     * @throws ClassNotFoundException
     */
    public static <T extends BaseOBObject> JSONArray getObjectList(List<T> t, @SuppressWarnings("rawtypes") Class classentity) throws JSONException,
            ClassNotFoundException
    {
        JSONArray allarray = new JSONArray();
        @SuppressWarnings("unused")
        Method[] methods = classentity.getDeclaredMethods();
        for (T entity : t)
        {
            allarray.put(getObjectEntity(entity, classentity));
        }
        return allarray;
    }

    /**
     * convert Openbravo entity to Our entity JSONOBject
     * 
     * @param t
     *            Openbravo entity
     * @param classEntity
     *            Our Entity class
     * @return return jsonObject entity.
     * @throws JSONException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends BaseOBObject> JSONObject getObjectEntity(T t, Class classEntity) throws JSONException, ClassNotFoundException
    {
        Method[] methods = classEntity.getDeclaredMethods();
        JSONObject item = new JSONObject();
        //allarray.put(array);           
        for (int j = 0; j < methods.length; j++)
        {
            if (methods[j].getName().startsWith("get"))
            {
                //get property name by method.
                String property = EntityDataFormateUtil.toLowerCaseFirstOne(methods[j].getName().replace("get", ""));
                //invoke set data to entity.
                item.put(property, t.get(property));
                if (t.get(property) != null)
                {
                    if (isJavaClass(t.get(property).getClass()))
                    {
                        if (t.get(property) instanceof Date)
                        {
                            
                            item.put(property, DATEFORMATE.format(t.get(property)));
                        }
                        else
                        {
                            item.put(property, t.get(property));
                        }
                    }
                    else
                    {
                        if (List.class.isAssignableFrom(t.get(property).getClass()))
                        {
                            //get the generics name.
                            String entityname = methods[j].toGenericString();
                            String classname = entityname.substring(entityname.indexOf("<") + 1, entityname.indexOf(">"));
                            //Get our define entity class.
                            Class classmodel = Class.forName(classname);
                            //get list value.
                            item.put(property, getObjectList((List) t.get(property), classmodel));
                        }
                        else
                        {
                            String entityname = t.get(property).getClass().getName();
                            Class classmodel = Class.forName(entityname.substring(0, entityname.indexOf("_")) + "Entity");
                            item.put(property, getObjectEntity((T) t.get(property), classmodel));
                        }
                    }
                }
                else
                {
                    item.put(property, "");
                }

            }
        }
        return item;
    }

    /**
     * if java basic type return true,user defined return false
     * 
     * @param clz
     * @return
     */
    public static boolean isJavaClass(Class<?> clz)
    {
        return clz != null && clz.getClassLoader() == null;
    }
}
