package Excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Иллюстрация работы с Excel
 */
public class Excel {

    /**
     * Создает файл Excel и заполняет его разными данными с разными форматами
     */
    public void create(String fileName) {
        System.out.println("Excel.test - Start");
        delete(fileName);
        Workbook workbook = new XSSFWorkbook(); // файл Excel
        Sheet sheet = workbook.createSheet("Проба пера"); // Excel лист
        DataFormat dataFormat = workbook.createDataFormat(); // Формат
        CellStyle cellStyle = workbook.createCellStyle(); // Стиль

        // В первую колонку добавим нумерацию
        for (int i = 0; i < 20; i++) {
            sheet.createRow(i).createCell(0).setCellValue(i+1);
        }

        int rowNumber = 0; // Нумерация строк с нуля
        int colNumber = 0; // Нумерация колонок тоже с нуля
        Row row = sheet.createRow(rowNumber); // Строка
        Cell cell = row.createCell(colNumber); // Ячейка
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // Стиль для выравнивания
        cell.setCellStyle(cellStyle); // Применим
        cell.setCellValue("Этот крутой текст"); // Значение в ячейку
        // Обязательно после заполнения всех данных
        sheet.autoSizeColumn(colNumber);
        // Дата рождения
        // Установим формат ячейки в датовый
        cellStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));
        rowNumber = 5; // Пятый ряд
        row = sheet.createRow(rowNumber);
        colNumber = 1; // Вторая колонка
        cell = row.createCell(colNumber);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(new Date());
        // Меняем размер  столбца
        sheet.autoSizeColumn(colNumber);
        // Деньги
        colNumber = 2; // Греться колонка
        cell = row.createCell(colNumber);
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(dataFormat.getFormat("###,##0.00"));
        cell.setCellStyle(moneyStyle);
        double d = Double.parseDouble("1234567890012.3456789");
        cell.setCellValue(d);
        // Обязательно после заполнения всех данных
        sheet.autoSizeColumn(colNumber);
        // double
        colNumber = 3; // Четвертая колонка
        cell = row.createCell(colNumber);
        cell.setCellValue(d);
        sheet.autoSizeColumn(colNumber);
        // Заполним для замены
        rowNumber = 6;
        colNumber = 2;
        row = sheet.createRow(rowNumber);
        cell = row.createCell(colNumber);
        cell.setCellValue("_COMPANY_NAME_");
        // Заполним для вставки таблицы
        rowNumber = 8;
        colNumber = 3;
        row = sheet.createRow(rowNumber);
        cell = row.createCell(colNumber);
        cell.setCellValue("_TABLE_CITY_");
        // После таблицы
        rowNumber = 9;
        colNumber = 2;
        row = sheet.createRow(rowNumber);
        cell = row.createCell(colNumber);
        cell.setCellValue("После таблицы 9");
        rowNumber = 13;
        sheet.createRow(rowNumber).createCell(colNumber).setCellValue("После таблицы 13");
        try {
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Значения для замены
    public class Val {

        String name;
        String value;

        public Val(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    // Ячейка в строке в таблице
    public class CellTable {

        int number; // Номер ячейки по порядку с 0
        Object value; // Значение

        public CellTable(int number, Object value) {
            this.number = number;
            this.value = value;
        }
    }

    // Строка в таблице
    public class RowTable {

        int number; // Номер строки по порядку с 0
        ArrayList<CellTable> cellTables; // Список ячеек строки

        public RowTable() {
            cellTables = new ArrayList<>();
        }

        // Добавление значение в конец
        public void add(Object o) {
            cellTables.add(new CellTable(cellTables.size() + 1, o));
        }
    }

    // Таблица
    public class TableE {

        public ArrayList<RowTable> rows; // Список строк

        public TableE() {
            rows = new ArrayList<>();
        }

        public void add(RowTable r) {
            rows.add(r);
        }
    }

    /**
     * Открывает Excel и заменяет в нем поля на данные https://java-online.ru/java-excel-read.xhtml
     */
    public void fill(String fileName) {
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("_COMPANY_NAME_", "Геликон Про"));
        // Список таблиц
        HashMap<String, TableE> tables = new HashMap<>();
        // таблица
        TableE table = new TableE();
        // Строка таблицы
        RowTable rowTable = new RowTable();
        rowTable.number = 0;
        rowTable.add("Первая строка");
        rowTable.add("Лондон");
        rowTable.add(10000000);
        table.add(rowTable); // Добавим в таблицу
        rowTable = new RowTable();
        rowTable.number = 1;
        rowTable.add("Вторая строка");
        rowTable.add("Париж");
        rowTable.add(6000000);
        table.add(rowTable);
        rowTable = new RowTable();
        rowTable.number = 2;
        rowTable.add("Третья строка");
        rowTable.add("Нью-Йорк");
        rowTable.add(15000000);
        table.add(rowTable);
        tables.put("_TABLE_CITY_", table);
        // Читаем
        XSSFWorkbook workbook = null;
        File file = new File(fileName);
        try {
            //workbook = (XSSFWorkbook) WorkbookFactory.create(file);
            // Надо так, иначе исходный файл так же модифицируется
            InputStream is = new FileInputStream(file);
            workbook = (XSSFWorkbook) WorkbookFactory.create(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert workbook != null;
        String sheetName = workbook.getSheetName(0); // Имя первой страница
        Sheet sheet = workbook.getSheet(sheetName); // Первая страница
        // Итератор для всех строчек листа
        Iterator<Row> rowIterator = sheet.iterator();
        // Перебираем все непустые строки
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            System.out.println("rowNum=" + row.getRowNum());
            // Итератор для ячеек текущей строки
            Iterator<Cell> cellIterator = row.cellIterator();
            // Перебираем все непустые ячейки
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                System.out.println("columnNumber=" + cell.getColumnIndex());
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        System.out.println("StringValue=" + cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) { // Это цифра - дата
                            System.out.println("DateValue=" + cell.getDateCellValue());
                        } else {
                            System.out.println("NumericValue=" + cell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        System.out.println("BooleanValue=" + cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        System.out.println("FormulaValue=" + cell.getCellFormula());
                        break;
                    case BLANK:
                        System.out.println("Blank");
                        break;
                    case _NONE:
                        System.out.println("_NONE");
                        break;
                    default:
                        System.out.println("default");
                }
                if (cellType == CellType.STRING) {
                    String sourceValue = cell.getStringCellValue();
                    // Ищем в значениях для замены
                    for (Val val : vals) {
                        if (sourceValue.equalsIgnoreCase(val.name)) {
                            // Заменяем
                            cell.setCellValue(val.value);
                        }
                    }
                }
            }
        }
        // Таблицы
        for (String n : tables.keySet()) {
            // Найдем
            int rowNumber = -1;
            int colNumber = -1;
            String tableName = null;
            for (Row row : sheet) {
                for (Cell cell : row) {
                    CellType cellType = cell.getCellType();
                    if (cellType == CellType.STRING) {
                        if (cell.getStringCellValue().equals(n)) {
                            // Есть, запомним номера
                            rowNumber = row.getRowNum();
                            colNumber = cell.getColumnIndex();
                            tableName = n;
                        }
                    }
                }
            }
            if (rowNumber != -1) {
                // Ищем в таблицах для замены
                TableE table1 = tables.get(tableName);
                sheet.shiftRows(rowNumber, sheet.getLastRowNum(), 1, true, false);
                Row row = sheet.createRow(rowNumber); // Строка
                Cell cell = row.createCell(colNumber); // Ячейка
                // Нашли - вставляем таблицу t
                // По строкам
                int rowsCount = table1.rows.size();
                for (int i = 0; i < rowsCount; i++) {
                    // Добавим строку в эксель
                    Row row1 = sheet.createRow(rowNumber++);
                    // Получим список для строки под номером i
                    int finalI = i;
                    RowTable rt = table1.rows.stream()
                            .filter(r1 -> r1.number == finalI).findAny().orElse(null);
                    if (rt != null) {
                        // Колонка
                        int cn = cell.getColumnIndex();
                        // По столбцам
                        for (int j = 0; j < rt.cellTables.size(); j++) {
                            Cell cell1 = row1.createCell(cn++);
                            String s = rt.cellTables.get(j).value.toString();
                            cell1.setCellValue(s);
                        }
                    }
                }
            }
        }
        System.out.println("Ура!");
/*
        for (Row row : sheet) {
            for (Cell cell : row) {
                CellType cellType = cell.getCellType();
                if (cellType == CellType.STRING) {
                    String sourceValue = cell.getStringCellValue();
                    // Ищем в таблицах для замены
                    TableE table1 = tables.get(sourceValue);
                    if (table1 != null) {
                        // Координаты ячейки
                        int currentRowNumber = cell.getRowIndex();
                        // По строкам
                        int rowsCount = table1.rows.size();
                        for (int i = 0; i < rowsCount; i++) {
                            // Добавим строку в эксель
                            Row row1 = sheet.createRow(currentRowNumber++);
                            // Получим список для строки под номером i
                            int finalI = i;
                            RowTable rt = table1.rows.stream()
                                    .filter(r1 -> r1.number == finalI).findAny().orElse(null);
                            if (rt != null) {
                                // Колонка
                                int cn = cell.getColumnIndex();
                                // По столбцам
                                for (int j = 0; j < rt.cellTables.size(); j++) {
                                    Cell cell1 = row1.createCell(cn++);
                                    String s = rt.cellTables.get(j).value.toString();
                                    cell1.setCellValue(s);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Таблицы
        // Флаг что еще не нашли
        // Когда нашли - больше не перебираем, так как теряется нумерация из-за вставки строк
        boolean notFoundYet = true;
        // Итератор для всех строчек листа
        rowIterator = sheet.iterator();
        // Перебираем все непустые строки
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            System.out.println("rowNum=" + row.getRowNum());
            // Итератор для ячеек текущей строки
            Iterator<Cell> cellIterator = row.cellIterator();
            // Перебираем все непустые ячейки
            while (cellIterator.hasNext() && notFoundYet) {
                Cell cell = cellIterator.next();
                System.out.println("columnNumber=" + cell.getColumnIndex());
                CellType cellType = cell.getCellType();
                if (cellType == CellType.STRING) {
                    String sourceValue = cell.getStringCellValue();
                    // Ищем в таблицах для замены
                    TableE table1 = tables.get(sourceValue);
                    if (table1 != null) {
                        // Координаты ячейки
                        int currentRowNumber = cell.getRowIndex();
                        // По строкам
                        int rowsCount = table1.rows.size();
                        for (int i = 0; i < rowsCount; i++) {
                            // Добавим строку в эксель
                            Row row1 = sheet.createRow(currentRowNumber++);
                            // Получим список для строки под номером i
                            int finalI = i;
                            RowTable rt = table1.rows.stream()
                                    .filter(r1 -> r1.number == finalI).findAny().orElse(null);
                            if (rt != null) {
                                // Колонка
                                int cn = cell.getColumnIndex();
                                // По столбцам
                                for (int j = 0; j < rt.cellTables.size(); j++) {
                                    Cell cell1 = row1.createCell(cn++);
                                    String s = rt.cellTables.get(j).value.toString();
                                    cell1.setCellValue(s);
                                }
                            }
                        }
                        notFoundYet = false; // Нашли и заменили - флаг ненайденности в фальш
                    }
                }
            }
        }

         */
        System.out.println("Excel modified");
        try {
            FileOutputStream outputStream = new FileOutputStream("D:/TEMP/fileNameNew.xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String fileName) {
        File file = new File(fileName);
        System.out.println(file.delete());
    }

    public void test() {
        create("D:/TEMP/fileName.xlsx");
        fill("D:/TEMP/fileName.xlsx");
    }
}
