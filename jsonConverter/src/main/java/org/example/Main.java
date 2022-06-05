package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static List<Employee> parseCSV(String[] columnMapping, String filename) {

        try (CSVReader reader = new CSVReader(new FileReader(filename))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }
    public static void writeString(String json, String filename){
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Employee> parseXML(String filename) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filename));
      //  Node root = doc.getDocumentElement();

        NodeList nodeList = doc.getElementsByTagName("employee");
        for(int i = 0; i < nodeList.getLength(); i++) {
            Map<String, String> empMap = new HashMap<>();
            Node node_ = nodeList.item(i);

            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;

                NodeList elementIdList = element.getElementsByTagName("id");
                Element id = (Element) elementIdList.item(0);
                NodeList idNum = id.getChildNodes();
                String id_ = ((Node)idNum.item(0)).getNodeValue();


                NodeList elementFirstNameList = element.getElementsByTagName("firstName");
                Element firstName = (Element) elementFirstNameList.item(0);
                NodeList idFirstName = firstName.getChildNodes();
                String first_Name = (((Node)idFirstName.item(0)).getNodeValue());

                NodeList elementLastNameList = element.getElementsByTagName("lastName");
                Element lastName = (Element) elementLastNameList.item(0);
                NodeList idLastName = lastName.getChildNodes();
                String last_Name = ((Node)idLastName.item(0)).getNodeValue();

                NodeList elementCountryList = element.getElementsByTagName("country");
                Element country = (Element) elementCountryList.item(0);
                NodeList idCountry = country.getChildNodes();
                String country_ =  ((Node)idCountry.item(0)).getNodeValue();

                NodeList elementAgeList = element.getElementsByTagName("age");
                Element age = (Element) elementAgeList.item(0);
                NodeList idAge = age.getChildNodes();
                String age_ = ((Node)idAge.item(0)).getNodeValue();

                list.add(new Employee(Long.valueOf(id_), first_Name, last_Name, country_, Integer.valueOf(age_)));
            }
        }
        return list;
    }

    public static String readString(String filename) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(new FileReader(filename))) {

            String buf;
            while ((buf = buffer.readLine()) != null) {
                json.append(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static List<Employee> jsonToList(String json) throws ParseException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        List<Employee> list = gson.fromJson(json, listType);
//        System.out.println(list.toString());
        return list;
    }


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ParseException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, "data2.json");

        String json3 = readString("data.json");
        List<Employee> list3 = jsonToList(json3);

        for (Employee emp : list3) {
            System.out.println(emp);
        }

    }

}