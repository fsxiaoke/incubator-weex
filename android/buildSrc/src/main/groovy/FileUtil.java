/*
 * Copyright (C) 2015 HouKx <hkx.aidream@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 *
 * @author chowjee
 */
public class FileUtil {


    public static String getFileMD5(InputStream in) {
        byte buffer[] = new byte[1024];
        String ret=null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            int len;
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }

            ret= FileUtil.asHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {

        } catch (IOException e) {

        }
        return ret;
    }

    /**
     *
     * @param buf
     * @return
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)//
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    /**
     *
     * @param file
     * @return String
     */
    public static String getFileStr(File file) {
        String error = "";
        FileInputStream inStream=null;
        ByteArrayOutputStream bos=null;
        try {
            //FileInputStream
            inStream=new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int length=-1;
            while( (length = inStream.read(buffer)) != -1) {
                bos.write(buffer,0,length);
                // Writes count bytes from the byte array buffer starting at offset index to this stream.
                //
            }
            error= bos.toString();
            error = new String(bos.toByteArray(),"UTF-8");
            //
            // return new String(bos.toByteArray(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inStream!=null){
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return error;
    }

    /**
     *
     * @param data byte[]
     * @param target
     */
    public static void writeToFile(byte[] data, File target) throws IOException {
        FileOutputStream fo = null;
        try {
            ReadableByteChannel src = Channels.newChannel(new ByteArrayInputStream(data));
            fo = new FileOutputStream(target);
            FileChannel out = fo.getChannel();
            out.transferFrom(src, 0, data.length);
        } finally {
            if (fo != null) {
                fo.close();
            }
        }
    }

    public static boolean deleteFileOrDir(String fileName) {
        File path= new File(fileName);
        if (!path.exists()) {
            return true;
        }
        if (path.isFile()) {
            return path.delete();
        }
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFileOrDir(file.getAbsolutePath());
            }
        }
        return path.delete();
    }

    public static boolean fileExist(String fileName){
        File file = new File(fileName);
        return file.exists();
    }
}
