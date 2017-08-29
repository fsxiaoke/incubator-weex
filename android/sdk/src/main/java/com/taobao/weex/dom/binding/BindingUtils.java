/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.taobao.weex.dom.binding;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.el.parse.Block;
import com.taobao.weex.el.parse.Parser;

/**
 * util's for binding and statment
 * Created by jianbai.gbj on 2017/8/17.
 */
public class BindingUtils {

    public static final String BINDING = "@binding";

    /**
     * @param value check object is binding expression
     * */
    public static boolean isBinding(Object value){
        if(value instanceof JSONObject){
            JSONObject  object = (JSONObject) value;
            if(object.containsKey(BINDING)){
                return  true;
            }
        }else if(value instanceof JSONArray){
            JSONArray array = (JSONArray) value;
            for(int i=0; i<array.size(); i++){
                if(isBinding(array.get(i))){
                    return  true;
                }
            }
        }
        return  false;
    }

    /**
     * parse binding code to block, enable fast execute
     * */
    public static Object bindingBlock(Object value){
        if(value instanceof JSONObject){
            JSONObject  object = (JSONObject) value;
            if(object.containsKey(BINDING)){
                Object binding = object.get(BINDING);
                if(!(binding instanceof Block)){
                    object.put(BINDING, Parser.parse(binding.toString()));
                }
            }
        }else if(value instanceof JSONArray){
            JSONArray array = (JSONArray) value;
            for(int i=0; i<array.size(); i++){
                bindingBlock(array.get(i));
            }
        }
        return  value;
    }

    public static boolean isVif(String name){
        return WXStatement.WX_IF.equals(name);
    }

    public static boolean isVfor(String name){
        return WXStatement.WX_FOR.equals(name);
    }

    public static Block vifBlock(String code){
        return Parser.parse(code);
    }

    public static Object vforBlock(Object vfor){
        if(vfor instanceof  JSONObject){
            if(((JSONObject) vfor).containsKey(WXStatement.WX_FOR_LIST)){
                Object list = ((JSONObject) vfor).get(WXStatement.WX_FOR_LIST);
                if(!(list instanceof Block)){
                    ((JSONObject) vfor).put(WXStatement.WX_FOR_LIST, Parser.parse(list.toString()));
                }
            }
        }
        return vfor;
    }


}