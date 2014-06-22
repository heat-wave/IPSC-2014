import java.util.*;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


public class A {
    FastScanner in;
    PrintWriter out;
    public void solve() throws IOException, ScriptException {

        int rows = in.nextInt();
        int cols = in.nextInt();
        char [] [] table = new char [rows][cols];
        for (int i = 0; i < rows; i++) {
            table[i] = in.next().toCharArray();
        }

        String [] pokemon = new String[719];
        rip("http://en.wikipedia.org/wiki/List_of_Pokémon", pokemon);

        String [] names = new String[44];
        int [] dates = new int[44];
        rip("http://history1900s.about.com/od/worldleaders/a/uspresidents.htm", names, dates);
        names[36] = "Richard Nixon";
        names[30] = "Herbert Clark Hoover";
        names[29] = "Calvin Coolidge";
        names[43] = "Barack Obama";
        names[37] = "Gerald Ford";
        names[8] = "William Henry Harrison";
        names[26] = "William Howard Taft";

        String [] short_el = new String[118];
        String [] long_el = new String[118];
        rip("http://www.webelements.com/nexus/content/list-elements-atomic-number", short_el, long_el);
        long_el[12] = "aluminum";
        long_el[15] = "sulfur";
        long_el[54] = "cesium";

        for (int type = 0; type < 2; type++) {
            
            int queries = in.nextInt();
            for (int i = 0; i < queries; i++) {
                int r = in.nextInt();
                int c = in.nextInt();
                String question = in.nextLine();
                if (question.contains("Pokemon")) {
                    int pos = question.indexOf("from");
                    if (pos != -1) {
                        String search = question.substring(pos + 5);
                        int index = 0;
                        while (index < pokemon.length && !pokemon[index].equals(search)) {
                            index++;
                        }
                        String ans = pokemon[index + 1];
                        fillTable(r, c, table, ans, type == 0);
                        continue;
                    }
                    pos = question.indexOf("into");
                    if (pos != -1) {
                        String search = question.substring(pos + 5);
                        int index = 0;
                        while (index < pokemon.length && !pokemon[index].equals(search)) {
                            index++;
                        }
                        String ans = pokemon[index - 1];
                        if (search.equals("Flareon") || search.equals("Jolteon") || search.equals("Vaporeon")) {
                            ans = "eevee";
                        }
                        fillTable(r, c, table, ans, type == 0);
                        continue;
                    }
                    pos = question.indexOf('#');
                    int num = parse(question.substring(pos + 1));
                    fillTable(r, c, table, pokemon[num - 1], type == 0);
                    continue;
                }
                if (question.contains("Chemical")) {
                    int pos = question.indexOf("number");
                    if (pos != -1) {
                        String ans = long_el[parse(question.substring(pos + 7)) - 1];
                        fillTable(r, c, table, ans, type == 0);
                        continue;
                    }
                    int index = 0;
                    while (index < short_el.length && !short_el[index].equals(question.substring(question.lastIndexOf(' ') + 1))) {
                        index++;
                    }
                    String ans = long_el[index];
                    fillTable(r, c, table, ans, type == 0);
                    continue;
                }
                if (question.contains("Color")) {
                    int pos = question.indexOf('#');
                    if (pos != -1) {
                        String ans = rip(question.substring(pos + 1));
                        fillTable(r, c, table, ans, type == 0);
                        continue;
                    }
                    question = question.substring(question.indexOf('[') + 1, question.indexOf(']'));
                    String [] args = question.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < 3; j++) {
                        String aux = Integer.toHexString(Integer.parseInt(args[j]));
                        if (aux.length() == 1) {
                            sb.append(0);
                        }
                        sb.append(aux);
                    }
                    String ans = rip(sb.toString());
                    fillTable(r, c, table, ans, type == 0);
                    continue;
                }
                if (question.contains("office")) {
                    if (question.contains("First") && Character.isDigit(question.charAt(question.length() - 1))) {
                        int year = Integer.parseInt(question.substring(question.length() - 4));
                        int index = 0;
                        while (index < dates.length && dates[index] < year) {
                            index++;
                        }
                        if (fillTable(r, c, table, names[index].substring(0, names[index].indexOf(' ')), type == 0)) {
                            if (fillTable(r, c, table, names[index + 1].substring(0, names[index + 1].indexOf(' ')), type == 0)) {
                                fillTable(r, c, table, names[index - 1].substring(0, names[index - 1].indexOf(' ')), type == 0);
                            };
                        };
                        continue;
                    }
                    if (question.contains("Last") && Character.isDigit(question.charAt(question.length() - 1))) {
                        int year = Integer.parseInt(question.substring(question.length() - 4));
                        int index = 0;
                        while (index < dates.length && dates[index] < year) {
                            index++;
                        }
                        if (fillTable(r, c, table, names[index].substring(names[index].lastIndexOf(' ') + 1), type == 0)) {
                            if (fillTable(r, c, table, names[index + 1].substring(names[index + 1].lastIndexOf(' ') + 1), type == 0)) {
                                fillTable(r, c, table, names[index - 1].substring(names[index - 1].lastIndexOf(' ') + 1), type == 0);
                            }
                        };
                        continue;
                    }
                }
                int pos = question.indexOf("before");
                if (pos != -1) {
                    String search = question.substring(pos + 7);
                    int index = 0;
                    while (!names[index].equals(search)) {
                        index++;
                    }
                    index--;
                    if (question.contains("First")) {
                        fillTable(r, c, table, names[index].substring(0, names[index].indexOf(' ')), type == 0);
                    }
                    if (question.contains("Last")) {
                        fillTable(r, c, table, names[index].substring(names[index].lastIndexOf(' ') + 1), type == 0);
                    }
                    continue;
                }
                pos = question.indexOf("after");
                if (pos != -1) {
                    String search = question.substring(pos + 6);
                    int index = 0;
                    while (index < names.length && !names[index].equals(search)) {
                        index++;
                    }
                    index++;
                    if (question.contains("First")) {
                        fillTable(r, c, table, names[index].substring(0, names[index].indexOf(' ')), type == 0);
                    }
                    if (question.contains("Last")) {
                        fillTable(r, c, table, names[index].substring(names[index].lastIndexOf(' ') + 1), type == 0);
                    }
                    continue;
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                out.print(table[i][j]);
            }
            out.println();
        }
    }

    public void run() {
        try {
            in = new FastScanner(new FileInputStream("k1.in"));
            out = new PrintWriter(new FileOutputStream("k1.out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;
        public boolean hasMoreTokens() throws IOException{
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                String s = nextLine();
                if (s == null) return false;
                tokenizer = new StringTokenizer(s);
            }
            return true;
        }
        FastScanner(InputStream inputStream) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }

        public String nextLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            while (tokenizer != null && tokenizer.hasMoreTokens()) {
                sb.append(" ");
                sb.append(tokenizer.nextToken());
            }
            if (sb.length() == 0) {
                return reader.readLine();
            }
            return sb.toString();
        }


        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens())
                tokenizer = new StringTokenizer(nextLine());
            return tokenizer.nextToken();
        }

        public int nextInt() throws NumberFormatException, IOException {
            return Integer.parseInt(next());
        }
        public long nextLong() throws NumberFormatException, IOException {
            return Long.parseLong(next());
        }


        public double nextDouble() throws NumberFormatException, IOException {
            return Double.parseDouble(next());
        }

    }

    public int parse(String s) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        double result = (Double)engine.eval(s);
        return (int)result;
    }

    public void rip(String url, String[] pokemon) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element table = doc.select("table[class = wikitable sortable]").first();

        Iterator<Element> ite = table.select("a[href][title]").iterator();
        int i = 0;
        while (ite.hasNext()) {
            pokemon[i++] = ite.next().text();
        }

    }

    public void rip(String url, String[] names, int[] dates) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Iterator<Element> presidents = doc.select("ol").iterator();
        presidents.next();
        presidents.next();
        Iterator<Element> real = presidents.next().select("li").iterator();
        int i = 0;
        while (i < 44) {
            String aux = real.next().text();
            if (aux == null) {
                continue;
            }
            names[i] = aux.substring(0, aux.indexOf("(") - 1);
            if (i != 43) {
                dates[i++] = Integer.parseInt(aux.substring(aux.length() - 5, aux.length() - 1));
            }
            else {
                dates[i++] = 2014;
            }
        }


    }

    public void rip(String url, String[] lit, String[] names) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements tables = doc.select("table");
        Iterator<Element> liter = tables.get(1).select("td").iterator();

        int i = 0;
        while (liter.hasNext()) {
            names[i] = liter.next().text();
            lit[i++] = liter.next().text();
            String aux = liter.next().text();
            //System.out.println(lit[i - 1] + " " + names[i - 1]);
        }

    }

    public String rip(String code) throws IOException {
        Document doc = Jsoup.connect("http://coloreminder.com/" + code).get();
        Element ans = doc.getElementById("colorname_from_input");
        return ans.attributes().get("value");

    }

    public boolean fillTable(int row, int col, char[][] table, String word, boolean direction) {
        int i = 0;
        word = word.toLowerCase();
        if (direction) {
            while (i < word.length() && row < table.length && col < table[row].length &&
                    (table[row][col] == '#' || table[row][col] == word.charAt(i))) {
                table[row][col] = word.charAt(i);
                i++;
                col++;
            }
            if (i < word.length()) {
                while (i > 0) {
                    table[row][col - i] = '#';
                    i--;
                }
                return true;
            }
            return false;
        }
        else {
            while (i < word.length() && row < table.length && col < table[row].length &&
                    (table[row][col] == '#' || table[row][col] == word.charAt(i))) {
                table[row][col] = word.charAt(i);
                i++;
                row++;
            }
            if (i < word.length()) {
                while (i > 0) {
                    table[row - i][col] = '#';
                    i--;
                }
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        new A().run();
    }
}
