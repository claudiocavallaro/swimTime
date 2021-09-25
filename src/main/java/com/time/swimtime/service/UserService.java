package com.time.swimtime.service;

import com.time.swimtime.model.User;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public String find(String nome){
        String url = "https://aquatime.it/tempim.php?AtletaSRC="+ nome +"&Azione=1";

        String cookie = "_ga=GA1.2.1127064407.1632581065; _gid=GA1.2.301384308.1632581065; " +
                "regione=999;";

        List<User> userList = new ArrayList<>();

        try {
            String content = Jsoup.connect(url).cookie("cookie", cookie).get().toString();
            TagNode tag = new org.htmlcleaner.HtmlCleaner().clean(content);
            Document doc = new DomSerializer(new CleanerProperties()).createDOM(tag);

            String nomeAtleta = "";
            for (int i = 1 ; i <= 5 ; i++) {
                //------NOME----------------------------
                XPath xpath = XPathFactory.newInstance().newXPath();
                String expressionNome = "(//div[1]/center[5]/table[@class='datatable']/tbody/tr[" + i + "]/td[1])[1]";
                XPathExpression exprNome = xpath.compile(expressionNome);
                nomeAtleta = exprNome.evaluate(doc);
                // se non ci sono più atleti e quindi la xpath torna nulla esci dal ciclo altrimenti prosegui
                if (!(nomeAtleta.equals(""))) {
                    User a = new User();
                    a.setNome(nome);

                    //---------ANNO----------------------------
                    String expressionAnno = "(//div[1]/center[5]/table[@class='datatable']/tbody/tr[" + i + "]/td[2])[1]";
                    XPathExpression exprAnno = xpath.compile(expressionAnno);
                    String anno = exprAnno.evaluate(doc);
                    a.setAnno(anno);

                    //----------SESSO---------------
                    String expressionSesso = "(//div[1]/center[5]/table[@class='datatable']/tbody/tr[" + i + "]/td[4])[1]";
                    XPathExpression exprSesso = xpath.compile(expressionSesso);
                    String sesso = exprSesso.evaluate(doc);
                    a.setSesso(sesso);

                    //--------SOCIETA--------------------
                    String expressionSoc = "(//div[1]/center[5]/table[@class='datatable']/tbody/tr[" + i + "]/td[3])[1]";
                    XPathExpression exprSoc = xpath.compile(expressionSoc);
                    String soc = exprSoc.evaluate(doc);
                    a.setSocieta(soc);

                    String expressionLink = "(//div[1]/center[5]/table[@class='datatable']/tbody/tr[" + i + "]/td[1]/a/@href)[1]";
                    XPathExpression exprLink = xpath.compile(expressionLink);
                    String link = "https://aquatime.it/tempim.php?" + exprLink.evaluate(doc).replaceAll("amp;", "");
                    a.setCodice(link);

                    userList.add(a);
                } else {
                    break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList.toString();
    }
}
