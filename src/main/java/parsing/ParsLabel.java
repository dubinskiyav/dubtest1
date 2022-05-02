package parsing;

import java.util.ArrayList;
import java.util.List;

public class ParsLabel {

    // собирает ${label} по массиву
    public List<String> parseDollarLabel(List<String> sl) {
        if (sl == null) {
            return null;
        }
        if (sl.size() == 0) {
            return new ArrayList<>();
        }
        List<String> dl = new ArrayList<>();
        boolean dollar = false; // доллар еще не нашли
        boolean curlyBraceOpen = false; // открывающую фигурную скобку еще не нашли
        int labelLength = 0; // Из скольки элементов собрали метку
        String label = "";
        for (String text : sl) {
            if (!dollar) { // $ еще не нашли
                label = "";
                // Ищем $
                int dollarPos = text.indexOf("$");
                dollar = dollarPos != -1;
                if (dollar) { // доллар нашли
                    label = "$"; // Начинаем получать метку
                    labelLength = 0;
                    if (dollarPos > 0) {
                        // добавим то что до $
                        dl.add(text.substring(0, dollarPos));
                        // удалим то что до $
                        text = text.substring(dollarPos);
                    }
                    text = text.substring(1); // Удалим и сам доллар
                } else {
                    // Добавим и идем к следующему
                    if (!text.isEmpty()) {
                        dl.add(text);
                    }
                }
            }
            if (dollar) {
                labelLength++;
                // ищем открывающуюся скобку в text
                if (!curlyBraceOpen) { // ее еще не нашли
                    int cboPos = text.indexOf("{");
                    if (cboPos == -1) { // Скобку в остатке не нашли
                        // Добавим остаток к label
                        label = label + text;
                        // dl.add(label + text); // добавляем и переходим к слеюующему элементу
                    } else { // Скобку нашли, проверяем
                        if (cboPos != 0) { // скобка не следует сразу же за долларом - едем дальше
                            dollar = false; // Значит это не метка
                            // Добавим остаток и идем к следующему (про доллар не забывваем)
                            dl.add(label + text);
                        } else {
                            curlyBraceOpen = true; // скобку нашли в правильном месте
                            label = label + "{"; // Теперь в label у нас ${
                            text = text.substring(1);
                        }
                    }
                }
                if (curlyBraceOpen) {
                    // Ищем закрывающую фигурную скобку
                    int cbcPos = text.indexOf("}");
                    if (cbcPos == -1) { // Скобку в остатке не нашли
                        label = label + text; // Добавляем к метке все и идем дальше
                    } else {
                        // Скобку нашли, все запоминаем и обнуляем
                        label = label + text.substring(0, cbcPos + 1); // Метка теперь так ${aaa}
                        dl.add(label);
                        text = text.substring(cbcPos + 1);
                        if (text.length() > 0) { // пустую строку не добавляем
                            dl.add(text);
                            labelLength--; // раз добавили то уменьшаем счетчик
                        }
                        curlyBraceOpen = false;
                        dollar = false;
                        if (labelLength > 2) {
                            // Надо надобавлять пустых, чтобы осталось равным кол-во
                            while (labelLength > 2) {
                                labelLength--;
                                dl.add("");
                            }
                        }
                        labelLength = 0;
                    }
                }
            }
        }
        // Все проверим
        if (dollar || (dl.size() != sl.size())) {
            // Доллар нашли, но не нашли все остальное
            // или не получилось сохранить количество
            // - Ошибка парсинга - все в зад
            dl.clear(); // Обнуляем
            dl.addAll(sl); // копируем
        }
        return dl;
    }

    public void test1() {
        String s = "потребления холодной воды ${v_05_02} л/с, ${v_05_01} куб.м/час, ${v_05_03} куб. м./сутки";
        System.out.println(s);
        List<String> sl = new ArrayList<>();
        sl.add("потребления холодной ");
        sl.add("воды ");
        sl.add("${v_05_02}");
        sl.add(" л/с, $");
        sl.add("{v_05_");
        sl.add("01");
        sl.add("} ");
        sl.add("куб.м/час, ");
        sl.add("$");
        sl.add("{v_05_03} куб.");
        sl.add(" м./сутки");
        List<String> dl = parseDollarLabel(sl);
        for (String text : dl) {
            System.out.println("|" + text + "|");
        }
    }

    public void test2() {
        String s = "потребления холодной воды ${v_05_02} л/с, ${v_05_01} куб.м/час, ${v_05_03} куб. м./сутки";
        System.out.println(s);
        List<String> sl = new ArrayList<>();
        sl.add("потребления холодной ");
        sl.add("воды ");
        sl.add("${v_05_02}");
        sl.add(" л/с, $");
        sl.add("{v_05_");
        sl.add("01");
        sl.add("} ");
        sl.add("куб.м/час, ");
        sl.add("$");
        sl.add("{v_05_03} куб.");
        sl.add(" м./сутки");
        for (String text : sl) {
            System.out.print(text);
        }
        System.out.println();
        List<String> dl = new ArrayList<>();
        dl.add("потребления холодной ");
        dl.add("воды ");
        dl.add("${v_05_02}"); // Этот не трогаем
        dl.add(" л/с, ");     // От сюда забрали $
        dl.add("${v_05_01}");
        dl.add(" ");          //  От сюда забрали скобку
        dl.add("куб.м/час, "); // не трогаем
        dl.add("${v_05_03}"); // от сюда забрали после скобки
        dl.add(" куб.");
        dl.add(" м./сутки");
        for (String text : dl) {
            System.out.print(text);
        }
        // Попытка 1
        dl = new ArrayList<>();
        boolean dollar = false; // доллар еще не нашли
        boolean curlyBraceOpen = false; // открывающую фигурную скобку еще не нашли
        boolean curlyBraceClose = false; // закрывающую фигурную скобку еще не нашли
        String label = "";
        System.out.println();
        for (String text : sl) {
            if (!dollar) { // $ еще не нашли
                label = "";
                // Ищем $
                int dollarPos = text.indexOf("$");
                dollar = dollarPos != -1;
                if (dollar) { // доллар нашли
                    label = "$"; // Начинаем получать метку
                    if (dollarPos > 0) {
                        // добавим то что до $
                        dl.add(text.substring(0, dollarPos));
                        // удалим то что до $
                        text = text.substring(dollarPos);
                    }
                    text = text.substring(1); // Удалим и сам доллар
                } else {
                    // Добавим и идем к следующему
                    if (!text.isEmpty()) {
                        dl.add(text);
                    }
                }
            }
            if (dollar) {
                // ищем открывающуюся скобку в text
                if (!curlyBraceOpen) { // ее еще не нашли
                    int cboPos = text.indexOf("{");
                    if (cboPos == -1) { // Скобку в остатке не нашли
                        // Добавим остаток к label
                        label = label + text;
                        // dl.add(label + text); // добавляем и переходим к слеюующему элементу
                    } else { // Скобку нашли, проверяем
                        if (cboPos != 0) { // скобка не следует сразу же за долларом - едем дальше
                            dollar = false; // Значит это не метка
                            // Добавим остаток и идем к следующему (про доллар не забывваем)
                            dl.add(label + text);
                        } else {
                            curlyBraceOpen = true; // скобку нашли в правильном месте
                            label = label + "{"; // Теперь в label у нас ${
                            text = text.substring(1);
                        }
                    }
                }
                if (curlyBraceOpen) {
                    // Ищем закрывающую фигурную скобку
                    int cbcPos = text.indexOf("}");
                    if (cbcPos == -1) { // Скобку в остатке не нашли
                        label = label + text; // Добавляем к метке все и идем дальше
                    } else {
                        // Скобку нашли, все запоминаем и обнуляем
                        label = label + text.substring(0, cbcPos + 1); // Метка теперь так ${aaa}
                        dl.add(label);
                        text = text.substring(cbcPos + 1);
                        if (text.length() > 0) { // пустую строку не добавляем
                            dl.add(text);
                        }
                        curlyBraceOpen = false;
                        dollar = false;
                    }
                }
            }
        }
        // Все проверим
        if (dollar) {
            // Доллар нашли, но не нашли все остальное - Ошибка парсинга - все в зад
            System.out.println("Ошибка парсинга - все в зад");
            dl.clear(); // Обнуляем
            dl.addAll(sl); // копируем
        } else {
            // Надо все переприсвоить
            System.out.println("проверим");
        }
        for (String text : dl) {
            System.out.print(text);
        }
        System.out.println();
        for (String text : dl) {
            System.out.println("|" + text + "|");
        }
        System.out.println("Было");
        for (String text : sl) {
            System.out.println("|" + text + "|");
        }
    }
}
