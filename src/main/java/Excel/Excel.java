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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Иллюстрация работы с Excel
 */
public class Excel {

    String fileName = "D:/TEMP/fileName.xlsx";
    String fileNameNew = "D:/TEMP/fileNameNew.xlsx";
    /**
     * Создает файл Excel и заполняет его разными данными с разными форматами
     */
    public void create() {
        System.out.println("Excel.test - Start");
        delete(fileName);
        Workbook workbook = new XSSFWorkbook(); // файл Excel
        Sheet sheet = workbook.createSheet("Проба пера"); // Excel лист
        DataFormat dataFormat = workbook.createDataFormat(); // Формат
        CellStyle cellStyle = workbook.createCellStyle(); // Стиль

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
        cell.setCellValue("_TABLE_WORKER_NAME_");
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

    /**
     * Открывает Excel и заменяет в нем поля на данные
     * https://java-online.ru/java-excel-read.xhtml
     */
    public void fill() {
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("_COMPANY_NAME_","Геликон Про"));
        // Читаем
        File file = new File(fileName);
        XSSFWorkbook workbook = null;
        try {
            workbook = (XSSFWorkbook) WorkbookFactory.create(file);
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
                // Change to getCellType() if using POI 4.x
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
                    for (Val val : vals) {
                        if (sourceValue.equalsIgnoreCase(val.name)) {
                            // Заменяем
                            cell.setCellValue(val.value);
                        }
                    }
                }
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(fileNameNew);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String fileName){
        File file = new File(fileName);
        System.out.println(file.delete());
    }

    public void test() {
        create();
        fill();
    }
}
