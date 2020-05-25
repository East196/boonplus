package com.github.east196.lab.javacn;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
 
public class Sogou {
 
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\test\\搜狗标准词库.scel") ;
        FileInputStream input = new FileInputStream(file);
        byte[] str = new byte[128];
        Map<Integer , String> pyDic = new HashMap<Integer,String>() ;
        List<WordLibrary> pyAndWord = new ArrayList<WordLibrary>();
         
         
        byte[] num;
        int hzPosition = 0;
        input.read(str, 0, 128) ;
         
        if (str[4] == 0x44)
        {
            hzPosition = 0x2628;
        }
        if (str[4] == 0x45)
        {
            hzPosition = 0x26C4;
        }
        /********************字库名称开始**************************/
        input.getChannel().position(0x130);
        int lenc = input.read(str, 0, 128);
        System.out.println("字库名称:" + toString(str,lenc));
         
        /********************字库名称结束*************************/
 
 
        /********************字库类别开始**************************/
        input.getChannel().position(0x338);
        lenc = input.read(str, 0, 128);
        System.out.println("字库类别:" + toString(str,lenc));
         
        /********************字库类别结束*************************/
         
        /********************字库信息开始**************************/
        input.getChannel().position(0x540);
        lenc = input.read(str, 0, 128);
        System.out.println("字库信息:" + toString(str,lenc));
         
        /********************字库信息结束*************************/
         
        /********************字库示例开始**************************/
        input.getChannel().position(0xd40);
        lenc = input.read(str, 0, 128);
        System.out.println("字库示例:" + toString(str,lenc));
         
        /********************字库示例结束*************************/
         
        input.getChannel().position(0x1540) ;
        str = new byte[4];
        input.read(str, 0, 4);
 
        while (true)
        {
            num = new byte[4];
            input.read(num, 0, 4);
            int mark = num[0] + num[1]*256;
            str = new byte[128];
            if(num[2]>0){
                input.read(str, 0, (num[2]));
                String py = new String(str,0,num[2]);
                py = py.replaceAll("\0", "");
                pyDic.put(mark, py);
                if (py == "zuo") //最后一个拼音
                {
                    break;
                }
            }else{
                break ;
            }
        }
        input.getChannel().position(hzPosition);
        int i = 0, count = 0, samePYcount = 0;
        while (true)
        {
            num = new byte[4];
            input.read(num, 0, 4);
            samePYcount = num[0] + num[1]*256;
            count = num[2] + num[3]*256;
            //接下来读拼音
            str = new byte[256];
            for (i = 0; i < count; i++)
            {
                str[i] = (byte) input.read();
            }
            String wordPY = "";
            for (i = 0; i < count/2; i++)
            {
                int key = str[i*2] + str[i*2 + 1]*256;
                wordPY += pyDic.get(key) + " ";
            }
            //接下来读词语
            for (int s = 0; s < samePYcount; s++) //同音词，使用前面相同的拼音
            {
                num = new byte[2];
                input.read(num, 0, 2);
                int hzBytecount = num[0] + num[1]*256;
                str = new byte[hzBytecount];
                int len = input.read(str, 0, hzBytecount);
                String word = toString(str , len);
                pyAndWord.add(new WordLibrary(word , wordPY));
                byte[] temp = new byte[12];
                for (i = 0; i < 12; i++)
                {
                    temp[i] = (byte) input.read();
                }
            }
            if (file.length() == input.getChannel().position()) //判断文件结束
            {
                input.close();
                break;
            }
        }
        for (WordLibrary w : pyAndWord)
        {
            System.out.println(w.getWord()+" PY:"+w.getPinYinString());
        }
    }
    /**
     * 
     * @param data
     * @return
     */
    public static String toString(byte[] data , int len){
        StringBuffer strb = new StringBuffer() ;
        for(int ix=0 ; ix<len ; ix+=2){
            if(data[ix+1]<=0 && data[ix]<=0){
                continue ;
            }
            int d = data[ix]+data[ix+1]*256 ;
            if(data[ix]<=0){
                d = d+256 ;
            }
            if(d!=0){
                strb.append((char)d);
            }
        }
        return strb.toString() ;
    }
}
class WordLibrary
{
    private int count = 1;
    private String[] pinYin;
    private String pinYinString = "";
    private String word;
    public WordLibrary(String word , String pinYinString){
        this.word = word ;
        this.pinYinString = pinYinString ;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String[] getPinYin() {
        return pinYin;
    }
    public void setPinYin(String[] pinYin) {
        this.pinYin = pinYin;
    }
    public String getPinYinString() {
        return pinYinString;
    }
    public void setPinYinString(String pinYinString) {
        this.pinYinString = pinYinString;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
 
}