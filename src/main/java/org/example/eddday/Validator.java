package org.example.eddday;

public class Validator {
    public static boolean isDateValid(String dateStr) {
        if (dateStr.length() != 10 || dateStr.charAt(4) != '-' || dateStr.charAt(7) != '-') {
            AlertUtils.showAlert("Используете формат ГГГГ-MM-ДД c тире и цифрами!!!!");
            return false;
        }
        String[] par = dateStr.split("-");
        int year, month, day;
        try {
            year = Integer.parseInt(par[0]);
            month = Integer.parseInt(par[1]);
            day = Integer.parseInt(par[2]);
        } catch (NumberFormatException e) {
            AlertUtils.showAlert("вводите только цифрами");
            return false;
        }
        if (month < 1 || month > 12) {
            AlertUtils.showAlert("Месяцы от 1 до 12");
            return false;
        }
        int maxDay = getMaxDay(year, month);
        if (day < 1 || day > maxDay) {
            AlertUtils.showAlert("Некорректное число дней. В этом месяце " + maxDay + " дней.");
            return false;
        }
        return true;
    }
    public static int getMaxDay(int year, int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (isLeapYear(year)) ? 29 : 28;
            default:
                return 0;
        }
    }
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}