import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Разбор xml
 */
public class XMLParseTest {

    /**
     * Разбор xml из строки В строке обязательно должен быть корневой элемент
     *
     * @param xml                - строка для разбора
     * @param rootName           - имя корневого элемента, если null - "data"
     * @param fieldName          - имя элемента для поля, если null - "field"
     * @param fieldTabName       - имя элемента для поля-таблицы, если null - "fieldtab"
     * @param nameAttributeName  - имя атрибута для имени поля ... - "name"
     * @param valueAttributeName - имя атрибута для значения поля ... - "value"
     * @return Map поле:значение
     */
    public Map<String, String> parseXML(
            String xml,
            String rootName,
            String fieldName,
            String fieldTabName,
            String nameAttributeName,
            String valueAttributeName
    ) {
        // https://java-course.ru/begin/xml/
        // https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        /* Пример правильной строки для разбора
        xml = "<data>\n"
                + "    <field name=\"req_id\" value=\"602\"/>\n"
                + "    <field name=\"requesttype_id\" value=\"10001\"/>\n"
                + "    <field name=\"redirect\" value=\"newrequesttp\"/>\n"
                + "</data>";

         */
        if (rootName == null) {
            rootName = "data";
        }
        if (fieldName == null) {
            fieldName = "field";
        }
        if (nameAttributeName == null) {
            nameAttributeName = "name";
        }
        if (valueAttributeName == null) {
            valueAttributeName = "value";
        }
        if (fieldTabName == null) {
            fieldTabName = "fieldtab";
        }
        Map<String, String> map = new HashMap<>();
        // Создадим фабрику
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Создаем построитель документа
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(
                    "Ошибка при создании фабрики построителя документов: " + e.getMessage());
        }
        // Считываем из строки
        InputSource is = new InputSource(new StringReader(xml));
        Document document = null;
        try {
            // Создается дерево DOM документа из String
            document = builder.parse(is);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Ошибка при парсинге строки: " + e.getMessage());
        }
        // Получаем корневой элемент
        Node root = document.getDocumentElement();
        // Список полей-таблиц
        NodeList fieldTableList = document.getElementsByTagName(fieldTabName);
        System.out.println("List 'fieldtab' nodes:");
        for (int i = 0; i < fieldTableList.getLength(); i++) {
            Node data = fieldTableList.item(i);
            System.out.println(data.getNodeName());
        }
        // Проверим на совпадение
        if (!rootName.equals(root.getNodeName()) && false) {
            throw new RuntimeException(
                    String.format("Корневой элемент должен быть '%s' а не '%s': ", rootName,
                            root.getNodeName()));
        }
        // Список нод для данных
        NodeList dataList = document.getElementsByTagName(rootName);
        for (int i1 = 0; i1 < dataList.getLength(); i1++) {
            // Просматриваем все подэлементы корневого - т.е. поля
            NodeList fields = dataList.item(i1).getChildNodes();
            for (int ii = 0; ii < fields.getLength(); ii++) {
                Node field = fields.item(ii);
                // Если этот элемент - нода и именем fieldName - это, то что нам нужно
                if (field.getNodeType() == Node.ELEMENT_NODE
                        && field.getNodeName().equals(fieldName)) {
                    // Преобразуем к элементу
                    Element element = (Element) field;
                    // Добавим в результат из атрибутов имя поля и значение, если есть
                    if (!element.getAttribute(nameAttributeName).isEmpty()) {
                        map.put(
                                element.getAttribute(nameAttributeName),
                                element.getAttribute(valueAttributeName
                                ));
                    }
                }
            }
        }
        return map;
    }

    public Map<String, String> parseXML(String xml) {
        return parseXML(xml, null, null, null, null, null);
    }

    public void test() {
        List<String> patternFileNameList = Arrays.asList("a", "b", "c", "d", "e");
        XMLParseTest xmlParseTest = new XMLParseTest();
        String xml = "<data>\n"
                + "    <field name=\"req_id\" value=\"602\"/>\n"
                + "    <fieldtab>\n"
                + "    </fieldtab>"
                + "    <field name=\"requesttype_id\" value=\"10001\"/>\n"
                + "    <field name=\"redirect\" value=\"newrequesttp\"/>\n"
                + "    <fieldtab>\n"
                + "    </fieldtab>"
                + "</data>";
        String rootName = "data";
        String fieldName = "field";
        String nameAttributeName = "name";
        String valueAttributeName = "value";
        String fieldTabName = "fieldtab";
        System.out.println(xml);
        xmlParseTest.parseXML(
                        xml,
                        rootName,
                        fieldName,
                        fieldTabName,
                        nameAttributeName,
                        valueAttributeName
                )
                .forEach((k, v) -> {
                    System.out.println((k + "=" + v));
                });
        xmlParseTest.parseXML(xml)
                .forEach((k, v) -> {
                    System.out.println((k + "=" + v));
                });
        System.out.println(patternFileNameList.get(0));

    }

}
