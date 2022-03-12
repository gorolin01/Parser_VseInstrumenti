package sample;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Border;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;

//https://www.avito.ru/sevastopol/remont_i_stroitelstvo/instrumenty-ASgBAgICAURYoks?f=ASgBAgICA0RYokuyww3m3jnKww3q3zk

public class Main {

    private static String SiteName = "Resanta";
    private static String mainUrl = "https://simferopol.resantagroup.ru/tortsovochnaya-pila-vikhr-pt-210/";
    private static int fromPage = 1;
    private static int toPage = 1;
    private static int TotalLines;

    public static void main(String[] args) {
        try {
            parser(mainUrl, fromPage, toPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parser(String mainUrl, int fromPage, int toPage) throws IOException {

        Excel excel = new Excel();
        excel.createExcel();

        TotalLines = 0;

        for(int j=fromPage;j<=toPage;j++){

            //System.out.println("Connect to "+j+" page...");
            //Connection connect = Jsoup.connect(mainUrl+"&p="+j)
            System.out.println("Connect to page...");
            Connection connect = Jsoup.connect(mainUrl)
                    .userAgent("Mozilla");
            boolean connected=false;
            Document doc=null;
            while(!connected){
                try{
                    doc = connect.get();
                    connected=true;
                }catch(Exception ex){

                }finally{
                    System.out.println("connected: "+connected);
                    if(!connected){
                        try{
                            Thread.sleep(1000);
                        }catch(Exception ex){

                        }
                    }
                }
            }
            System.out.println("Ok!");
            Elements select;

            select=doc.select(".product_features");

            ///*RKV*/System.out.println("RKV: " + select);

            /*String[] arrImgMiddleUrl=new String[select.size()];
            for(int i=0;i<select.size();i++){
                Element element=select.get(i);
                Elements photoWrapper=element.select(".iva-item-sliderLink-bJ9Pv");
                arrImgMiddleUrl[i]=photoWrapper.attr("href");
            }*/

            select=doc.select(".product_features");

            /*RKV*/System.out.println("RKV: " + select.size());

            for(int i=0;i<select.size();i++) {
                /*String name;
                String adsUrl;
                String prise;
                String what = "";
                String city = "";
                String date;
                String imgMiddleUrl;

                *//*imgMiddleUrl = arrImgMiddleUrl[i];*//*
                Element element = select.get(i);
                Elements select2 = element.select(".iva-item-titleStep-_CxvN");
                Elements title = select2.select("a");
                name = title.attr("title");
                adsUrl = "https://www.avito.ru" + title.attr("href");
                prise = element.select(".price-text-E1Y7h").text();

                //Element element2=element.get(i);
                Elements data = element.select(".iva-item-description-S2pXQ");
                Elements p = data.select("p");
                Element singleP;
                if (p.size() > 0) {
                    singleP = p.get(0);
                    what = singleP.text();
                }
                if (p.size() > 1) {
                    singleP = p.get(1);
                    city = singleP.text();
                }
                Elements time = data.select(".date");
                date = time.text();*/

                /*RKV*/
                Date date = new Date();
                new File("parsing_" + SiteName).mkdir();
                String [] URLPage = new String[3]; //тут указываем ссылки на страници для парсинга
                URLPage = new String[]{"https://simferopol.resantagroup.ru/777/", "https://simferopol.resantagroup.ru/pylesos-stroitelnyy-ps-150020-resanta/", "https://simferopol.resantagroup.ru/tortsovochnaya-pila-vikhr-pt-210/"};

                for(int w = 0; w < URLPage.length; w++) {
                    new File("parsing_" + SiteName + "/" + doc.select(".product_name").text()).mkdirs();

                    select = doc.select(".product_features");
                    for (int q = 0; q < select.select("tr").size(); q++) {
                        excel.setCell(q, 1, select.select("tr").get(q).select("span").text());
                        excel.setCell(q, 2, select.select("tr").get(q).select(".product_features-value").text());
                    }
                }

                System.out.println(URLPage.length);


                //запись в файл
                /*excel.setCell(TotalLines + i, 1, specifications);
                excel.setCell(TotalLines + i, 2, adsUrl);
                excel.setCell(TotalLines + i, 3, prise);
                excel.setCell(TotalLines + i, 4, what);
                excel.setCell(TotalLines + i, 5, city);
                excel.setCell(TotalLines + i, 6, date);
                excel.setCell(TotalLines + i, 7, imgMiddleUrl);*/

            }

            TotalLines += select.size();

        }

        excel.Build("AvitoOutFile.xlsx");

    }

}
