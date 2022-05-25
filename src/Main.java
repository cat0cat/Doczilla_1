import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static String path = "D:" + File.separator + "Test2";
    public static String fileName = "all.txt";

    public static void main(String[] args) {
        File folder = new File(path);
        clearTheFile(fileName);
        ArrayList<File> fileList = new ArrayList<>();
        listOfFiles(folder, fileList);
        System.out.println("До сортировки:");
        for (File fileName : fileList) {
            System.out.println(fileName);
        }
        //сортируем файлы
        fileList.sort((o1, o2) -> {
            String fileName1 = o1.getName();
            String fileName2 = o2.getName();
            return fileName1.compareTo(fileName2);
        });
        System.out.println("После сортировки:");
        for (File fileName : fileList) {
            System.out.println(fileName);
        }
        //склеиваем в один файл all.txt
        split(fileList);

        findRequired(fileList);
    }

    // добавляем все файлы в список
    public static void listOfFiles(File x, ArrayList<File> fileList) {
        File[] fld = x.listFiles();
        assert fld != null;
        for (File file : fld) {
            if (file.isFile()) {
                fileList.add(file);
            } else {
                listOfFiles(file, fileList);
            }
        }
    }

    // склеиваем все файлы
    public static void split(ArrayList<File> fileList) {
        for (File file : fileList) {
            try (InputStream in = new FileInputStream(file);
                 OutputStream os = new FileOutputStream(fileName, true)) {
                byte[] buffer = new byte[1 << 20];
                int count;
                while ((count = in.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
                os.write(+'\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // очищаем файл all.txt
    public static void clearTheFile(String file) {
        try {
            FileWriter writer = new FileWriter(file, false);
            PrintWriter printWriter = new PrintWriter(writer, false);
            printWriter.flush();
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //находим строку в одинарных кавычках
    public static void findRequired (ArrayList<File> fileList){
        ArrayList<String> list = new ArrayList<>();
        System.out.println("Строки в кавычках:");
        for (File file : fileList) {
            Pattern pat = Pattern.compile("require (‘.*’)");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                String s;
                while ((line = br.readLine()) != null) {
                    Matcher m = pat.matcher(line);
                    while (m.find()) {
                        s = m.group().replace("require ", "")
                                .replaceAll("‘", "")
                                .replaceAll("’", "");
                        System.out.println(s);
                        list.add(s);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Map<String, Integer>  duplicatedCount = new HashMap<>();
        list.forEach(a -> duplicatedCount.put(a, duplicatedCount.getOrDefault(a, 0) +1));
        duplicatedCount.forEach((k,v) -> System.out.println(v + " раз встречается " + k));
    }
}

