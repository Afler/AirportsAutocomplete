import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/*
* Перечитывать все строки файла при каждом поиске нельзя  
(в том числе читать только определенную колонку у каждой строки).

* Создавать новые файлы или редактировать текущий нельзя  
(в том числе использовать СУБД).

Хранить весь файл в памяти нельзя  
(не только в качестве массива байт, но и в структуре, которая так или иначе содержит все
данные из файла).
*
Для корректной работы программе требуется не более 7 МБ памяти  
(все запуски java –jar должны выполняться с флагом ).

Скорость поиска должна быть максимально высокой с учетом требований выше  
(в качестве ориентира можно взять число из скриншота выше: на поиск по «Bo», который
выдает 68 строк, требуется 25 мс).

Сложность поиска меньше чем O(n), где n — число строк файла.

Должны соблюдаться принципы ООП и SOLID.

Ошибочные и краевые ситуации должны быть корректно обработаны.
*/

public class Main {
    public static void main(String[] args) {
        int searchingColumn = Integer.parseInt(args[0]);
        System.out.println(searchingColumn);
        if (searchingColumn <= 0 || searchingColumn > 14) {
            throw new IllegalArgumentException("Допустимые столбцы для поиска 1-14");
        }

        try {
            HashMap<String, String> history = new HashMap<>();

            /*
             * Make a Hashmap<StringPrefix, StringsWithThatPrefix> of selected column values
             * */
//            HashMap<Character, List<String>> searchingColumnData = new HashMap<>();
//
//            StringBuilder dataBuilder = new StringBuilder();
//            while ((currStr = reader.readLine()) != null) {
//                String substring = currStr.split(",")[searchingColumn - 1].replaceAll("^\"*|\"*$", "");
//                Character key = substring.charAt(0);
//                dataBuilder.append(substring).append("[").append(currStr).append("]").append("\n");
//                if (!searchingColumnData.containsKey(key)) {
//                    searchingColumnData.put(key, new ArrayList<>());
//                }
//                searchingColumnData.get(key).add(dataBuilder.toString());
//            }

            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\resources\\airports.csv"));
            String currStr;
            Scanner scanner = new Scanner(System.in);
            String request;
            System.out.println("Введите запрос: ");
            String cachedResultsForRequest;
            while (!(request = scanner.nextLine()).equals("!quit")) {
                if ((cachedResultsForRequest = history.get(request)) != null) {
                    System.out.println(cachedResultsForRequest);
                } else {
                    StringBuilder ans = new StringBuilder();
                    String substring;
                    long start = System.currentTimeMillis();
                    while ((currStr = reader.readLine()) != null) {
//                        substring = currStr.split(",")[searchingColumn - 1].replaceAll("^\"*|\"*$", "");
                        substring = currStr.split(",")[searchingColumn - 1].replace("\"", "");
                        if (substring.startsWith(request)) {
                            ans.append(substring).append("[").append(currStr).append("]").append("\n");
                        }
                    }
                    long end = System.currentTimeMillis();
                    history.put(request, ans.toString());
                    System.out.println(ans);
                    System.out.println("Время поиска: " + (end - start) + "мс");
                }
                System.out.println("Введите запрос: ");
            }

            reader.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}

















