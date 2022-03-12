package sample;

import java.io.*;
import java.net.URL;
import java.util.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewMain {

    private static String SiteName = "test"; //имя главного каталога
    //private static String mainUrl = "https://simferopol.resantagroup.ru"; //нужно для загрузки киртинок
    private static int fromPage = 1;
    private static int toPage = 1;
    private static int COUNT_IMG = 3;  //количество выгружаемых картинок

    public static void main(String[] args) {
        loadURLofPage("https://krasnodar.vseinstrumenti.ru/instrument/i-oborudovanie-po-vidam-rabot/i-oborudovanie-dlya-rabot-po-betonu/", 10);

        try {
            parser(fromPage, toPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document getDoc(String url) {

        Excel excel = new Excel();
        excel.createExcel();

        System.out.println("Connect to page...");
        Connection connect = Jsoup.connect(url)
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

        return doc;

    }

    public static ArrayList<String> getArrayStrOnFile(String pathname) {
        ArrayList<String> Data = new ArrayList<>();
        try {
            File file = new File(pathname);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();
            while (line != null) {
                Data.add(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Data;
    }

    public static String [] corrector(String title, String attribute) {
        String metric;
        String [] res = new String[2];

        if(title.contains(",")) {
            metric = title.substring(title.indexOf(",") + 2);
            title = title.substring(0, title.indexOf(","));
            attribute = attribute + " " + metric;
        }
        title = title + ":";

        res[0] = title;
        res[1] = attribute;

        return res;
    }

    private static void Download(String URL, String Name, String URLSave) throws Exception {

        try{
            String fileName = Name;
            String website = URL;

            System.out.println("Downloading File From: " + website);

            java.net.URL url = new URL(website);
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(URLSave + "/" + fileName);
            byte[] buffer = new byte[2048];

            int length = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                System.out.println("Buffer Read of length: " + length);
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }

    //запись в txt файл
    private static void writeOnTxt(ArrayList<String> data, String FileName) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FileName)));
            for (int i = 0; i < data.size(); i++) {
                writer.write(data.get(i) + "\r\n");
            }
            writer.flush();
        }   catch (IOException e) {
            e.printStackTrace();
        }

    }

    //выгружает ссылки на товар из каталогов
    public static void loadURLofPage(String URLCategory, int page){
        Elements select;
        String URL;

        ArrayList<String> data = new ArrayList<>();
        String MainURL = "https://krasnodar.vseinstrumenti.ru";

        for(int i = 1; i < page; i++){
            if(i > 1){
                URL = URLCategory + "/page" + page + "/#goods";
            }else{
                URL = URLCategory + "/#goods";
            }
            Document doc = getDoc(URL);

            //поиск ссылки на товары в html коде страницы
            select = doc.select(".listing-grid").select(".product-tile");
            for(int e = 0; e < select.size(); e++){
                data.add(MainURL + select.get(e).select(".image").select("a").attr("href"));
            }
        }
        
        writeOnTxt(data, "pageForParsing.txt");

    }

    public static void parser(int fromPage, int toPage) throws IOException, NullPointerException {

        Elements select;

        //Date date = new Date();
        new File("parsing_" + SiteName).mkdir();
        ArrayList<String> URLPage = getArrayStrOnFile("pageForParsing.txt"); //тут указываем ссылки на страници для парсинга

        for(int w = 0; w < URLPage.size(); w++) {

            Document doc = getDoc(URLPage.get(w));
            Excel excel = new Excel();
            excel.createExcel();
            int Row = 0;
            ArrayList<String> resList;

            //регулярным выражением нужно убрать знак "/" из названия!
            //создает подпапки с названием товара
            String nameFolder = doc.select(".content-heading").select("h1").text().replace("/", "-").replace("*", "x").replace(":", " ").replace("\"", "");

            new File("parsing_" + SiteName + "/" + nameFolder).mkdirs();

            //загрузка картинок(готово)
            for(int e = 0; e < doc.select(".listing-carousel").select(".zoom").size() && e < COUNT_IMG; e++){
                String s = doc.select(".listing-carousel").select(".zoom").get(e).attr("style");
                if(s.contains("/")){
                    try {
                        Download(s.substring(23, s.lastIndexOf("')")), s.substring(s.lastIndexOf("/")).replace("/", "").replace("')", ""), "parsing_" + SiteName + "/" + nameFolder);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            /*for(int z = 0; z < doc.select(".owl-stage").get(0).select(".zoom").size(); z++){
                System.out.println("PAM PAM: " + doc.select(".owl-stage").get(0).select(".zoom").get(z).html());
                System.out.println("PAM PAM: " + doc.select(".owl-stage").get(0).select(".zoom").attr("style"));
            }*/
            /*String test = doc.select(".listing-carousel").select(".zoom").attr("style");
            System.out.println(test.substring(23, test.lastIndexOf("')")));*/
            /*if(doc.select(".product_gallery-previews").select("div").select("a").size() == 0){
                if(doc.getElementById("product-image") != null) {
                    String s = doc.getElementById("product-image").attr("src");
                    try {
                        Download(s, s.substring(s.lastIndexOf("/")).replace("/", ""), "parsing_" + SiteName + "/" + nameFolder);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }*/

            //Акция
            resList = getArrayStrOnFile("sale.txt");
            if(resList.size() != 0){
                for(int e = 0; e < resList.size(); e++){
                    excel.setCell(Row + e, 0, resList.get(e));
                }
                Row = Row + resList.size() + 1;
            }

            //описание(готово)
            select = doc.select(".content-block");
            for(int e = 0; e < select.select("p").size(); e++){
                excel.setCell(Row, 0, select.select("p").get(e).text());  //не знаю максимальной длинны строки в ячейке excel. Может быть переполнение!
                Row++;
            }
            //Row = Row + 2;

            //характеристики(готово)
            select = doc.select(".features").select(".spoiler");

            excel.setCell(Row, 0, select.select("h3").text());  //заголовок описания
            Row++;

            for (int e = 0; e < select.select("li").size(); e++) {
                String [] res = corrector(select.select("li").get(e).select(".title").select("span").text(), select.select("li").get(e).select(".value").text());
                excel.setCell(e + Row, 0, res[0]);
                excel.setCell(e + Row, 1, res[1]);
            }

            Row = Row + select.select("li").size();
            Row++; //отступ

            //подвал
            resList = getArrayStrOnFile("bottom.txt");
            for(int e = 0; e < resList.size(); e++){
                excel.setCell(Row + e, 0, resList.get(e));
            }
            Row = Row + resList.size();

            //цена, количество и ссылка на товар
            Row = Row + 2;
            select = doc.select(".product-price");
            excel.setCell(Row, 0, select.select(".current-price").text());
            Row++;
            select = doc.select(".product-delivery");
            excel.setCell(Row, 0, select.text());
            Row++;
            excel.setCell(Row, 0, URLPage.get(w));

            excel.Build("parsing_" + SiteName + "/" + nameFolder + "/" + nameFolder + ".xlsx");

        }

        System.out.println(URLPage.size());

    }

}
