package sample.model;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sample.Main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Repository {

    private List<Task> tasks = new ArrayList<>();

    private static Repository instance;

    private Repository() {
        parseXML();
    }

    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
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
            final String path = "xml";
            final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

            List<File> files = new ArrayList<>();

            if(jarFile.isFile()) {  // Run with JAR file
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    final String name = entry.getName();
                    if (name.startsWith(path + "/task")) { //filter according to the path
                        System.out.println(name);
                        InputStream in = jar.getInputStream(entry);
                        File temp = File.createTempFile("Exam-", "-temp");
                        FileUtils.copyInputStreamToFile(in, temp);
                        files.add(temp);
                        System.out.println(temp.getName());
                        in.close();
                        temp.deleteOnExit();
                    }
                }
                jar.close();

            } else { // Run with IDE
                final URL url = Main.class.getResource("/" + path);
                if (url != null) {
                    try {
                        final File xmlFiles = new File(url.toURI());
                        for (File xmlFile : xmlFiles.listFiles()) {
                            files.add(xmlFile);
                            System.out.println(xmlFile);
                        }
                    } catch (URISyntaxException ex) {
                        // never happens
                    }
                }
            }


            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            for (File file : files) {
                List<Variant> variants = new ArrayList<>();

                Document document = builder.parse(file);
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

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


}
