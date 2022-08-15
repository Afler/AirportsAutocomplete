import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

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
(все запуски java –jar должны выполняться с флагом -Xmx7m).

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
        System.out.println("Колонка поиска: " + searchingColumn);
        if (searchingColumn <= 0 || searchingColumn > 14) {
            throw new IllegalArgumentException("Допустимые столбцы для поиска 1-14");
        }

        try {
            String path = System.getenv().getOrDefault("AutocompleteFilePath", "C:\\Users\\555\\IdeaProjects\\AirportsAutocomplete\\src\\main\\resources\\airports.csv");
            RandomAccessFile reader = new RandomAccessFile(path, "r");
            Map<Character, TreeMap<String, List<Long>>> prefixesMap = new TreeMap<>();
            String currStr;
            long pointer;
            String substring;
            Character key;
            TreeMap<String, List<Long>> innerMap;
            System.out.println("Предварительная обработка");
            while (true) {
                pointer = reader.getFilePointer();
                if ((currStr = reader.readLine()) != null) {
                    substring = currStr.split(",")[searchingColumn - 1].replace("\"", "").toLowerCase();
                    key = substring.charAt(0);
                    if (!prefixesMap.containsKey(key)) {
                        prefixesMap.put(key, new TreeMap<>());
                    }
                    innerMap = prefixesMap.get(key);
                    if (!innerMap.containsKey(substring)) {
                        innerMap.put(substring, new ArrayList<>());
                    }
                    innerMap.get(substring).add(pointer);
                } else {
                    break;
                }
            }
            System.out.println("Предварительная обработка завершена");

            Scanner scanner = new Scanner(System.in);
            String request;
            StringBuilder ans = new StringBuilder();
            System.out.println("Введите запрос: ");
            String innerMapKey;
            int count;
            long start = 0;
            long end = 0;
            Character desiredChar;
            while (!(request = scanner.nextLine().toLowerCase()).equals("!quit")) {
                if (!request.isBlank()) {
                    desiredChar = Character.toLowerCase(request.charAt(0));
                    count = 0;
                    reader.seek(0);
                    ans.setLength(0);
                    if (prefixesMap.containsKey(desiredChar)) {
                        start = System.currentTimeMillis();
                        for (Map.Entry<String, List<Long>> entry : prefixesMap.get(desiredChar).entrySet()) {
                            innerMapKey = entry.getKey();
                            if (innerMapKey.startsWith(request)) {
                                ++count;
                                ans.append(innerMapKey);
                                for (Long localPointer : entry.getValue()) {
                                    reader.seek(localPointer);
                                    ans.append("[").append(reader.readLine()).append("]").append("\n");
                                }
                            }
                        }
                        end = System.currentTimeMillis();
                    }

                    System.out.println(ans);
                    System.out.println("Количество совпадений: " + count);
                    System.out.println("Время поиска: " + (end - start) + "мс");
                } else {
                    System.out.println("Некорректный запрос");
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

















