package sample.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private List<Task> tasks = new ArrayList<Task>();

    public Repository() {
        parseXML();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTaskByNumber(int number) {
        for (Task task : tasks) {
            if (task.getNumber() == number) {
                return task;
            }
        }
        return null;
    }

    private void parseXML() {
        try {
            File dir = new File(String.valueOf(getClass().getClassLoader().getResource("xml").getFile()));
            File[] files = dir.listFiles();


            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            for (int i = 0; i < files.length; i++) {
                List<Variant> variants = new ArrayList<Variant>();
                
                Document document = builder.parse(files[i]);
                NodeList nodeList = document.getElementsByTagName("variant");
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node = nodeList.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        int number = Integer.parseInt(element.getAttribute("number"));
                        String question = element.getElementsByTagName("question").item(0).getTextContent();
                        String answer = element.getElementsByTagName("answer").item(0).getTextContent();
                        variants.add(new Variant(number, question, answer));
                    }
                }

                Element root = document.getDocumentElement();
                int num = Integer.parseInt(root.getAttribute("number"));
                tasks.add(new Task(num, variants));
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
