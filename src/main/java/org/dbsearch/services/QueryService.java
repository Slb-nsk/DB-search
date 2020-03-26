package org.dbsearch.services;

import com.google.gson.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class QueryService {
    OutputService output = new OutputService();

    //обработка операции search
    public void searchQuery(JsonObject inputObject, File outputFile) {

        if (!inputObject.has("criterias")) {
            output.error(outputFile, "Input file doesn't cointain 'criterias' word.");
        } else {
            JsonArray inputArray = inputObject.getAsJsonArray("criterias");
            JsonArray outputArray = new JsonArray(inputArray.size());
            for (int i = 0; i < inputArray.size(); i++) {
                JsonObject criteria = inputArray.get(i).getAsJsonObject();
                JsonArray resultList = new JsonArray();
                if (criteria.has("lastName")) {
                    //выдать список покупателей с этой фамилией

                } else if (criteria.has("productName") && criteria.has("minTimes")) {
                    //выдать список покупателей, купивших данный товар не менее указанного числа раз

                } else if (criteria.has("minExpenses") && criteria.has("maxExpenses")) {
                    //выдать список покупателей, у которых общая стоимость всех покупок за всё время попадает в интервал

                } else if (criteria.has("badCustomers")) {
                    //выдать список покупателей, купивших меньше всего товаров

                } else {
                    output.error(outputFile, "Input file contains unknown criteria.");
                }

                //добавляем результат в итоговый файл
                JsonObject outputObject = new JsonObject();
                outputObject.add("criteria", criteria);
                outputObject.add("results", resultList);
                outputArray.add(outputObject);
            }
            output.search(outputFile, outputArray);
        }
    }


    //обработка операции stat
    public void statQuery(JsonObject inputObject, File outputFile) {
        if (inputObject.size() != 2) {
            output.error(outputFile, "Invalid number of parameters.");
        } else if (!inputObject.has("startDate")) {
            output.error(outputFile, "'startDate' missing in input file.");
        } else if (!inputObject.has("endDate")) {
            output.error(outputFile, "'endDate' missing missing in input file.");
        } else {
            //Вычислить общее количество дней из двух дат, включительно, без выходных
            int totalDays;

            //Данные по покупателям за этот период, упорядоченные по общей стоимости покупок по убыванию
            JsonArray customers = new JsonArray();

            //Сумма покупок всех покупателей за период
            int totalExpenses;

            //Средние затраты всех покупателей за период
            double avgExpenses = new BigDecimal(totalExpenses/ customers.size())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            output.stat(outputFile, totalDays, customers, totalExpenses, avgExpenses);
        }
    }
}
