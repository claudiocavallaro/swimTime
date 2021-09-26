package com.time.swimtime.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.time.swimtime.model.Gara;
import com.time.swimtime.model.User;
import com.time.swimtime.persistence.UserDAO;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    private final String prefixUrl = "https://aquatime.it/tempim.php";


    public String find(String nome) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        UserDAO dao = UserDAO.getInstance();
        //controlla se l'untente c'è già nel DB così evitiamo lo scraping
        List<User> usersFromDb = dao.get(nome);
        if (usersFromDb.size() > 0) {
            logger.info("From DB");
            return gson.toJson(usersFromDb);
        }

        String url = "https://aquatime.it/tempim.php?AtletaSRC=" + nome + "&Azione=1";

        String cookie = "_ga=GA1.2.1127064407.1632581065; _gid=GA1.2.301384308.1632581065; " +
                "regione=999;";

        List<User> userList = new ArrayList<>();

        try {
            String content = Jsoup.connect(url).cookie("cookie", cookie).get().toString();
            TagNode tag = new org.htmlcleaner.HtmlCleaner().clean(content);
            Document doc = new DomSerializer(new CleanerProperties()).createDOM(tag);

            String nomeAtleta = "";
            for (int i = 1; i <= 5; i++) {
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
                    String link = exprLink.evaluate(doc).replaceAll("amp;", "").split("&")[0];
                    a.setCodice(link);

                    userList.add(a);
                } else {
                    break;
                }
            }


            userList.stream().forEach(u -> dao.insert(u.getNome(), u.getAnno(), u.getSesso(), u.getSocieta(), u.getCodice()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(userList);
    }

    @Cacheable("time")
    public String findTime(String id) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        List<Gara> lista = new ArrayList<>();
        UserDAO dao = UserDAO.getInstance();
        String url = dao.getCodice("1");
        scansionaPagine(lista, url, "1");
        scansionaPagine(lista, url, "2");
        scansionaPagine(lista, url, "3");
        scansionaPagine(lista, url, "4");
        scansionaPagine(lista, url, "5");
        return gson.toJson(lista);
    }

    private void scansionaPagine(List<Gara> listaGare, String url, String page) {
        String urlFinal = prefixUrl + url + "&Azione=2&tipoG=0&Vasca=0&page=" + page + "#box1";

        String cookie = "_ga=GA1.2.1127064407.1632581065; _gid=GA1.2.301384308.1632581065; " +
                "regione=999;";
        try {
            String content = Jsoup.connect(urlFinal).cookie("cookie", cookie).get().toString();
            TagNode tag = new org.htmlcleaner.HtmlCleaner().clean(content);
            Document doc = new DomSerializer(new CleanerProperties()).createDOM(tag);

            //------SCANSIONE ELENCO GARE----------------------------
            String garaString = " ";
            for (int i = 1; i <= 20; i++) {

                XPath xpath = XPathFactory.newInstance().newXPath();
                String expressionGara = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[1]";
                XPathExpression exprGara = xpath.compile(expressionGara);
                garaString = exprGara.evaluate(doc);

                if (garaString.equals("")) {
                    break;
                }

                int start = garaString.indexOf("(") + 1;
                int end = garaString.indexOf(")");

                String garaT = garaString.replaceAll("&deg;", "°");
                String garaP = garaT.replaceAll("&igrave;", "ì");
                String garaX = garaP.replaceAll("&quot;", "''");
                String gara = garaX.replaceAll("&agrave;", "à");
                if (!(gara.equals(""))) {
                    Gara gara1 = new Gara();
                    gara1.setCitta(gara);

                    String expressionData = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[2]";
                    XPathExpression exprData = xpath.compile(expressionData);
                    String data = exprData.evaluate(doc);
                    gara1.setData(data);

                    String expressionTipo = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[3]";
                    XPathExpression exprTipo = xpath.compile(expressionTipo);
                    String tipo = exprTipo.evaluate(doc);
                    gara1.setTipo(tipo);

                    String expressionTempo = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[4]/a/text()";
                    XPathExpression exprTempo = xpath.compile(expressionTempo);
                    String tempo = exprTempo.evaluate(doc);


                    if (tempo.equals("")) {
                        expressionTempo = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[4]";
                        exprTempo = xpath.compile(expressionTempo);
                        tempo = exprTempo.evaluate(doc);
                        gara1.setTempo(tempo);
                        if (tempo.equals("Squalificato")) {
                            gara1.setTime(999999999);
                        } else {
                            gara1.setTime(gara1.toTime(tempo));
                        }
                    } else {
                        if (gara1.getTipo().contains("4x")) {
                            gara1.setTempo("Tempo totale staffetta : " + tempo.substring(tempo.indexOf("gt;") + 3, tempo.length()));
                            gara1.setTime(gara1.toTime(tempo));
                        } else {
                            String tempoSt = tempo.substring(tempo.indexOf("gt;") + 3, tempo.length());
                            gara1.setTempo(tempoSt);
                            System.out.println(tempo);
                            gara1.setTime(gara1.toTime(tempoSt));
                        }
                    }


                    String expressionVasca = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[5]";
                    XPathExpression exprVasca = xpath.compile(expressionVasca);
                    String vasca = exprVasca.evaluate(doc);
                    gara1.setVasca(vasca);

                    String expressionFederazione = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[7]";
                    XPathExpression exprFederazione = xpath.compile(expressionFederazione);
                    String federazione = exprFederazione.evaluate(doc);
                    gara1.setFederazione(federazione);

                    String expressionCategoria = "//div[1]/center[8]/table/tbody/tr[" + i + "]/td[8]";
                    XPathExpression exprCategoria = xpath.compile(expressionCategoria);
                    String categoria = exprCategoria.evaluate(doc);
                    gara1.setCategoria(categoria);


                    /*String expressionLink = "//div[1]/center[5]/table/tbody/tr["+ i +"]/td[1]/a/@href";
                    XPathExpression exprLink = xpath.compile(expressionLink);
                    String link = exprLink.evaluate(doc);
                    System.out.println("link " + link);*/

                    //System.out.println(gara1.toString());
                    listaGare.add(gara1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
