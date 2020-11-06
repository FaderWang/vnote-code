package com.fader.vnote.io.xml;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public class XmlReaderExample {

    static void xpath() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = Resources.getResourceAsStream("users.xml");
        Document document = builder.parse(inputStream);
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.evaluate("/users/*", document, XPathConstants.NODESET);
//        List<UserEntity> userList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1, n = nodeList.getLength(); i <= n; i++) {
            stringBuilder.setLength(0);
            String path = "users/user[" + i + "]";
            String id = (String) xPath.evaluate(path + "/@id", document, XPathConstants.STRING);
            String name = (String)xPath.evaluate(path + "/name", document, XPathConstants.STRING);
            String createTime = (String)xPath.evaluate(path + "/createTime", document, XPathConstants.STRING);
            String password = (String)xPath.evaluate(path + "/passward", document, XPathConstants.STRING);
            String phone = (String)xPath.evaluate(path + "/phone", document, XPathConstants.STRING);
            String nickName = (String)xPath.evaluate(path + "/nickName", document, XPathConstants.STRING);

            stringBuilder.append("id=").append(id).append(",")
                    .append("name=").append(name).append(",")
                    .append("createTime=").append(createTime).append(",");
            System.out.println(stringBuilder);
        }
    }

    static void xpathParser() throws IOException {
        Reader reader = Resources.getResourceAsReader("users.xml");
        XPathParser xPathParser = new XPathParser(reader);
        List<XNode> xNodes = xPathParser.evalNodes("/users/*");
        for (int i = 0; i < xNodes.size(); i ++) {
            XNode xNode = xNodes.get(i);
            System.out.println(xNode.evalString("@id"));
//            System.out.println(xNode.getStringBody());
            Long id = xNode.getLongAttribute("id");
            List<XNode> childs = xNode.getChildren();
            String name = childs.get(0).getStringBody();

            System.out.println("id=" + id + ",name=" + name);
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
//        xpath();
        xpathParser();
    }

}
